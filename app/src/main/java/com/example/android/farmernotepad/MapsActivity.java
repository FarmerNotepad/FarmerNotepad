package com.example.android.farmernotepad;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<Double> latCoords = new ArrayList<>();
    private ArrayList<Double> longCoords = new ArrayList<>();
    private String markerTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (getIntent().hasExtra("NoteLat")){
            latCoords.add(getIntent().getDoubleExtra("NoteLat",90));
        }

        if (getIntent().hasExtra("NoteLong")){
            longCoords.add(getIntent().getDoubleExtra("NoteLong",90));
        }

        if (getIntent().hasExtra("Title")){
            markerTitle = getIntent().getStringExtra("Title");
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
        LatLng myNote = new LatLng(latCoords.get(0), longCoords.get(0));
        mMap.addMarker(new MarkerOptions().position(myNote).title(markerTitle));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myNote));
        GenericUtils.toast(MapsActivity.this,myNote.toString());
    }
}
