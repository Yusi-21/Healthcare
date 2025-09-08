package com.mirea.healthcare;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity {
    private AuthManager authManager;
    private AppDatabase db;
    private TextView patientNameView;
    private RecyclerView recordsRecyclerView;
    private ImageView emptyStateImage;
    private AppointmentAdapter adapter;
    private final List<Appointment> appointments = new ArrayList<>();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
//        checkAuthAndRedirect();

        authManager = new AuthManager(this);
        db = AppDatabase.getDatabase(this);

        patientNameView = findViewById(R.id.patient_name);
        recordsRecyclerView = findViewById(R.id.records_recycler_view);
        emptyStateImage = findViewById(R.id.empty_state_image);

        adapter = new AppointmentAdapter(appointments, null);
        adapter.setContext(this);  // Устанавливаем контекст
        adapter.setOnAppointmentDeletedListener(this::updateRecyclerView);
        recordsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recordsRecyclerView.setAdapter(adapter);

        // Проверяем, есть ли переданные данные (из Intent)
        if (getIntent().hasExtra("APPOINTMENT_DATA")) {
            String[] data = getIntent().getStringArrayExtra("APPOINTMENT_DATA");
            new Thread(() -> {
                assert data != null;
                Appointment newAppointment = new Appointment(data[0], data[1], data[2]);
                newAppointment.patientId = authManager.getCurrentUserId();
                db.appointmentDao().insert(newAppointment);
            }).start();
        }

        loadPatientAppointments();
        loadUserData(patientNameView);

        setupButton(R.id.icon_others, OthersActivity.class, null);
        setupButton(R.id.icon_notification, NotificationsActivity.class, null);
        setupButton(R.id.icon_qr_queue, GenerateQRActivity.class, null);
        setupButton(R.id.icon_username, ProfileActivity.class, null);
        setupButton(R.id.btn_book_appointment, SelectDoctorsActivity.class, null);
        setupButton(R.id.icon_home, null, () -> {
            Toast.makeText(this, getString(R.string.icon_home), Toast.LENGTH_SHORT).show();
        });
        setupButton(R.id.icon_ai_helper, null, () -> {
            Intent intent = new Intent(this, AiHelperActivity.class);
            intent.putExtra("USER_ID", authManager.getCurrentUserId()); // Берём ID из authManager
            startActivity(intent);
        });

//        checkWorkManager();
    }

//    private void checkWorkManager() {
//        WorkManager.getInstance(this)
//                .getWorkInfosForUniqueWorkLiveData("expired_appointments_check")
//                .observe(this, workInfos -> {
//                    for (WorkInfo info : workInfos) {
//                        Log.d("WorkManager", "Work state: " + info.getState());
//                        if (info.getState() == WorkInfo.State.FAILED) {
//                            Log.e("WorkManager", "Worker failed: " + info.getOutputData());
//                        }
//                    }
//                });
//    }

    private void loadPatientAppointments() {
        int patientId = authManager.getCurrentUserId();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Log.d("DateCheck", "Current date format: " + currentDate);

        new Thread(() -> {
            int closedCount = db.appointmentDao().closeExpiredAppointments(currentDate);
            runOnUiThread(() -> {
                if (closedCount > 0) {
                    Toast.makeText(this, "Closed " + closedCount + " expired appointments",
                            Toast.LENGTH_SHORT).show();
                    loadPatientAppointments(); // Перезагружаем данные
                }
            });
        }).start();

        db.appointmentDao()
                .getBookedAppointmentsForPatient(patientId, currentDate)
                .observe(this, appointments -> {
                    this.appointments.clear();
                    this.appointments.addAll(appointments);
                    adapter.updateData(appointments);
                    updateEmptyState(appointments.isEmpty());
                });
    }

    private void updateEmptyState(boolean isEmpty) {
        if (emptyStateImage != null) {
            emptyStateImage.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        }
    }

    private void updateRecyclerView() {
        if (appointments.isEmpty()) {
            emptyStateImage.setVisibility(View.VISIBLE);
            recordsRecyclerView.setVisibility(View.GONE);
        } else {
            emptyStateImage.setVisibility(View.GONE);
            recordsRecyclerView.setVisibility(View.VISIBLE);
            recordsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            recordsRecyclerView.setAdapter(new AppointmentAdapter(appointments));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Показываем биометрию при возвращении в приложение
//        showBiometricPrompt();
        // При возвращении на экран можно обновить данные
        loadUserData(patientNameView);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true); // отправляет приложение в фон, но не закрывает его
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}