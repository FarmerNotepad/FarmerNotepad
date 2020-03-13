package com.example.android.farmernotepad;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


public class FragmentColor extends Fragment {
    MainActivity myActivity;
    private ConstraintLayout allColors, redBtn, orangeBtn, yellowBtn, greenBtn, blueBtn, purpleBtn, blackBtn, greyBtn, whiteBtn;

    public FragmentColor() {
        // Required empty public constructor
    }


    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_color, container, false);
        myActivity = (MainActivity) getActivity();
        allColors = view.findViewById(R.id.allColorsLayout);
        redBtn = view.findViewById(R.id.redColorLayout);
        orangeBtn = view.findViewById(R.id.orangeColorLayout);
        yellowBtn = view.findViewById(R.id.yellowColorLayout);
        greenBtn = view.findViewById(R.id.greenColorLayout);
        blueBtn = view.findViewById(R.id.blueColorLayout);
        purpleBtn = view.findViewById(R.id.purpleColorLayout);
        blackBtn = view.findViewById(R.id.blackColorLayout);
        greyBtn = view.findViewById(R.id.greyColorLayout);
        whiteBtn = view.findViewById(R.id.whiteColorLayout);


        setClickListener(allColors, 0);
        setClickListener(redBtn, ContextCompat.getColor(context,R.color.Red));
        setClickListener(orangeBtn, ContextCompat.getColor(context,R.color.Orange));
        setClickListener(yellowBtn, ContextCompat.getColor(context,R.color.Yellow));
        setClickListener(greenBtn, ContextCompat.getColor(context,R.color.Green));
        setClickListener(blueBtn, ContextCompat.getColor(context,R.color.Blue));
        setClickListener(purpleBtn, ContextCompat.getColor(context,R.color.Purple));
        setClickListener(blackBtn, ContextCompat.getColor(context,R.color.Black));
        setClickListener(greyBtn, ContextCompat.getColor(context,R.color.Grey));
        setClickListener(whiteBtn, ContextCompat.getColor(context,R.color.White));


        return view;
    }

    private void setClickListener(ConstraintLayout colorBtn, int colorNumber) {
        colorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(myActivity.getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("filter_color", colorNumber);
                editor.apply();
                myActivity.filterColor(colorNumber);
            }
        });
    }

}
