package com.example.android.farmernotepad;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
                DatabaseHelper dbHelper = new DatabaseHelper(Backup.this);
                dbHelper.exportDB(Backup.this);
                Toast.makeText(Backup.this,"Database exported to " + Backup.this.getExternalFilesDir(null),Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void mailDB(){
            //TO DO send email with database attached
    }
}


