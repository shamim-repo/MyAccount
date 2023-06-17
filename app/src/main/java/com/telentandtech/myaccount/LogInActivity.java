package com.telentandtech.myaccount;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.SharedPreferencesKt;
import androidx.preference.PreferenceManager;

import com.telentandtech.myaccount.core.DataClass;
import com.telentandtech.myaccount.database.dao.UserDao;
import com.telentandtech.myaccount.database.dataBase.AccountDatabase;
import com.telentandtech.myaccount.database.entityes.User;
import com.telentandtech.myaccount.ui.main.login.LogInFragment;

public class LogInActivity extends AppCompatActivity {

    private User authUser;
    private AccountDatabase accountDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (sharedPreferences.getBoolean(DataClass.AUTHENTICATION_STATUS,false)){
            Intent intent=new Intent(this,DrawerActivity.class);
            intent.putExtra(DataClass.UID,sharedPreferences.getString(DataClass.UID,""));
            intent.putExtra(DataClass.USER_EMAIL,sharedPreferences.getString(DataClass.USER_EMAIL,""));
            intent.putExtra(DataClass.USER_NAME,sharedPreferences.getString(DataClass.USER_NAME,""));
            startActivity(intent);
            finish();
        }else {

            UserDao userDao = AccountDatabase.getInstance(getApplicationContext()).userDao();
            userDao.getUsers();


            setContentView(R.layout.activity_log_in);
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, LogInFragment.newInstance())
                        .commitNow();
            }
        }
    }

}