package com.telentandtech.myaccount;



import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import com.telentandtech.myaccount.core.DataClass;
import com.telentandtech.myaccount.ui.main.login.LogInFragment;

public class LogInActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            setContentView(R.layout.activity_log_in);
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getApplicationContext(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                if (sharedPreferences.getBoolean(DataClass.AUTHENTICATION_STATUS, false)) {
                    Intent intent = new Intent(this, DrawerActivity.class);
                    intent.putExtra(DataClass.UID, sharedPreferences.getString(DataClass.UID, ""));
                    intent.putExtra(DataClass.USER_EMAIL, sharedPreferences.getString(DataClass.USER_EMAIL, ""));
                    intent.putExtra(DataClass.USER_NAME, sharedPreferences.getString(DataClass.USER_NAME, ""));
                    startActivity(intent);
                    finish();
                } else
                    getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, LogInFragment.newInstance())
                        .commitNow();
            } else {
                requestPermissions(new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            if (sharedPreferences.getBoolean(DataClass.AUTHENTICATION_STATUS, false)) {
                Intent intent = new Intent(this, DrawerActivity.class);
                intent.putExtra(DataClass.UID, sharedPreferences.getString(DataClass.UID, ""));
                intent.putExtra(DataClass.USER_EMAIL, sharedPreferences.getString(DataClass.USER_EMAIL, ""));
                intent.putExtra(DataClass.USER_NAME, sharedPreferences.getString(DataClass.USER_NAME, ""));
                startActivity(intent);
                finish();
            } else
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, LogInFragment.newInstance())
                        .commitNow();
        } else {
            requestPermissions(new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

    }
}