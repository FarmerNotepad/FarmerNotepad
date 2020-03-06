package com.example.android.farmernotepad;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import androidx.appcompat.app.AppCompatActivity;

public class BackupActivity extends AppCompatActivity {
    private static BackupActivity activity;
    public static final int REQUEST_CODE = 5;
    String lastActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);
        lastActivity = getIncomingIntent();
        activity = this;

        final Button btnExport = findViewById(R.id.exportButton);
        final Button btnImport = findViewById(R.id.importButton);
        final EditText exportEmail = findViewById((R.id.editTextEmail));
        final CheckBox checkLocal = findViewById(R.id.checkBoxLocal);
        final CheckBox checkOnline = findViewById(R.id.checkBoxOnline);

        if (!FileUtils.checkStoragePermission(BackupActivity.this)) {
            FileUtils.requestStoragePermission(activity);
        }

        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkLocal.isChecked()) {
                    DatabaseHelper dbHelper = new DatabaseHelper(BackupActivity.this);
                        dbHelper.exportDB(BackupActivity.this);
                    Toast.makeText(BackupActivity.this, "Database exported to " + BackupActivity.this.getExternalFilesDir(null), Toast.LENGTH_SHORT).show();
                }
                if (checkOnline.isChecked()) {
                    mailDb(view);
                }
            }
        });

        btnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(BackupActivity.this).create();
                alertDialog.setTitle("Warning");
                alertDialog.setMessage("This will delete your current notes");
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                Intent filePickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                filePickerIntent.setType("*/*");
                                startActivityForResult(filePickerIntent, REQUEST_CODE);
                            }
                        });
                alertDialog.show();

            }
        });

    }


    public void mailDb(View view) {
        final EditText exportEmail = (EditText) findViewById(R.id.editTextEmail);
        ProgressDialog pDialog = new ProgressDialog(BackupActivity.this);
        pDialog.setMessage("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        new Thread(new Runnable() {

            @Override
            public void run() {

                if (GenericUtils.isOnline()) {
                    if (!exportEmail.getText().toString().equals("")) {
                        try {
                            EmailHandler sender = new EmailHandler("farmernotepad@gmail.com",
                                    "farmernotepad123");
                            sender.exportDbOnline("Your notes backup", "This is your database file.",
                                    "farmernotepad@gmail.com", exportEmail.getText().toString(), BackupActivity.this);
                            pDialog.dismiss();
                            GenericUtils.toast(getApplicationContext(), "Database Exported");

                        } catch (Exception e) {
                            Log.e("SendMail", e.getMessage(), e);
                            pDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Error sending email.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        pDialog.dismiss();
                        GenericUtils.toast(getApplicationContext(),"Please specify an email adress.");
                    }
                } else{
                        pDialog.dismiss();
                        GenericUtils.toast(getApplicationContext(), "No Internet connection found");
                    }
                }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    String realPath = FileUtils.getPathFromUri(BackupActivity.this, uri);


                    if (realPath.endsWith("FarmerNotepad.db")) {
                        if (FileUtils.checkStoragePermission(BackupActivity.this)) {
                            File source = new File(realPath);
                            File dest = BackupActivity.this.getDatabasePath(DatabaseHelper.DATABASE_NAME);

                            try {
                                FileUtils.copyDatabase(source, dest, BackupActivity.this);
                                //GenericUtils.toast(Backup.this, realPath);
                                Intent intent = new Intent();

                                if (lastActivity.matches("main")) {
                                    intent = new Intent(BackupActivity.this, MainActivity.class);
                                } else if (lastActivity.matches("wageCalc")) {
                                    intent = new Intent(BackupActivity.this, WageCalculatorActivity.class);

                                }
                                startActivity(intent);
                                BackupActivity.this.finish();

                            } catch (Exception e) {
                                Log.e("YOUR ERROR TAG HERE", "Copying failed", e);
                            }

                        } else {
                            Toast.makeText(BackupActivity.this, "Requires external storage permission", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(BackupActivity.this, "Error: Select a FarmerNotepad.db file", Toast.LENGTH_LONG).show();
                    }
                }
                break;

        }
    }

    private String getIncomingIntent() {
        if (getIntent().hasExtra("lastActivity")) {
            return getIntent().getStringExtra("lastActivity");
        } else {
            return "WRONG";
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();

        if (lastActivity.matches("main")) {
            intent = new Intent(BackupActivity.this, MainActivity.class);
        } else if (lastActivity.matches("wageCalc")) {
            intent = new Intent(BackupActivity.this, WageCalculatorActivity.class);

        }
        startActivity(intent);
        BackupActivity.this.finish();
    }


}


