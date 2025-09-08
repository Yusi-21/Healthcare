package com.mirea.healthcare;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SeePatientsActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private AppDatabase db;
    private int currentDoctorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_see_patients);

        db = AppDatabase.getDatabase(this);
        currentDoctorId = new AuthManager(this).getCurrentUserId(); // Получаем ID текущего врача

        recyclerView = findViewById(R.id.recyclerViewPatients);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadBookedAppointments();
    }

    private void loadBookedAppointments() {
        new Thread(() -> {
            List<Appointment> appointments = db.appointmentDao().getBookedAppointmentsForDoctor(currentDoctorId);

            runOnUiThread(() -> {
                PatientAppointmentAdapter adapter = new PatientAppointmentAdapter(appointments);
                recyclerView.setAdapter(adapter);
            });
        }).start();
    }
}