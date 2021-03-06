package com.example.android.farmernotepad;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ActivityPaymentsLog extends AppCompatActivity implements RecyclerViewAdapterWage.OnNoteListener {

    private RecyclerView mRecyclerView;
    private RecyclerViewAdapterWage mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<EntryEmployee> employeesArrayList = new ArrayList<>();
    boolean desc = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments_log);

        findViewById(R.id.wage_calculator).setOnTouchListener(new OnSwipeTouchListener(ActivityPaymentsLog.this) {

            public void onSwipeRight() {
                Intent intent = new Intent(ActivityPaymentsLog.this, MainActivity.class);
                startActivity(intent);
                ActivityPaymentsLog.this.finish();
            }


            public void onSwipeLeft() {
                Intent intent = new Intent(ActivityPaymentsLog.this, MainActivity.class);
                startActivity(intent);
                ActivityPaymentsLog.this.finish();
            }

        });


        loadEmployees();

        FloatingActionButton addEmployee = (FloatingActionButton) findViewById(R.id.addEmployeeBtn);

        addEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityPaymentsLog.this, ActivityEmployee.class);
                startActivity(intent);
                ActivityPaymentsLog.this.finish();
            }
        });
    }

    @Override
    public void onNoteClick(int position) {
        int mEmployeeID;
        EntryEmployee mEntryEmployee = (EntryEmployee) employeesArrayList.get(position);
        Intent intent = new Intent(ActivityPaymentsLog.this, ActivityEmployee.class);
        mEmployeeID = mEntryEmployee.getEmployeeID();
        intent.putExtra("employeeID", mEmployeeID);
        intent.putExtra("flag", "editEmployee");

        startActivity(intent);
        ActivityPaymentsLog.this.finish();
    }

    private void initRecyclerView() {

        mRecyclerView = findViewById(R.id.wageCalculatorRecyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new RecyclerViewAdapterWage(employeesArrayList, this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.payments_log_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.employeesSearch);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.farmerNotepad:
                Intent intentFarmerNotepad = new Intent(ActivityPaymentsLog.this, MainActivity.class);
                startActivity(intentFarmerNotepad);
                break;
            case R.id.employeesSort:
                employeesArrayList = GenericUtils.sortByTotalDebt(employeesArrayList, desc);
                mAdapter.notifyDataSetChanged();
                desc = !desc;
                break;

            case R.id.employeesBackup:
                Intent intentBackup = new Intent(ActivityPaymentsLog.this, ActivityBackup.class);
                intentBackup.putExtra("lastActivity", "wageCalc");
                startActivity(intentBackup);
                ActivityPaymentsLog.this.finish();
                break;

            case R.id.employeesFeedback:
                Intent intentFeedBack = new Intent(ActivityPaymentsLog.this, ActivityFeedback.class);
                startActivity(intentFeedBack);
                break;

            case R.id.employeesSettings:
                Intent intentSettings = new Intent(ActivityPaymentsLog.this, ActivitySettings.class);
                startActivity(intentSettings);
                break;


        }

        return super.onOptionsItemSelected(item);
    }

    private void loadEmployees() {
        DatabaseHelper dbHelper = new DatabaseHelper(ActivityPaymentsLog.this);
        Cursor cursor = dbHelper.getAllEmployees();

        if (cursor.moveToFirst()) {
            do {
                EntryEmployee newEntryEmployee = new EntryEmployee();
                newEntryEmployee.setEmployeeID(cursor.getInt(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_ID)));
                newEntryEmployee.setEmployeeName(cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_emp_Name)));
                newEntryEmployee.setEmployeePhone(cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_emp_Phone)));
                newEntryEmployee.setEmployeeSum(cursor.getDouble(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_emp_Sum)));
                employeesArrayList.add(newEntryEmployee);

            } while (cursor.moveToNext());
        }
        cursor.close();
        dbHelper.close();
        initRecyclerView();
    }

    @Override
    public void onBackPressed() {

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);

        String defLaunchActivity = sharedPreferences.getString("default_home_screen", "Main");

        if (defLaunchActivity.equals("Payments")) {
            ActivityPaymentsLog.this.finish();
        } else {
            Intent intent = new Intent(ActivityPaymentsLog.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            ActivityPaymentsLog.this.finish();
        }

    }

}