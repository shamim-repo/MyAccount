package com.telentandtech.myaccount.ui.main.drawer.classes.manageGroup.recycleViewHelper;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.telentandtech.myaccount.R;
import com.telentandtech.myaccount.core.OnClickListener;
import com.telentandtech.myaccount.core.TimeFormatHelper;
import com.telentandtech.myaccount.database.entityes.Group;

import java.util.List;

public class ManageGroupAdapter extends RecyclerView.Adapter<ManageGroupAdapter.ViewHolder> {
    private List<Group> groupList;
    private OnClickListener onClickListener;
    public ManageGroupAdapter(List<Group> groupList, OnClickListener onClickListener) {
        this.groupList = groupList;
        this.onClickListener = onClickListener;
    }


    public void remove(int position){
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, groupList.size());


    }
    public void editItem(int position,Group group){
        groupList.set(position,group);
        notifyItemChanged(position);
    }
    public void insertItem(int position,Group group) {
        notifyItemInserted(position);
        notifyItemRangeChanged(position, groupList.size());
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.manage_group_item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ManageGroupAdapter.ViewHolder holder, int position) {
        Group group=groupList.get(position);
        holder.classNameTextView.setText(group.getClass_name());
        holder.groupNameTextView.setText(group.getGroup_name());
        holder.timeTextView.setText(TimeFormatHelper.getTimeForTextView(group.getStart_time(),group.getEnd_time()));
        if (group.isActive()) {
            holder.activeTextView.setText("Active");
            holder.activeTextView.setTextColor(holder.itemView.getResources().getColor(R.color.light_green, Resources.getSystem().newTheme()));
        }else {
            holder.activeTextView.setText("Inactive");
            holder.activeTextView.setTextColor(holder.itemView.getResources().getColor(R.color.light_red, Resources.getSystem().newTheme()));
        }
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView classNameTextView;
        private TextView groupNameTextView;
        private TextView timeTextView;
        private MaterialButton deleteButton;
        private MaterialButton editButton;
        private TextView activeTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            timeTextView=itemView.findViewById(R.id.manage_group_time_text_view);
            classNameTextView = itemView.findViewById(R.id.manage_group_class_name_text_view1);
            groupNameTextView = itemView.findViewById(R.id.manage_group_group_name_text_view);
            deleteButton =itemView.findViewById(R.id.manage_group_delete_button);
            editButton =itemView.findViewById(R.id.manage_group_edit_button);
            activeTextView=itemView.findViewById(R.id.manage_group_active_status_text_view);

            deleteButton.setOnClickListener(v -> onClickListener.onClick(getAdapterPosition(),"delete"));

            editButton.setOnClickListener(v -> {
                onClickListener.onClick(getAdapterPosition(),"edit");
            });
        }
    }
}
