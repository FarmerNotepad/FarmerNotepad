package com.example.android.farmernotepad;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.view.menu.MenuView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


public class MainActivity extends AppCompatActivity implements RecyclerViewAdapterMain.OnNoteListener {

    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    boolean desc = false;
    private ArrayList<ListItem> concreteList = new ArrayList<>();
    private ArrayList<ListItem> allNotesList = new ArrayList<>();
    RecyclerViewAdapterMain adapter;
    private ActionMode mActionMode;
    private ArrayList<Integer> selectionTracker = new ArrayList<>();
    private boolean clickState = false;

    Button sortMenu;
    FloatingActionButton addNote;
    ConstraintLayout splashScreen, parentMain;
    private static DialogTabbed dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);

        addNote = findViewById(R.id.addNote);
        sortMenu = findViewById(R.id.sortMenu);
        splashScreen = findViewById(R.id.parent_Splash);
        parentMain = findViewById(R.id.parentMain);


        sortMenu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);


                // Create and show the dialog.
                DialogTabbed dialogFragment = new DialogTabbed();
                dialogFragment.show(ft, "dialog");

                setDialog(dialogFragment);


                //Dialog dialog = dialogFragment.getDialog();
                //Button btn = dialog.findViewById(R.id.allColorsBtn);
                //btn.setOnClickListener(this);

            }
        });

        findViewById(R.id.main).setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {

            public void onSwipeLeft() {
                Intent intent = new Intent(MainActivity.this, ActivityPaymentsLog.class);
                startActivity(intent);
                MainActivity.this.finish();
            }

        });


        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_add_note, null);
                alert.setView(mView);
                final AlertDialog alertDialog = alert.create();
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.show();

                GenericUtils.setDialogSize(alertDialog, 750, ViewGroup.LayoutParams.WRAP_CONTENT);

                Button text = (Button) alertDialog.findViewById(R.id.addTextNoteButton);
                Button checklist = (Button) alertDialog.findViewById(R.id.addChecklistNoteButton);

                text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, ActivityNewTextNote.class);
                        startActivity(intent);
                        alertDialog.dismiss();
                        MainActivity.this.finish();
                    }
                });

                checklist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, ActivityNewChecklist.class);
                        startActivity(intent);
                        alertDialog.dismiss();
                        MainActivity.this.finish();
                    }
                });

            }
        });

        Log.d(TAG, "onCreate: started.");
        GenericUtils.createNotificationChannel(MainActivity.this);
        loadNotes();
        loadChecklistNotes();
        concreteList.addAll(allNotesList);


        filterColor(sharedPreferences.getInt("filter_color", 0));
        sortHandler(sharedPreferences.getInt("sort_type", 0));
        viewTypeHandler(sharedPreferences.getInt("view_type", 0));
        autoBackupHandler();

    }

    private void setDialog(DialogTabbed dialog) {
        this.dialog = dialog;
    }

    public static DialogTabbed getDialog() {
        return dialog;
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
            case R.id.Feedback:
                Intent intent = new Intent(MainActivity.this, ActivityFeedback.class);
                startActivity(intent);
                break;

            case R.id.Sort:
                allNotesList = GenericUtils.sortByTitle(allNotesList, desc);
                adapter.notifyDataSetChanged();
                desc = !desc;

                break;

            case R.id.Backup:
                Intent intentBackup = new Intent(MainActivity.this, ActivityBackup.class);
                intentBackup.putExtra("lastActivity", "main");
                startActivity(intentBackup);
                MainActivity.this.finish();
                break;

            case R.id.paymentsLog:
                Intent intentWage = new Intent(MainActivity.this, ActivityPaymentsLog.class);
                startActivity(intentWage);
                MainActivity.this.finish();
                break;

            case R.id.showOnMap:
                ArrayList<Double> noteLat = new ArrayList<Double>();
                ArrayList<Double> noteLong = new ArrayList<Double>();
                ArrayList<String> mNoteTitles = new ArrayList<String>();
                for (int i = 0; i < allNotesList.size(); i++) {
                    if (allNotesList.get(i).getLatitude() != 0 || allNotesList.get(i).getLongitude() != 0) {
                        noteLat.add(allNotesList.get(i).getLatitude());
                        noteLong.add(allNotesList.get(i).getLongitude());
                        mNoteTitles.add(allNotesList.get(i).getInterfaceTitle());
                    }
                }
                Intent mapIntent = new Intent(MainActivity.this, ActivityMaps.class);
                mapIntent.putExtra("NoteLat", noteLat);
                mapIntent.putExtra("NoteLong", noteLong);
                mapIntent.putExtra("Title", mNoteTitles);
                startActivity(mapIntent);
                break;

            case R.id.Settings:
                Intent intentSettings = new Intent(MainActivity.this, ActivitySettings.class);
                startActivity(intentSettings);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview.");

        recyclerView = findViewById(R.id.recycler_view);
        adapter = new RecyclerViewAdapterMain(allNotesList, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (allNotesList.isEmpty()) {
            splashScreen.setVisibility(View.VISIBLE);
        } else {
            splashScreen.setVisibility(View.GONE);
        }
    }


    private void loadNotes() {
        DatabaseHelper dbHelper = new DatabaseHelper(MainActivity.this);
        Cursor cursor = dbHelper.getAllNotes();


        if (cursor.moveToFirst()) {
            do {
                EntryTextNote noteEntry = new EntryTextNote();
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
        DatabaseHelper dbHelper = new DatabaseHelper(MainActivity.this);
        Cursor cursor = dbHelper.getAllChecklists();
        Cursor cursorItems;


        if (cursor.moveToFirst()) {
            do {

                EntryChecklistNote noteEntry = new EntryChecklistNote();

                noteEntry.setNoteTitle(cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_noteTitle)));
                int current_id = (cursor.getInt(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_ID)));
                noteEntry.setNoteID(current_id);
                noteEntry.setColor(cursor.getInt(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_color)));
                noteEntry.setCreateDate((cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_noteCreateDate))));
                noteEntry.setModDate(cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_noteModDate)));
                noteEntry.setLatitude(cursor.getDouble(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_noteLatitude)));
                noteEntry.setLongitude(cursor.getDouble(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_noteLongitude)));
                cursorItems = dbHelper.getChecklistItems(current_id);
                ArrayList<ChecklistItemEntry> checkItems = new ArrayList<>();
                if (cursorItems.moveToFirst()) {
                    do {
                        ChecklistItemEntry itemToLoad = new ChecklistItemEntry();
                        itemToLoad.setItemText(cursorItems.getString(cursorItems.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_Item_Text)));
                        checkItems.add(itemToLoad);
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

        if(clickState){
            onNoteLongClick(position);
        } else {

            int noteType = allNotesList.get(position).getListItemType();

            if (noteType == ListItem.typeText) {
                EntryTextNote textNote = (EntryTextNote) allNotesList.get(position);
                mNoteID = textNote.getNoteID();
                intent = new Intent(this, ActivityNewTextNote.class);
                intent.putExtra("noteID", mNoteID);
                intent.putExtra("flag", "editNote");

            } else {
                EntryChecklistNote checklistNote = (EntryChecklistNote) allNotesList.get(position);
                mNoteID = checklistNote.getNoteID();
                intent = new Intent(this, ActivityNewChecklist.class);
                intent.putExtra("noteID", mNoteID);
                intent.putExtra("flag", "editNote");
            }



            startActivity(intent);
            MainActivity.this.finish();

        }

    }

    @Override
    public boolean onNoteLongClick(int position) {
        FloatingActionButton addNote = findViewById(R.id.addNote);
        addNote.setVisibility(View.INVISIBLE);
        sortMenu.setClickable(false);
        clickState = true;

        if (!selectionTracker.contains(position)) {
            selectionTracker.add(position);
        } else {
            selectionTracker.remove((Integer) position);
        }

        if (mActionMode != null) {
            mActionMode.setTitle(selectionTracker.size() + "/" + allNotesList.size());
            return false;
        }


        mActionMode = startSupportActionMode(mActionModeCallback);


        return true;
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.contextual_menu, menu);
            mode.setTitle(selectionTracker.size() + "/" + allNotesList.size());
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            switch (item.getItemId()) {
                case R.id.deleteNotes:
                    String print = ":";
                    for (int i = 0; i < selectionTracker.size(); i++) {
                        print += String.valueOf(selectionTracker.get(i)) + ";";
                    }

                    if(selectionTracker.size() > 1) {
                        GenericUtils.toast(MainActivity.this, selectionTracker.size() + " notes deleted successfully");
                    } else{
                        GenericUtils.toast(MainActivity.this, "Your note deleted successfully");
                    }
                    deleteMultipleNotes();
                    mode.finish();
                    return true;

                default:
                    return false;
            }

        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
            FloatingActionButton addNote = findViewById(R.id.addNote);
            addNote.setVisibility(View.VISIBLE);
            selectionTracker.clear();
            clickState = false;
            sortMenu.setClickable(true);
        }
    };


    public void autoBackupHandler() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (sharedPreferences.getBoolean("backup_check", false)) {
            String receiverEmail = sharedPreferences.getString("backup_email", "");
            if (!receiverEmail.equals("")) {
                String currentTime = GenericUtils.getDateTime();
                String previousBackupDate = sharedPreferences.getString("backup_date", "");

                if (!previousBackupDate.equals("")) {
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        Date currentDate = dateFormat.parse(currentTime);
                        Date prevBackup = dateFormat.parse(previousBackupDate);
                        prevBackup = GenericUtils.addMonth(prevBackup);
                        if (currentDate.after(prevBackup)) {

                            new Thread(new Runnable() {

                                @Override
                                public void run() {
                                    if (GenericUtils.isOnline()) {
                                        try {
                                            EmailHandler sender = new EmailHandler("farmernotepad@gmail.com",
                                                    "farmernotepad123");
                                            sender.exportDbOnline("Your notes auto backup", "Auto-backed up database.",
                                                    "farmernotepad@gmail.com", receiverEmail, MainActivity.this);
                                            GenericUtils.toast(getApplicationContext(), "Database Exported");
                                        } catch (Exception e) {
                                            Log.e("SendMail", e.getMessage(), e);
                                            GenericUtils.toast(getApplicationContext(), "Error sending email.");
                                        }
                                    } else {
                                        GenericUtils.toast(getApplicationContext(), "No Internet connection found");
                                    }
                                }
                            }).start();

                            editor.putString("backup_date", currentTime);
                            editor.apply();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    editor.putString("backup_date", currentTime);
                    editor.apply();
                }
            } else {
                GenericUtils.toast(this, "Set email address for auto-backup.");
            }
        }
    }

    public void filterColor(int colorToFilter) {
        ArrayList<ListItem> filteredList = new ArrayList<ListItem>();
        if (colorToFilter == 0) {
            allNotesList.clear();
            allNotesList.addAll(concreteList);
            sortMenu.setBackgroundColor(getColor(R.color.background));
            sortMenu.setText("SORT MENU (All Notes)");
            sortMenu.setTextColor(getColor(R.color.Black));

        } else {
            for (int i = 0; i < concreteList.size(); i++) {
                if (concreteList.get(i).getColor() == colorToFilter) {
                    filteredList.add(concreteList.get(i));
                }
            }
            SharedPreferences sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(this);
            sortMenu.setBackgroundColor(colorToFilter);
            String textToSet = "Sort Menu (" + sharedPreferences.getString(String.valueOf(colorToFilter), "") + ")";
            sortMenu.setText(textToSet);

            if (colorToFilter == getColor(R.color.White) || colorToFilter == getColor(R.color.Yellow)) {
                sortMenu.setTextColor(getColor(R.color.Black));
            } else {
                sortMenu.setTextColor(getColor(R.color.White));
            }


            allNotesList.clear();
            allNotesList.addAll(filteredList);
        }
        adapter.notifyDataSetChanged();
    }

    public void sortHandler(int sortType) {
        switch (sortType) {
            case 0:
                allNotesList = GenericUtils.sortByCreateDate(allNotesList, desc);
                break;
            case 1:
                allNotesList = GenericUtils.sortByTitle(allNotesList, desc);
                break;
            case 2:
                allNotesList = GenericUtils.sortByModDate(allNotesList, desc);
                break;
            case 3:
                allNotesList = GenericUtils.sortByColor(allNotesList, desc);
                break;
        }
        adapter.notifyDataSetChanged();
        desc = !desc;

    }

    public void viewTypeHandler(int viewType) {
        switch (viewType) {
            case 0:
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                break;
            case 1:
                recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
                break;
            case 2:
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
                break;
        }
    }

    @Override
    public void onBackPressed() {

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);

        String defLaunchActivity = sharedPreferences.getString("default_home_screen", "Main");

        if (defLaunchActivity.equals("Payments")) {
            Intent intent = new Intent(MainActivity.this, ActivityPaymentsLog.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            MainActivity.this.finish();
        }

    }

    private void deleteMultipleNotes() {
        ArrayList<ListItem> toDelete = new ArrayList<>();
        DatabaseHelper dbHelper = new DatabaseHelper(MainActivity.this);
        for (int i = 0; i < selectionTracker.size(); i++) {
            ListItem toBeDeleted = allNotesList.get(selectionTracker.get(i));
            if (toBeDeleted.getListItemType() == ListItem.typeText) {
                dbHelper.deleteNote(toBeDeleted.getNoteID());
            } else {
                dbHelper.deleteChecklist(toBeDeleted.getNoteID());
            }
            toDelete.add(toBeDeleted);
        }
        dbHelper.close();
        allNotesList.removeAll(toDelete);
        adapter.notifyDataSetChanged();
    }

    public void filterByDate(int month, int year) throws ParseException {
        ArrayList<ListItem> filteredList = new ArrayList<ListItem>();
        SimpleDateFormat format1 =new SimpleDateFormat("dd/MM/yyyy");
        int noteMonth = 15;
        int noteYear = 30;
        Calendar calendar = Calendar.getInstance();


        for (int i = 0; i < concreteList.size(); i++) {
            Date noteDate = format1.parse(concreteList.get(i).getCreateDate());
            assert noteDate != null;
            calendar.setTime(noteDate);
            noteMonth = calendar.get(Calendar.MONTH);
            noteYear = calendar.get(Calendar.YEAR);
            if (noteMonth + 1 == month && noteYear == year) {
                filteredList.add(concreteList.get(i));
            }
        }
        allNotesList.clear();
        allNotesList.addAll(filteredList);
        adapter.notifyDataSetChanged();

        GenericUtils.toast(this,"Notes created on " + String.valueOf(month) + "/"+ String.valueOf(year));
    }


}
