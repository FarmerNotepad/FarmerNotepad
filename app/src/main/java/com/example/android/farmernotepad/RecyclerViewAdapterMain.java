package com.example.android.farmernotepad;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapterMain extends RecyclerView.Adapter<RecyclerViewAdapterMain.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapterMain";

    private ArrayList<String> mTextNoteTitle = new ArrayList<>();
    private ArrayList<String> mTextNoteContent = new ArrayList<>();
    private OnNoteListener mOnNoteListener;

    private ArrayList<String> mTextNoteID = new ArrayList<>();



    public RecyclerViewAdapterMain(ArrayList<String> mTextNoteTitle, ArrayList<String> mTextNoteContent, OnNoteListener onNoteListener) {
        this.mTextNoteTitle = mTextNoteTitle;
        this.mTextNoteContent = mTextNoteContent;
        this.mOnNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_note_item_layout, parent, false);
        ViewHolder holder = new ViewHolder(view, mOnNoteListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        holder.textNoteTitle.setText(mTextNoteTitle.get(position));
        holder.textNoteContent.setText(mTextNoteContent.get(position));
    }

    @Override
    public int getItemCount() {
        return mTextNoteTitle.size();
    }

    public class  ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textNoteTitle;
        TextView textNoteContent;
        LinearLayout parentLayout;

        OnNoteListener onNoteListener;


        public ViewHolder(View itemView, OnNoteListener onNoteListener){
            super(itemView);
            textNoteTitle = itemView.findViewById(R.id.textNoteTitle);
            textNoteContent = itemView.findViewById(R.id.textNoteContent);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }

    }

    public interface OnNoteListener{
        void onNoteClick(int position);
    }
}
