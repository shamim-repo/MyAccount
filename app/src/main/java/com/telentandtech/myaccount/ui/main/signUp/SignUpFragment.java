package com.telentandtech.myaccount.ui.main.signUp;

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
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.telentandtech.myaccount.DrawerActivity;
import com.telentandtech.myaccount.R;
import com.telentandtech.myaccount.core.DataClass;
import com.telentandtech.myaccount.core.DataValidation;
import com.telentandtech.myaccount.database.entityes.User;

public class SignUpFragment extends Fragment {

    private SignUpViewModel mViewModel;
    private TextInputEditText fullNameEditText;
    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;
    private TextInputEditText confirmPasswordEditText;
    private MaterialButton signUpButton;
    private User authUser;

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fullNameEditText=view.findViewById(R.id.signup_full_name_edit_text_field1);
        emailEditText=view.findViewById(R.id.signup_email_edit_text_field1);
        passwordEditText=view.findViewById(R.id.signup_password_edit_text_field1);
        confirmPasswordEditText=view.findViewById(R.id.signup_confirm_password_edit_text_field1);
        signUpButton=view.findViewById(R.id.signup_button);

        signUpButton.setOnClickListener(v -> {
            String fullName=fullNameEditText.getText().toString().trim();
            String email=emailEditText.getText().toString().trim();
            String password=passwordEditText.getText().toString().trim();
            String confirmPassword=confirmPasswordEditText.getText().toString().trim();
            if(checkData(fullName,email,password,confirmPassword)){
                mViewModel.insertUser(fullName,email,password);
            }
        });
        mViewModel.getUserResultMutableLiveData().observe(getViewLifecycleOwner(), userResult -> {
            if(!userResult.isSuccessful()){
                Toast.makeText(getContext(), userResult.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), userResult.getMessage(), Toast.LENGTH_SHORT).show();
                setSharedPreference(userResult.getUser());
                startActivity(new Intent(getContext(), DrawerActivity.class));
                getActivity().finish();
            }
        });

    }

    private void setSharedPreference(User user) {
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean(DataClass.AUTHENTICATION_STATUS,true);
        editor.putString(DataClass.UID,user.getUid());
        editor.putString(DataClass.USER_EMAIL,user.getEmail());
        editor.putString(DataClass.USER_NAME,user.getFull_name());
        editor.apply();
    }

    private boolean checkData(String fullName,String email,String password, String confirmPassword){
        if(fullName.length()<3) {
            fullNameEditText.setError("Name must be 3 character long");
            return false;
        }
        if(!DataValidation.emailValidation(email)) {
            emailEditText.setError("Invalid email");
            return false;
        }
        if(password.length()<8) {
            passwordEditText.setError("Password must be 8 character long");
            return false;
        }
        if(!password.equals(confirmPassword)) {
            passwordEditText.setError("");
            confirmPasswordEditText.setError("Password not matched");
            return false;
        }
        return true;
    }
}