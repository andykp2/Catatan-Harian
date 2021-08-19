package com.andykrisperdawan.catatanharian;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("HOME");
    }
    public void actionDailyNote(View view) {
        Intent intent = new Intent(this, NoteActivity.class);
        intent.putExtra(GlobalVariable.CATATAN_HARIAN, "Catatan Harian");
        intent.putExtra(GlobalVariable.NILAI, 123);
        startActivity(intent);
    }

    public void actionLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(GlobalVariable.LOGIN, "Login");
        startActivity(intent);
    }
}
