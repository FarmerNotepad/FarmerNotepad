package com.example.android.farmernotepad;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
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
    private ArrayList<EntryWage> mNewPaymentList = new ArrayList<>();
    private Menu mMenu;
    static EmployeeActivity activity;
    private int employeeIntentID;
    boolean desc = false;
    private EntryEmployee mEntryEmployee = new EntryEmployee();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        FloatingActionButton confirmSaveButton = findViewById(R.id.saveEmployeeBtn);
        final EditText employeeFullName = (EditText) findViewById(R.id.employeeFullName);
        final EditText employeePhoneNumber = (EditText) findViewById(R.id.employeePhoneNumber);
        TextView employeeTotalDebt = findViewById(R.id.employeeTotalDebt);
        Button addPaymentDayOff = (Button) findViewById(R.id.addPaymentBtn);
        activity = this;
        employeeIntentID = getIncomingIntent();

        addPaymentDayOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPaymentDialogBox();
            }
        });


        initRecyclerView();

        if (employeeIntentID != 0) {
            employeeFullName.setEnabled(false);
            employeePhoneNumber.setEnabled(false);
            addPaymentDayOff.setClickable(false);
            addPaymentDayOff.setVisibility(View.INVISIBLE);


            loadEditableEmployee(employeeIntentID);

            confirmSaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EntryEmployee mNewEntryEmployee = new EntryEmployee();
                    mNewEntryEmployee.setEmployeeID(employeeIntentID);

                    if (employeeFullName.getText().toString().trim().equals("") || employeeFullName.getText().toString().equals(null)) {
                        Toast.makeText(getApplicationContext(), "Fill Employee Name", Toast.LENGTH_SHORT).show();
                    } else {
                        mNewEntryEmployee.setEmployeeName(employeeFullName.getText().toString().trim());
                        mNewEntryEmployee.setEmployeePhone(employeePhoneNumber.getText().toString());
                        mNewEntryEmployee.setEmployeeSum(Double.parseDouble(employeeTotalDebt.getText().toString()));
                        mNewEntryEmployee.setEmployeePaymentItems(mNewPaymentList);

                        DatabaseHelper dbHelper = new DatabaseHelper(EmployeeActivity.this);
                        Boolean checkInsert = dbHelper.updateEmployee(mNewEntryEmployee);

                        if (checkInsert) {
                            Toast.makeText(getApplicationContext(), "Employee Saved", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EmployeeActivity.this, WageCalculatorActivity.class);
                            startActivity(intent);
                            dbHelper.close();
                            EmployeeActivity.this.finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Insertion Failed", Toast.LENGTH_SHORT).show();
                        }
                        dbHelper.close();
                    }
                }
            });

        } else {

            confirmSaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EntryEmployee mNewEntryEmployee = new EntryEmployee();

                    if (employeeFullName.getText().toString().trim().equals("") || employeeFullName.getText().toString().equals(null)) {
                        Toast.makeText(getApplicationContext(), "Fill Employee Name", Toast.LENGTH_SHORT).show();
                    } else {
                        mNewEntryEmployee.setEmployeeName(employeeFullName.getText().toString().trim());
                        mNewEntryEmployee.setEmployeePhone(employeePhoneNumber.getText().toString());
                        mNewEntryEmployee.setEmployeeSum(Double.parseDouble(employeeTotalDebt.getText().toString()));
                        mNewEntryEmployee.setEmployeePaymentItems(mNewPaymentList);

                        DatabaseHelper dbHelper = new DatabaseHelper(EmployeeActivity.this);
                        Boolean checkInsert = dbHelper.insertEmployee(mNewEntryEmployee);

                        if (checkInsert) {
                            Toast.makeText(getApplicationContext(), "Employee Saved", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EmployeeActivity.this, WageCalculatorActivity.class);
                            startActivity(intent);
                            dbHelper.close();
                            EmployeeActivity.this.finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Insertion Failed", Toast.LENGTH_SHORT).show();
                        }
                        dbHelper.close();
                    }
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
                findViewById(R.id.employeeFullName).setEnabled(true);
                findViewById(R.id.employeePhoneNumber).setEnabled(true);
                findViewById(R.id.addPaymentBtn).setVisibility(View.VISIBLE);
                findViewById(R.id.addPaymentBtn).setClickable(true);
                break;

            case R.id.sortPayments:
                GenericUtils.sortByPayment(mNewPaymentList, desc);
                mAdapter.notifyDataSetChanged();
                desc = !desc;
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
        final EditText newPaymentWage = alertDialog.findViewById(R.id.newPaymentWage);
        final EditText newPaymentDescription = alertDialog.findViewById(R.id.newPaymentDescription);
        final CheckedTextView dayOffCheckedTextView = alertDialog.findViewById(R.id.dayOffCheckedTextView);
        final Calendar myCalendar = Calendar.getInstance();
        final EditText newPaymentDate = alertDialog.findViewById(R.id.newPaymentDate);





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

        dayOffCheckedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayOffChecked(alertDialog);
            }
        });


        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EntryWage mNewEntryWage = new EntryWage();

                if (newPaymentWorkHours == null || newPaymentWage == null) {
                    Toast.makeText(getApplicationContext(), "Update Failed", Toast.LENGTH_SHORT).show();
                } else {
                    mNewEntryWage.setWageCreateDate(GenericUtils.getDateTime());
                    mNewEntryWage.setWageWorkDate(newPaymentDate.getText().toString());
                    mNewEntryWage.setWageDesc(newPaymentDescription.getText().toString());

                    String hours = newPaymentWorkHours.getText().toString().trim();

                    if (newPaymentWage.getText().toString().equals("")) {
                        mNewEntryWage.setWageWage(0);
                    } else {
                        mNewEntryWage.setWageWage(Double.parseDouble(newPaymentWage.getText().toString()));

                    }

                    if (GenericUtils.isNumeric(hours)) {
                        mNewEntryWage.setWageHours(Integer.parseInt(hours));
                    }

                    if (dayOffCheckedTextView.isChecked()) {
                        mNewEntryWage.setWageType(2);
                        mNewEntryWage.setWageDesc("DAY OFF");
                    } else {
                        mNewEntryWage.setWageType(1);
                    }
                    mNewPaymentList.add(mNewEntryWage);
                    Toast.makeText(getApplicationContext(), "Payment Added", Toast.LENGTH_SHORT).show();
                    mAdapter.notifyDataSetChanged();
                    alertDialog.dismiss();
                }
            }
        });

        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                okButton.callOnClick();
                dialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }

    private void editPaymentDialogBox(EntryWage paymentItem, final int position) {

        final AlertDialog.Builder alert = new AlertDialog.Builder(EmployeeActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.add_payment_day_off_dialog_box, null);
        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();

        Button okButton = alertDialog.findViewById(R.id.addPaymentOk);
        Button cancelButton =  alertDialog.findViewById(R.id.addPaymentCancel);

        final EditText newPaymentWorkHours = alertDialog.findViewById(R.id.newPaymentWorkHours);
        final EditText newPaymentWage = alertDialog.findViewById(R.id.newPaymentWage);
        final EditText newPaymentDescription = alertDialog.findViewById(R.id.newPaymentDescription);
        final CheckedTextView dayOffCheckedTextView = alertDialog.findViewById(R.id.dayOffCheckedTextView);
        final Calendar myCalendar = Calendar.getInstance();
        final EditText newPaymentDate = alertDialog.findViewById(R.id.newPaymentDate);


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

        if(paymentItem.getWageType() != 2) {
            newPaymentDate.setText(paymentItem.getWageWorkDate());
            newPaymentWorkHours.setText(String.valueOf((paymentItem.getWageHours())));
            newPaymentWage.setText(String.valueOf(paymentItem.getWageWage()));
            newPaymentDescription.setText(paymentItem.getWageDesc());
        } else{
            dayOffChecked(alertDialog);
        }

        dayOffCheckedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayOffChecked(alertDialog);
            }
        });



        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EntryWage mNewEntryWage = new EntryWage();

                if (newPaymentWorkHours == null || newPaymentWage == null) {
                    Toast.makeText(getApplicationContext(), "Update Failed", Toast.LENGTH_SHORT).show();
                } else {
                    mNewEntryWage.setWageCreateDate(GenericUtils.getDateTime());
                    mNewEntryWage.setWageWorkDate(newPaymentDate.getText().toString());
                    String hours = newPaymentWorkHours.getText().toString().trim();


                    if (newPaymentWage.getText().toString().equals("")) {
                        mNewEntryWage.setWageWage(0);
                    } else {
                        mNewEntryWage.setWageWage(Double.parseDouble(newPaymentWage.getText().toString()));
                    }

                    if (GenericUtils.isNumeric(hours)) {
                        mNewEntryWage.setWageHours(Integer.parseInt(hours));
                    }

                    mNewEntryWage.setWageDesc(newPaymentDescription.getText().toString());

                    if (dayOffCheckedTextView.isChecked()) {
                        mNewEntryWage.setWageType(2);
                        mNewEntryWage.setWageDesc("DAY OFF");
                    } else {
                        mNewEntryWage.setWageType(1);
                    }
                    mNewPaymentList.set(position, mNewEntryWage);
                    mAdapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), "Payment Added", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
            }
        });

        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                okButton.callOnClick();
                dialog.dismiss();
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
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                calcDebt();
            }
        });
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNoteClick(int position) {
        EntryWage paymentItem = mNewPaymentList.get(position);
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

    private void loadEditableEmployee(int employeeID) {

        EditText employeeNameEditText = findViewById(R.id.employeeFullName);
        EditText employeePhoneEditText = findViewById(R.id.employeePhoneNumber);
        TextView employeeTotalDebtTextView = findViewById(R.id.employeeTotalDebt);
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        Cursor cursor = dbHelper.getEmployee(employeeID);
        Cursor cursorItems = dbHelper.getEmployeeWages(employeeID);


        if (cursor != null)
            cursor.moveToFirst();


        mEntryEmployee.setEmployeeName(cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_emp_Name)));
        mEntryEmployee.setEmployeePhone(cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_emp_Phone)));
        mEntryEmployee.setEmployeeSum(cursor.getDouble(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_emp_Sum)));
        cursor.close();

        if (cursorItems.moveToFirst()) {
            do {
                EntryWage wageItems = new EntryWage();

                wageItems.setWageWorkDate(cursorItems.getString(cursorItems.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_wage_Date)));
                wageItems.setWageType(cursorItems.getInt(cursorItems.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_wage_Type)));
                wageItems.setWageDesc(cursorItems.getString(cursorItems.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_wage_Desc)));
                wageItems.setWageHours(cursorItems.getInt(cursorItems.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_wage_Hours)));
                wageItems.setWageWage(cursorItems.getDouble(cursorItems.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_wage_Wage)));
                mNewPaymentList.add(wageItems);


            } while (cursorItems.moveToNext());
        }
        cursorItems.close();

        mEntryEmployee.setEmployeePaymentItems(mNewPaymentList);
        employeeNameEditText.setText(mEntryEmployee.getEmployeeName());
        employeePhoneEditText.setText(mEntryEmployee.getEmployeePhone());
        employeeTotalDebtTextView.setText(String.valueOf(mEntryEmployee.getEmployeeSum()));

        dbHelper.close();

    }

    public void calcDebt() {
        double debt = 0;
        for (int i = 0; i < mNewPaymentList.size(); i++) {
            if (mNewPaymentList.get(i).getWageType() == 1) {
                debt = debt + mNewPaymentList.get(i).getWageWage();
            }
        }
        TextView employeeTotalDebt = findViewById(R.id.employeeTotalDebt);
        employeeTotalDebt.setText(String.valueOf(debt));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(EmployeeActivity.this, WageCalculatorActivity.class);
        startActivity(intent);
        EmployeeActivity.this.finish();
    }

    private void dayOffChecked(AlertDialog alertDialog){
        final CheckedTextView dayOffCheckedTextView = alertDialog.findViewById(R.id.dayOffCheckedTextView);
        final EditText newPaymentWorkHours = alertDialog.findViewById(R.id.newPaymentWorkHours);
        final EditText newPaymentWage = alertDialog.findViewById(R.id.newPaymentWage);
        final EditText newPaymentDescription = alertDialog.findViewById(R.id.newPaymentDescription);

        if(!dayOffCheckedTextView.isChecked()){
            newPaymentWorkHours.getText().clear();
            newPaymentWage.getText().clear();
            newPaymentDescription.getText().clear();
            newPaymentWorkHours.setPaintFlags(newPaymentWorkHours.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            newPaymentWage.setPaintFlags(newPaymentWage.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            newPaymentDescription.setPaintFlags(newPaymentDescription.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            newPaymentWorkHours.setEnabled(false);
            newPaymentWage.setEnabled(false);
            newPaymentDescription.setEnabled(false);
            dayOffCheckedTextView.setCheckMarkDrawable(R.drawable.ic_checked);
            dayOffCheckedTextView.setChecked(true);
        } else{
            newPaymentWorkHours.setPaintFlags(newPaymentWorkHours.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            newPaymentWage.setPaintFlags(newPaymentWage.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            newPaymentDescription.setPaintFlags(newPaymentDescription.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            newPaymentWorkHours.setEnabled(true);
            newPaymentWage.setEnabled(true);
            newPaymentDescription.setEnabled(true);
            dayOffCheckedTextView.setCheckMarkDrawable(null);
            dayOffCheckedTextView.setChecked(false);
        }
    }

}
