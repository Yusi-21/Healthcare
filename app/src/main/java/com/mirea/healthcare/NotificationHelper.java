//package com.mirea.healthcare;
//
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.os.Build;
//
//import androidx.core.app.ActivityCompat;
//import androidx.core.app.NotificationCompat;
//import androidx.core.app.NotificationManagerCompat;
//
//public class NotificationHelper { // красный
//    private final Context context;
//    private final NotificationManagerCompat notificationManager;
//
//    public NotificationHelper(Context context) {
//        this.context = context;
//        this.notificationManager = NotificationManagerCompat.from(context);
//        createNotificationChannel();
//    }
//
//    private void createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(
//                    "polyclinic_channel",
//                    "Polyclinic Notifications",
//                    NotificationManager.IMPORTANCE_HIGH);
//            channel.setDescription("Notifications for doctors and patients");
//            notificationManager.createNotificationChannel(channel);
//        }
//    }
//
//    private void sendNotification(Notification notification, int notificationId) {
//        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS)
//                == PackageManager.PERMISSION_GRANTED) {
//            notificationManager.notify(notificationId, notification);
//        }
//    }
//
//    public void sendAppointmentNotification(Appointment appointment, String title) {
//        if (appointment == null) return;
//
//        try {
//            Intent intent = new Intent(context, NotificationsActivity.class);
//            PendingIntent pendingIntent = PendingIntent.getActivity(
//                    context, appointment.id, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
//
//            String contentText = (appointment.specialization != null ? appointment.specialization : "Запись") +
//                    " в " + (appointment.time != null ? appointment.time : "");
//
//            Notification notification = new NotificationCompat.Builder(context, "polyclinic_channel")
//                    .setSmallIcon(R.drawable.icon_doctor_nurse)
//                    .setContentTitle(title != null ? title : "Новая запись: " +
//                            (appointment.patientName != null ? appointment.patientName : ""))
//                    .setContentText(contentText)
//                    .setPriority(NotificationCompat.PRIORITY_HIGH)
//                    .setContentIntent(pendingIntent)
//                    .setAutoCancel(true)
//                    .build();
//
//            sendNotification(notification, appointment.id);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    public void sendNewAppointmentNotification(Appointment appointment) {
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "polyclinic_channel")
//                .setSmallIcon(R.drawable.icon_doctor_nurse)
//                .setContentTitle("Новая запись")
//                .setContentText(String.format("%s на %s %s",
//                        appointment.patientName, appointment.date, appointment.time))
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setAutoCancel(true);
//
//        // Добавляем действия для врача
//        if (appointment.specialization != null) {
//            Intent confirmIntent = new Intent(context, AppointmentActionReceiver.class)
//                    .setAction("CONFIRM_APPOINTMENT")
//                    .putExtra("appointment_id", appointment.id);
//            PendingIntent confirmPendingIntent = PendingIntent.getBroadcast(
//                    context, appointment.id, confirmIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
//
//            builder.addAction(R.drawable.icon_find, "Подтвердить", confirmPendingIntent);
//        }
//
//        sendNotification(builder.build(), appointment.id);
//    }

//    public void sendReminderNotification(Appointment appointment) {
//        Notification notification = new NotificationCompat.Builder(context, "polyclinic_channel")
//                .setSmallIcon(R.drawable.icon_doctor_nurse)
//                .setContentTitle("Напоминание о приёме")
//                .setContentText(String.format("Через 10 минут приём с %s", appointment.patientName))
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setAutoCancel(true)
//                .build();
//
//        sendNotification(notification, appointment.id + 1000);
//    }

//    public void sendDiagnosisNotification(Appointment appointment) {
//        Notification notification = new NotificationCompat.Builder(context, "polyclinic_channel")
//                .setSmallIcon(R.drawable.icon_doctor_nurse)
//                .setContentTitle("Результаты приёма")
//                .setContentText(String.format("Диагноз: %s", appointment.diagnosis))
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setAutoCancel(true)
//                .build();
//
//        sendNotification(notification, appointment.id + 2000);
//    }
//}
