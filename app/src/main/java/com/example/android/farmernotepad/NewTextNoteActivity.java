package com.example.android.farmernotepad;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

import static android.widget.Toast.LENGTH_SHORT;

public class NewTextNoteActivity extends AppCompatActivity {
    private Menu mMenu;
    private int noteColor;
    static NewTextNoteActivity activity;
    private int noteIntentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        noteColor = getColor(R.color.White);
        activity = this;

        final EditText noteTitle =  findViewById(R.id.editTitle);
        final EditText noteText = findViewById(R.id.editText);
        FloatingActionButton confirmSaveButton = findViewById(R.id.confirmSave);
        final CheckBox checkLocation = findViewById(R.id.checkBoxLoc);
        noteIntentID = getIncomingIntent();

        if(noteIntentID != 0){

            checkLocation.setVisibility(View.INVISIBLE);
            noteText.setEnabled(false);
            noteTitle.setEnabled(false);
            loadEditableNote(noteIntentID);
            confirmSaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextNoteEntry myNewTextNote = new TextNoteEntry();

                    if (noteTitle.getText().toString().equals("")) {
                        myNewTextNote.setNoteTitle(GenericUtils.getDateTime());
                    } else {
                        myNewTextNote.setNoteTitle(noteTitle.getText().toString());
                    }
                    myNewTextNote.setNoteText(noteText.getText().toString());
                    myNewTextNote.setModDate(GenericUtils.getDateTime());

                    DatabaseHelper dbHelper = new DatabaseHelper(NewTextNoteActivity.this);
                    Boolean checkInsert = dbHelper.updateNote(myNewTextNote);

                    if (checkInsert == true) {
                        Toast.makeText(getApplicationContext(), "Note Updated", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(NewTextNoteActivity.this, MainActivity.class);
                        startActivity(intent);
                        NewTextNoteActivity.this.finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Update Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });




        }else {


            confirmSaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextNoteEntry myNewTextNote = new TextNoteEntry();
                    Boolean checkPermission = LocationFunctions.checkPermission(NewTextNoteActivity.this);

                    if (noteTitle.getText().toString().equals("")) {
                        myNewTextNote.setNoteTitle(GenericUtils.getDateTime());
                    } else {
                        myNewTextNote.setNoteTitle(noteTitle.getText().toString());
                    }
                    myNewTextNote.setNoteText(noteText.getText().toString());
                    myNewTextNote.setCreateDate(GenericUtils.getDateTime());
                    myNewTextNote.setModDate(GenericUtils.getDateTime());
                    myNewTextNote.setColor(noteColor);

                    if ( checkPermission && checkLocation.isChecked()) {
                        double[] myCoords = LocationFunctions.getLocation(NewTextNoteActivity.this);
                        if (myCoords != null) {
                            myNewTextNote.setLatitude(myCoords[0]);
                            myNewTextNote.setLongitude(myCoords[1]);
                        }
                    }
                    DatabaseHelper dbHelper = new DatabaseHelper(NewTextNoteActivity.this);
                    Boolean checkInsert = dbHelper.insertNote(myNewTextNote);
                    if (checkInsert) {
                        Toast.makeText(getApplicationContext(), "Note Saved", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(NewTextNoteActivity.this, MainActivity.class);
                        startActivity(intent);
                        NewTextNoteActivity.this.finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Insertion Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            checkLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkLocation.isChecked()) {
                        Boolean checkPerm = LocationFunctions.checkPermission(NewTextNoteActivity.this);
                        if (checkPerm == false) {
                            LocationFunctions.requestPermission(activity);
                        }
                    } else {
                        Toast.makeText(NewTextNoteActivity.this, "Check after onclick", LENGTH_SHORT).show();
                    }
                }
            });

        }

    }



    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (noteIntentID != 0) {
            inflater.inflate(R.menu.edit_note_menu, menu);
        }
        else {
            inflater.inflate(R.menu.note_menu,menu);
        }
        this.mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.pickColor:
                final AlertDialog.Builder alert = new AlertDialog.Builder(NewTextNoteActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.color_picker_dialog_box,  null);
                alert.setView(mView);
                final MenuItem pickColorItem =  mMenu.findItem(R.id.pickColor);

                final AlertDialog alertDialog =alert.create();

                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.show();
                //Window window = alertDialog.getWindow();
                //window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                ImageButton buttonWhite = alertDialog.findViewById(R.id.colorWhite);
                buttonWhite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pickColorItem.setIcon(R.drawable.ic_stop_white_24dp);
                        noteColor = getColor(R.color.White);
                        alertDialog.dismiss();
                    }
                });
                ImageButton buttonRed = alertDialog.findViewById(R.id.colorRed);
                buttonRed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pickColorItem.setIcon(R.drawable.ic_stop_red_24dp);
                        noteColor = getColor(R.color.Red);
                        alertDialog.dismiss();
                    }
                });
                ImageButton buttonBlue = alertDialog.findViewById(R.id.colorBlue);
                buttonBlue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pickColorItem.setIcon(R.drawable.ic_stop_blue_24dp);
                        noteColor = getColor(R.color.Blue);
                        alertDialog.dismiss();
                    }
                });
                ImageButton buttonGreen = alertDialog.findViewById(R.id.colorGreen);
                buttonGreen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pickColorItem.setIcon(R.drawable.ic_stop_green_24dp);
                        noteColor = getColor(R.color.Green);
                        alertDialog.dismiss();
                    }
                });
                ImageButton buttonYellow = alertDialog.findViewById(R.id.colorYellow);
                buttonYellow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pickColorItem.setIcon(R.drawable.ic_stop_yellow_24dp);
                        noteColor = getColor(R.color.Yellow);
                        alertDialog.dismiss();
                    }
                });
                ImageButton buttonGrey = alertDialog.findViewById(R.id.colorGrey);
                buttonGrey.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pickColorItem.setIcon(R.drawable.ic_stop_grey_24dp);
                        noteColor = getColor(R.color.LightGrey);
                        alertDialog.dismiss();
                    }
                });
                ImageButton buttonBlack = alertDialog.findViewById(R.id.colorBlack);
                buttonBlack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pickColorItem.setIcon(R.drawable.ic_stop_black_24dp);
                        noteColor = getColor(R.color.Black);
                        alertDialog.dismiss();
                    }
                });
                ImageButton buttonOrange = alertDialog.findViewById(R.id.colorOrange);
                buttonOrange.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pickColorItem.setIcon(R.drawable.ic_stop_orange_24dp);
                        noteColor = getColor(R.color.Orange);
                        alertDialog.dismiss();
                    }
                });
                ImageButton buttonPurple = alertDialog.findViewById(R.id.colorPurple);
                buttonPurple.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pickColorItem.setIcon(R.drawable.ic_stop_purple_24dp);
                        noteColor = getColor(R.color.Purple);
                        alertDialog.dismiss();
                    }
                });



                break;

            case R.id.editNote:

                findViewById(R.id.editText).setEnabled(true);
                findViewById(R.id.editTitle).setEnabled(true);

                break;

            case R.id.deleteNote:
                if(getIntent().hasExtra("flag")){
                    int noteID = getIntent().getIntExtra("noteID", 0);
                    DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
                    Boolean checkDelete = dbHelper.deleteNote(noteID);
                    if (checkDelete){
                        Intent intent = new Intent(NewTextNoteActivity.this, MainActivity.class);
                        startActivity(intent);
                        NewTextNoteActivity.this.finish();
                        Toast.makeText(getApplicationContext(),"Note Deleted", LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Delete failed",LENGTH_SHORT).show();
                    }
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private int getIncomingIntent(){
        if(getIntent().hasExtra("flag")){
            return getIntent().getIntExtra("noteID", 0);
        }
        else {
            return 0;
        }
    }



    private void loadEditableNote(int noteID){
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        Cursor cursor = dbHelper.getNote(noteID);

        if (cursor != null)
            cursor.moveToFirst();

                String textNoteTitle = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_noteTitle));
                String textNoteText = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_noteText));
                noteColor = cursor.getInt(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_color));

        cursor.close();

        TextView title = findViewById(R.id.editTitle);
        TextView text = findViewById(R.id.editText);

        title.setText(textNoteTitle);
        text.setText(textNoteText);

    }

}

