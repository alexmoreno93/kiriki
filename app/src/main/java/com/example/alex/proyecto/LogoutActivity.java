package com.example.alex.proyecto;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class LogoutActivity extends AppCompatActivity {

    SQLiteDatabase bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bd = getApplication().openOrCreateDatabase(MainActivity.BBDD,MODE_PRIVATE,null);
        Cursor c = bd.rawQuery("SELECT * FROM Usuario", null);


        if(c.moveToFirst()){
            bd.execSQL("DELETE FROM Usuario");
        }

        Intent Principal = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(Principal);
    }
}
