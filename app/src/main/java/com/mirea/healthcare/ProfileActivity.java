package com.mirea.healthcare;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;

public class ProfileActivity extends BaseActivity {
    private EditText phoneEditText, addressEditText, usernameEditText;
    private TextView emailTextView;
    private User currentUser;
    private AppDatabase db;
    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        // Инициализация Room Database и AuthManager
        db = AppDatabase.getDatabase(this);
        authManager = new AuthManager(this); // Создаем экземпляр AuthManager

        // Инициализация полей
        usernameEditText = findViewById(R.id.fullNameText);
        phoneEditText = findViewById(R.id.phoneText);
        addressEditText = findViewById(R.id.addressText);
        emailTextView = findViewById(R.id.emailText);

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

            runOnUiThread(() -> {
                if (currentUser != null) {
                    usernameEditText.setText(currentUser.getUsername());
                    emailTextView.setText(currentUser.getEmail());
                    phoneEditText.setText(currentUser.getPhone());
                    addressEditText.setText(currentUser.getAddress());
                }
            });
        }).start();
    }

    private void saveProfile() {
        new Thread(() -> {
            try {
                currentUser.setUsername(usernameEditText.getText().toString());
                currentUser.setPhone(phoneEditText.getText().toString());
                currentUser.setAddress(addressEditText.getText().toString());

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