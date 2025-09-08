//package com.mirea.healthcare;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.util.Log;
//
//import java.util.concurrent.Executors;
//
//public class AppointmentActionReceiver extends BroadcastReceiver {
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        if (intent == null) return;
//
//        int appointmentId = intent.getIntExtra("appointment_id", -1);
//        if (appointmentId == -1) return;
//
//        Executors.newSingleThreadExecutor().execute(() -> {
//            try {
//                AppDatabase db = AppDatabase.getDatabase(context);
//                Appointment appointment = db.appointmentDao().getById(appointmentId);
//                if (appointment == null) return;
//
//                String action = intent.getAction();
//                if (action == null) return;
//
//                switch (action) {
//                    case "CONFIRM_APPOINTMENT":
//                        db.appointmentDao().updateStatus(appointmentId, AppointmentStatus.CONFIRMED);
//                        break;
//                    case "CANCEL_APPOINTMENT":
//                        db.appointmentDao().updateStatus(appointmentId, AppointmentStatus.CANCELLED);
//                        break;
//                }
//            } catch (Exception e) {
//                Log.e("AppointmentAction", "Error processing action", e);
//            }
//        });
//    }
//}