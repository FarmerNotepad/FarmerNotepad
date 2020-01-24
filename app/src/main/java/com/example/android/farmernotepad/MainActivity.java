package com.example.android.farmernotepad;



import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.content.ContextWrapper;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db =dbHelper.getReadableDatabase();
        //Cursor cursor = db.rawQuery(dbHelper.testSQL, new String [] {});
        //TextNoteEntry noteOne = new TextNoteEntry();
        //noteOne.setNoteText(cursor.toString());
        //String testResult = noteOne.getNoteText();
        //Log.d("OK",testResult);
    }
}
