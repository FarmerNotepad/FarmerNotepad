package com.example.android.farmernotepad;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapterWage extends RecyclerView.Adapter<RecyclerViewAdapterWage.WageViewHolder> implements Filterable {

    private ArrayList<Employee> employeesArrayList = new ArrayList<>();
    private OnNoteListener mOnNoteListener;


    @NonNull
    @Override
    public WageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_wage_item, parent, false);
        WageViewHolder holder = new WageViewHolder(view, mOnNoteListener);
        return holder;
    }

    public RecyclerViewAdapterWage(ArrayList<Employee> employeesArrayList, OnNoteListener onNoteListener) {
        this.employeesArrayList = employeesArrayList;
        this.mOnNoteListener = onNoteListener;
    }

    @Override
    public void onBindViewHolder(@NonNull WageViewHolder holder, int position) {
        Employee currentEmployee = (Employee) employeesArrayList.get(position);

        holder.mEmployeeName.setText(currentEmployee.getEmployeeName());
        holder.mTotalDebt.setText(String.valueOf(currentEmployee.getEmployeeSum()));
    }

    @Override
    public int getItemCount() {
        return employeesArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    public static class WageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mEmployeeName;
        public TextView mTotalDebt;
        LinearLayout parentLayout;
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
