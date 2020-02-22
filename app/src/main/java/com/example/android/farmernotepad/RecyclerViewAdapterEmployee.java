package com.example.android.farmernotepad;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapterEmployee extends RecyclerView.Adapter<RecyclerViewAdapterEmployee.ViewHolder> {

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
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        WageEntry currentWage = (WageEntry) mNewPaymentList.get(position);

        holder.paymentDate.setText(currentWage.getWageWorkDate());
        holder.paymentWorkHours.setText(String.valueOf(currentWage.getWageHours()));
        holder.paymentRate.setText(String.valueOf(currentWage.getWageRate()));
        holder.paymentDescription.setText(currentWage.getWageDesc());
        float hours = (float) currentWage.getWageHours();
        float rate = (float) currentWage.getWageRate();
        holder.paymentTotalDebt.setText(String.valueOf(hours * rate));

        holder.paymentDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNewPaymentList.remove(position);
                notifyItemRemoved(position);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mNewPaymentList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView paymentDate;
        TextView paymentWorkHours;
        TextView paymentRate;
        TextView paymentDescription;
        TextView paymentTotalDebt;
        Button paymentDelete;
        OnNoteListener onNoteListener;

        public ViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);

            paymentDate = itemView.findViewById(R.id.date);
            paymentWorkHours = itemView.findViewById(R.id.workHours);
            paymentRate = itemView.findViewById(R.id.rate);
            paymentDescription = itemView.findViewById(R.id.description);
            paymentTotalDebt = itemView.findViewById(R.id.employmentDebt);
            paymentDelete = itemView.findViewById(R.id.deletePayment);
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
