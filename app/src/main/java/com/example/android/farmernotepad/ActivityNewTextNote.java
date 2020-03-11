package com.example.android.farmernotepad;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;
import static com.example.android.farmernotepad.GenericUtils.CHANNEL_ID;

public class ActivityNewTextNote extends AppCompatActivity {
    private Menu mMenu;
    private int noteColor;
    static ActivityNewTextNote activity;
    private int noteIntentID;


    ArrayList<Double> noteLat = new ArrayList<Double>();
    ArrayList<Double> noteLong = new ArrayList<Double>();
    ArrayList<String> mNoteTitle = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //String defColor = sharedPreferences.getString("default_color", "");

        noteColor = Color.parseColor(sharedPreferences.getString("default_color", "#FFFFFF"));
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
                    EntryTextNote myNewTextNote = new EntryTextNote();
                    myNewTextNote.setNoteID(noteIntentID);

                    if (noteTitle.getText().toString().equals("")) {
                        myNewTextNote.setNoteTitle(GenericUtils.getDateTime());
                    } else {
                        myNewTextNote.setNoteTitle(noteTitle.getText().toString());
                    }
                    myNewTextNote.setNoteText(noteText.getText().toString());
                    myNewTextNote.setModDate(GenericUtils.getDateTime());
                    myNewTextNote.setColor(noteColor);

                    DatabaseHelper dbHelper = new DatabaseHelper(ActivityNewTextNote.this);
                    Boolean checkInsert = dbHelper.updateNote(myNewTextNote);

