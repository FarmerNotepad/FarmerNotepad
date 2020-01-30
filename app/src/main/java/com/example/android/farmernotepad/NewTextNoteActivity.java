package com.example.android.farmernotepad;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.ActivityChooserView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.widget.Toast.LENGTH_SHORT;

public class NewTextNoteActivity extends AppCompatActivity {
    private static final int PERMISSION_COARSE_LOCATION = 177;
    private Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);


        final EditText noteTitle =  findViewById(R.id.editTitle);
        final EditText noteText = findViewById(R.id.editText);
        FloatingActionButton confirmSaveButton = findViewById(R.id.confirmSave);
        final CheckBox checkLocation = findViewById(R.id.checkBoxLoc);

        if(getIntent().hasExtra("flag")){

            getIncomingIntent();

        }else {


            confirmSaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextNoteEntry myNewTextNote = new TextNoteEntry();
                    Boolean checkPermission = checkPermission();

                    if (noteTitle.getText().toString().equals("")) {
                        myNewTextNote.setNoteTitle(getDateTime());
                    } else {
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
                    if (checkInsert = true) {
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
                        if (checkPerm == false) {
                            requestPermission();
                        }
                    } else {
                        Toast.makeText(NewTextNoteActivity.this, "Check after onclick", LENGTH_SHORT).show();
                    }
                }
            });

        }

    }



    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.note_menu, menu);
        this.mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.pickColor:
                final AlertDialog.Builder alert = new AlertDialog.Builder(NewTextNoteActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.color_picker_dialog_box,  null);
                alert.setView(mView);
                final MenuItem pickColorItem =  mMenu.findItem(R.id.pickColor);

                final AlertDialog alertDialog =alert.create();

                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.show();
                //Window window = alertDialog.getWindow();
                //window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                ImageButton buttonWhite = alertDialog.findViewById(R.id.colorWhite);
                buttonWhite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pickColorItem.setIcon(R.drawable.ic_stop_white_24dp);
                    }
                });
                ImageButton buttonRed = alertDialog.findViewById(R.id.colorRed);
                buttonRed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pickColorItem.setIcon(R.drawable.ic_stop_red_24dp);
                    }
                });
                ImageButton buttonBlue = alertDialog.findViewById(R.id.colorBlue);
                buttonBlue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pickColorItem.setIcon(R.drawable.ic_stop_blue_24dp);
                    }
                });
                ImageButton buttonGreen = alertDialog.findViewById(R.id.colorGreen);
                buttonGreen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pickColorItem.setIcon(R.drawable.ic_stop_green_24dp);
                    }
                });
                ImageButton buttonYellow = alertDialog.findViewById(R.id.colorYellow);
                buttonYellow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pickColorItem.setIcon(R.drawable.ic_stop_yellow_24dp);
                    }
                });
                ImageButton buttonGrey = alertDialog.findViewById(R.id.colorGrey);
                buttonGrey.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pickColorItem.setIcon(R.drawable.ic_stop_grey_24dp);
                    }
                });
                ImageButton buttonBlack = alertDialog.findViewById(R.id.colorBlack);
                buttonBlack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pickColorItem.setIcon(R.drawable.ic_stop_black_24dp);
                    }
                });
                ImageButton buttonOrange = alertDialog.findViewById(R.id.colorOrange);
                buttonOrange.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pickColorItem.setIcon(R.drawable.ic_stop_orange_24dp);
                    }
                });
                ImageButton buttonPurple = alertDialog.findViewById(R.id.colorPurple);
                buttonPurple.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pickColorItem.setIcon(R.drawable.ic_stop_purple_24dp);
                    }
                });



                break;
        }
        return super.onOptionsItemSelected(item);
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

    private void getIncomingIntent(){
        if(getIntent().hasExtra("flag")){
            int noteID = getIntent().getIntExtra("noteID", 0);
            loadEditableNote(noteID);
        }
    }


    private void loadEditableNote(int noteID){
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db =dbHelper.getReadableDatabase();
        Cursor cursor = dbHelper.getNote(noteID);

        if (cursor != null)
            cursor.moveToFirst();

                String textNoteTitle = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_noteTitle));
                String textNoteText = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_noteText));

        cursor.close();

        TextView title = findViewById(R.id.editTitle);
        TextView text = findViewById(R.id.editText);

        title.setText(textNoteTitle);
        text.setText(textNoteText);

    }

}

