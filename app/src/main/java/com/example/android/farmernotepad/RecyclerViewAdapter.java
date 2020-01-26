package com.example.android.farmernotepad;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mTextNoteTitle = new ArrayList<>();
    private ArrayList<String> mTextNoteContent = new ArrayList<>();
    private Context mContext;


    public RecyclerViewAdapter(ArrayList<String> mTextNoteTitle, ArrayList<String> mTextNoteContent, Context mContext) {
        this.mTextNoteTitle = mTextNoteTitle;
        this.mTextNoteContent = mTextNoteContent;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_note_item_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        holder.textNoteTitle.setText(mTextNoteTitle.get(position));
        holder.textNoteContent.setText(mTextNoteContent.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, mTextNoteTitle.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTextNoteTitle.size();
    }

    public class  ViewHolder extends RecyclerView.ViewHolder{

        TextView textNoteTitle;
        TextView textNoteContent;
        LinearLayout parentLayout;


        public ViewHolder(View itemView){
            super(itemView);
            textNoteTitle = itemView.findViewById(R.id.textNoteTitle);
            textNoteContent = itemView.findViewById(R.id.textNoteContent);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
