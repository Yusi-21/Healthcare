package com.mirea.healthcare;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Locale;

public class BaseActivity extends AppCompatActivity {
    private static final int NOTIFICATION_PERMISSION_CODE = 1001;

    protected AppDatabase db;
    protected AuthManager authManager;

    protected Context coctext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        authManager = new AuthManager(this);
        db = AppDatabase.getDatabase(this);

        applySavedLanguage(); // Применяем язык до super.onCreate()
        super.onCreate(savedInstanceState);

//        // Настройка периодической проверки каждые 15 минут
//        Constraints constraints = new Constraints.Builder()
//                .setRequiresBatteryNotLow(true)
//                .build();
//
//        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(
//                ExpiredAppointmentsWorker.class,
//                15, TimeUnit.MINUTES)
//                .setConstraints(constraints)
//                .build();
//
//        WorkManager.getInstance(context)
//                .enqueueUniquePeriodicWork(
//                        "appointments_check",
//                        ExistingPeriodicWorkPolicy.KEEP,
//                        workRequest);
    }

    protected void checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_PERMISSION_CODE);
            }
        }
    }

    protected void checkUserAuthentication() {
        if (authManager == null || authManager.getCurrentUserId() == -1) {
            startActivity(new Intent(this, LoginActivity.class));

            finish();
        }
    }

    protected void checkAuthAndRedirect() {
        if (authManager.getCurrentUserId() == -1) {
            startActivity(new Intent(this, LoginActivity.class));

            finish();
        }
    }

    protected void loadUserData(TextView nameView) {
        int userId = authManager.getCurrentUserId();
        if (userId == -1) return;

        new Thread(() -> {
            User user = db.userDao().getUserById(userId);
            runOnUiThread(() -> {
                if (user != null && nameView != null) {
                    nameView.setText(user.getUsername());
                }
            });
        }).start();
    }

    protected String getCurrentUserName() {
        int userId = authManager.getCurrentUserId();
        if (userId == -1) return "";

        User user = db.userDao().getUserById(userId);
        return user != null ? user.getUsername() : "";
    }

    protected void applySavedLanguage() {
        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        String langCode = prefs.getString("app_lang", Locale.getDefault().getLanguage());
        setAppLanguage(langCode);
    }

    private void setAppLanguage(String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    protected void saveLanguage(String langCode) {
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("app_lang", langCode);
        editor.apply();
    }

    @SuppressLint("ClickableViewAccessibility")
    protected void setupButton(int buttonId, Class<?> targetActivity, Runnable customAction) {
        View button = findViewById(buttonId);
        button.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100).start();
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                v.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                if (targetActivity != null) {
                    startActivity(new Intent(this, targetActivity));
                }
                if (customAction != null) {
                    customAction.run();
                }
            }
            return true;
        });
    }

    protected void showCustomToast(String message, boolean isSuccess) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, findViewById(R.id.custom_toast_layout));

        TextView text = layout.findViewById(R.id.custom_toast_text);
        text.setText(message);

        int backgroundColor = isSuccess
                ? ContextCompat.getColor(this, R.color.toast_success)
                : ContextCompat.getColor(this, R.color.toast_error);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            layout.setBackgroundTintList(ColorStateList.valueOf(backgroundColor));
        } else {
            GradientDrawable shape = (GradientDrawable) layout.getBackground();
            shape.setColor(backgroundColor);
        }

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    protected void showDialogWithStyledButtons(DialogInterface dialog) {
        DialogUtils.setupButtons(dialog, this);
        if (dialog instanceof Dialog) {
            ((Dialog) dialog).show();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    protected void setupButton(int buttonId, Class<?> targetActivity) {
        setupButton(buttonId, targetActivity, null);
    }

    protected void setupLogoutButton() {
        Button btnLogout = findViewById(R.id.btn_logout);
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                authManager.logout();
                startActivity(new Intent(this, LoginActivity.class));
                Toast.makeText(this, R.string.logged_out_successfully, Toast.LENGTH_LONG).show();

                finish();
            });
        }
    }
}
