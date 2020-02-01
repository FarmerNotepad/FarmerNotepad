package com.example.android.farmernotepad;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class LocationFunctions {
    public static final int PERMISSION_FINE_LOCATION = 177;

    public static double[] getLocation(Context ctx){
        if ( ContextCompat.checkSelfPermission( ctx, android.Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED ) {
            LocationManager mLocationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
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

    public static boolean checkPermission(Context ctx){
        if ( ContextCompat.checkSelfPermission( ctx, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            return false;
        }
        else {
            return true;
        }
    }

    public static void requestPermission(Activity act){
        ActivityCompat.requestPermissions( act, new String[] { android.Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_FINE_LOCATION);
    }

}
