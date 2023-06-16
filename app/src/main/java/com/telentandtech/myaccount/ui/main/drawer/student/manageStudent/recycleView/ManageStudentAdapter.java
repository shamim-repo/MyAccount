package com.telentandtech.myaccount.ui.main.drawer.student.manageStudent.recycleView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.telentandtech.myaccount.R;
import com.telentandtech.myaccount.core.OnClickListener;
import com.telentandtech.myaccount.database.entityes.Students;

import java.util.List;

public class ManageStudentAdapter extends RecyclerView.Adapter<ManageStudentAdapter.ViewHolder>{

    private List<Students> studentsList;
    private OnClickListener onClickListener;

    public ManageStudentAdapter(List<Students> studentsList, OnClickListener onClickListener) {
        this.studentsList = studentsList;
        this.onClickListener = onClickListener;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.manage_student_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Students student = studentsList.get(position);
        holder.studentNameTextView.setText(student.getFull_name());
        holder.studentIdTextView.setText(student.getId());
        holder.studentPhoneTextView.setText(student.getPhone());
        holder.studentClassTextView.setText(student.getClass_name());
        holder.studentGroupTextView.setText(student.getGroup_name());
        holder.uidTextView.setText("UID :"+student.getStudent_id());
    }

    @Override
    public int getItemCount() {
        return studentsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView studentNameTextView;
        private TextView studentIdTextView;
        private TextView studentClassTextView;
        private TextView studentGroupTextView;
        private TextView studentPhoneTextView;
        private MaterialButton editStudentButton;
        private MaterialButton detailStudentButton;
        private MaterialButton deleteStudentButton;
        private TextView uidTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            studentNameTextView = itemView.findViewById(R.id.manage_student_full_name_text_view);
            studentIdTextView = itemView.findViewById(R.id.manage_student_id_text_view);
            studentPhoneTextView = itemView.findViewById(R.id.manage_student_contact_text_view);
            studentClassTextView = itemView.findViewById(R.id.manage_student_class_text_view);
            studentGroupTextView = itemView.findViewById(R.id.manage_student_group_text_view);
            detailStudentButton = itemView.findViewById(R.id.manage_student_detail_button);
            editStudentButton = itemView.findViewById(R.id.manage_student_edit_button);
            deleteStudentButton = itemView.findViewById(R.id.manage_student_delete_button);
            uidTextView = itemView.findViewById(R.id.manage_student_uid_text_view);


            editStudentButton.setOnClickListener(v ->
                    onClickListener.onClick(getAdapterPosition(),"edit"));
            deleteStudentButton.setOnClickListener(v ->
                    onClickListener.onClick(getAdapterPosition(),"delete"));
            detailStudentButton.setOnClickListener(v ->
                    onClickListener.onClick(getAdapterPosition(),"detail"));
        }
    }
}
