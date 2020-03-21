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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.widget.Toast.LENGTH_SHORT;
import static com.example.android.farmernotepad.GenericUtils.CHANNEL_ID;

public class ActivityNewChecklist extends AppCompatActivity implements RecyclerViewAdapterChecklist.OnChecklistItemListener {

    private ArrayList<ChecklistItemEntry> mChecklistItem = new ArrayList<>();
    private Menu mMenu;
    private int noteColor;
    static ActivityNewChecklist activity;
    private int noteIntentID;
    RecyclerViewAdapterChecklist adapter;
    private boolean editable;
    androidx.appcompat.widget.Toolbar toolbar;


    ArrayList<Double> noteLat = new ArrayList<Double>();
    ArrayList<Double> noteLong = new ArrayList<Double>();
    ArrayList<String> mNoteTitle = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_checklist);
        final EditText checklistTitle = findViewById(R.id.checklistTitleEditText);
        final CheckBox checkLocation = findViewById(R.id.checkBoxLocChecklist);
        activity = this;
        noteIntentID = getIncomingIntent();

        toolbar = findViewById(R.id.newChkListToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        noteColor = Color.parseColor(sharedPreferences.getString("default_color", "#FFFFFF"));


        Button addItemButton = (Button) findViewById(R.id.addChecklistItemButton);
        FloatingActionButton saveChecklistButton = findViewById(R.id.confirmSaveChecklist);

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemDialogBox();
            }
        });

        checkLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkLocation.isChecked()) {
                    Boolean checkPerm = LocationFunctions.checkPermission(ActivityNewChecklist.this);
                    if (checkPerm == false) {
                        LocationFunctions.requestPermission(activity);
                    }
                }
            }
        });

        if (noteIntentID != 0) {

            checkLocation.setVisibility(View.INVISIBLE);
            checklistTitle.setEnabled(false);
            loadEditableChecklist(noteIntentID);
            addItemButton.setClickable(false);
            addItemButton.setVisibility(View.GONE);

            saveChecklistButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EntryChecklistNote myNewChecklist = new EntryChecklistNote();
                    myNewChecklist.setNoteID(noteIntentID);


                    if (checklistTitle.getText().toString().equals("")) {
                        myNewChecklist.setNoteTitle(GenericUtils.getDateTime());
                    } else {
                        myNewChecklist.setNoteTitle(checklistTitle.getText().toString());
                    }

                    myNewChecklist.setModDate(GenericUtils.getDateTime());
                    myNewChecklist.setColor(noteColor);



                    myNewChecklist.setChecklistItems(mChecklistItem);

                    DatabaseHelper dbHelper = new DatabaseHelper(ActivityNewChecklist.this);
                    Boolean checkUpdate = dbHelper.updateChecklist(myNewChecklist);
                    if (checkUpdate) {
                        Toast.makeText(getApplicationContext(), "Checklist Updated", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ActivityNewChecklist.this, MainActivity.class);
                        startActivity(intent);
                        ActivityNewChecklist.this.finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Update Failed", Toast.LENGTH_SHORT).show();
                    }
                    dbHelper.close();
                }
            });
        } else {

            saveChecklistButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EntryChecklistNote myNewChecklist = new EntryChecklistNote();
                    Boolean checkPermission = LocationFunctions.checkPermission(ActivityNewChecklist.this);

                    if (checklistTitle.getText().toString().equals("")) {
                        myNewChecklist.setNoteTitle(GenericUtils.getDateTime());
                    } else {
                        myNewChecklist.setNoteTitle(checklistTitle.getText().toString());
                    }

                    myNewChecklist.setCreateDate(GenericUtils.getDateTime());
                    myNewChecklist.setModDate(GenericUtils.getDateTime());
                    myNewChecklist.setColor(noteColor);

                    if (checkPermission && checkLocation.isChecked()) {
                        double[] myCoords = LocationFunctions.getLocation(ActivityNewChecklist.this);
                        if (myCoords != null) {
                            myNewChecklist.setLatitude(myCoords[0]);
                            myNewChecklist.setLongitude(myCoords[1]);
                        }
                    }


                    myNewChecklist.setChecklistItems(mChecklistItem);

                    DatabaseHelper dbHelper = new DatabaseHelper(ActivityNewChecklist.this);
                    Boolean checkInsert = dbHelper.insertChecklist(myNewChecklist);
                    if (checkInsert) {
                        Toast.makeText(getApplicationContext(), "Checklist Saved", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ActivityNewChecklist.this, MainActivity.class);
                        startActivity(intent);
                        ActivityNewChecklist.this.finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Insertion Failed", Toast.LENGTH_SHORT).show();
                    }
                    dbHelper.close();
                }
            });
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        initRecyclerViewAdapterChecklist();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initRecyclerViewAdapterChecklist();
    }

    private void initRecyclerViewAdapterChecklist() {
        RecyclerView recyclerView = findViewById(R.id.checklistRecyclerView);
        adapter = new RecyclerViewAdapterChecklist(mChecklistItem, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    private void addItemDialogBox() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(ActivityNewChecklist.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_add_checklist_item, null);
        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();

        Button okButton = (Button) alertDialog.findViewById(R.id.okButton);
        Button cancelButton = (Button) alertDialog.findViewById(R.id.cancelButton);
        final EditText editText = (EditText) alertDialog.findViewById(R.id.checklistEditText);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String checklistItem = editText.getText().toString();
                if (checklistItem.equals(null) || checklistItem.equals("")) {
                    editText.getText().clear();
                    alertDialog.dismiss();
                } else {
                    ChecklistItemEntry mNewItem = new ChecklistItemEntry();
                    mNewItem.setItemText(checklistItem);
                    mNewItem.setIsChecked(0);
                    mChecklistItem.add(mNewItem);
                    adapter.notifyDataSetChanged();
                }
                alertDialog.dismiss();
            }
        });

        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                okButton.callOnClick();
                dialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.getText().clear();
                alertDialog.dismiss();
            }
        });

    }

    private void editItemDialogBox(final String itemText, final int position) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(ActivityNewChecklist.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_add_checklist_item, null);
        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();

        Button okButton = (Button) alertDialog.findViewById(R.id.okButton);
        Button cancelButton = (Button) alertDialog.findViewById(R.id.cancelButton);
        final EditText editText = (EditText) alertDialog.findViewById(R.id.checklistEditText);

        editText.setText(mChecklistItem.get(position).getItemText());

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String checklistItem = editText.getText().toString();
                if (checklistItem.equals(null) || checklistItem.equals("")) {
                    editText.getText().clear();
                    alertDialog.dismiss();
                } else {
                    //String itemText = editText.getText().toString();
                    //mChecklistItem.set(position, itemText);
                    mChecklistItem.get(position).setItemText(checklistItem);
                    adapter.notifyDataSetChanged();
                }
                alertDialog.dismiss();
            }
        });

        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                okButton.callOnClick();
                dialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.getText().clear();
                alertDialog.dismiss();
            }
        });

    }


    @Override
    public void onChecklistNoteClick(int position) {
        if (editable) {
            String itemText = mChecklistItem.get(position).getItemText();
            editItemDialogBox(itemText, position);
        }
        else {

            int isItChecked = mChecklistItem.get(position).getIsChecked();
            if (isItChecked == 0){
                mChecklistItem.get(position).setIsChecked(1);
            }
            else {
                mChecklistItem.get(position).setIsChecked(0);
            }
            adapter.notifyDataSetChanged();
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
            GenericUtils.tintMenuIcon(ActivityNewChecklist.this, colorPicker, noteColor);
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
            case R.id.deleteNote:
                final AlertDialog alertDeleteDialog = new AlertDialog.Builder(ActivityNewChecklist.this).create();
                alertDeleteDialog.setTitle("Delete Note");
                alertDeleteDialog.setMessage("Delete this note?");
                alertDeleteDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseHelper dbHelper = new DatabaseHelper(ActivityNewChecklist.this);
                                Boolean checkDelete = dbHelper.deleteChecklist(noteIntentID);
                                if (checkDelete) {
                                    Intent intent = new Intent(ActivityNewChecklist.this, MainActivity.class);
                                    startActivity(intent);
                                    ActivityNewChecklist.this.finish();
                                    Toast.makeText(getApplicationContext(), "Checklist Deleted", LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Deletion failed", LENGTH_SHORT).show();
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

            case R.id.pickColor:
                final android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(ActivityNewChecklist.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_color_picker, null);
                alert.setView(mView);
                final MenuItem pickColorItem = mMenu.findItem(R.id.pickColor);

                final android.app.AlertDialog alertDialog = alert.create();

                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.show();
                GenericUtils.setDialogSize(alertDialog, 880, 880);

                ImageButton buttonWhite = alertDialog.findViewById(R.id.colorWhite);
                buttonWhite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        noteColor = getColor(R.color.White);
                        GenericUtils.tintMenuIcon(ActivityNewChecklist.this, pickColorItem, noteColor);
                        alertDialog.dismiss();
                    }
                });
                ImageButton buttonRed = alertDialog.findViewById(R.id.colorRed);
                buttonRed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        noteColor = getColor(R.color.Red);
                        GenericUtils.tintMenuIcon(ActivityNewChecklist.this, pickColorItem, noteColor);
                        alertDialog.dismiss();
                    }
                });
                ImageButton buttonBlue = alertDialog.findViewById(R.id.colorBlue);
                buttonBlue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        noteColor = getColor(R.color.Blue);
                        GenericUtils.tintMenuIcon(ActivityNewChecklist.this, pickColorItem, noteColor);
                        alertDialog.dismiss();
                    }
                });
                ImageButton buttonGreen = alertDialog.findViewById(R.id.colorGreen);
                buttonGreen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        noteColor = getColor(R.color.Green);
                        GenericUtils.tintMenuIcon(ActivityNewChecklist.this, pickColorItem, noteColor);
                        alertDialog.dismiss();
                    }
                });
                ImageButton buttonYellow = alertDialog.findViewById(R.id.colorYellow);
                buttonYellow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        noteColor = getColor(R.color.Yellow);
                        GenericUtils.tintMenuIcon(ActivityNewChecklist.this, pickColorItem, noteColor);
                        alertDialog.dismiss();
                    }
                });
                ImageButton buttonGrey = alertDialog.findViewById(R.id.colorGrey);
                buttonGrey.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        noteColor = getColor(R.color.Grey);
                        GenericUtils.tintMenuIcon(ActivityNewChecklist.this, pickColorItem, noteColor);
                        alertDialog.dismiss();
                    }
                });
                ImageButton buttonBlack = alertDialog.findViewById(R.id.colorBlack);
                buttonBlack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        noteColor = getColor(R.color.Black);
                        GenericUtils.tintMenuIcon(ActivityNewChecklist.this, pickColorItem, noteColor);
                        alertDialog.dismiss();
                    }
                });
                ImageButton buttonOrange = alertDialog.findViewById(R.id.colorOrange);
                buttonOrange.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        noteColor = getColor(R.color.Orange);
                        GenericUtils.tintMenuIcon(ActivityNewChecklist.this, pickColorItem, noteColor);
                        alertDialog.dismiss();
                    }
                });
                ImageButton buttonPurple = alertDialog.findViewById(R.id.colorPurple);
                buttonPurple.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        noteColor = getColor(R.color.Purple);
                        GenericUtils.tintMenuIcon(ActivityNewChecklist.this, pickColorItem, noteColor);
                        alertDialog.dismiss();
                    }
                });


                break;

            case R.id.editNote:
                editable = true;
                findViewById(R.id.checklistTitleEditText).setEnabled(true);
                findViewById(R.id.addChecklistItemButton).setClickable(true);
                findViewById(R.id.addChecklistItemButton).setVisibility(View.VISIBLE);
                mMenu.findItem(R.id.pickColor).setEnabled(true);
                break;


            case R.id.shareNote:
                String toSend = "";
                for (int i = 0; i < mChecklistItem.size(); i++) {
                    toSend = toSend + mChecklistItem.get(i).getItemText() + System.lineSeparator();
                }

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        toSend);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

                break;

            case R.id.showOnMap:
                if (noteLat.get(0) == 0 && noteLong.get(0) == 0) {
                    GenericUtils.toast(ActivityNewChecklist.this, "Note has no location.");
                } else {
                    Intent mapIntent = new Intent(ActivityNewChecklist.this, ActivityMaps.class);
                    mapIntent.putExtra("NoteLat", noteLat);
                    mapIntent.putExtra("NoteLong", noteLong);
                    mapIntent.putExtra("Title", mNoteTitle);
                    startActivity(mapIntent);
                }
                break;

            case R.id.pinStatusBar:
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
                TextView checklistTitle = findViewById(R.id.checklistTitleEditText);
                String checklistText = "";
                for (ChecklistItemEntry s : mChecklistItem){
                            checklistText += " \u2022" + s.getItemText();
                }



                notificationBuilder.setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.ic_assignment_late_black_24dp)
                        .setTicker(checklistTitle.getText().toString())
                        .setPriority(Notification.PRIORITY_DEFAULT) // this is deprecated in API 26 but you can still use for below 26. check below update for 26 API
                        .setContentTitle(checklistTitle.getText().toString())
                        .setContentText(checklistText)
                        .setContentInfo("Info");

                NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(1, notificationBuilder.build());
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private int getIncomingIntent() {
        if (getIntent().hasExtra("flag")) {
            editable = false;
            return getIntent().getIntExtra("noteID", 0);
        } else {
            editable = true;
            return 0;
        }
    }

    private void loadEditableChecklist(int noteID) {
        DatabaseHelper dbHelper = new DatabaseHelper(ActivityNewChecklist.this);
        Cursor cursor = dbHelper.getSingleChecklist(noteID);
        Cursor cursorItems = dbHelper.getSingleChecklistItems(noteID);

        if (cursor != null)
            cursor.moveToFirst();

        String textNoteTitle = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_noteTitle));
        noteColor = cursor.getInt(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_color));

        noteLat.add(cursor.getDouble(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_noteLatitude)));
        noteLong.add(cursor.getDouble(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_noteLongitude)));
        mNoteTitle.add(textNoteTitle);

        cursor.close();

        if (cursorItems.moveToFirst()) {
            do {
                ChecklistItemEntry mItemToLoad = new ChecklistItemEntry();
                mItemToLoad.setItemText(cursorItems.getString(cursorItems.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_Item_Text)));
                mItemToLoad.setIsChecked(cursorItems.getInt(cursorItems.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_isChecked)));
                mChecklistItem.add(mItemToLoad);
            } while (cursorItems.moveToNext());
        }
        cursorItems.close();
        dbHelper.close();

        EditText title = findViewById(R.id.checklistTitleEditText);

        title.setText(textNoteTitle);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ActivityNewChecklist.this, MainActivity.class);
        startActivity(intent);
        ActivityNewChecklist.this.finish();
    }

}
