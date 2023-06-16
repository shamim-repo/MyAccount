package com.telentandtech.myaccount.ui.main.drawer.classes.manageClass;

import static com.telentandtech.myaccount.core.DataClass.UID;
import static com.telentandtech.myaccount.core.DataClass.USER_EMAIL;
import static com.telentandtech.myaccount.core.DataClass.USER_NAME;

import androidx.lifecycle.ViewModelProvider;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;
import com.google.android.material.snackbar.Snackbar;
import com.telentandtech.myaccount.R;
import com.telentandtech.myaccount.core.OnClickListener;
import com.telentandtech.myaccount.database.entityes.User;
import com.telentandtech.myaccount.ui.main.drawer.classes.manageClass.recycleViewHelper.ManageClassAdapter;

import java.lang.annotation.Target;

public class ManageClassFragment extends Fragment implements OnClickListener {

    private ManageClassViewModel mViewModel;
    private RecyclerView recyclerView;
    private ManageClassAdapter adapter;
    private User authUser;
    private int lastDeletePosition;


    public static ManageClassFragment newInstance() {
        return new ManageClassFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_manage_class, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ManageClassViewModel.class);
        getSharedPreferences();
    }

    private void getSharedPreferences() {
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getContext());
        authUser = new User(
                sharedPreferences.getString(UID,""),
                sharedPreferences.getString(USER_NAME,""),
                sharedPreferences.getString(USER_EMAIL,"")
        );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=view.findViewById(R.id.class_manage_recycler_view);

        mViewModel.getAllClassesList(authUser.getUid());

        mViewModel.getClassListLiveData().observe(getViewLifecycleOwner(), classes -> {
            if (classes.isSuccessful()) {
                adapter = new ManageClassAdapter(classes.getClassList(), this);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(adapter);
            }else {
                Toast.makeText(getContext(), classes.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        mViewModel.getDeleteClassLiveData().observe(getViewLifecycleOwner(), deleteClass -> {
            if (deleteClass.isSuccessful() && deleteClass.getClasse()!=null) {
                adapter.removeClass(lastDeletePosition);
                Snackbar.make(getView(), deleteClass.getMessage(), Snackbar.LENGTH_LONG)
                        .setAction("Undo", v -> {
                            mViewModel.insertClass(deleteClass.getClasse());
                        }).show();

            }else {
                Toast.makeText(getContext(), deleteClass.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        mViewModel.getInsertClassLiveData().observe(getViewLifecycleOwner(), insertClass -> {
            if (insertClass.isSuccessful()) {
                adapter.insertClass(lastDeletePosition,insertClass.getClasse());
            }else {
                Toast.makeText(getContext(), insertClass.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(int position, String option) {
        MaterialAlertDialogBuilder builder=
                new MaterialAlertDialogBuilder(getContext())
                        .setTitle("Delete Class")
                        .setMessage("Are you sure you want to delete this class?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                                mViewModel.deleteClass(adapter.getClass(position));
                                lastDeletePosition=position;})
                        .setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}