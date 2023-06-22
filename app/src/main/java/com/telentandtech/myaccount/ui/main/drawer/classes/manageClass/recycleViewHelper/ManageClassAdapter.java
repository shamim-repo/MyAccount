package com.telentandtech.myaccount.ui.main.drawer.classes.manageClass.recycleViewHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.telentandtech.myaccount.R;

import com.telentandtech.myaccount.core.OnClickListener;
import com.telentandtech.myaccount.database.entityes.Classe;

import java.util.List;

public class ManageClassAdapter extends RecyclerView.Adapter<ManageClassAdapter.ViewHolder> {

    private List<Classe> classList;
    private OnClickListener onClickListener;

    public ManageClassAdapter(List<Classe> classList, OnClickListener onClickListener) {
        this.classList = classList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ManageClassAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mange_class_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ManageClassAdapter.ViewHolder holder, int position) {
        Classe classe=classList.get(position);
        holder.classNameTextView.setText(classe.getClass_name());
    }
    public Classe getClass(int position) {
        return classList.get(position);
    }
    public void removeClass(int position) {
        classList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, classList.size());
    }
    @Override
    public int getItemCount() {
        return classList.size();
    }

    public void insertClass(int position,Classe classe) {
        classList.add(position,classe);
        notifyItemInserted(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView classNameTextView;
        private MaterialButton deleteClassButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            classNameTextView=itemView.findViewById(R.id.manage_class_class_name_text_view);
            deleteClassButton=itemView.findViewById(R.id.manage_class_delete_button);
            deleteClassButton.setOnClickListener(v -> onClickListener.onClick(getAdapterPosition(),"delete"));
        }
    }
}
