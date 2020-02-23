package com.example.android.farmernotepad;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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

public class WageCalculatorActivity extends AppCompatActivity implements RecyclerViewAdapterWage.OnNoteListener {

    private RecyclerView mRecyclerView;
    private RecyclerViewAdapterWage mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Employee> employeesArrayList = new ArrayList<>();
    boolean desc = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wage_calculator);

        loadEmployees();

        FloatingActionButton addEmployee = (FloatingActionButton) findViewById(R.id.addEmployeeBtn);

        addEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WageCalculatorActivity.this, EmployeeActivity.class);
                startActivity(intent);
                WageCalculatorActivity.this.finish();
            }
        });
    }

    @Override
    public void onNoteClick(int position) {
        int mEmployeeID;
        Employee mEmployee = (Employee) employeesArrayList.get(position);
        Intent intent = new Intent(WageCalculatorActivity.this, EmployeeActivity.class);
        mEmployeeID = mEmployee.getEmployeeID();
        intent.putExtra("employeeID", mEmployeeID);
        intent.putExtra("flag", "editEmployee");

        startActivity(intent);
        WageCalculatorActivity.this.finish();
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
        inflater.inflate(R.menu.wage_calculator_menu, menu);

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
        switch (item.getItemId()){
            case R.id.employeesSort:
                employeesArrayList = GenericUtils.sortByTotalDebt(employeesArrayList,desc);
                mAdapter.notifyDataSetChanged();
                desc = !desc;
                break;

            case R.id.employeesBackup:
                Intent intentBackup = new Intent(WageCalculatorActivity.this, Backup.class);
                startActivity(intentBackup);
                break;

            case R.id.employeesFeedback:
                Intent intent = new Intent(WageCalculatorActivity.this, FeedbackActivity.class);
                startActivity(intent);
                break;


        }

        return super.onOptionsItemSelected(item);
    }

    private void loadEmployees() {
        DatabaseHelper dbHelper = new DatabaseHelper(WageCalculatorActivity.this);
        Cursor cursor = dbHelper.getAllEmployees();

        if (cursor.moveToFirst()) {
            do {
                Employee newEmployee = new Employee();
                newEmployee.setEmployeeID(cursor.getInt(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_ID)));
                newEmployee.setEmployeeName(cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_emp_Name)));
                newEmployee.setEmployeePhone(cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_emp_Phone)));
                newEmployee.setEmployeeSum(cursor.getDouble(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_emp_Sum)));
                employeesArrayList.add(newEmployee);

            } while (cursor.moveToNext());
        }
        cursor.close();
        dbHelper.close();
        initRecyclerView();
    }
}
