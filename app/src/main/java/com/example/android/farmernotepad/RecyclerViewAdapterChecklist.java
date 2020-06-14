package com.example.android.farmernotepad;

import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapterChecklist extends RecyclerView.Adapter<RecyclerViewAdapterChecklist.ViewHolderChecklist> {

    private ArrayList<ChecklistItemEntry> mChecklistItem = new ArrayList<>();
    private OnChecklistItemListener mOnChecklistItemListener;
    public LinearLayout parentLayout;

    public RecyclerViewAdapterChecklist(ArrayList<ChecklistItemEntry> mChecklistItem, OnChecklistItemListener onChecklistItemListener) {
        this.mChecklistItem = mChecklistItem;
        this.mOnChecklistItemListener = onChecklistItemListener;

    }

    @NonNull
    @Override
    public ViewHolderChecklist onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_checklist_item, parent, false);
        ViewHolderChecklist holder = new ViewHolderChecklist(view, mOnChecklistItemListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderChecklist holder, final int position) {
        holder.checklistItemTextView.setText(mChecklistItem.get(position).getItemText());
        int isItChecked = mChecklistItem.get(position).getIsChecked();
        if (isItChecked == 1) {
            holder.checklistItemTextView.setPaintFlags(holder.checklistItemTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else if (isItChecked == 0) {
            holder.checklistItemTextView.setPaintFlags(holder.checklistItemTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        holder.deleteItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mChecklistItem.remove(position);
                notifyItemRemoved(position);
                notifyDataSetChanged();
            }
        });

        Drawable unwrappedDrawable = ContextCompat.getDrawable(parentLayout.getContext(), R.drawable.rounded_corners);
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        parentLayout.setBackground(wrappedDrawable);
        Drawable unwrappedDrawable2 = ContextCompat.getDrawable(parentLayout.getContext(), R.drawable.recycler_view_items_outline);
        Drawable wrappedDrawable2 = DrawableCompat.wrap(unwrappedDrawable2);
        parentLayout.setForeground(wrappedDrawable2);


    }

    @Override
    public int getItemCount() {
        return mChecklistItem.size();
    }

    public class ViewHolderChecklist extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView checklistItemTextView;
        Button deleteItemButton;
        EditText checklistItem;
        OnChecklistItemListener onChecklistItemListener;

        public ViewHolderChecklist(@NonNull View itemView, OnChecklistItemListener onChecklistItemListener) {
            super(itemView);

            checklistItem = itemView.findViewById(R.id.checklistEditText);
            parentLayout = itemView.findViewById(R.id.parent_checklistLayout);
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

    public interface OnChecklistItemListener {
        void onChecklistNoteClick(int position);
    }
}