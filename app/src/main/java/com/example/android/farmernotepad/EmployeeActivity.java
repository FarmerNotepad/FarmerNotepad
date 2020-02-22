package com.example.android.farmernotepad;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.widget.Toast.LENGTH_SHORT;

public class EmployeeActivity extends AppCompatActivity implements RecyclerViewAdapterEmployee.OnNoteListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<WageEntry> mNewPaymentList = new ArrayList<>();
    private Menu mMenu;
    static EmployeeActivity activity;
    private int employeeIntentID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        FloatingActionButton confirmSaveButton = findViewById(R.id.saveEmployeeBtn);
        final EditText employeeFullName = (EditText) findViewById(R.id.employeeFullName);
        final EditText employeePhoneNumber = (EditText) findViewById(R.id.employeePhoneNumber);
        activity = this;
        employeeIntentID = getIncomingIntent();


        initRecyclerView();

        if (employeeIntentID != 0) {

            final Float totalDebt = loadEditableEmployee(employeeIntentID);
                TextView employeeTotalDebt = findViewById(R.id.employeeTotalDebt);
            employeeTotalDebt.setText(totalDebt.toString());

            confirmSaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Employee mNewEmployee = new Employee();
                    mNewEmployee.setEmployeeID(employeeIntentID);

                    if (employeeFullName.getText().toString().trim().equals("") || employeeFullName.getText().toString().equals(null)) {
                        Toast.makeText(getApplicationContext(), "Fill Employee Name", Toast.LENGTH_SHORT).show();
                    } else {
                        mNewEmployee.setEmployeeName(employeeFullName.getText().toString().trim());
                        mNewEmployee.setEmployeePhone(Double.parseDouble(employeePhoneNumber.getText().toString()));
                        mNewEmployee.setEmployeeSum(totalDebt);

                        ArrayList<WageEntry> employeePaymentItems = new ArrayList<>();

                        employeePaymentItems.addAll(mNewPaymentList);
                        mNewEmployee.setEmployeePaymentItems(employeePaymentItems);

                        DatabaseHelper dbHelper = new DatabaseHelper(EmployeeActivity.this);
                        Boolean checkInsert = dbHelper.updateEmployee(mNewEmployee);

                        if (checkInsert) {
                            Toast.makeText(getApplicationContext(), "Employee Saved", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EmployeeActivity.this, WageCalculatorActivity.class);
                            startActivity(intent);
                            dbHelper.close();
                        } else {
                            Toast.makeText(getApplicationContext(), "Insertion Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

        } else {

            confirmSaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Employee mNewEmployee = new Employee();

                    if (employeeFullName.getText().toString().trim().equals("") || employeeFullName.getText().toString().equals(null)) {
                        Toast.makeText(getApplicationContext(), "Fill Employee Name", Toast.LENGTH_SHORT).show();
                    } else {
                        mNewEmployee.setEmployeeName(employeeFullName.getText().toString().trim());
                        mNewEmployee.setEmployeePhone(Double.parseDouble(employeePhoneNumber.getText().toString()));
                        //mNewEmployee .setEmployeeSum();

                        ArrayList<WageEntry> employeePaymentItems = new ArrayList<>();

                        employeePaymentItems.addAll(mNewPaymentList);
                        mNewEmployee.setEmployeePaymentItems(employeePaymentItems);

                        DatabaseHelper dbHelper = new DatabaseHelper(EmployeeActivity.this);
                        Boolean checkInsert = dbHelper.insertEmployee(mNewEmployee);

                        if (checkInsert) {
                            Toast.makeText(getApplicationContext(), "Employee Saved", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EmployeeActivity.this, WageCalculatorActivity.class);
                            startActivity(intent);
                            dbHelper.close();
                        } else {
                            Toast.makeText(getApplicationContext(), "Insertion Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

            mNewPaymentList.add(new WageEntry());

            Button addPaymentDayOff = (Button) findViewById(R.id.addPaymentBtn);
            addPaymentDayOff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addPaymentDialogBox();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (employeeIntentID != 0) {
            inflater.inflate(R.menu.employee_edit_menu, menu);
        } else {
            //inflater.inflate(R.menu.note_menu, menu);
        }
        this.mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteEmployee:
                final android.app.AlertDialog alertDeleteDialog = new android.app.AlertDialog.Builder(EmployeeActivity.this).create();
                alertDeleteDialog.setTitle("Delete Employee");
                alertDeleteDialog.setMessage("Delete this Employee?");
                alertDeleteDialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
                                Boolean checkDelete = dbHelper.deleteEmployee(employeeIntentID);
                                if (checkDelete) {
                                    Intent intent = new Intent(EmployeeActivity.this, WageCalculatorActivity.class);
                                    startActivity(intent);
                                    EmployeeActivity.this.finish();
                                    Toast.makeText(getApplicationContext(), "Employee Deleted", LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Deletion Failed", LENGTH_SHORT).show();
                                }
                                alertDeleteDialog.dismiss();
                                dbHelper.close();
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

            case R.id.editEmployee:
                //findViewById(R.id.checklistTitleEditText).setEnabled(true);
                //findViewById(R.id.addChecklistItemButton).setClickable(true);
                //findViewById(R.id.addChecklistItemButton).setVisibility(View.VISIBLE);
                break;

        }
            return super.onOptionsItemSelected(item);
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
                } else {
                    mNewWageEntry.setWageCreateDate(GenericUtils.getDateTime());
                    mNewWageEntry.setWageWorkDate(newPaymentDate.getText().toString());
                    mNewWageEntry.setWageHours(Double.parseDouble(newPaymentWorkHours.getText().toString()));
                    mNewWageEntry.setWageRate(Double.parseDouble(newPaymentRate.getText().toString()));
                    mNewWageEntry.setWageDesc(newPaymentDescription.getText().toString());
                    int hours = Integer.parseInt(newPaymentWorkHours.getText().toString());
                    int rate = Integer.parseInt(newPaymentRate.getText().toString());
                    employmentDebt.setText(String.valueOf(hours * rate));

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

    private void editPaymentDialogBox(WageEntry paymentItem, final int position) {

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

        newPaymentDate.setText(paymentItem.getWageWorkDate());
        newPaymentWorkHours.setText(String.valueOf((int) paymentItem.getWageHours()));
        newPaymentRate.setText(String.valueOf((int) paymentItem.getWageRate()));
        newPaymentDescription.setText(paymentItem.getWageDesc());

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                WageEntry mNewWageEntry = new WageEntry();

                if (newPaymentWorkHours == null || newPaymentRate == null) {
                    Toast.makeText(getApplicationContext(), "Update Failed", Toast.LENGTH_SHORT).show();
                } else {
                    mNewWageEntry.setWageCreateDate(GenericUtils.getDateTime());
                    mNewWageEntry.setWageWorkDate(newPaymentDate.getText().toString());
                    mNewWageEntry.setWageHours(Double.parseDouble(newPaymentWorkHours.getText().toString()));
                    mNewWageEntry.setWageRate(Double.parseDouble(newPaymentRate.getText().toString()));
                    mNewWageEntry.setWageDesc(newPaymentDescription.getText().toString());
                    int hours = Integer.parseInt(newPaymentWorkHours.getText().toString());
                    int rate = Integer.parseInt(newPaymentRate.getText().toString());
                    employmentDebt.setText(String.valueOf(hours * rate));

                    if (dayOffCheckedTextView.isChecked()) {
                        mNewWageEntry.setWageType(2);
                    } else {
                        mNewWageEntry.setWageType(1);
                    }
                    mNewPaymentList.set(position, mNewWageEntry);
                    mAdapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), "Payment Added", Toast.LENGTH_SHORT).show();
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
        WageEntry paymentItem = loadEditablePaymentItem(employeeIntentID, position);
        editPaymentDialogBox(paymentItem, position);
    }

    private void updateEditText(EditText newPaymentDate, Calendar myCalendar) {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        newPaymentDate.setText(sdf.format(myCalendar.getTime()));
    }

    private int getIncomingIntent() {
        if (getIntent().hasExtra("flag")) {
            return getIntent().getIntExtra("employeeID", 0);
        } else {
            return 0;
        }
    }

    private float loadEditableEmployee(int employeeID) {

        EditText employeeNameEditText = findViewById(R.id.employeeFullName);
        EditText employeePhoneEditText = findViewById(R.id.employeePhoneNumber);
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        Cursor cursor = dbHelper.getEmployee(employeeID);
        Cursor cursorItems = dbHelper.getEmployeeWages(employeeID);
        float totalEmployeeDebt = 0;

        if (cursor != null)
            cursor.moveToFirst();

        String employeeFullName = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_emp_Name));
        String employeePhone = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_emp_Phone));
        cursor.close();

        if (cursorItems.moveToFirst()) {
            do {
                WageEntry wageItems = new WageEntry();

                wageItems.setWageWorkDate(cursorItems.getString(cursorItems.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_wage_Date)));
                wageItems.setWageType(cursorItems.getInt(cursorItems.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_wage_Type)));
                wageItems.setWageDesc(cursorItems.getString(cursorItems.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_wage_Desc)));
                wageItems.setWageHours(cursorItems.getDouble(cursorItems.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_wage_Hours)));
                wageItems.setWageRate(cursorItems.getDouble(cursorItems.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_wage_Rate)));
                mNewPaymentList.add(wageItems);

                totalEmployeeDebt += (float) (wageItems.getWageHours() * wageItems.getWageRate());

            } while (cursorItems.moveToNext());
        }
        cursorItems.close();

        employeeNameEditText.setText(employeeFullName);
        employeePhoneEditText.setText(employeePhone);

        return totalEmployeeDebt;
    }

    private WageEntry loadEditablePaymentItem(int employeeID, int position) {

        WageEntry wageItem = new WageEntry();

        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        Cursor cursorItems = dbHelper.getEmployeeWages(employeeID);

        if (cursorItems.moveToPosition(position)) {
            wageItem.setWageWorkDate(cursorItems.getString(cursorItems.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_wage_Date)));
            wageItem.setWageType(cursorItems.getInt(cursorItems.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_wage_Type)));
            wageItem.setWageDesc(cursorItems.getString(cursorItems.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_wage_Desc)));
            wageItem.setWageHours(cursorItems.getDouble(cursorItems.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_wage_Hours)));
            wageItem.setWageRate(cursorItems.getDouble(cursorItems.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_wage_Rate)));
        }
        cursorItems.close();

        return wageItem;
    }

}
