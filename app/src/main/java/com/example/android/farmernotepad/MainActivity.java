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
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


public class MainActivity extends AppCompatActivity implements RecyclerViewAdapterMain.OnNoteListener {

    private static final String TAG = "MainActivity";
    boolean desc = false;


    private ArrayList<ListItem> allNotesList = new ArrayList<>();
    RecyclerViewAdapterMain adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: started.");
        loadNotes();
        loadChecklistNotes();
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.searchNoteMenuItem);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

    return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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
                        alertDialog.dismiss();
                        MainActivity.this.finish();
                    }
                });

                checklist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, NewChecklistActivity.class);
                        startActivity(intent);
                        alertDialog.dismiss();
                        MainActivity.this.finish();
                    }
                });

                break;

            case R.id.Feedback:
                Intent intent = new Intent(MainActivity.this, FeedbackActivity.class);
                startActivity(intent);
                break;

            case R.id.Sort:
                allNotesList = GenericUtils.sortByTitle(allNotesList,desc);
                adapter.notifyDataSetChanged();
                desc = !desc;
                break;

            case R.id.Backup:
                Intent intentBackup = new Intent(MainActivity.this, Backup.class);
                startActivity(intentBackup);
                break;

            case R.id.wageCalculator:
                Intent intentWage = new Intent(MainActivity.this, WageCalculatorActivity.class);
                startActivity(intentWage);
                break;

            case R.id.showOnMap:
                ArrayList<Double> noteLat = new ArrayList<Double>();
                ArrayList<Double> noteLong = new ArrayList<Double>();
                ArrayList<String> mNoteTitles = new ArrayList<String>();
                for (int i =0; i < allNotesList.size(); i++) {
                    if (allNotesList.get(i).getLatitude() != 0 || allNotesList.get(i).getLongitude() != 0) {
                        noteLat.add(allNotesList.get(i).getLatitude());
                        noteLong.add(allNotesList.get(i).getLongitude());
                        mNoteTitles.add(allNotesList.get(i).getInterfaceTitle());
                    }
                }
                Intent mapIntent = new Intent(MainActivity.this, MapsActivity.class);
                mapIntent.putExtra("NoteLat", noteLat);
                mapIntent.putExtra("NoteLong", noteLong);
                mapIntent.putExtra("Title",mNoteTitles);
                startActivity(mapIntent);
                break;

            case R.id.Settings:
                Intent intentSettings = new Intent (MainActivity.this,SettingsActivity.class);
                startActivity(intentSettings);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview.");

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        adapter = new RecyclerViewAdapterMain(allNotesList, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,1 ));
    }

    private void loadNotes() {
        DatabaseHelper dbHelper = new DatabaseHelper(MainActivity.this);
        Cursor cursor = dbHelper.getAllNotes();



        if (cursor.moveToFirst()) {
            do {
                TextNoteEntry noteEntry = new TextNoteEntry();
                noteEntry.setNoteTitle(cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_noteTitle)));
                noteEntry.setNoteText(cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_noteText)));
                noteEntry.setNoteID(cursor.getInt(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_ID)));
                noteEntry.setColor(cursor.getInt(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_color)));
                noteEntry.setCreateDate((cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_noteCreateDate))));
                noteEntry.setModDate(cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_noteModDate)));
                noteEntry.setLatitude(cursor.getDouble(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_noteLatitude)));
                noteEntry.setLongitude(cursor.getDouble(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_noteLongitude)));
                allNotesList.add(noteEntry);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        initRecyclerView();
        dbHelper.close();
    }

    private void loadChecklistNotes() {
        DatabaseHelper dbHelper =new DatabaseHelper(MainActivity.this);
        Cursor cursor = dbHelper.getAllChecklists();
        Cursor cursorItems;


        if (cursor.moveToFirst()) {
            do {

                ChecklistNoteEntry noteEntry = new ChecklistNoteEntry();

                noteEntry.setNoteTitle(cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_noteTitle)));
                int current_id = (cursor.getInt(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_ID)));
                noteEntry.setNoteID(current_id);
                noteEntry.setColor(cursor.getInt(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_color)));
                noteEntry.setCreateDate((cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_noteCreateDate))));
                noteEntry.setModDate(cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_noteModDate)));
                noteEntry.setLatitude(cursor.getDouble(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_noteLatitude)));
                noteEntry.setLongitude(cursor.getDouble(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_noteLongitude)));
                cursorItems = dbHelper.getChecklistItems(current_id);
                ArrayList<String> checkItems = new ArrayList<>();
                if (cursorItems.moveToFirst()) {
                    do {
                        checkItems.add(cursorItems.getString(cursorItems.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_Item_Text)));
                    }
                    while (cursorItems.moveToNext());
                }

                cursorItems.close();

                noteEntry.setChecklistItems(checkItems);
                allNotesList.add(noteEntry);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        initRecyclerView();
        dbHelper.close();
    }


    @Override
    public void onNoteClick(int position) {

        Intent intent;
        int mNoteID;

        int noteType = allNotesList.get(position).getListItemType();

        if (noteType == ListItem.typeText) {
            TextNoteEntry textNote = (TextNoteEntry) allNotesList.get(position);
            mNoteID = textNote.getNoteID();
            intent = new Intent(this, NewTextNoteActivity.class);
            intent.putExtra("noteID", mNoteID);
            intent.putExtra("flag", "editNote");

        }
        else{
            ChecklistNoteEntry checklistNote = (ChecklistNoteEntry) allNotesList.get(position);
            mNoteID = checklistNote.getNoteID();
            intent = new Intent(this, NewChecklistActivity.class);
            intent.putExtra("noteID", mNoteID);
            intent.putExtra("flag", "editNote");
        }

        startActivity(intent);
        MainActivity.this.finish();
    }


}
