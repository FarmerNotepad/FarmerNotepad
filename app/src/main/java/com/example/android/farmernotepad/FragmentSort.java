package com.example.android.farmernotepad;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.NumberPicker;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;


public class FragmentSort extends Fragment {

    ConstraintLayout alphabetical, createDate, modDate, byColor, filterByDate;
    boolean desc = false;
    MainActivity myActivity;

    public FragmentSort() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sort, container, false);

        myActivity = (MainActivity) getActivity();
        alphabetical = view.findViewById(R.id.alphabeticalLayout);
        createDate = view.findViewById(R.id.createLayout);
        modDate = view.findViewById(R.id.modLayout);
        byColor = view.findViewById(R.id.colorLayout);
        filterByDate = view.findViewById(R.id.filterLayout);

        alphabetical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(myActivity.getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("sort_type", 1);
                editor.apply();
                myActivity.sortHandler(1);
                DialogTabbed dialog = MainActivity.getDialog();
                dialog.dismiss();
            }
        });


        createDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(myActivity.getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("sort_type", 0);
                editor.apply();
                myActivity.sortHandler(0);
                DialogTabbed dialog = MainActivity.getDialog();
                dialog.dismiss();
            }
        });


        modDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(myActivity.getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("sort_type", 2);
                editor.apply();
                myActivity.sortHandler(2);
                DialogTabbed dialog = MainActivity.getDialog();
                dialog.dismiss();
            }
        });


        byColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(myActivity.getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("sort_type", 3);
                editor.apply();
                myActivity.sortHandler(3);
                DialogTabbed dialog = MainActivity.getDialog();
                dialog.dismiss();
            }
        });


        filterByDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(getContext());
                View mView = getLayoutInflater().inflate(R.layout.dialog_filter_date, null);

                final NumberPicker monthPicker = (NumberPicker) mView.findViewById(R.id.monthPicker);
                final NumberPicker yearPicker = (NumberPicker) mView.findViewById(R.id.yearPicker);

                Calendar cal = Calendar.getInstance();

                monthPicker.setMinValue(1);
                monthPicker.setMaxValue(12);
                monthPicker.setValue(cal.get(Calendar.MONTH) + 1);

                yearPicker.setMinValue(2020);
                yearPicker.setMaxValue(yearPicker.getMinValue() + 10);
                yearPicker.setValue(2020);

               alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            myActivity.filterByDate(monthPicker.getValue(),yearPicker.getValue());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });

                alert.setView(mView);
                final AlertDialog alertDialog = alert.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();

                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            myActivity.filterByDate(monthPicker.getValue(),yearPicker.getValue());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        alertDialog.dismiss();
                    }
                });

                GenericUtils.setDialogSize(alertDialog, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


            }
        });

        return view;
    }
}
