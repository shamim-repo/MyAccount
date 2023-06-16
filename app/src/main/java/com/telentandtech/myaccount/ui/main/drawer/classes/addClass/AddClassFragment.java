package com.telentandtech.myaccount.ui.main.drawer.classes.addClass;

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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.telentandtech.myaccount.R;
import com.telentandtech.myaccount.database.entityes.Classe;
import com.telentandtech.myaccount.database.entityes.User;

import java.sql.Timestamp;

public class AddClassFragment extends Fragment {
    private TextInputEditText classNameEditText;
    private AddClassViewModel mViewModel;
    private User authUser;

    public static AddClassFragment newInstance() {
        return new AddClassFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_class, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AddClassViewModel.class);
        getSharedPref();

    }

    private void getSharedPref() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        authUser = new User(
                sharedPreferences.getString(UID,""),
                sharedPreferences.getString(USER_NAME,""),
                sharedPreferences.getString(USER_EMAIL,""));
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        classNameEditText = view.findViewById(R.id.class_name_edit_text);
        MaterialButton addButton = view.findViewById(R.id.add_class_add_button);


        addButton.setOnClickListener(v -> {
            String className = classNameEditText.getText().toString();
            if(checkData(className)){
                mViewModel.insertClass(new Classe(className,
                        new Timestamp(System.currentTimeMillis()),authUser.getUid()));
                addButton.setEnabled(false);
            }
        });
        mViewModel.getInsertClassLiveData().observe(getViewLifecycleOwner(), classes -> {
            addButton.setEnabled(true);
            if(classes.isSuccessful()){
                Toast.makeText(getContext(), classes.getMessage(), Toast.LENGTH_SHORT).show();
                classNameEditText.setText("");
            }else{
                Toast.makeText(getContext(), classes.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private Boolean checkData(String className){
        if(className.length()<3){
            classNameEditText.setError("Class name must be at least 3 characters long");
            classNameEditText.requestFocus();
            return false;
        }
        return true;

    }
}