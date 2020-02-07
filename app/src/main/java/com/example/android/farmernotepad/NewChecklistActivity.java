package com.example.android.farmernotepad;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.widget.Toast.LENGTH_SHORT;

public class NewChecklistActivity extends AppCompatActivity implements RecyclerViewAdapterChecklist.OnChecklistItemListener {

    private ArrayList<String> mChecklistItem = new ArrayList<>();
    private Menu mMenu;
    private int noteColor;
    static NewChecklistActivity activity;
    private int noteIntentID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_checklist);
        noteColor = getColor(R.color.White);
        final EditText checklistTitle = findViewById(R.id.checklistTitleEditText);
        final CheckBox checkLocation = findViewById(R.id.checkBoxLocChecklist);
        activity = this;
        noteIntentID = getIncomingIntent();

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
                    Boolean checkPerm = LocationFunctions.checkPermission(NewChecklistActivity.this);
                    if (checkPerm == false) {
                        LocationFunctions.requestPermission(activity);
                    }
                } else {
                    Toast.makeText(NewChecklistActivity.this, "Check after onclick", LENGTH_SHORT).show();
                }
            }
        });
        if (noteIntentID != 0) {
            loadEditableChecklist(noteIntentID);

            saveChecklistButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ChecklistNoteEntry myNewChecklist = new ChecklistNoteEntry();
                    myNewChecklist.setNoteID(noteIntentID);


                    if (checklistTitle.getText().toString().equals("")) {
                        myNewChecklist.setNoteTitle(GenericUtils.getDateTime());
                    } else {
                        myNewChecklist.setNoteTitle(checklistTitle.getText().toString());
                    }

                    myNewChecklist.setModDate(GenericUtils.getDateTime());
                    myNewChecklist.setColor(noteColor);

                    ArrayList<String> items = new ArrayList<>();
                    items.addAll(mChecklistItem);

                    myNewChecklist.setChecklistItems(items);

                    DatabaseHelper dbHelper = new DatabaseHelper(NewChecklistActivity.this);
                    Boolean checkUpdate = dbHelper.updateChecklist(myNewChecklist);
                    if (checkUpdate) {
                        Toast.makeText(getApplicationContext(), "Checklist Updated", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(NewChecklistActivity.this, MainActivity.class);
                        startActivity(intent);
                        NewChecklistActivity.this.finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Update Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {

            saveChecklistButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ChecklistNoteEntry myNewChecklist = new ChecklistNoteEntry();
                    Boolean checkPermission = LocationFunctions.checkPermission(NewChecklistActivity.this);

                    if (checklistTitle.getText().toString().equals("")) {
                        myNewChecklist.setNoteTitle(GenericUtils.getDateTime());
                    } else {
                        myNewChecklist.setNoteTitle(checklistTitle.getText().toString());
                    }

                    myNewChecklist.setCreateDate(GenericUtils.getDateTime());
                    myNewChecklist.setModDate(GenericUtils.getDateTime());
                    myNewChecklist.setColor(noteColor);

                    if (checkPermission && checkLocation.isChecked()) {
                        double[] myCoords = LocationFunctions.getLocation(NewChecklistActivity.this);
                        if (myCoords != null) {
                            myNewChecklist.setLatitude(myCoords[0]);
                            myNewChecklist.setLongitude(myCoords[1]);
                        }
                    }
                    ArrayList<String> items = new ArrayList<>();
                    items.addAll(mChecklistItem);

                    myNewChecklist.setChecklistItems(items);

                    DatabaseHelper dbHelper = new DatabaseHelper(NewChecklistActivity.this);
                    Boolean checkInsert = dbHelper.insertChecklist(myNewChecklist);
                    if (checkInsert) {
                        Toast.makeText(getApplicationContext(), "Checklist Saved", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(NewChecklistActivity.this, MainActivity.class);
                        startActivity(intent);
                        NewChecklistActivity.this.finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Insertion Failed", Toast.LENGTH_SHORT).show();
                    }
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
        RecyclerViewAdapterChecklist adapter = new RecyclerViewAdapterChecklist(mChecklistItem, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    private void addItemDialogBox() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(NewChecklistActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.add_checklist_item_dialog_box, null);
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
                mChecklistItem.add(checklistItem);
                alertDialog.dismiss();

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
        addItemDialogBox();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (noteIntentID != 0) {
            inflater.inflate(R.menu.edit_note_menu, menu);
        } else {
            inflater.inflate(R.menu.note_menu, menu);
        }
        this.mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteNote:
                final android.app.AlertDialog alertDeleteDialog = new android.app.AlertDialog.Builder(NewChecklistActivity.this).create();
                alertDeleteDialog.setTitle("Delete Note");
                alertDeleteDialog.setMessage("Delete this note?");
                alertDeleteDialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
                                Boolean checkDelete = dbHelper.deleteChecklist(noteIntentID);
                                if (checkDelete) {
                                    Intent intent = new Intent(NewChecklistActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    NewChecklistActivity.this.finish();
                                    Toast.makeText(getApplicationContext(), "Checklist Deleted", LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Deletion failed", LENGTH_SHORT).show();
                                }
                                alertDeleteDialog.dismiss();
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

    private void loadEditableChecklist(int noteID) {
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        Cursor cursor = dbHelper.getSingleChecklist(noteID);
        Cursor cursorItems = dbHelper.getSingleChecklistItems(noteID);

        if (cursor != null)
            cursor.moveToFirst();

        String textNoteTitle = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_noteTitle));
        noteColor = cursor.getInt(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_color));
        cursor.close();

        if (cursorItems.moveToFirst()) {
            do {
                mChecklistItem.add(cursorItems.getString(cursorItems.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_Item_Text)));
            } while (cursorItems.moveToNext());
        }


        TextView title = findViewById(R.id.checklistTitleEditText);


        title.setText(textNoteTitle);


    }

}
