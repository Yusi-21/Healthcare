package com.mirea.healthcare;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;

public class OthersDoctorActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_others_doctor);

        checkUserAuthentication();

        setupButton(R.id.go_to_profile_doctor, ProfileDoctorActivity.class);
        setupButton(R.id.go_to_see_patients, SeePatientsActivity.class);
//        setupButton(R.id.go_to_analysis_doctor, AnalysisActivity.class);
        setupButton(R.id.go_to_first_aid_doctor, FirstAidActivity.class);
        setupButton(R.id.go_to_notification_doctor, NotificationsActivity.class);
        setupButton(R.id.go_to_language_doctor, LanguageActivity.class);
        setupButton(R.id.go_to_about_us_doctor, AboutUsActivity.class);

        setupLogoutButton();
    }
}