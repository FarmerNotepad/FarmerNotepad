package com.example.android.farmernotepad;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

public class NewTextNoteActivity extends AppCompatActivity {
    private Menu mMenu;
    private int noteColor;
    static NewTextNoteActivity activity;
    private int noteIntentID;

    ArrayList<Double> noteLat = new ArrayList<Double>();
    ArrayList<Double> noteLong = new ArrayList<Double>();
    ArrayList<String> mNoteTitle = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        noteColor = getColor(R.color.White);
        activity = this;

        final EditText noteTitle = findViewById(R.id.editTitle);
        final EditText noteText = findViewById(R.id.editText);
        FloatingActionButton confirmSaveButton = findViewById(R.id.confirmSave);
        final CheckBox checkLocation = findViewById(R.id.checkBoxLoc);
        noteIntentID = getIncomingIntent();

        if (noteIntentID != 0) {

            checkLocation.setVisibility(View.INVISIBLE);
            noteText.setEnabled(false);
            noteTitle.setEnabled(false);
            loadEditableNote(noteIntentID);
            confirmSaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextNoteEntry myNewTextNote = new TextNoteEntry();
                    myNewTextNote.setNoteID(noteIntentID);

                    if (noteTitle.getText().toString().equals("")) {
                        myNewTextNote.setNoteTitle(GenericUtils.getDateTime());
                    } else {
                        myNewTextNote.setNoteTitle(noteTitle.getText().toString());
                    }
                    myNewTextNote.setNoteText(noteText.getText().toString());
                    myNewTextNote.setModDate(GenericUtils.getDateTime());
                    myNewTextNote.setColor(noteColor);

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


        } else {


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

                    if (checkPermission && checkLocation.isChecked()) {
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
                    dbHelper.close();
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
        } else {
            inflater.inflate(R.menu.note_menu, menu);
        }
        MenuItem colorPicker = menu.findItem(R.id.pickColor);
        if (colorPicker != null) {
            GenericUtils.tintMenuIcon(NewTextNoteActivity.this, colorPicker, noteColor);
        }

        this.mMenu = menu;
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu mMenu) {
        if (noteIntentID != 0) {
            mMenu.findItem(R.id.pickColor).setEnabled(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.pickColor:
                final AlertDialog.Builder alert = new AlertDialog.Builder(NewTextNoteActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.color_picker_dialog_box, null);
                alert.setView(mView);
                final MenuItem pickColorItem = mMenu.findItem(R.id.pickColor);

                final AlertDialog alertDialog = alert.create();

                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.show();

                ImageButton buttonWhite = alertDialog.findViewById(R.id.colorWhite);
                buttonWhite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        noteColor = getColor(R.color.White);
                        GenericUtils.tintMenuIcon(NewTextNoteActivity.this,pickColorItem,noteColor);
                        alertDialog.dismiss();
                    }
                });
                ImageButton buttonRed = alertDialog.findViewById(R.id.colorRed);
                buttonRed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        noteColor = getColor(R.color.Red);
                        GenericUtils.tintMenuIcon(NewTextNoteActivity.this,pickColorItem,noteColor);
                        alertDialog.dismiss();
                    }
                });
                ImageButton buttonBlue = alertDialog.findViewById(R.id.colorBlue);
                buttonBlue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        noteColor = getColor(R.color.Blue);
                        GenericUtils.tintMenuIcon(NewTextNoteActivity.this,pickColorItem,noteColor);
                        alertDialog.dismiss();
                    }
                });
                ImageButton buttonGreen = alertDialog.findViewById(R.id.colorGreen);
                buttonGreen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        noteColor = getColor(R.color.Green);
                        GenericUtils.tintMenuIcon(NewTextNoteActivity.this,pickColorItem,noteColor);
                        alertDialog.dismiss();
                    }
                });
                ImageButton buttonYellow = alertDialog.findViewById(R.id.colorYellow);
                buttonYellow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        noteColor = getColor(R.color.Yellow);
                        GenericUtils.tintMenuIcon(NewTextNoteActivity.this,pickColorItem,noteColor);
                        alertDialog.dismiss();
                    }
                });
                ImageButton buttonGrey = alertDialog.findViewById(R.id.colorGrey);
                buttonGrey.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        noteColor = getColor(R.color.Grey);
                        GenericUtils.tintMenuIcon(NewTextNoteActivity.this,pickColorItem,noteColor);
                        alertDialog.dismiss();
                    }
                });
                ImageButton buttonBlack = alertDialog.findViewById(R.id.colorBlack);
                buttonBlack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        noteColor = getColor(R.color.Black);
                        GenericUtils.tintMenuIcon(NewTextNoteActivity.this,pickColorItem,noteColor);
                        alertDialog.dismiss();
                    }
                });
                ImageButton buttonOrange = alertDialog.findViewById(R.id.colorOrange);
                buttonOrange.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        noteColor = getColor(R.color.Orange);
                        GenericUtils.tintMenuIcon(NewTextNoteActivity.this,pickColorItem,noteColor);
                        alertDialog.dismiss();
                    }
                });
                ImageButton buttonPurple = alertDialog.findViewById(R.id.colorPurple);
                buttonPurple.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        noteColor = getColor(R.color.Purple);
                        GenericUtils.tintMenuIcon(NewTextNoteActivity.this,pickColorItem,noteColor);
                        alertDialog.dismiss();
                    }
                });


                break;

            case R.id.editNote:

                findViewById(R.id.editText).setEnabled(true);
                findViewById(R.id.editTitle).setEnabled(true);
                mMenu.findItem(R.id.pickColor).setEnabled(true);

                break;

            case R.id.deleteNote:
                final AlertDialog alertDeleteDialog = new AlertDialog.Builder(NewTextNoteActivity.this).create();
                alertDeleteDialog.setTitle("Delete Note");
                alertDeleteDialog.setMessage("Delete this note?");
                alertDeleteDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                int noteID = getIntent().getIntExtra("noteID", 0);
                                DatabaseHelper dbHelper = new DatabaseHelper(NewTextNoteActivity.this);
                                Boolean checkDelete = dbHelper.deleteNote(noteID);
                                if (checkDelete) {
                                    Intent intent = new Intent(NewTextNoteActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    NewTextNoteActivity.this.finish();
                                    Toast.makeText(getApplicationContext(), "Note Deleted", LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Delete failed", LENGTH_SHORT).show();
                                }
                                alertDeleteDialog.dismiss();
                                dbHelper.close();
                            }
                        });
                alertDeleteDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        alertDeleteDialog.dismiss();
                    }
                });
                alertDeleteDialog.show();

                break;

             case R.id.shareNote:
                 EditText noteText = findViewById(R.id.editText);
                 Intent sendIntent = new Intent();
                 sendIntent.setAction(Intent.ACTION_SEND);
                 sendIntent.putExtra(Intent.EXTRA_TEXT,
                         noteText.getText().toString());
                 sendIntent.setType("text/plain");
                 startActivity(sendIntent);

                 break;

            case R.id.showOnMap:
                if(noteLat.get(0) == 0 && noteLong.get(0) == 0) {
                    GenericUtils.toast(NewTextNoteActivity.this,"Note has no location.");
                }
                else {
                    Intent mapIntent = new Intent(NewTextNoteActivity.this, MapsActivity.class);
                    mapIntent.putExtra("NoteLat", noteLat);
                    mapIntent.putExtra("NoteLong", noteLong);
                    mapIntent.putExtra("Title",mNoteTitle);
                    startActivity(mapIntent);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private int getIncomingIntent() {
        if (getIntent().hasExtra("flag")) {
            return getIntent().getIntExtra("noteID", 0);
        } else {
            return 0;
        }
    }


    private void loadEditableNote(int noteID) {
        DatabaseHelper dbHelper = new DatabaseHelper(NewTextNoteActivity.this);
        Cursor cursor = dbHelper.getNote(noteID);

        if (cursor != null)
            cursor.moveToFirst();

        String textNoteTitle = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_noteTitle));
        String textNoteText = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_noteText));
        noteColor = cursor.getInt(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_color));

        noteLat.add(cursor.getDouble(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_noteLatitude)));
        noteLong.add(cursor.getDouble(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_noteLongitude)));
        mNoteTitle.add(textNoteTitle);

        cursor.close();

        TextView title = findViewById(R.id.editTitle);
        TextView text = findViewById(R.id.editText);

        title.setText(textNoteTitle);
        text.setText(textNoteText);

        dbHelper.close();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(NewTextNoteActivity.this, MainActivity.class);
        startActivity(intent);
        NewTextNoteActivity.this.finish();
    }
}

