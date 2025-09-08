package com.mirea.healthcare;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DoctorAppointmentsActivity extends BaseActivity {
    private AppDatabase db;
    private RecyclerView recyclerView;
    private AppointmentAdapter adapter;
    private int patientId;
    private List<Appointment> appointments = new ArrayList<>();
    private String specialization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_doctor_appointments);

        db = AppDatabase.getDatabase(this);
        patientId = new AuthManager(this).getCurrentUserId();
        specialization = getIntent().getStringExtra("SPECIALIZATION");// new added

        recyclerView = findViewById(R.id.recycler_view_appointments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new AppointmentAdapter(new ArrayList<>(), appointment-> {
            showConfirmationDialog(appointment);
        });

        recyclerView.setAdapter(adapter);

        // Получаем текущую дату
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());// new added
        String currentDate = sdf.format(new Date());// new added

        db.appointmentDao().getOpenAppointmentsBySpecialization(specialization, currentDate) // new added
                .observe(this, appointments -> {
            adapter.updateData(appointments);
        });
    }

    private void showConfirmationDialog(Appointment appointment) {
        try {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.MyDatePickerDialogTheme);

            // Формируем сообщение с данными о записи
            String message = getString(R.string.appointment_confirmation) + " "
                    + getString(R.string.to) + " "
                    + appointment.doctorName + " "
                    + getString(R.string.at) + " "
                    + appointment.date + " "
                    + getString(R.string.at) + " "
                    + appointment.time + " ?";

            builder.setTitle(getString(R.string.make_appointment))
                    .setMessage(message)
                    .setPositiveButton(getString(R.string.book), (dialog, which) -> {
                        bookAppointment(appointment);
                    })
                    .setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.dismiss());

            AlertDialog dialog = builder.create();
            showDialogWithStyledButtons(dialog); // extends from BaseActivity implements from DialogUtils (Design)
            dialog.show();
        } catch (Exception e) {
            Log.e("AppointmentActivity", "Error showing confirmation dialog", e);
        }
    }

    private void bookAppointment(Appointment appointment) {
        new Thread(() -> {
            // Обновляем запись в БД
            appointment.patientId = authManager.getCurrentUserId();
            appointment.patientName = getCurrentUserName();
            appointment.status = AppointmentStatus.BOOKED;
            db.appointmentDao().update(appointment);

            runOnUiThread(() -> {
                Toast.makeText(this, getString(R.string.notification_appointment_booked), Toast.LENGTH_SHORT).show();
                finish(); // Закрываем активность
            });
        }).start();
    }
}