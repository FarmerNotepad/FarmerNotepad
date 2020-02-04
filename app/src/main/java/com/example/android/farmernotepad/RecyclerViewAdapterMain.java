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
    private ArrayList<TextNoteEntry> textNotes;
    private ArrayList<Object> allNotesList;
    private OnNoteListener mOnNoteListener;




    public RecyclerViewAdapterMain( ArrayList<Object> allNotesList, ArrayList<TextNoteEntry> textNotes, OnNoteListener onNoteListener) {
        this.allNotesList = allNotesList;
        this.textNotes = textNotes;
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
        TextNoteEntry currentNote = textNotes.get(position);

        holder.textNoteTitle.setText(currentNote.getNoteTitle());
        holder.textNoteContent.setText(currentNote.getNoteText());
    }

    @Override
    public int getItemCount() {
        return textNotes.size();
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
