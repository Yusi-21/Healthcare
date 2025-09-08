package com.mirea.healthcare;

import android.content.Context;
import android.util.Log;

import androidx.work.Worker;

import androidx.annotation.NonNull;
import androidx.work.WorkerParameters;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExpiredAppointmentsWorker extends Worker {
    private static final String TAG = "AppointmentsWorker";

    public ExpiredAppointmentsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

//    @NonNull
//    @Override
//    public Result doWork() {
//        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
//            String currentDateTime = sdf.format(new Date());
//            if (currentDateTime == null || currentDateTime.split(" ").length != 2) {
//                return Result.failure();
//            }
//
//            String[] parts = currentDateTime.split(" ");
//            String currentDate = parts[0];
//            String currentTime = parts[1];
//
//            AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
//            NotificationHelper notificationHelper = new NotificationHelper(getApplicationContext());
//
//            // 1. Закрываем просроченные записи
//            int expiredCount = db.appointmentDao().countExpiredAppointments(currentDate);
//            if (expiredCount > 0) {
//                db.appointmentDao().closeExpiredAppointments(currentDate);
//            }
//
//            // 2. Проверяем записи, о которых нужно напомнить
//            Calendar calendar = Calendar.getInstance();
//            calendar.add(Calendar.MINUTE, 10);
//            String reminderTime = String.format(Locale.getDefault(), "%02d:%02d",
//                    calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
//
//            List<Appointment> appointmentsToRemind = db.appointmentDao()
//                    .getAppointmentsByTimeRange(
//                            AppointmentStatus.BOOKED,
//                            currentDate,
//                            currentTime,
//                            reminderTime
//                    );
//
//            if (appointmentsToRemind != null) {
//                for (Appointment appointment : appointmentsToRemind) {
//                    if (appointment != null) {
//                        notificationHelper.sendReminderNotification(appointment);
//                        db.appointmentDao().updateStatus(appointment.id, AppointmentStatus.REMINDER_SENT);
//                    }
//                }
//            }
//
//            return Result.success();
//        } catch (Exception e) {
//            Log.e(TAG, "Error in worker", e);
//            return Result.failure();
//        }
//    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            AppDatabase db = AppDatabase.getDatabase(getApplicationContext());

            // Логируем перед обновлением
            int countBefore = db.appointmentDao().countExpiredAppointments(currentDate);
            Log.d("Worker", "Expired appointments found: " + countBefore);

            if (countBefore > 0) {
                db.appointmentDao().closeExpiredAppointments(currentDate);
                // Логируем после обновления
                int countAfter = db.appointmentDao().countExpiredAppointments(currentDate);
                Log.d("Worker", "Appointments closed: " + (countBefore - countAfter));
                return Result.success();
            }
            return Result.success();
        } catch (Exception e) {
            Log.e("Worker", "Error", e);
            return Result.failure();
        }
    }
}