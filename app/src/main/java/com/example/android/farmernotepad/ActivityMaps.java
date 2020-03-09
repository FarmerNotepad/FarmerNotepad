package com.example.android.farmernotepad;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import androidx.fragment.app.FragmentActivity;

public class ActivityMaps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<Double> latCoords = new ArrayList<>();
    private ArrayList<Double> longCoords = new ArrayList<>();
    private ArrayList<String> markerTitle = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (getIntent().hasExtra("NoteLat")) {
            latCoords = (ArrayList<Double>) getIntent().getSerializableExtra("NoteLat");
            //latCoords.add(getIntent().getDoubleArrayListExtra("NoteLat"));
        }

        if (getIntent().hasExtra("NoteLong")) {
            longCoords = (ArrayList<Double>) getIntent().getSerializableExtra("NoteLong");
        }

        if (getIntent().hasExtra("Title")) {
            markerTitle = getIntent().getStringArrayListExtra("Title");
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        for (int i = 0; i < latCoords.size(); i++) {
            LatLng myNote = new LatLng(latCoords.get(i), longCoords.get(i));
            mMap.addMarker(new MarkerOptions().position(myNote).title(markerTitle.get(i)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myNote));
            GenericUtils.toast(ActivityMaps.this, myNote.toString());
        }
    }
}
