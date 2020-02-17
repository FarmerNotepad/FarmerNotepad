package com.example.android.farmernotepad;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class EmployeeActivity extends AppCompatActivity implements RecyclerViewAdapterEmployee.OnNoteListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<WageEntry> mNewPaymentList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        FloatingActionButton confirmSaveButton = findViewById(R.id.saveEmployeeBtn);
        final EditText employeeFullName = (EditText) findViewById(R.id.employeeFullName);
        final EditText employeePhoneNumber = (EditText) findViewById(R.id.employeePhoneNumber);

        confirmSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Employee mNewEmployee = new Employee();

                if (employeeFullName.getText().toString().trim().equals("") || employeeFullName.getText().toString().equals(null)) {
                    Toast.makeText(getApplicationContext(), "Fill Employee Name", Toast.LENGTH_SHORT).show();
                } else {
                    mNewEmployee.setEmployeeName(employeeFullName.getText().toString().trim());
                    mNewEmployee.setEmployeePhone(Integer.parseInt(employeePhoneNumber.getText().toString()));

                    DatabaseHelper dbHelper = new DatabaseHelper(EmployeeActivity.this);
                    Boolean checkInsert = dbHelper.insertEmployee(mNewEmployee);
                    if(checkInsert){
                        Toast.makeText(getApplicationContext(), "Employee Saved", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EmployeeActivity.this, WageCalculatorActivity.class);
                        startActivity(intent);
                    } else{
                        Toast.makeText(getApplicationContext(), "Insertion Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //mNewPaymentList.add(new WageEntry(3.5,"DEH",8,"14/2/2020"));

        initRecyclerView();

        Button addPaymentDayOff = (Button) findViewById(R.id.addPaymentBtn);

        addPaymentDayOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPaymentDialogBox();
            }
        });

    }

    private void addPaymentDialogBox() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(EmployeeActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.add_payment_day_off_dialog_box, null);
        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();

        Button okButton = (Button) alertDialog.findViewById(R.id.addPaymentOk);
        Button cancelButton = (Button) alertDialog.findViewById(R.id.addPaymentCancel);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }

    private void initRecyclerView() {

        mRecyclerView = findViewById(R.id.addPaymentRecyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new RecyclerViewAdapterEmployee(mNewPaymentList, this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onNoteClick(int position) {

    }
}
