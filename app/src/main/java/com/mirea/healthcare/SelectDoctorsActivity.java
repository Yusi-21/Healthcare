package com.mirea.healthcare;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SelectDoctorsActivity extends AppCompatActivity {
    private AppDatabase db;
    private List<Doctor> doctors = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_doctors);

        db = AppDatabase.getDatabase(this);

        boolean isEditMode = getIntent().getBooleanExtra("EDIT_MODE", false);
        int appointmentId = getIntent().getIntExtra("APPOINTMENT_ID", -1);

        doctors.add(new Doctor(R.drawable.icon_cardiologist,
                getString(R.string.cardiologist),
                getString(R.string.cardiologist_profession),
                Specializations.CARDIOLOGIST));

        doctors.add(new Doctor(R.drawable.icon_neurologist,
                getString(R.string.neurologist),
                getString(R.string.neurologist_profession),
                Specializations.NEUROLOGIST));

        doctors.add(new Doctor(R.drawable.icon_ophthalmologist2,
                getString(R.string.ophthalmologist),
                getString(R.string.ophthalmologist_profession),
                Specializations.OPHTHALMOLOGIST));

        doctors.add(new Doctor(R.drawable.icon_pediatrician,
                getString(R.string.pediatrician),
                getString(R.string.pediatrician_profession),
                Specializations.PEDIATRICIAN));

        doctors.add(new Doctor(R.drawable.icon_surgeon,
                getString(R.string.surgeon),
                getString(R.string.surgeon_profession),
                Specializations.SURGEON));

        doctors.add(new Doctor(R.drawable.icon_therapist,
                getString(R.string.therapist),
                getString(R.string.therapist_profession),
                Specializations.THERAPIST));


        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        RecyclerView recyclerView = findViewById(R.id.doctorsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DoctorAdapter adapter = new DoctorAdapter(
                doctors,
                this,
                isEditMode,
                appointmentId,
                new DoctorAdapter.OnDoctorClickListener() {
                    @Override
                    public void onDoctorClick(Doctor doctor) {
                        Intent intent = new Intent(SelectDoctorsActivity.this, DoctorAppointmentsActivity.class);
                        intent.putExtra("SPECIALIZATION", doctor.getSpecializationKey());// new added
                        startActivity(intent);
                    }
                }
        );
        recyclerView.setAdapter(adapter);
    }
}