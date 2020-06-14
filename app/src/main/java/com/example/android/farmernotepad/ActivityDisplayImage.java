package com.example.android.farmernotepad;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityDisplayImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);

        FloatingActionButton closeDisplay = findViewById(R.id.closeDisplay);

        closeDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityDisplayImage.this.finish();
            }
        });

        Bundle extras = getIntent().getExtras();
        byte[] byteImageView = extras.getByteArray("picture");

        Bitmap bmp = BitmapFactory.decodeByteArray(byteImageView, 0, byteImageView.length);
        PhotoView image = findViewById(R.id.fullImageDisplay);

        image.setImageBitmap(bmp);
    }
}
