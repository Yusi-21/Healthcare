package com.mirea.healthcare;

import android.app.Application;
import android.util.Log;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import java.util.concurrent.TimeUnit;

public class HealthcareApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("HealthcareApp", "Application created");
        scheduleAppointmentCheck();
    }

    private void scheduleAppointmentCheck() {
        PeriodicWorkRequest checkRequest = new PeriodicWorkRequest.Builder(
                ExpiredAppointmentsWorker.class,
                5, // Интервал - 5 секунд
                TimeUnit.SECONDS
        ).build();

        // Для периодических задач используем enqueueUniquePeriodicWork()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "expired_appointments_check",
                ExistingPeriodicWorkPolicy.REPLACE, // Используем специальный класс для периодических задач
                checkRequest
        );
        Log.d("HealthcareApp", "Worker scheduled");
    }
}