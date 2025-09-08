package com.mirea.healthcare;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


import androidx.activity.EdgeToEdge;

public class RegisterDoctorActivity extends BaseActivity {
    private EditText etEmail, etPassword, etUsername;
    private Button btnRegister;
    private Spinner spinnerSpecialization;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_doctor);

        // Инициализация UI элементов
        etEmail = findViewById(R.id.et_email_doctor);
        etPassword = findViewById(R.id.et_password_doctor);
        etUsername = findViewById(R.id.et_username_doctor);
        btnRegister = findViewById(R.id.btn_sign_up_doctor);
        spinnerSpecialization = findViewById(R.id.spinner_specialization);

        // Инициализация базы данных
        db = AppDatabase.getDatabase(this);

        // Настройка Spinner для выбора специализации
        setupSpecializationSpinner();

        btnRegister.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String username = etUsername.getText().toString().trim();

            // Валидация полей
            if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
                showCustomToast(getString(R.string.fill_all_fields), false);
                return;
            }

            new Thread(() -> {
                // Создание нового пользователя
                User user = new User(email, password, username, UserTypes.DOCTOR, "");
                db.userDao().insert(user);  // Просто вставляем без возврата ID

                // Получаем только что созданного пользователя по email
                User createdUser = db.userDao().getUserByEmail(email);
                if (createdUser == null) {
                    throw new IllegalStateException("User creation failed: email=" + email);
                }

                // Создание записи врача
                DoctorEntity doctorEntity = new DoctorEntity();
                doctorEntity.userId = createdUser.getId();
                doctorEntity.specialization = getSpecializationKey(spinnerSpecialization.getSelectedItemPosition());
                db.doctorDao().insert(doctorEntity);

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

    private void setupSpecializationSpinner() {
        // Получаем локализованные названия специализаций
        String[] specializations = getResources().getStringArray(R.array.doctor_specializations);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.custom_spinner_item,
                specializations
        );
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        spinnerSpecialization.setAdapter(adapter);

        spinnerSpecialization.setPopupBackgroundResource(android.R.color.white);
    }

    private String getSpecializationKey(int position) {
        switch (position) {
            case 0: return Specializations.CARDIOLOGIST;
            case 1: return Specializations.NEUROLOGIST;
            case 2: return Specializations.OPHTHALMOLOGIST;
            case 3: return Specializations.PEDIATRICIAN;
            case 4: return Specializations.SURGEON;
            case 5: return Specializations.THERAPIST;

            default: return Specializations.THERAPIST;
        }
    }

}