                    if (checkInsert == true) {
                        Toast.makeText(getApplicationContext(), "Note Updated", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ActivityNewTextNote.this, MainActivity.class);
                        startActivity(intent);
                        ActivityNewTextNote.this.finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Update Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });


        } else {


            confirmSaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EntryTextNote myNewTextNote = new EntryTextNote();
                    Boolean checkPermission = LocationFunctions.checkPermission(ActivityNewTextNote.this);

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
                        double[] myCoords = LocationFunctions.getLocation(ActivityNewTextNote.this);
                        if (myCoords != null) {
                            myNewTextNote.setLatitude(myCoords[0]);
                            myNewTextNote.setLongitude(myCoords[1]);
                        }
                    }
                    DatabaseHelper dbHelper = new DatabaseHelper(ActivityNewTextNote.this);
                    Boolean checkInsert = dbHelper.insertNote(myNewTextNote);
                    if (checkInsert) {
                        Toast.makeText(getApplicationContext(), "Note Saved", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ActivityNewTextNote.this, MainActivity.class);
                        startActivity(intent);
                        ActivityNewTextNote.this.finish();
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
                        Boolean checkPerm = LocationFunctions.checkPermission(ActivityNewTextNote.this);
                        if (checkPerm == false) {
                            LocationFunctions.requestPermission(activity);
                        }
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
            GenericUtils.tintMenuIcon(ActivityNewTextNote.this, colorPicker, noteColor);
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
                final android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(ActivityNewTextNote.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_color_picker, null);
                alert.setView(mView);
                final MenuItem pickColorItem = mMenu.findItem(R.id.pickColor);

                final android.app.AlertDialog alertDialog = alert.create();

                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.show();

                ImageButton buttonWhite = alertDialog.findViewById(R.id.colorWhite);
                buttonWhite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        noteColor = getColor(R.color.White);
                        GenericUtils.tintMenuIcon(ActivityNewTextNote.this,pickColorItem,noteColor);
                        alertDialog.dismiss();
                    }
                });
                ImageButton buttonRed = alertDialog.findViewById(R.id.colorRed);
                buttonRed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        noteColor = getColor(R.color.Red);
                        GenericUtils.tintMenuIcon(ActivityNewTextNote.this,pickColorItem,noteColor);
                        alertDialog.dismiss();
                    }
                });
                ImageButton buttonBlue = alertDialog.findViewById(R.id.colorBlue);
                buttonBlue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        noteColor = getColor(R.color.Blue);
                        GenericUtils.tintMenuIcon(ActivityNewTextNote.this,pickColorItem,noteColor);
                        alertDialog.dismiss();
                    }
                });
                ImageButton buttonGreen = alertDialog.findViewById(R.id.colorGreen);
                buttonGreen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        noteColor = getColor(R.color.Green);
                        GenericUtils.tintMenuIcon(ActivityNewTextNote.this,pickColorItem,noteColor);
                        alertDialog.dismiss();
                    }
                });
                ImageButton buttonYellow = alertDialog.findViewById(R.id.colorYellow);
                buttonYellow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        noteColor = getColor(R.color.Yellow);
                        GenericUtils.tintMenuIcon(ActivityNewTextNote.this,pickColorItem,noteColor);
                        alertDialog.dismiss();
                    }
                });
                ImageButton buttonGrey = alertDialog.findViewById(R.id.colorGrey);
                buttonGrey.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        noteColor = getColor(R.color.Grey);
                        GenericUtils.tintMenuIcon(ActivityNewTextNote.this,pickColorItem,noteColor);
                        alertDialog.dismiss();
                    }
                });
                ImageButton buttonBlack = alertDialog.findViewById(R.id.colorBlack);
                buttonBlack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        noteColor = getColor(R.color.Black);
                        GenericUtils.tintMenuIcon(ActivityNewTextNote.this,pickColorItem,noteColor);
                        alertDialog.dismiss();
                    }
                });
                ImageButton buttonOrange = alertDialog.findViewById(R.id.colorOrange);
                buttonOrange.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        noteColor = getColor(R.color.Orange);
                        GenericUtils.tintMenuIcon(ActivityNewTextNote.this,pickColorItem,noteColor);
                        alertDialog.dismiss();
                    }
                });
                ImageButton buttonPurple = alertDialog.findViewById(R.id.colorPurple);
                buttonPurple.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        noteColor = getColor(R.color.Purple);
                        GenericUtils.tintMenuIcon(ActivityNewTextNote.this,pickColorItem,noteColor);
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
                final android.app.AlertDialog alertDeleteDialog = new android.app.AlertDialog.Builder(ActivityNewTextNote.this).create();
                alertDeleteDialog.setTitle("Delete Note");
                alertDeleteDialog.setMessage("Delete this note?");
                alertDeleteDialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                int noteID = getIntent().getIntExtra("noteID", 0);
                                DatabaseHelper dbHelper = new DatabaseHelper(ActivityNewTextNote.this);
                                Boolean checkDelete = dbHelper.deleteNote(noteID);
                                if (checkDelete) {
                                    Intent intent = new Intent(ActivityNewTextNote.this, MainActivity.class);
                                    startActivity(intent);
                                    ActivityNewTextNote.this.finish();
                                    Toast.makeText(getApplicationContext(), "Note Deleted", LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Delete failed", LENGTH_SHORT).show();
                                }
                                alertDeleteDialog.dismiss();
                                dbHelper.close();
                            }
                        });
                alertDeleteDialog.setButton(android.app.AlertDialog.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
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
                    GenericUtils.toast(ActivityNewTextNote.this,"Note has no location.");
                }
                else {
                    Intent mapIntent = new Intent(ActivityNewTextNote.this, ActivityMaps.class);
                    mapIntent.putExtra("NoteLat", noteLat);
                    mapIntent.putExtra("NoteLong", noteLong);
                    mapIntent.putExtra("Title",mNoteTitle);
                    startActivity(mapIntent);
                }
                break;

            case R.id.pinStatusBar:
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
                TextView title = findViewById(R.id.editTitle);
                TextView text = findViewById(R.id.editText);

                notificationBuilder.setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.ic_assignment_late_black_24dp)
                        .setTicker(title.getText().toString())
                        .setPriority(Notification.PRIORITY_MAX) // this is deprecated in API 26 but you can still use for below 26. check below update for 26 API
                        .setContentTitle(title.getText().toString())
                        .setContentText(text.getText().toString())
                        .setContentInfo("Info");

                NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(1, notificationBuilder.build());

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
        DatabaseHelper dbHelper = new DatabaseHelper(ActivityNewTextNote.this);
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
        Intent intent = new Intent(ActivityNewTextNote.this, MainActivity.class);
        startActivity(intent);
        ActivityNewTextNote.this.finish();
    }
}

