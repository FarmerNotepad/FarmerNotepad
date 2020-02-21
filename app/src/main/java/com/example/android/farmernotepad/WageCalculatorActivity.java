package com.example.android.farmernotepad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class WageCalculatorActivity extends AppCompatActivity implements RecyclerViewAdapterWage.OnNoteListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Employee> employeesArrayList = new ArrayList<>();

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
    }

    private void initRecyclerView(){

        mRecyclerView = findViewById(R.id.wageCalculatorRecyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new RecyclerViewAdapterWage(employeesArrayList, this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void loadEmployees(){
        DatabaseHelper dbHelper = new DatabaseHelper(WageCalculatorActivity.this);
        Cursor cursor = dbHelper.getAllEmployees();

        if (cursor.moveToFirst()) {
            do{
                Employee newEmployee = new Employee();
                newEmployee.setEmployeeID(cursor.getInt(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_ID)));
                newEmployee.setEmployeeName(cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_emp_Name)));
                newEmployee.setEmployeePhone(cursor.getInt(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_emp_Phone)));
                newEmployee.setEmployeeSum(cursor.getDouble(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_emp_Sum)));
                employeesArrayList.add(newEmployee);

            }while(cursor.moveToNext());
        }
        cursor.close();
        initRecyclerView();
    }
}
