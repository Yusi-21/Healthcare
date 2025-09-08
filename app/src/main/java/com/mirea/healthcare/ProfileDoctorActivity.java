package com.mirea.healthcare;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileDoctorActivity extends AppCompatActivity {

    private EditText doctorPhoneEditText, doctorAddressEditText, doctorUernameEditText;
    private TextView doctorEmailTextView, doctorProfessionText;
    private User currentUser;
    private AppDatabase db;
    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_doctor);

        db = AppDatabase.getDatabase(this);
        authManager = new AuthManager(this);
        // Инициализация полей
        doctorUernameEditText = findViewById(R.id.fullNameText);
        doctorPhoneEditText = findViewById(R.id.phoneText);
        doctorAddressEditText = findViewById(R.id.addressText);
        doctorEmailTextView = findViewById(R.id.emailText);
        doctorProfessionText = findViewById(R.id.professionText);

        // Загрузка данных пользователя
        loadUserData();

        // Обработчик сохранения
        findViewById(R.id.saveButton).setOnClickListener(v -> saveProfile());
        findViewById(R.id.cancelButton).setOnClickListener(v -> finish());
    }


    private void loadUserData() {
        new Thread(() -> {
            // Получаем текущего пользователя через AuthManager
            int currentUserId = authManager.getCurrentUserId();
            currentUser = db.userDao().getUserById(currentUserId);

            DoctorEntity doctorEntity = db.doctorDao().getByUserId(currentUserId);
            String specialization = doctorEntity != null ? doctorEntity.specialization : "";

            runOnUiThread(() -> {
                if (currentUser != null) {
                    doctorUernameEditText.setText(currentUser.getUsername());
                    doctorEmailTextView.setText(currentUser.getEmail());
                    doctorPhoneEditText.setText(currentUser.getPhone());
                    doctorAddressEditText.setText(currentUser.getAddress());

                    doctorProfessionText.setText(getSpecializationDisplayName(specialization));
                }
            });
        }).start();
    }

    private String getSpecializationDisplayName(String specializationKey) {
        String[] specializations = getResources().getStringArray(R.array.doctor_specializations);

        switch (specializationKey) {
            case Specializations.CARDIOLOGIST:
                return specializations[0];
            case Specializations.NEUROLOGIST:
                return specializations[1];
            case Specializations.OPHTHALMOLOGIST:
                return specializations[2];
            case Specializations.PEDIATRICIAN:
                return specializations[3];
            case Specializations.SURGEON:
                return specializations[4];
            case Specializations.THERAPIST:
                return specializations[5];
            default:
                return specializations[5]; // По умолчанию терапевт
        }
    }

    private void saveProfile() {
        new Thread(() -> {
            try {
                currentUser.setUsername(doctorUernameEditText.getText().toString());
                currentUser.setPhone(doctorPhoneEditText.getText().toString());
                currentUser.setAddress(doctorAddressEditText.getText().toString());

                // Сохраняем в БД
                db.userDao().updateUser(currentUser);

                runOnUiThread(() -> {
                    Toast.makeText(this, R.string.profile_updated, Toast.LENGTH_SHORT).show();
                    Class<?> targetActivity;
                    if (currentUser.getUserType().equals(UserTypes.DOCTOR)) {
                        targetActivity = DoctorHomeActivity.class;
                    } else {
                        targetActivity = HomeActivity.class;
                    }

                    startActivity(new Intent(this, targetActivity));
                    finish();
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, R.string.profile_updated_error, Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }
}