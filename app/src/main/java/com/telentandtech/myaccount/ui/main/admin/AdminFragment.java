package com.telentandtech.myaccount.ui.main.admin;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.telentandtech.myaccount.R;
import com.telentandtech.myaccount.core.DataValidation;
import com.telentandtech.myaccount.database.entityes.User;

import java.util.List;

public class AdminFragment extends Fragment {

    private AdminViewModel mViewModel;
    private MaterialButton updateButton;
    private TextInputEditText passwordEditText;
    private TextInputEditText confirmPasswordEditText;
    private Spinner spinner;
    private List<User> userList;

    public static AdminFragment newInstance() {
        return new AdminFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AdminViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        passwordEditText = view.findViewById(R.id.admin_password_edit_text_field);
        confirmPasswordEditText = view.findViewById(R.id.admin_confirm_password_edit_text_field);
        spinner = view.findViewById(R.id.admin_email_spinner);
        updateButton = view.findViewById(R.id.admin_update_button);

        mViewModel.getUsers();
        mViewModel.getUserListLiveData().observe(getViewLifecycleOwner(), userListResult -> {
            if (userListResult.getIsSuccessful() ) {
                userList = userListResult.getUsers();

                userListToEmailList(userList);

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_dropdown_item, userListToEmailList(userListResult.getUsers()));
                spinner.setAdapter(arrayAdapter);
                spinner.setOnItemSelectedListener(onItemSelectedListener);

            }else {
                Toast.makeText(getContext(), userListResult.getMessage(), Toast.LENGTH_SHORT).show();
            }});

        updateButton.setOnClickListener(v -> {
            String password = passwordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();
            String email = spinner.getSelectedItem().toString();
                if(checkData(password, confirmPassword, email)){
                    User user=userList.get(spinner.getSelectedItemPosition());
                    user.setEmail(email);
                    user.setPassword(password);
                    mViewModel.updateUser(user);
                }
        });
        mViewModel.getUserUpdateLiveData().observe(getViewLifecycleOwner(), userResult -> {
            if (userResult.isSuccessful()){
                Toast.makeText(getContext(), userResult.getMessage(), Toast.LENGTH_SHORT).show();
                mViewModel.getUsers();
            }else {
                Toast.makeText(getContext(), userResult.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private boolean checkData(String password, String confirmPassword, String email) {
        if(!DataValidation.emailValidation(email)){
            spinner.requestFocus();
            Toast.makeText(getContext(), "Please select email", Toast.LENGTH_SHORT).show();
        return false;
        }
        if (password.length()<8){
            passwordEditText.setError("Password must be at least 8 characters");
            return false;
        }if (!password.equals(confirmPassword)){
            confirmPasswordEditText.setError("Password does not match");
            return false;
        }
        return true;
    }



    private Spinner.OnItemSelectedListener onItemSelectedListener = new Spinner.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            User user = userList.get(position);
            passwordEditText.setText(user.getPassword());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private String[] userListToEmailList(List<User> userList) {
        //user list to email list
        String [] emailList = new String[userList.size()];
        for (int i = 0; i < userList.size(); i++) {
            emailList[i] = userList.get(i).getEmail();
        }
        return emailList;
    }
}