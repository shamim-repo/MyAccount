package com.telentandtech.myaccount.ui.main.drawer.editProfile;

import androidx.lifecycle.ViewModelProvider;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import com.telentandtech.myaccount.R;
import com.telentandtech.myaccount.core.DataClass;
import com.telentandtech.myaccount.database.entityes.User;

public class EditProfileFragment extends Fragment {

    private EditProfileViewModel mViewModel;
    private TextInputEditText fullNameEitText;
    private TextInputEditText currentPasswordEitText;
    private TextInputEditText newPasswordEitText;
    private TextInputEditText confirmPasswordEitText;
    private TextView emailView;
    private MaterialButton updateButton;
    private User authUser;

    public static EditProfileFragment newInstance() {
        return new EditProfileFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EditProfileViewModel.class);
        getSharePreference();
    }

    private void getSharePreference() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        authUser = new User(
                sharedPreferences.getString(DataClass.UID, ""),
                sharedPreferences.getString(DataClass.USER_NAME, ""),
                sharedPreferences.getString(DataClass.USER_EMAIL, "")
        );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fullNameEitText = view.findViewById(R.id.edit_profile_full_name_edit_text_field1);
        currentPasswordEitText = view.findViewById(R.id.edit_profile_current_pass_edit_text_field);
        newPasswordEitText = view.findViewById(R.id.edit_profile_password_edit_text_field);
        confirmPasswordEitText = view.findViewById(R.id.edit_profile_confirm_password_edit_text_field);
        emailView = view.findViewById(R.id.edit_profile_email_text_view);
        updateButton = view.findViewById(R.id.edit_profile_update_button);

        if (authUser != null) {
            if (authUser.getUid() != null) {
                mViewModel.getUser(authUser.getUid());
            }
        }
        mViewModel.getUserLiveData().observe(getViewLifecycleOwner(), userResult -> {
            if (userResult.isSuccessful()) {
                Log.d("EditProfileFragment", "onViewCreated: " + userResult.getUser().getFull_name());
                authUser = userResult.getUser();
                fullNameEitText.setText(authUser.getFull_name());
                emailView.setText(authUser.getEmail());
            }
        });

        updateButton.setOnClickListener(v -> {
            String fullName = fullNameEitText.getText().toString();
            String currentPassword = currentPasswordEitText.getText().toString();
            String newPassword = newPasswordEitText.getText().toString();
            String confirmPassword = confirmPasswordEitText.getText().toString();

            if (fullName.length()<3) {
                fullNameEitText.setError("Name must be at least 3 characters");
                fullNameEitText.requestFocus();
                return;
            }
            if (currentPassword.isEmpty()) {
                currentPasswordEitText.setError("Current password is required");
                currentPasswordEitText.requestFocus();
                return;
            }
            if (newPassword.isEmpty()) {
                newPasswordEitText.setError("New password is required");
                newPasswordEitText.requestFocus();
                return;
            }
            if (confirmPassword.isEmpty()) {
                confirmPasswordEitText.setError("Confirm password is required");
                confirmPasswordEitText.requestFocus();
                return;
            }
            if (newPassword.length()<8) {
                newPasswordEitText.setError("Password must be at least 8 characters");
                newPasswordEitText.requestFocus();
                return;
            }
            if (!newPassword.equals(confirmPassword)) {
                confirmPasswordEitText.setError("Password not match");
                confirmPasswordEitText.requestFocus();
                return;
            }
            mViewModel.updateProfile(authUser, fullName, newPassword);
        });
        mViewModel.getProfileUpdateLiveData().observe(getViewLifecycleOwner(), userResult -> {
            if (userResult.isSuccessful()) {
                fullNameEitText.setText(userResult.getUser().getFull_name());
                Toast.makeText(getContext(), userResult.getMessage(), Toast.LENGTH_SHORT).show();
            }else
                Toast.makeText(getContext(), userResult.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}