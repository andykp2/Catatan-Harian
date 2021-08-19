package com.andykrisperdawan.catatanharian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class InsertAndViewActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int REQUEST_CODE_STORAGE = 100;
    int evenetID = 0;
    EditText edtFileName, edtContent;
    Button btnSimpan;
    boolean isEditable = false;
    String fileName = "";
    String tempCatatan = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_and_view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edtFileName = findViewById(R.id.edt_namafile);
        edtContent = findViewById(R.id.edt_notes);
        btnSimpan = findViewById(R.id.btn_submit);

        btnSimpan.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();

        if (extras != null){
            fileName = extras.getString("filename");
            edtFileName.setText(fileName);
            getSupportActionBar().setTitle("Ubah Catatan");
        } else {
            getSupportActionBar().setTitle("Tambah Catatan");
        }
        evenetID = 1;
        if (Build.VERSION.SDK_INT >= 23) {
            if (periksaIzinPenyimpanan()) {
                bacaFile();
            }
        } else {
            bacaFile();
        }
    }

    @Override
    public  void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_submit:
            evenetID = 2;
            if (!tempCatatan.equals(edtContent.getText().toString())) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (periksaIzinPenyimpanan()) {
                        tampilkanDialogKonfirmasiPenyimpanan();
                    }
                } else {
                    tampilkanDialogKonfirmasiPenyimpanan();
                }
            }
            break;
        }
    }
    public boolean periksaIzinPenyimpanan() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE_STORAGE);
                return false;
            }
        } else {
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions, @NonNull int[] grantsResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantsResults);
        switch (requestCode) {
            case REQUEST_CODE_STORAGE:
                    if (grantsResults[0] ==
                            PackageManager.PERMISSION_GRANTED) {
                        if (evenetID == 1) {
                            bacaFile();
                        } else {
                            tampilkanDialogKonfirmasiPenyimpanan();
                        }
                    }
                    break;
                }
        }

        void bacaFile() {
        String path =
                Environment.getExternalStorageDirectory().toString() +
                        "/Catatan";
        File file = new File(path, edtFileName.getText().toString());
        if (file.exists()) {

            StringBuilder text = new StringBuilder();

            try {
                BufferedReader br = new BufferedReader(new FileReader(file));

                String line = br.readLine();
                while (line != null) {
                    text.append(line);
                    line = br.readLine();
                }
                br.close();
            } catch (IOException e) {
                System.out.println("Error " + e.getMessage());
            }
            tempCatatan = text.toString();
            edtContent.setText(text.toString());
        }
    }
    void buatDanUbah() {
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            return;
        }
        String path =
                Environment.getExternalStorageDirectory().toString() +
                        "/Catatan";
        File parent = new File(path);
        if (parent.exists()) {
            File file = new File(path,
                    edtFileName.getText().toString());
            FileOutputStream outputStream = null;
            try {
                file.createNewFile();
                outputStream = new FileOutputStream(file);
                OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream);
                streamWriter.append(edtContent.getText());
                streamWriter.flush();
                streamWriter.close();
                outputStream.flush();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            parent.mkdir();
            File file = new File(path,
                    edtFileName.getText().toString());
            FileOutputStream outputStream = null;
            try {
                file.createNewFile();
                outputStream = new FileOutputStream(file, false);

                outputStream.write(edtContent.getText().toString().getBytes());
                outputStream.flush();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        onBackPressed();
    }
    void tampilkanDialogKonfirmasiPenyimpanan() {
        new AlertDialog.Builder(this)
                .setTitle("Simpan Catatan")
                .setMessage("Apakah Anda yakin ingin menyimpan Catatan ini?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        buatDanUbah();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }
    @Override
    public void onBackPressed() {
        if (!tempCatatan.equals(edtContent.getText().toString())) {
            tampilkanDialogKonfirmasiPenyimpanan();
        }
        super.onBackPressed();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}