package com.example.android.farmernotepad;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NewChecklistActivity extends AppCompatActivity implements RecyclerViewAdapterChecklist.OnChecklistItemListener {

    private ArrayList<String> mChecklistItem = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_checklist);

        Button addItemButton = (Button) findViewById(R.id.addChecklistItemButton);
        FloatingActionButton saveChecklistButton = findViewById(R.id.confirmSaveChecklist);


        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemDialogBox();
            }
        });

        saveChecklistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        initRecyclerViewAdapterChecklist();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initRecyclerViewAdapterChecklist();
    }

    private void initRecyclerViewAdapterChecklist(){
        RecyclerView recyclerView = findViewById(R.id.checklistRecyclerView);
        RecyclerViewAdapterChecklist adapter = new RecyclerViewAdapterChecklist(mChecklistItem,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    private void addItemDialogBox(){
        final AlertDialog.Builder alert = new AlertDialog.Builder(NewChecklistActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.add_checklist_item_dialog_box, null);
        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();

        Button okButton = (Button) alertDialog.findViewById(R.id.okButton);
        Button cancelButton = (Button) alertDialog.findViewById(R.id.cancelButton);
        final EditText editText = (EditText) alertDialog.findViewById(R.id.checklistEditText);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String checklistItem = editText.getText().toString();
                mChecklistItem.add(checklistItem);
                alertDialog.dismiss();

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.getText().clear();
                alertDialog.dismiss();
            }
        });

    }

    @Override
    public void onChecklistNoteClick(int position) {
        addItemDialogBox();
    }
}
