package com.telentandtech.myaccount.ui.main.drawer.paymentAndDue.recycleView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.telentandtech.myaccount.R;
import com.telentandtech.myaccount.core.DataClass;
import com.telentandtech.myaccount.core.DateObj;
import com.telentandtech.myaccount.core.OnClickListener;
import com.telentandtech.myaccount.database.entityes.Payments;

import java.util.List;

public class PaymentDueAdapter extends RecyclerView.Adapter<PaymentDueAdapter.ViewHolder>{

    private List<Payments> paymentsList;
    private OnClickListener onClickListener;

    public PaymentDueAdapter(List<Payments> paymentsList, OnClickListener onClickListener) {
        this.paymentsList = paymentsList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public PaymentDueAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.manage_payment_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentDueAdapter.ViewHolder holder, int position) {
        Payments payments = paymentsList.get(position);
        holder.studentNameTextView.setText(payments.getStudent_name());
        holder.idTextView.setText(String.valueOf(payments.getId()));
        holder.paymentAmountTextView.setText(String.valueOf(payments.getPayment_amount()));
        holder.paymentMonthTextView.setText(String.valueOf(payments.getPayment_month()));
        holder.contactTextView.setText(payments.getPhone());
        if (payments.getPayment_status()){
            holder.cardView.setCardBackgroundColor(holder.itemView.getResources().getColor(R.color.darker_green,null));
            holder.paidDateTextView.setVisibility(View.VISIBLE);
            holder.paidDateTextView.setText(DateObj.timestampToDateString(payments.getPayment_timestamp()));
            holder.editButton.setVisibility(View.VISIBLE);
            holder.payButton.setVisibility(View.GONE);
        }else {
            holder.cardView.setCardBackgroundColor(holder.itemView.getResources().getColor(R.color.darker_red,null));
            holder.paidDateTextView.setVisibility(View.GONE);
            holder.editButton.setVisibility(View.GONE);
            holder.payButton.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return paymentsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView studentNameTextView;
        private TextView idTextView;
        private TextView paymentAmountTextView;
        private TextView paidDateTextView;
        private TextView paymentMonthTextView;
        private TextView contactTextView;
        private MaterialButton payButton;
        private MaterialButton editButton;
        private CardView cardView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            studentNameTextView = itemView.findViewById(R.id.payment_student_full_name_text_view);
            idTextView = itemView.findViewById(R.id.payment_student_id_text_view);
            paymentMonthTextView = itemView.findViewById(R.id.payment_month_text_view);
            paymentAmountTextView = itemView.findViewById(R.id.payment_amount_text_view);
            paidDateTextView = itemView.findViewById(R.id.paid_date_text_view);
            contactTextView = itemView.findViewById(R.id.payment_contact_text_view);
            payButton = itemView.findViewById(R.id.payment_pay_button);
            editButton = itemView.findViewById(R.id.payment_edit_button);
            cardView = itemView.findViewById(R.id.payment_card_view);

            payButton.setOnClickListener(v -> onClickListener.onClick(getAdapterPosition(),"pay"));
            editButton.setOnClickListener(v -> onClickListener.onClick(getAdapterPosition(),"Un Pay"));

        }
    }
}
