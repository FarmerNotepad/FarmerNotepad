package com.example.android.farmernotepad;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

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

        initRecyclerView();

        confirmSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Employee mNewEmployee = new Employee();

                if (employeeFullName.getText().toString().trim().equals("") || employeeFullName.getText().toString().equals(null)) {
                    Toast.makeText(getApplicationContext(), "Fill Employee Name", Toast.LENGTH_SHORT).show();
                } else {
                    mNewEmployee.setEmployeeName(employeeFullName.getText().toString().trim());
                    mNewEmployee.setEmployeePhone(Double.parseDouble(employeePhoneNumber.getText().toString()));

                    ArrayList<WageEntry> employeePaymentItems = new ArrayList<>();

                    employeePaymentItems.addAll(mNewPaymentList);
                    mNewEmployee.setEmployeePaymentItems(employeePaymentItems);

                    DatabaseHelper dbHelper = new DatabaseHelper(EmployeeActivity.this);
                    Boolean checkInsert = dbHelper.insertEmployee(mNewEmployee);

                    if (checkInsert) {
                        Toast.makeText(getApplicationContext(), "Employee Saved", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EmployeeActivity.this, WageCalculatorActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Insertion Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        mNewPaymentList.add(new WageEntry(3.5, "DEH", 8, "14/2/2020"));

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

        final EditText newPaymentWorkHours = alertDialog.findViewById(R.id.newPaymentWorkHours);
        final EditText newPaymentRate = alertDialog.findViewById(R.id.newPaymentRate);
        final EditText newPaymentDescription = alertDialog.findViewById(R.id.newPaymentDescription);
        final CheckedTextView dayOffCheckedTextView = alertDialog.findViewById(R.id.dayOffCheckedTextView);
        final Calendar myCalendar = Calendar.getInstance();
        final EditText newPaymentDate = (EditText) alertDialog.findViewById(R.id.newPaymentDate);
        final TextView employmentDebt = (TextView) findViewById(R.id.employmentDebt);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateEditText(newPaymentDate, myCalendar);
            }
        };

        newPaymentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EmployeeActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                WageEntry mNewWageEntry = new WageEntry();

                if (newPaymentWorkHours == null || newPaymentRate == null) {
                    Toast.makeText(getApplicationContext(), "Update Failed", Toast.LENGTH_SHORT).show();
                }else{
                    mNewWageEntry.setWageCreateDate(GenericUtils.getDateTime());
                    mNewWageEntry.setWageWorkDate(newPaymentDate.getText().toString());
                    mNewWageEntry.setWageHours(Double.parseDouble(newPaymentWorkHours.getText().toString()));
                    mNewWageEntry.setWageRate(Double.parseDouble(newPaymentRate.getText().toString()));
                    mNewWageEntry.setWageDesc(newPaymentDescription.getText().toString());
                    int hours = Integer.parseInt(newPaymentWorkHours.getText().toString());
                    int rate = Integer.parseInt(newPaymentRate.getText().toString());
                    employmentDebt.setText(String.valueOf(hours*rate));

                    if (dayOffCheckedTextView.isChecked()) {
                        mNewWageEntry.setWageType(2);
                    } else {
                        mNewWageEntry.setWageType(1);
                    }
                    mNewPaymentList.add(mNewWageEntry);
                    Toast.makeText(getApplicationContext(), "Payment Added", Toast.LENGTH_SHORT).show();
                    mAdapter.notifyDataSetChanged();
                    alertDialog.dismiss();
                }
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
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNoteClick(int position) {

    }

    private void updateEditText(EditText newPaymentDate, Calendar myCalendar) {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        newPaymentDate.setText(sdf.format(myCalendar.getTime()));
    }
}
