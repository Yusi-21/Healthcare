package com.mirea.healthcare;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.splashscreen.SplashScreen;

public class LoginActivity extends BaseActivity {
    private boolean isDataLoaded = false;

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView goToRegister;
    private AuthManager authManager;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        applySavedLanguage();

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        splashScreen.setKeepOnScreenCondition(() -> !isDataLoaded);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            isDataLoaded = true;
        }, 3000);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password_login);
        btnLogin = findViewById(R.id.btn_login);
        goToRegister = findViewById(R.id.go_to_register_tv);

        authManager = new AuthManager(this);
        db = AppDatabase.getDatabase(this);

        // Проверяем, авторизован ли пользователь
        if (authManager.isLoggedIn()) {
            launchHomeBasedOnUserType(authManager.getCurrentUserId());
            return;
        }

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                showCustomToast(getString(R.string.fill_all_fields), false);
                return;
            }

            new Thread(() -> {
                User user = db.userDao().getUser(email, password);
                runOnUiThread(() -> {
                    if (user != null) {
                        authManager.saveUserCredentials(user.getId());
                        showCustomToast(getString(R.string.login_successful), true);
                        launchHomeBasedOnUserType(user.getId());
                    } else {
                        showCustomToast(getString(R.string.invalid_email_password), false);
                    }
                });
            }).start();
        });

        goToRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, CheckDoctorPatientActivity.class));
        });
    }

    private void launchHomeBasedOnUserType(int userId) {
        new Thread(() -> {
            User user = db.userDao().getUserById(userId);
            if (user != null) {
                Intent intent = UserTypes.DOCTOR.equals(user.getUserType())
                        ? new Intent(this, DoctorHomeActivity.class)
                        : new Intent(this, HomeActivity.class);

                runOnUiThread(() -> {
                    startActivity(intent);
                    finish();
                });
            }
        }).start();
    }
}
