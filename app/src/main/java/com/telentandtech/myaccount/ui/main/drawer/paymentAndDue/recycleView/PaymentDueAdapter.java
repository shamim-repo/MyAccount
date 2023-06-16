package com.telentandtech.myaccount.ui.main.drawer.paymentAndDue.recycleView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.telentandtech.myaccount.R;
import com.telentandtech.myaccount.database.entityes.Payments;

import java.util.List;

public class PaymentDueAdapter extends RecyclerView.Adapter<PaymentDueAdapter.ViewHolder>{

    private List<Payments> paymentsList;

    public PaymentDueAdapter(List<Payments> paymentsList) {
        this.paymentsList = paymentsList;
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
        holder.paymentDateTextView.setText(String.valueOf(payments.getPayment_month()));
        holder.contactTextView.setText(payments.getPhone());
        if (payments.getPayment_status()){
            holder.paymentStatusImageView.setImageResource(R.drawable.baseline_check_circle_24);
            holder.editButton.setVisibility(View.VISIBLE);
            holder.payButton.setVisibility(View.GONE);
        }else {
            holder.paymentStatusImageView.setImageResource(R.drawable.baseline_dangerous_24);
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
        private TextView paymentDateTextView;
        private TextView contactTextView;
        private MaterialButton payButton;
        private MaterialButton editButton;
        private ImageView paymentStatusImageView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            studentNameTextView = itemView.findViewById(R.id.payment_student_full_name_text_view);
            idTextView = itemView.findViewById(R.id.payment_student_id_text_view);
            paymentAmountTextView = itemView.findViewById(R.id.payment_amount_text_view);
            paymentDateTextView = itemView.findViewById(R.id.payment_date_text_view);
            contactTextView = itemView.findViewById(R.id.payment_contact_text_view);
            payButton = itemView.findViewById(R.id.payment_pay_button);
            editButton = itemView.findViewById(R.id.payment_edit_button);
            paymentStatusImageView = itemView.findViewById(R.id.payment_status_image_view);
            payButton.setOnClickListener(v -> {

            });
            editButton.setOnClickListener(v -> {

            });

        }
    }
}
