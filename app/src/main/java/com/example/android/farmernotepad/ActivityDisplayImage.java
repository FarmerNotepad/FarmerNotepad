package com.example.android.farmernotepad;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoView;

public class ActivityDisplayImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);

        Bundle extras = getIntent().getExtras();
        byte[] byteImageView = extras.getByteArray("picture");

        Bitmap bmp = BitmapFactory.decodeByteArray(byteImageView, 0, byteImageView.length);
        PhotoView image = findViewById(R.id.fullImageDisplay);

        image.setImageBitmap(bmp);
    }
}
