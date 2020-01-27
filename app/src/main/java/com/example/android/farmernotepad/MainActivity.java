package com.example.android.farmernotepad;


import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ArrayList<String> mTextNoteTitle = new ArrayList<>();
    private ArrayList<String> mTextNoteContent = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: started.");

        loadNotes();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_main);

        clearRecyclerView();
        loadNotes();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setContentView(R.layout.activity_main);

        clearRecyclerView();
        loadNotes();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.newNote:
                final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.add_note_dialog_box, null);
                alert.setView(mView);
                final AlertDialog alertDialog = alert.create();
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.show();

                Button text = (Button) alertDialog.findViewById(R.id.addTextNoteButton);
                Button checklist = (Button) alertDialog.findViewById(R.id.addChecklistNoteButton);

                text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, NewTextNoteActivity.class);
                        startActivity(intent);
                    }
                });

                checklist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, NewChecklistActivity.class);
                        startActivity(intent);
                    }
                });

                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview.");

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mTextNoteTitle, mTextNoteContent, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadNotes(){
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db =dbHelper.getReadableDatabase();
        Cursor cursor = dbHelper.getAllNotes();

        initRecyclerView();

        if(cursor.moveToFirst()){
            do{
                String textNoteTitle = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_noteTitle));
                String textNoteText = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_noteText));

                mTextNoteTitle.add(textNoteTitle);
                mTextNoteContent.add(textNoteText);
                initRecyclerView();
            }
            while(cursor.moveToNext());
        }
        cursor.close();
    }

    private void clearRecyclerView(){
        if(mTextNoteContent!=null && mTextNoteTitle!=null) {
            mTextNoteTitle.clear();
            mTextNoteContent.clear();
        }
    }

}
