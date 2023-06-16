package com.telentandtech.myaccount.ui.main.login;


import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.telentandtech.myaccount.DrawerActivity;
import com.telentandtech.myaccount.R;
import com.telentandtech.myaccount.core.DataClass;
import com.telentandtech.myaccount.core.DataValidation;
import com.telentandtech.myaccount.database.entityes.User;
import com.telentandtech.myaccount.ui.main.admin.AdminFragment;
import com.telentandtech.myaccount.ui.main.signUp.SignUpFragment;

public class LogInFragment extends Fragment {

    private LogInFragmentViewModel mViewModel;
    private MaterialButton loginButton;
    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;
    private TextView createAccountTextView;


    public static LogInFragment newInstance() {
        return new LogInFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LogInFragmentViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_log_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginButton = view.findViewById(R.id.login_button);
        emailEditText = view.findViewById(R.id.login_email_edit_text_field1);
        passwordEditText = view.findViewById(R.id.login_password_edit_text_field1);
        createAccountTextView = view.findViewById(R.id.create_new_account_text_view);

        mViewModel.getAuthResult().observe(getViewLifecycleOwner(), userResult -> {
            if(userResult.isSuccessful()) {
                if (userResult.getUser().getEmail().equals(DataClass.ADMIN_EMAIL)) {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .setTransition(androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .replace(R.id.container,new AdminFragment())
                            .commit();
                }else {
                setAuthStatusPreference(userResult.getUser());
                gotoDrawerActivity();
                }
            }
            else
                Toast.makeText(getContext(), userResult.getMessage(), Toast.LENGTH_SHORT).show();
        });

        loginButton.setOnClickListener(v -> {
            String email=emailEditText.getText().toString();
            String password=passwordEditText.getText().toString();

            if(checkData(email,password)) {
                    mViewModel.authenticateUser(email, password);
            }
        });

        createAccountTextView.setOnClickListener(v -> getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .setTransition(androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.container,new SignUpFragment())
                .addToBackStack("Login").commit());

    }

    private boolean checkData(String email,String password){
        if(!DataValidation.emailValidation(email)) {
            emailEditText.setError("Enter Valid Email");
            return false;
        }
        if(password.trim().length()<8) {
            passwordEditText.setError("Password must be 8 character long");
            return false;
        }
        return true;
    }

    private void gotoDrawerActivity(){
        Intent intent=new Intent(getContext(), DrawerActivity.class);
        startActivity(intent);
    }

    private void setAuthStatusPreference(User user){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(DataClass.AUTHENTICATION_STATUS, true);
        editor.putString(DataClass.UID, user.getUid());
        editor.putString(DataClass.USER_EMAIL, user.getEmail());
        editor.putString(DataClass.USER_NAME, user.getFull_name());
        editor.apply();
    }
}