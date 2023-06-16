package com.telentandtech.myaccount;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import com.telentandtech.myaccount.LogInActivity;

import com.telentandtech.myaccount.core.DataClass;
import com.telentandtech.myaccount.database.entityes.User;
import com.telentandtech.myaccount.databinding.ActivityDrawerBinding;

public class DrawerActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityDrawerBinding binding;
    static final float END_SCALE = 0.7f;
    private View contentView;
    private MaterialButton editProfileButton;
    private MaterialButton logoutButton;
    private TextView userName;
    private TextView userEmail;

    private User authUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        getUserSharedPreference(preferences);

        binding = ActivityDrawerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        contentView = findViewById(R.id.app_bar_drawer);

        setSupportActionBar(binding.appBarDrawer.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);

                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });

        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_dashboard, R.id.nav_add_class,
                R.id.nav_add_group, R.id.nav_mange_group,
                R.id.nav_add_fee, R.id.nav_add_student,
                R.id.nav_manage_fee, R.id.nav_manage_student,
                R.id.nav_attendance, R.id.nav_payment,
                R.id.nav_setting, R.id.nav_about)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_drawer);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // set user name and email in drawer header
        editProfileButton = navigationView.getHeaderView(0).findViewById(R.id.edit_profile_button);
        logoutButton = navigationView.getHeaderView(0).findViewById(R.id.logout_button);
        userName = navigationView.getHeaderView(0).findViewById(R.id.user_name_text_view);
        userEmail = navigationView.getHeaderView(0).findViewById(R.id.user_email_text_view);

        userEmail.setText(authUser.getEmail());
        userName.setText(authUser.getFull_name());

        editProfileButton.setOnClickListener(v -> {
            navController.navigate(R.id.nav_edit_profile);
        });
        logoutButton.setOnClickListener(v -> {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(DataClass.AUTHENTICATION_STATUS, false);
            editor.remove(DataClass.USER_NAME);
            editor.remove(DataClass.USER_EMAIL);
            editor.remove(DataClass.UID);
            editor.commit();
            Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
            startActivity(intent);
            finish();

        });


    }

    private void getUserSharedPreference(SharedPreferences sharedPreferences) {
        authUser = new User(
                sharedPreferences.getString(DataClass.UID, null),
                sharedPreferences.getString(DataClass.USER_NAME, "Unknown"),
                sharedPreferences.getString(DataClass.USER_EMAIL, "Unknown")
        );
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_drawer);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}