package com.example.android.farmernotepad;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.widget.Toast.LENGTH_SHORT;

public class NewTextNoteActivity extends AppCompatActivity {
    private static final int PERMISSION_COARSE_LOCATION = 177;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        final EditText noteTitle =  findViewById(R.id.editTitle);
        final EditText noteText = findViewById(R.id.editText);
        FloatingActionButton confirmSaveButton = findViewById(R.id.confirmSave);
        final CheckBox checkLocation = findViewById(R.id.checkBoxLoc);


        confirmSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextNoteEntry myNewTextNote = new TextNoteEntry();
                Boolean checkPermission = checkPermission();

                if (noteTitle.getText().toString().equals("")){
                        myNewTextNote.setNoteTitle(getDateTime()); }
                    else {
                        myNewTextNote.setNoteTitle(noteTitle.getText().toString());
                }
                myNewTextNote.setNoteText(noteText.getText().toString());
                myNewTextNote.setCreateDate(getDateTime());
                myNewTextNote.setModDate(getDateTime());
                //myNewTextNote.setColor(TO DO);
                if (checkPermission == true && checkLocation.isChecked()) {
                    double[] myCoords = getLocation();
                    if (myCoords != null) {
                        myNewTextNote.setLatitude(myCoords[0]);
                        myNewTextNote.setLongitude(myCoords[1]);
                    }
                }
                DatabaseHelper dbHelper = new DatabaseHelper(NewTextNoteActivity.this);
                Boolean checkInsert = dbHelper.insertNote(myNewTextNote);
                if (checkInsert == true) {
                    Toast.makeText(getApplicationContext(), "Note Saved", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(NewTextNoteActivity.this, MainActivity.class);
                    startActivity(intent);
                    NewTextNoteActivity.this.finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Insertion Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        checkLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkLocation.isChecked()) {
                    Boolean checkPerm = checkPermission();
                    if (checkPerm == false){
                        requestPermission();
                    }
                }
                else {
                    Toast.makeText(NewTextNoteActivity.this,"Check after onclick", LENGTH_SHORT).show();
                }
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.note_menu, menu);
        return true;
    }

     private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private double[] getLocation(){
        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED ) {
            LocationManager mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            MyLocationListener myLocationListener = new MyLocationListener();
            if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                mLocationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, myLocationListener, null);
                Location loc = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (loc != null) {
                    double[] coords = {loc.getLatitude(), loc.getLongitude()};
                    //String longi = String.valueOf(coords[0]);
                    //Toast.makeText(getApplicationContext(), longi, LENGTH_SHORT).show();
                    return coords;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
            else {
                return null;
            }
        }

    private boolean checkPermission(){
        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            return false;
        }
        else {
            return true;
        }
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions( this, new String[] { android.Manifest.permission.ACCESS_COARSE_LOCATION},
                PERMISSION_COARSE_LOCATION);
    }


}

