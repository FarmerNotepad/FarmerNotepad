package com.example.android.farmernotepad;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Backup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);

        final Button btnExport = findViewById(R.id.exportButton);
        final Button btnImport = findViewById(R.id.importButton);
        final EditText exportEmail = findViewById((R.id.editTextEmail));
        final CheckBox checkLocal = findViewById(R.id.checkBoxLocal);
        final CheckBox checkOnline = findViewById(R.id.checkBoxOnline);
        final TextView localPath = findViewById(R.id.textViewPath);

        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkLocal.isChecked()) {
                    DatabaseHelper dbHelper = new DatabaseHelper(Backup.this);
                    dbHelper.exportDB(Backup.this);
                    Toast.makeText(Backup.this, "Database exported to " + Backup.this.getExternalFilesDir(null), Toast.LENGTH_SHORT).show();
                }
                if (checkOnline.isChecked()) {
                    mailDb(view);
                }
            }
        });

    }


    public void mailDb(View view) {
        final EditText exportEmail = (EditText) findViewById(R.id.editTextEmail);

        new Thread(new Runnable() {

            @Override
            public void run() {
                if (GenericUtils.isOnline()) {
                    try {
                        EmailHandler sender = new EmailHandler("farmernotepad@gmail.com",
                                "farmernotepad123");
                        sender.exportDbOnline("Your notes backup", "This is your database file.",
                                "farmernotepad@gmail.com", exportEmail.getText().toString(), Backup.this);
                        //Toast.makeText(getApplicationContext(), "Database exported.", Toast.LENGTH_SHORT).show();
                        GenericUtils.toast(getApplicationContext(),"Database Exported");

                    } catch (Exception e) {
                        Log.e("SendMail", e.getMessage(), e);
                        Toast.makeText(getApplicationContext(), "Error sending email.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //Toast.makeText(getApplicationContext(), "No internet Connection found", Toast.LENGTH_SHORT).show();
                    GenericUtils.toast(getApplicationContext(),"No Internet connection found");
                }
            }
        }).start();
    }


}


