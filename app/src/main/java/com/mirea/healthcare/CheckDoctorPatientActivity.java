package com.mirea.healthcare;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class CheckDoctorPatientActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_check_doctor_patient);

        CardView cardDoctor = findViewById(R.id.card_doctor);
        CardView cardPatient = findViewById(R.id.card_patient);

        cardDoctor.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterDoctorActivity.class));
            finish();
        });

        cardPatient.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterPatientActivity.class));
            finish();
        });
    }
}