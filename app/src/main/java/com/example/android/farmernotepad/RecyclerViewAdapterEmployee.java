package com.example.android.farmernotepad;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapterEmployee extends RecyclerView.Adapter<RecyclerViewAdapterEmployee.ViewHolder> {

    private ArrayList<EntryWage> mNewPaymentList = new ArrayList<>();
    private OnNoteListener mOnNoteListener;
    private LinearLayout parentPaymentLayout;


    public RecyclerViewAdapterEmployee(ArrayList<EntryWage> mNewPaymentList, OnNoteListener onNoteListener) {
        this.mNewPaymentList = mNewPaymentList;
        this.mOnNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_payment_item, parent, false);
        ViewHolder holder = new ViewHolder(view, mOnNoteListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        EntryWage currentWage = (EntryWage) mNewPaymentList.get(position);

        holder.paymentDate.setText(currentWage.getWageWorkDate());
        holder.paymentWorkHours.setText(String.valueOf(currentWage.getWageHours()));
        holder.paymentDescription.setText(currentWage.getWageDesc());
        holder.paymentTotalDebt.setText(String.valueOf(currentWage.getWageWage()));

        holder.paymentDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNewPaymentList.remove(position);
                notifyItemRemoved(position);
                notifyDataSetChanged();

            }
        });

        Drawable unwrappedDrawable = ContextCompat.getDrawable(parentPaymentLayout.getContext(), R.drawable.rounded_corners);
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        parentPaymentLayout.setBackground(wrappedDrawable);
        Drawable unwrappedDrawable2 = ContextCompat.getDrawable(parentPaymentLayout.getContext(), R.drawable.recycler_view_items_outline);
        Drawable wrappedDrawable2 = DrawableCompat.wrap(unwrappedDrawable2);
        parentPaymentLayout.setForeground(wrappedDrawable2);


    }

    @Override
    public int getItemCount() {
        return mNewPaymentList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView paymentDate;
        TextView paymentWorkHours;
        TextView paymentDescription;
        TextView paymentTotalDebt;
        Button paymentDelete;
        OnNoteListener onNoteListener;

        public ViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);

            paymentDate = itemView.findViewById(R.id.date);
            paymentWorkHours = itemView.findViewById(R.id.workHours);
            paymentDescription = itemView.findViewById(R.id.description);
            paymentTotalDebt = itemView.findViewById(R.id.employmentDebt);
            paymentDelete = itemView.findViewById(R.id.deletePayment);
            parentPaymentLayout = itemView.findViewById(R.id.parent_paymentLayout);
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
