package com.example.android.farmernotepad;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.widget.Toast.LENGTH_SHORT;

public class NewTextNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        final EditText noteTitle =  findViewById(R.id.editTitle);
        final EditText noteText = findViewById(R.id.editText);
        FloatingActionButton confirmSaveButton = findViewById(R.id.confirmSave);



        confirmSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextNoteEntry myNewTextNote = new TextNoteEntry();
                if (noteTitle.getText().toString().equals("")){
                        myNewTextNote.setNoteTitle(getDateTime()); }
                    else {
                        myNewTextNote.setNoteTitle(noteTitle.getText().toString());
                }
                myNewTextNote.setNoteText(noteText.getText().toString());
                myNewTextNote.setCreateDate(getDateTime());
                myNewTextNote.setModDate(getDateTime());
                //myNewTextNote.setColor(TO DO);
                //myNewTextNote.setLongitude(TO DO);
                //myNewTextNote.setLatitude(TO DO);
                DatabaseHelper dbHelper = new DatabaseHelper(NewTextNoteActivity.this);
                Boolean checkInsert = dbHelper.insertNote(myNewTextNote);
                if (checkInsert = true) {
                    Toast.makeText(getApplicationContext(), "Note Saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Insertion Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.note_menu, menu);
        return true;
    }

     private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }






}

