package com.example.android.farmernotepad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class ActivityLaunch extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);

        String defLaunchActivity = sharedPreferences.getString("default_home_screen", "Main");
        if(defLaunchActivity.equals("Payments")) {
            Intent intent = new Intent(this, ActivityPaymentsLog.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(intent);
        }

    }
}
