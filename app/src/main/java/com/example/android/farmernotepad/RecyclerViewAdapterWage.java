package com.example.android.farmernotepad;

import android.graphics.drawable.Drawable;
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
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapterWage extends RecyclerView.Adapter<RecyclerViewAdapterWage.WageViewHolder> implements Filterable {

    private final ArrayList<EntryEmployee> employeesArrayListFull;
    private ArrayList<EntryEmployee> employeesArrayList = new ArrayList<>();
    private OnNoteListener mOnNoteListener;
    public LinearLayout parentLayout;


    @NonNull
    @Override
    public WageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_wage_item, parent, false);
        WageViewHolder holder = new WageViewHolder(view, mOnNoteListener);
        return holder;
    }

    public RecyclerViewAdapterWage(ArrayList<EntryEmployee> employeesArrayList, OnNoteListener onNoteListener) {
        this.employeesArrayList = employeesArrayList;
        this.employeesArrayListFull = new ArrayList<>(employeesArrayList);
        this.mOnNoteListener = onNoteListener;
    }

    @Override
    public void onBindViewHolder(@NonNull WageViewHolder holder, int position) {
        EntryEmployee currentEntryEmployee = (EntryEmployee) employeesArrayList.get(position);

        holder.mEmployeeName.setText(currentEntryEmployee.getEmployeeName());
        holder.mTotalDebt.setText(String.valueOf(currentEntryEmployee.getEmployeeSum()));

        Drawable unwrappedDrawable = ContextCompat.getDrawable(parentLayout.getContext(), R.drawable.rounded_corners);
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        parentLayout.setBackground(wrappedDrawable);
        Drawable unwrappedDrawable2 = ContextCompat.getDrawable(parentLayout.getContext(), R.drawable.recycler_view_items_outline);
        Drawable wrappedDrawable2 = DrawableCompat.wrap(unwrappedDrawable2);
        parentLayout.setForeground(wrappedDrawable2);


    }

    @Override
    public int getItemCount() {
        return employeesArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return employeesFilter;
    }

    private Filter employeesFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<EntryEmployee> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(employeesArrayListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (EntryEmployee item : employeesArrayListFull) {
                    if (item.getEmployeeName().toLowerCase().contains(filterPattern)) {
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
            employeesArrayList.clear();
            employeesArrayList.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };


    public class WageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mEmployeeName;
        public TextView mTotalDebt;
        OnNoteListener onNoteListener;


        public WageViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);

            mEmployeeName = itemView.findViewById(R.id.employeeName);
            mTotalDebt = itemView.findViewById(R.id.totalDebt);
            parentLayout = itemView.findViewById(R.id.parent_wageLayout);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }


    public interface OnNoteListener {
        void onNoteClick(int position);
    }
}