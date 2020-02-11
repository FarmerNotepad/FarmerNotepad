package com.example.android.farmernotepad;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapterChecklist extends RecyclerView.Adapter<RecyclerViewAdapterChecklist.ViewHolderChecklist>{

    private ArrayList<String> mChecklistItem = new ArrayList<>();
    private OnChecklistItemListener mOnChecklistItemListener;

    public RecyclerViewAdapterChecklist(ArrayList<String> mChecklistItem,OnChecklistItemListener onChecklistItemListener) {
        this.mChecklistItem = mChecklistItem;
        this.mOnChecklistItemListener = onChecklistItemListener;

    }

    @NonNull
    @Override
    public ViewHolderChecklist onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checklist_note_item_layout, parent, false);
        ViewHolderChecklist holder = new ViewHolderChecklist(view, mOnChecklistItemListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderChecklist holder, final int position) {
        holder.checklistItemTextView.setText(mChecklistItem.get(position));

        holder.deleteItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mChecklistItem.remove(position);
                notifyItemRemoved(position);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mChecklistItem.size();
    }

    public class ViewHolderChecklist extends RecyclerView.ViewHolder implements View.OnClickListener{

        LinearLayout parent_checklistLayout;
        TextView checklistItemTextView;
        Button deleteItemButton;
        EditText checklistItem;
        OnChecklistItemListener onChecklistItemListener;

        public ViewHolderChecklist(@NonNull View itemView, OnChecklistItemListener onChecklistItemListener) {
            super(itemView);

            checklistItem = itemView.findViewById(R.id.checklistEditText);
            parent_checklistLayout = itemView.findViewById(R.id.parent_checklistLayout);
            checklistItemTextView = itemView.findViewById(R.id.checklistItemTextView);
            deleteItemButton = itemView.findViewById(R.id.deleteItemButton);
            this.onChecklistItemListener = onChecklistItemListener;


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onChecklistItemListener.onChecklistNoteClick(getAdapterPosition());
        }
    }

    public interface OnChecklistItemListener{
        void onChecklistNoteClick(int position);
    }
}
