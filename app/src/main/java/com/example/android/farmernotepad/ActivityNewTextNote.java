package com.example.android.farmernotepad;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;

import static android.widget.Toast.LENGTH_SHORT;
import static com.example.android.farmernotepad.GenericUtils.CHANNEL_ID;

public class ActivityNewTextNote extends AppCompatActivity {
    private Menu mMenu;
    private int noteColor;
    static ActivityNewTextNote activity;
    String currentPhotoPath;
    private int noteIntentID;
    ImageView attachedImage;
    FloatingActionButton deleteImage;
    androidx.appcompat.widget.Toolbar toolbar;

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    static final int REQUEST_TAKE_PHOTO = 1;


    ArrayList<Double> noteLat = new ArrayList<Double>();
    ArrayList<Double> noteLong = new ArrayList<Double>();
    ArrayList<String> mNoteTitle = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        toolbar = findViewById(R.id.newTxtNoteToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        noteColor = Color.parseColor(sharedPreferences.getString("default_color", "#FFFFFF"));
        activity = this;

        attachedImage = findViewById(R.id.newTextNoteImagePlaceholder);
        deleteImage = findViewById(R.id.deleteImageBtnText);
        attachedImage.setVisibility(View.GONE);
        deleteImage.setVisibility(View.GONE);


        final EditText noteTitle = findViewById(R.id.editTitle);
        final EditText noteText = findViewById(R.id.editText);
        FloatingActionButton confirmSaveButton = findViewById(R.id.confirmSave);
        final CheckBox checkLocation = findViewById(R.id.checkBoxLoc);
        noteIntentID = getIncomingIntent();
        LinearLayout newTxtNoteBackground = findViewById(R.id.newTxtNoteBackground);

        newTxtNoteBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteText.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });

        deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attachedImage.setImageDrawable(null);

                attachedImage.setVisibility(View.GONE);
                deleteImage.setVisibility(View.GONE);
            }
        });

        attachedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (attachedImage != null || attachedImage.getDrawable() != null) {

                    byte[] byteImageView = imageViewToByte(attachedImage);

                    Intent intentDisplay = new Intent(ActivityNewTextNote.this, ActivityDisplayImage.class);
                    intentDisplay.putExtra("picture", byteImageView);
                    startActivity(intentDisplay);
                }
            }
        });

        if (noteIntentID != 0) {

            checkLocation.setVisibility(View.INVISIBLE);
            noteText.setEnabled(false);
            noteTitle.setEnabled(false);
            newTxtNoteBackground.setEnabled(false);
            loadEditableNote(noteIntentID);

            confirmSaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EntryTextNote myNewTextNote = new EntryTextNote();
                    myNewTextNote.setNoteID(noteIntentID);

                    if (noteTitle.getText().toString().equals("")) {
                        myNewTextNote.setNoteTitle(GenericUtils.getDateTime());
                    } else {
                        myNewTextNote.setNoteTitle(noteTitle.getText().toString());
                    }
                    myNewTextNote.setNoteText(noteText.getText().toString());
                    myNewTextNote.setModDate(GenericUtils.getDateTime());
                    myNewTextNote.setColor(noteColor);

                    DatabaseHelper dbHelper = new DatabaseHelper(ActivityNewTextNote.this);
                    Boolean checkInsert = dbHelper.updateNote(myNewTextNote);

                    if (attachedImage.getDrawable() == null) {
                        Boolean deleteImage = dbHelper.deleteTextImage(noteIntentID);
                    } else {
                        Boolean updateImage = dbHelper.updateTextImage(noteIntentID, imageViewToByte(attachedImage));
                    }


                    if (checkInsert == true) {
                        Toast.makeText(getApplicationContext(), "Note Updated", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ActivityNewTextNote.this, MainActivity.class);
                        startActivity(intent);
                        ActivityNewTextNote.this.finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Update Failed", Toast.LENGTH_SHORT).show();
                    }

                    dbHelper.close();
                }
            });


        } else {


            confirmSaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EntryTextNote myNewTextNote = new EntryTextNote();
                    Boolean checkPermission = LocationFunctions.checkPermission(ActivityNewTextNote.this);

                    if (noteTitle.getText().toString().equals("")) {
                        myNewTextNote.setNoteTitle(GenericUtils.getDateTime());
                    } else {
                        myNewTextNote.setNoteTitle(noteTitle.getText().toString());
                    }
                    myNewTextNote.setNoteText(noteText.getText().toString());
                    myNewTextNote.setCreateDate(GenericUtils.getDateTime());
                    myNewTextNote.setModDate(GenericUtils.getDateTime());
                    myNewTextNote.setColor(noteColor);

                    if (checkPermission && checkLocation.isChecked()) {
                        double[] myCoords = LocationFunctions.getLocation(ActivityNewTextNote.this);
                        if (myCoords != null) {
                            myNewTextNote.setLatitude(myCoords[0]);
                            myNewTextNote.setLongitude(myCoords[1]);
                        }
                    }

                    DatabaseHelper dbHelper = new DatabaseHelper(ActivityNewTextNote.this);
                    long checkInsert = dbHelper.insertNote(myNewTextNote);

                    if (attachedImage.getDrawable() != null) {
                        boolean insertImage = dbHelper.insertTextImage(checkInsert, imageViewToByte(attachedImage));
                    }

                    if (checkInsert != -1) {
                        Toast.makeText(getApplicationContext(), "Note Saved", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ActivityNewTextNote.this, MainActivity.class);
                        startActivity(intent);
                        ActivityNewTextNote.this.finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Insertion Failed", Toast.LENGTH_SHORT).show();
                    }


                    dbHelper.close();
                }
            });

            checkLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkLocation.isChecked()) {
                        Boolean checkPerm = LocationFunctions.checkPermission(ActivityNewTextNote.this);
                        if (checkPerm == false) {
                            LocationFunctions.requestPermission(activity);
                        }
                    }
                }
            });

        }

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (noteIntentID != 0) {
            inflater.inflate(R.menu.edit_note_menu, menu);
        } else {
            inflater.inflate(R.menu.note_menu, menu);
        }
        MenuItem colorPicker = menu.findItem(R.id.pickColor);
        if (colorPicker != null) {
            GenericUtils.tintMenuIcon(ActivityNewTextNote.this, colorPicker, noteColor);
        }

        this.mMenu = menu;
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu mMenu) {
        if (noteIntentID != 0) {
            mMenu.findItem(R.id.pickColor).setEnabled(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.attachImage:

                final androidx.appcompat.app.AlertDialog.Builder alertAttachImage = new androidx.appcompat.app.AlertDialog.Builder(ActivityNewTextNote.this);
                View myView = getLayoutInflater().inflate(R.layout.dialog_attached_image, null);
                alertAttachImage.setView(myView);
                final AlertDialog alertDialogImage = alertAttachImage.create();
                alertDialogImage.setCanceledOnTouchOutside(true);
                alertDialogImage.show();

                GenericUtils.setDialogSize(alertDialogImage, 750, ViewGroup.LayoutParams.WRAP_CONTENT);

                Button camera = (Button) alertDialogImage.findViewById(R.id.cameraBtn);
                Button gallery = (Button) alertDialogImage.findViewById(R.id.galleryBtn);

                camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                        } else {
                            dispatchTakePictureIntent();
                        }

                        alertDialogImage.dismiss();
                    }
                });


                gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                            requestPermissions(permissions, PERMISSION_CODE);

                        } else {
                            pickImageFromGallery();
                        }

                        alertDialogImage.dismiss();
                    }
                });


                break;

            case R.id.pickColor:
                final AlertDialog.Builder alert = new AlertDialog.Builder(ActivityNewTextNote.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_color_picker, null);
                alert.setView(mView);
                final MenuItem pickColorItem = mMenu.findItem(R.id.pickColor);

                final AlertDialog alertDialog = alert.create();

                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.show();
                GenericUtils.setDialogSize(alertDialog, 890, 890);

                ImageButton buttonWhite = alertDialog.findViewById(R.id.colorWhite);
                buttonWhite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        noteColor = getColor(R.color.White);
                        GenericUtils.tintMenuIcon(ActivityNewTextNote.this, pickColorItem, noteColor);
                        alertDialog.dismiss();
                    }
                });
                ImageButton buttonRed = alertDialog.findViewById(R.id.colorRed);
                buttonRed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        noteColor = getColor(R.color.Red);
                        GenericUtils.tintMenuIcon(ActivityNewTextNote.this, pickColorItem, noteColor);
                        alertDialog.dismiss();
                    }
                });
                ImageButton buttonBlue = alertDialog.findViewById(R.id.colorBlue);
                buttonBlue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        noteColor = getColor(R.color.Blue);
                        GenericUtils.tintMenuIcon(ActivityNewTextNote.this, pickColorItem, noteColor);
                        alertDialog.dismiss();
                    }
                });
                ImageButton buttonGreen = alertDialog.findViewById(R.id.colorGreen);
                buttonGreen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        noteColor = getColor(R.color.Green);
                        GenericUtils.tintMenuIcon(ActivityNewTextNote.this, pickColorItem, noteColor);
                        alertDialog.dismiss();
                    }
                });
                ImageButton buttonYellow = alertDialog.findViewById(R.id.colorYellow);
                buttonYellow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        noteColor = getColor(R.color.Yellow);
                        GenericUtils.tintMenuIcon(ActivityNewTextNote.this, pickColorItem, noteColor);
                        alertDialog.dismiss();
                    }
                });
                ImageButton buttonGrey = alertDialog.findViewById(R.id.colorGrey);
                buttonGrey.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        noteColor = getColor(R.color.Grey);
                        GenericUtils.tintMenuIcon(ActivityNewTextNote.this, pickColorItem, noteColor);
                        alertDialog.dismiss();
                    }
                });
                ImageButton buttonBlack = alertDialog.findViewById(R.id.colorBlack);
                buttonBlack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        noteColor = getColor(R.color.Black);
                        GenericUtils.tintMenuIcon(ActivityNewTextNote.this, pickColorItem, noteColor);
                        alertDialog.dismiss();
                    }
                });
                ImageButton buttonOrange = alertDialog.findViewById(R.id.colorOrange);
                buttonOrange.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        noteColor = getColor(R.color.Orange);
                        GenericUtils.tintMenuIcon(ActivityNewTextNote.this, pickColorItem, noteColor);
                        alertDialog.dismiss();
                    }
                });
                ImageButton buttonPurple = alertDialog.findViewById(R.id.colorPurple);
                buttonPurple.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        noteColor = getColor(R.color.Purple);
                        GenericUtils.tintMenuIcon(ActivityNewTextNote.this, pickColorItem, noteColor);
                        alertDialog.dismiss();
                    }
                });

                break;

            case R.id.editNote:

                findViewById(R.id.editText).setEnabled(true);
                findViewById(R.id.editTitle).setEnabled(true);
                mMenu.findItem(R.id.pickColor).setEnabled(true);
                findViewById(R.id.newTxtNoteBackground).setEnabled(true);

                break;

            case R.id.deleteNote:
                final AlertDialog alertDeleteDialog = new AlertDialog.Builder(ActivityNewTextNote.this).create();
                alertDeleteDialog.setTitle("Delete Note");
                alertDeleteDialog.setMessage("Delete this note?");
                alertDeleteDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                int noteID = getIntent().getIntExtra("noteID", 0);
                                DatabaseHelper dbHelper = new DatabaseHelper(ActivityNewTextNote.this);
                                Boolean checkDelete = dbHelper.deleteNote(noteID);
                                if (checkDelete) {
                                    Intent intent = new Intent(ActivityNewTextNote.this, MainActivity.class);
                                    startActivity(intent);
                                    ActivityNewTextNote.this.finish();
                                    Toast.makeText(getApplicationContext(), "Note Deleted", LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Delete failed", LENGTH_SHORT).show();
                                }
                                alertDeleteDialog.dismiss();
                                dbHelper.close();
                            }
                        });
                alertDeleteDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        alertDeleteDialog.dismiss();
                    }
                });
                alertDeleteDialog.show();

                break;

            case R.id.shareNote:
                EditText noteText = findViewById(R.id.editText);
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        noteText.getText().toString());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

                break;

            case R.id.showOnMap:
                if (noteLat.get(0) == 0 && noteLong.get(0) == 0) {
                    GenericUtils.toast(ActivityNewTextNote.this, "Note has no location.");
                } else {
                    Intent mapIntent = new Intent(ActivityNewTextNote.this, ActivityMaps.class);
                    mapIntent.putExtra("NoteLat", noteLat);
                    mapIntent.putExtra("NoteLong", noteLong);
                    mapIntent.putExtra("Title", mNoteTitle);
                    startActivity(mapIntent);
                }
                break;

            case R.id.pinStatusBar:
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
                TextView title = findViewById(R.id.editTitle);
                TextView text = findViewById(R.id.editText);

                notificationBuilder.setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.ic_notification_farm)
                        .setTicker(title.getText().toString())
                        .setPriority(Notification.PRIORITY_MAX)
                        .setContentTitle(title.getText().toString())
                        .setContentText(text.getText().toString())
                        .setContentInfo("Info");

                NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(1, notificationBuilder.build());

                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private int getIncomingIntent() {
        if (getIntent().hasExtra("flag")) {
            return getIntent().getIntExtra("noteID", 0);
        } else {
            return 0;
        }
    }


    private void loadEditableNote(int noteID) {
        DatabaseHelper dbHelper = new DatabaseHelper(ActivityNewTextNote.this);
        Cursor cursor = dbHelper.getNote(noteID);

        if (cursor != null)
            cursor.moveToFirst();

        String textNoteTitle = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_noteTitle));
        String textNoteText = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_noteText));
        noteColor = cursor.getInt(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_color));

        noteLat.add(cursor.getDouble(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_noteLatitude)));
        noteLong.add(cursor.getDouble(cursor.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_noteLongitude)));
        mNoteTitle.add(textNoteTitle);

        cursor.close();


        Cursor cursorImage = dbHelper.getTextImage(noteID);

        if (cursorImage != null && cursorImage.getCount() > 0) {
            cursorImage.moveToFirst();

            byte[] imageByteArray = cursorImage.getBlob(cursorImage.getColumnIndex(FeedReaderContract.FeedTextNote.COLUMN_imageBlob));

            Bitmap imageBitmap = getImage(imageByteArray);
            attachedImage.setImageBitmap(imageBitmap);

            attachedImage.setVisibility(View.VISIBLE);
            deleteImage.setVisibility(View.VISIBLE);
        }

        cursorImage.close();

        TextView title = findViewById(R.id.editTitle);
        TextView text = findViewById(R.id.editText);

        title.setText(textNoteTitle);
        text.setText(textNoteText);

        dbHelper.close();

    }

    public void pickImageFromGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery();
                } else {
                    Toast.makeText(this, "Permission denied...!", LENGTH_SHORT).show();
                }

                break;

            case MY_CAMERA_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Camera permission granted", Toast.LENGTH_LONG).show();
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                } else {
                    Toast.makeText(this, "Camera permission denied", Toast.LENGTH_LONG).show();
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {

            Glide
                    .with(this)
                    .load(data.getData())
                    .into(attachedImage);

            attachedImage.setVisibility(View.VISIBLE);
            deleteImage.setVisibility(View.VISIBLE);

        } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {

            Glide
                    .with(this)
                    .load(currentPhotoPath)
                    .into(attachedImage);

            attachedImage.setVisibility(View.VISIBLE);
            deleteImage.setVisibility(View.VISIBLE);


        }

    }

    private byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this, "Error while creating file", Toast.LENGTH_LONG).show();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.farmernotepad.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ActivityNewTextNote.this, MainActivity.class);
        startActivity(intent);
        ActivityNewTextNote.this.finish();
    }
}