package com.example.android.farmernotepad;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapterEmployee  extends RecyclerView.Adapter<RecyclerViewAdapterEmployee.ViewHolder> implements Filterable {

    private ArrayList<WageEntry> mNewPaymentList = new ArrayList<>();
    private OnNoteListener mOnNoteListener;

    public RecyclerViewAdapterEmployee(ArrayList<WageEntry> mNewPaymentList, OnNoteListener onNoteListener) {
        this.mNewPaymentList = mNewPaymentList;
        this.mOnNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_payment_item, parent, false);
        ViewHolder holder = new ViewHolder(view, mOnNoteListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WageEntry currentWage = (WageEntry) mNewPaymentList.get(position);

        holder.paymentDate.setText(currentWage.getWageWorkDate());
        holder.paymentWorkHours.setText(String.valueOf(currentWage.getWageHours()));
        holder.paymentRate.setText(String.valueOf(currentWage.getWageRate()));
        holder.paymentDescription.setText(currentWage.getWageDesc());
        int hours = (int) currentWage.getWageHours();
        int rate = (int) currentWage.getWageRate();
        holder.paymentTotalDebt.setText(String.valueOf(hours*rate));

    }

    @Override
    public int getItemCount() {
        return mNewPaymentList.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView paymentDate;
        TextView paymentWorkHours;
        TextView paymentRate;
        TextView paymentDescription;
        TextView paymentTotalDebt;
        OnNoteListener onNoteListener;

        public ViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);

            paymentDate = itemView.findViewById(R.id.date);
            paymentWorkHours = itemView.findViewById(R.id.workHours);
            paymentRate = itemView.findViewById(R.id.rate);
            paymentDescription = itemView.findViewById(R.id.description);
            paymentTotalDebt = itemView.findViewById(R.id.employmentDebt);
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
