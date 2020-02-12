package com.example.android.farmernotepad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class WageCalculatorActivity extends AppCompatActivity implements RecyclerViewAdapterWage.OnNoteListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    //RecyclerViewAdapterWage adapterWage;
    private ArrayList<Employee> employeesArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wage_calculator);

        employeesArrayList.add(new Employee("Peter",111));

        initRecyclerView();

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
        employeesArrayList.get(position);
        Intent intent = new Intent(WageCalculatorActivity.this, EmployeeActivity.class);
        startActivity(intent);
    }

    private void initRecyclerView(){

        mRecyclerView = findViewById(R.id.wageCalculatorRecyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new RecyclerViewAdapterWage(employeesArrayList, this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }
}
