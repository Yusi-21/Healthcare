package com.mirea.healthcare;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DoctorHomeActivity extends BaseActivity {
    private RecyclerView rvAppointments;
    private AuthManager authManager;
    private AppDatabase db;
    private TextView doctorNameView;
    private AppointmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_doctor_home);
        checkAuthAndRedirect();

        authManager = new AuthManager(this);
        db = AppDatabase.getDatabase(this);

        doctorNameView = findViewById(R.id.doctor_name);
        rvAppointments = findViewById(R.id.records_recycler_view_doctor);

        rvAppointments.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AppointmentAdapter(new ArrayList<>()); // Инициализируем пустым списком
        rvAppointments.setAdapter(adapter);

        loadAppointments();
        loadUserData(doctorNameView);

        setupButton(R.id.icon_others_doctor, OthersDoctorActivity.class, null);
        setupButton(R.id.icon_notification_doctor, NotificationsActivity.class, null);
        setupButton(R.id.icon_username_doctor, ProfileDoctorActivity.class, null);
        setupButton(R.id.btn_add_schedule, null, this::showAddScheduleDialog);
        setupButton(R.id.icon_home_doctor, null, () -> {
            Toast.makeText(this, getString(R.string.icon_home), Toast.LENGTH_SHORT).show();
        });
    }

    private void showAddScheduleDialog() {
        int doctorId = authManager.getCurrentUserId();
        new Thread(() -> {
            User doctor = db.userDao().getUserById(doctorId);
            DoctorEntity doctorEntity = db.doctorDao().getByUserId(doctorId); // Получаем запись врача

            if (doctor != null && doctorEntity != null) {
                String specializationKey = doctorEntity.specialization; // Получаем ключ специализации

                runOnUiThread(() -> {
                    showDatePickerDialog(doctor.getUsername(), specializationKey);
                });
            } else {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Doctor data not found", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private void showDatePickerDialog(String doctorName, String specializationKey) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, R.style.MyDatePickerDialogTheme,
                (view, year, month, dayOfMonth) -> {
                    @SuppressLint("DefaultLocale") String selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                    showTimePickerDialog(doctorName, specializationKey, selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        showDialogWithStyledButtons(datePickerDialog); // extends from BaseActivity implements from DialogUtils (Design)
        datePickerDialog.show();
    }

    private void showTimePickerDialog(String doctorName, String specializationKey, String date) {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this, R.style.MyDatePickerDialogTheme,
                (view, hourOfDay, minute) -> {
                    @SuppressLint("DefaultLocale") String selectedTime = String.format("%02d:%02d", hourOfDay, minute);
                    saveAppointmentSlot(doctorName, specializationKey, date, selectedTime);
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );

        showDialogWithStyledButtons(timePickerDialog); // extends from BaseActivity implements from DialogUtils (Design)
        timePickerDialog.show();
    }

    private void saveAppointmentSlot(String doctorName, String specializationKey, String date, String time) {
        int doctorId = authManager.getCurrentUserId();
        new Thread(() -> {
            Appointment slot = new Appointment();
            slot.doctorId = doctorId;
            slot.doctorName = doctorName;
            slot.specialization = specializationKey;  // Добавляем специализацию
            slot.date = date;
            slot.time = time;
            slot.status = AppointmentStatus.OPEN;

            db.appointmentDao().insert(slot);

            runOnUiThread(() -> {
                Toast.makeText(this, getString(R.string.added_new_records) + " " + date + " " + time, Toast.LENGTH_SHORT).show();
                loadAppointments(); // Обновляем список
            });
        }).start();
    }


    @Override
    protected void onResume() {
        super.onResume();
//        loadAppointments();
        loadUserData(doctorNameView); // При возвращении на экран можно обновить данные

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true); // отправляет приложение в фон, но не закрывает его
    }

    private void loadAppointments() {
        int doctorId = authManager.getCurrentUserId();
        if (doctorId == -1) return;

        new Thread(() -> {
            try {
                List<Appointment> appointments = db.appointmentDao().getBookedAppointmentsForDoctor(doctorId);
                runOnUiThread(() -> {
                    if (appointments != null) {
                        adapter.updateData(appointments); // Используем метод updateData
                    } else {
                        Toast.makeText(this, "Failed to load appointments", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}
