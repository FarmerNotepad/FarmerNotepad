package com.example.android.farmernotepad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class WageCalculatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wage_calculator);

        FloatingActionButton addEmployee = (FloatingActionButton) findViewById(R.id.addEmployeeBtn);

        addEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WageCalculatorActivity.this, EmployeeActivity.class);
                startActivity(intent);
            }
        });
    }
}
