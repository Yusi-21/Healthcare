package com.mirea.healthcare;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "appointments")
public class Appointment {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "doctor_id")
    public int doctorId;

    @ColumnInfo(name = "doctor_name")
    public String doctorName;

    @ColumnInfo(name = "specialization")
    public String specialization;

    @ColumnInfo(name = "patient_id")
    public int patientId = -1;

    @ColumnInfo(name = "patient_name")
    public String patientName;

    @ColumnInfo(name = "date")
    public String date; // format: "YYYY-MM-DD"

    @ColumnInfo(name = "time")
    public String time; // format: "HH:MM"

    @ColumnInfo(name = "status")
    public String status;

    @ColumnInfo(name = "diagnosis")
    public String diagnosis;

    public Appointment() {
    }

    public Appointment(String doctorName, String date, String time) {
        this();
        this.doctorName = doctorName;
        this.date = date;
        this.time = time;
        this.status = AppointmentStatus.OPEN;
    }
}
