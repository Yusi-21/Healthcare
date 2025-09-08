package com.mirea.healthcare;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;

public class OthersActivity extends BaseActivity {
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_others);

        checkUserAuthentication();

        setupButton(R.id.go_to_profile, ProfileActivity.class);
        setupButton(R.id.go_to_select_doctors, SelectDoctorsActivity.class);
        setupButton(R.id.go_to_analysis, AnalysisActivity.class);
        setupButton(R.id.go_to_first_aid, FirstAidActivity.class);
        setupButton(R.id.go_to_notification, NotificationsActivity.class);
        setupButton(R.id.go_to_language, LanguageActivity.class);
        setupButton(R.id.go_to_about_us, AboutUsActivity.class);

        setupLogoutButton();
    }
}