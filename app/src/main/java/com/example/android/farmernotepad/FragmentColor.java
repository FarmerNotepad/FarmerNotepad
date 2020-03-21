package com.example.android.farmernotepad;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


public class FragmentColor extends Fragment {
    MainActivity myActivity;
    private ConstraintLayout allColors, redBtn, orangeBtn, yellowBtn, greenBtn, blueBtn, purpleBtn, blackBtn, greyBtn, whiteBtn;
    private TextView redText,orangeText,yellowText,greenText,blueText,purpleText,blackText,greyText,whiteText;


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
        redText = view.findViewById(R.id.redText);
        orangeBtn = view.findViewById(R.id.orangeColorLayout);
        orangeText = view.findViewById(R.id.orangeText);
        yellowBtn = view.findViewById(R.id.yellowColorLayout);
        yellowText = view.findViewById(R.id.yellowText);
        greenBtn = view.findViewById(R.id.greenColorLayout);
        greenText = view.findViewById(R.id.greenText);
        blueBtn = view.findViewById(R.id.blueColorLayout);
        blueText = view.findViewById(R.id.blueText);
        purpleBtn = view.findViewById(R.id.purpleColorLayout);
        purpleText = view.findViewById(R.id.purpleText);
        blackBtn = view.findViewById(R.id.blackColorLayout);
        blackText = view.findViewById(R.id.blackText);
        greyBtn = view.findViewById(R.id.greyColorLayout);
        greyText = view.findViewById(R.id.greyText);
        whiteBtn = view.findViewById(R.id.whiteColorLayout);
        whiteText = view.findViewById(R.id.whiteText);



        setTextViews();

        setClickListener(allColors, 0);
        setClickListener(redBtn, ContextCompat.getColor(context,R.color.Red));
        setLongClickListener(redBtn,redText,ContextCompat.getColor(context,R.color.Red));
        setClickListener(orangeBtn, ContextCompat.getColor(context,R.color.Orange));
        setLongClickListener(orangeBtn,orangeText,ContextCompat.getColor(context,R.color.Orange));
        setClickListener(yellowBtn, ContextCompat.getColor(context,R.color.Yellow));
        setLongClickListener(yellowBtn,yellowText,ContextCompat.getColor(context,R.color.Yellow));
        setClickListener(greenBtn, ContextCompat.getColor(context,R.color.Green));
        setLongClickListener(greenBtn,greenText,ContextCompat.getColor(context,R.color.Green));
        setClickListener(blueBtn, ContextCompat.getColor(context,R.color.Blue));
        setLongClickListener(blueBtn,blueText,ContextCompat.getColor(context,R.color.Blue));
        setClickListener(purpleBtn, ContextCompat.getColor(context,R.color.Purple));
        setLongClickListener(purpleBtn,purpleText,ContextCompat.getColor(context,R.color.Purple));
        setClickListener(blackBtn, ContextCompat.getColor(context,R.color.Black));
        setLongClickListener(blackBtn,blackText,ContextCompat.getColor(context,R.color.Black));
        setClickListener(greyBtn, ContextCompat.getColor(context,R.color.Grey));
        setLongClickListener(greyBtn,greyText,ContextCompat.getColor(context,R.color.Grey));
        setClickListener(whiteBtn, ContextCompat.getColor(context,R.color.White));
        setLongClickListener(whiteBtn,whiteText,ContextCompat.getColor(context,R.color.White));


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
                DialogTabbed dialog = MainActivity.getDialog();
                dialog.dismiss();
            }
        });
    }

    private void setTextViews(){
        int[]  colors = {ContextCompat.getColor(context,R.color.Red),ContextCompat.getColor(context,R.color.Orange),ContextCompat.getColor(context,R.color.Yellow),
                ContextCompat.getColor(context,R.color.Green),ContextCompat.getColor(context,R.color.Blue),ContextCompat.getColor(context,R.color.Purple),
                ContextCompat.getColor(context,R.color.Black),ContextCompat.getColor(context,R.color.Grey),ContextCompat.getColor(context,R.color.White)};
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(myActivity.getApplicationContext());

        TextView[] myTextViews = {redText,orangeText,yellowText,greenText,blueText,purpleText,blackText,greyText,whiteText};
        int i =0;
        for (int s: colors){
            myTextViews[i].setText(sharedPreferences.getString(String.valueOf(colors[i]),myTextViews[i].getText().toString()));
            i++;
        }
    }

    private void setLongClickListener(ConstraintLayout colorBtn,TextView textView, int colorNumber) {
        colorBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(myActivity.getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();


                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                final EditText edittext = new EditText(context);
                edittext.setText(textView.getText());
                alert.setMessage("Color Category");
                alert.setCancelable(true);
                alert.setView(edittext);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String e = edittext.getText().toString();
                        editor.putString(String.valueOf(colorNumber), e);
                        editor.apply();
                        setTextViews();
                    }
                });

                alert.show();
                return false;
            }
        });

    }

}
