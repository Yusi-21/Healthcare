package com.mirea.healthcare;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;

public class RegisterPatientActivity extends BaseActivity {
    private EditText etEmail, etPassword, etUsername;
    private Button btnRegister;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_patient);

        etEmail = findViewById(R.id.et_email_patient);
        etPassword = findViewById(R.id.et_password_patient);
        etUsername = findViewById(R.id.et_username_patient);
        btnRegister = findViewById(R.id.btn_sign_up_patient);

        db = AppDatabase.getDatabase(this);

        btnRegister.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String username = etUsername.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
                showCustomToast(getString(R.string.fill_all_fields), false);
                return;
            }

            new Thread(() -> {
                User existingUser = db.userDao().getUserByEmail(email);
                if (existingUser != null) {
                    runOnUiThread(() ->
                            showCustomToast(getString(R.string.user_exists), false));
                    return;
                }

                User user = new User(email, password, username, UserTypes.PATIENT, "");
                db.userDao().insert(user);

                runOnUiThread(() -> {
                    showCustomToast(getString(R.string.registration_success), true);
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    }, 300);
                });
            }).start();
        });
    }

}