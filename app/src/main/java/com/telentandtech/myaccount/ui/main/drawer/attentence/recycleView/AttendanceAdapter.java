package com.telentandtech.myaccount.ui.main.drawer.attentence.recycleView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.telentandtech.myaccount.R;
import com.telentandtech.myaccount.core.OnClickListener;
import com.telentandtech.myaccount.database.entityes.Attendance;

import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {

    public List<Attendance> attendanceList;
    private OnClickListener onClickListener;

    public AttendanceAdapter(List<Attendance> attendanceList, OnClickListener onClickListener) {
        this.attendanceList = attendanceList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public AttendanceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attendance_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceAdapter.ViewHolder holder, int position) {
        Attendance attendance = attendanceList.get(position);
        holder.nameTextView.setText(attendance.getStudent_name());
        holder.idTextView.setText(attendance.getId());
        holder.groupTextView.setText(attendance.getGroup_name());
        holder.phoneTextView.setText(attendance.getPhone());
        holder.classTextView.setText(attendance.getClass_name());
        if (attendance.isAttended()){
            holder.cardView.setCardBackgroundColor(holder.itemView.getResources().getColor(R.color.darker_green, null));
        }else {
            holder.cardView.setCardBackgroundColor(holder.itemView.getResources().getColor(R.color.darker_red, null));
        }

    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView phoneTextView;
        private TextView idTextView;
        private TextView groupTextView;
        private TextView classTextView;
        private CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.attendance_full_name_text_view);
            idTextView = itemView.findViewById(R.id.attendance_student_id_text_view);
            groupTextView = itemView.findViewById(R.id.attendance_student_group_text_view);
            phoneTextView = itemView.findViewById(R.id.attendance_student_contact_text_view);
            classTextView = itemView.findViewById(R.id.attendance_student_class_text_view);
            cardView = itemView.findViewById(R.id.attendance_card_view);

            cardView.setOnClickListener(v -> onClickListener.onClick(getAdapterPosition(),"attendance"));

        }
    }
}
