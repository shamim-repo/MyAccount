package com.telentandtech.myaccount.ui.main.drawer.fee.manageFee.recyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import com.telentandtech.myaccount.R;
import com.telentandtech.myaccount.core.DateObj;
import com.telentandtech.myaccount.core.OnClickListener;
import com.telentandtech.myaccount.database.entityes.Fees;

import java.util.List;

public class ManageFeeAdapter extends RecyclerView.Adapter<ManageFeeAdapter.ViewHolder>{

    private List<Fees> feesList;
    private OnClickListener onClickListener;

    public ManageFeeAdapter(List<Fees> feesList, OnClickListener onClickListener) {
        this.feesList = feesList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ManageFeeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.manage_fee_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ManageFeeAdapter.ViewHolder holder, int position) {
        Fees fees=feesList.get(position);
        holder.classNameTextView.setText(fees.getClass_name());
        holder.groupNameTextView.setText(fees.getGroup_name());
        holder.feeAmountTextView.setText(fees.getFee_amount().toString());
        holder.feeMonthTextView.setText(DateObj.longToMonthYear(fees.getFee_month()));

    }

    //remove item from list
    public void removeItem(int position){
        feesList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,feesList.size());
    }
    //undo delete
    public void restoreItem(Fees fees,int position){
        feesList.add(position,fees);
        notifyItemInserted(position);
        notifyItemRangeChanged(position,feesList.size());
    }
    //update item in list
    public void updateItem(Fees fees,int position){
        feesList.set(position,fees);
        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return feesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView classNameTextView;
        private TextView groupNameTextView;
        private TextView feeAmountTextView;
        private TextView feeMonthTextView;
        private MaterialButton deleteFeeButton;
        private MaterialButton editFeeButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            classNameTextView=itemView.findViewById(R.id.manage_fee_class_name_text_view);
            groupNameTextView=itemView.findViewById(R.id.manage_fee_group_name_text_view);
            deleteFeeButton=itemView.findViewById(R.id.manage_fee_delete_button);
            editFeeButton=itemView.findViewById(R.id.manage_fee_edit_button);
            feeAmountTextView=itemView.findViewById(R.id.manage_fee_fee_amount_text_view);
            feeMonthTextView=itemView.findViewById(R.id.manage_fee_fee_month_text_view);

            deleteFeeButton.setOnClickListener(v -> onClickListener.onClick(getAdapterPosition(),"delete"));
            editFeeButton.setOnClickListener(v -> onClickListener.onClick(getAdapterPosition(),"edit"));

        }
    }


}
