package com.example.android.farmernotepad;

import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapterMain extends RecyclerView.Adapter implements Filterable {

    private static final String TAG = "RecyclerViewAdapterMain";
    private ArrayList<ListItem> allNotesList;
    private ArrayList<ListItem> allNotesListFull;
    private OnNoteListener mOnNoteListener;


    public RecyclerViewAdapterMain(ArrayList<ListItem> allNotesList, OnNoteListener onNoteListener) {
        this.allNotesList = allNotesList;
        this.allNotesListFull = new ArrayList<>(allNotesList);
        this.mOnNoteListener = onNoteListener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType) {
            case ListItem.typeChecklist:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_note_item_layout, parent, false);
                return new ChecklistViewHolder(itemView, mOnNoteListener);
            default:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.text_note_item_layout, parent, false);
                return new TextNoteViewHolder(itemView, mOnNoteListener);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        switch (getItemViewType(position)) {
            case ListItem.typeText:
                ((TextNoteViewHolder) holder).bindView(position);
                //((TextNoteViewHolder) holder).textNoteTitle.setBackgroundColor(allNotesList.get(position).getColor());
                //((TextNoteViewHolder) holder).textNoteContent.setBackgroundColor(allNotesList.get(position).getColor());

                break;
            case ListItem.typeChecklist:
                ((ChecklistViewHolder) holder).bindView(position);
                //((TextNoteViewHolder) holder).textNoteTitle.setBackgroundColor(allNotesList.get(position).getColor());
                //((TextNoteViewHolder) holder).textNoteContent.setBackgroundColor(allNotesList.get(position).getColor());
                break;
        }

    }

    @Override
    public int getItemCount() {
        if (allNotesList == null) {
            return 0;
        } else {
            return allNotesList.size();
        }
    }

    @Override
    public Filter getFilter() {
        return notesFilter;
    }

    private Filter notesFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<ListItem> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(allNotesListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (ListItem item : allNotesListFull) {
                    if (item.getInterfaceTitle().toLowerCase().contains(filterPattern) || item.getInterfaceText().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            allNotesList.clear();
            allNotesList.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };

    public class TextNoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textNoteTitle;
        TextView textNoteContent;
        LinearLayout parentLayout;

        OnNoteListener onNoteListener;


        public TextNoteViewHolder(View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            textNoteTitle = itemView.findViewById(R.id.textNoteTitle);
            textNoteContent = itemView.findViewById(R.id.textNoteContent);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }

        void bindView(int position) {
            TextNoteEntry textNote = (TextNoteEntry) allNotesList.get(position);
            textNoteTitle.setText(textNote.getNoteTitle());
            textNoteContent.setText(textNote.getNoteText());
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }

    }

    public class ChecklistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textNoteTitle;
        TextView textNoteContent;
        LinearLayout parentLayout;

        OnNoteListener onNoteListener;


        public ChecklistViewHolder(View itemView, OnNoteListener onNoteListener) {
            super(itemView);

            textNoteTitle = itemView.findViewById(R.id.textNoteTitle);
            textNoteContent = itemView.findViewById(R.id.textNoteContent);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }

        void bindView(int position) {
            ChecklistNoteEntry checklistNote = (ChecklistNoteEntry) allNotesList.get(position);
            textNoteTitle.setText(checklistNote.getNoteTitle());

            String result = "";
            for (String s : checklistNote.getChecklistItems()) {
                result += " \u2022" + s;
            }
            textNoteContent.setText(result);
            textNoteContent.setBackgroundColor(checklistNote.getColor());
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }

    @Override
    public int getItemViewType(int position) {
        return allNotesList.get(position).getListItemType();
    }
}
