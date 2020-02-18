package com.example.android.farmernotepad;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class Backup extends AppCompatActivity {
    private static Backup activity;
    public static final int REQUEST_CODE = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);
        activity = this;

        final Button btnExport = findViewById(R.id.exportButton);
        final Button btnImport = findViewById(R.id.importButton);
        final EditText exportEmail = findViewById((R.id.editTextEmail));
        final CheckBox checkLocal = findViewById(R.id.checkBoxLocal);
        final CheckBox checkOnline = findViewById(R.id.checkBoxOnline);
        final TextView localPath = findViewById(R.id.textViewPath);

        if (!FileUtils.checkStoragePermission(Backup.this)){
            FileUtils.requestStoragePermission(activity);
        }

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

        btnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(Backup.this).create();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){
            case REQUEST_CODE:
                if(resultCode==RESULT_OK){
                    Uri uri = data.getData();
                    String realPath = FileUtils.getPathFromUri(Backup.this,uri);
                    //EditText checkPath = findViewById(R.id.editTextEmail);
                    //checkPath.setText(realPath);
                    if (realPath.endsWith("FarmerNotepad.db")) {
                        if (FileUtils.checkStoragePermission(Backup.this)){
                            File source = new File(realPath);
                            File dest = Backup.this.getDatabasePath(DatabaseHelper.DATABASE_NAME);

                            try {
                                FileUtils.copyDatabase(source, dest);
                                Toast.makeText(Backup.this, realPath, Toast.LENGTH_LONG).show();

                            }
                            catch (IOException e) {
                                Log.e("YOUR ERROR TAG HERE", "Copying failed", e);
                            }

                        }
                        else { Toast.makeText(Backup.this,"Requires external storage permission",Toast.LENGTH_LONG).show(); }
                    }
                    else {
                        Toast.makeText(Backup.this,"Error: Select a FarmerNotepad.db file", Toast.LENGTH_LONG).show();
                    }
                }
                break;

        }
    }


}


