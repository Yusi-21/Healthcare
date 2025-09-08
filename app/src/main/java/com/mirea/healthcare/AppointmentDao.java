package com.mirea.healthcare;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AppointmentDao {
    @Insert
    void insert(Appointment appointment);

//    @Query("SELECT * FROM appointments WHERE doctor_id = :doctorId ORDER BY date, time ASC")
//    List<Appointment> getByDoctorId(int doctorId);

    @Query("SELECT * FROM appointments WHERE status = 'open' AND specialization = :specialization AND date >= :currentDate ORDER BY date, time ASC")
    LiveData<List<Appointment>> getOpenAppointmentsBySpecialization(String specialization, String currentDate);

    @Query("SELECT * FROM appointments WHERE patient_id = :patientId AND status = 'booked' AND date >= :currentDate ORDER BY date, time ASC")
    LiveData<List<Appointment>> getBookedAppointmentsForPatient(int patientId, String currentDate);

    @Query("SELECT * FROM appointments WHERE doctor_id = :doctorId AND patient_id IS NOT NULL")
    List<Appointment> getBookedAppointmentsForDoctor(int doctorId);

    @Query("SELECT COUNT(*) FROM appointments WHERE date < :currentDate AND status != 'closed'")
    int countExpiredAppointments(String currentDate);

    @Query("UPDATE appointments SET status = 'closed' WHERE date < :currentDate AND status != 'closed'")
    int closeExpiredAppointments(String currentDate);

//    @Query("SELECT * FROM appointments WHERE patient_id = :userId")
//    List<Appointment> getAllForUser(int userId);

    @Query("DELETE FROM appointments WHERE id = :id")
    void deleteById(int id);

    @Update
    void update(Appointment appointment);

    // Обновление статуса напоминания
//    @Query("UPDATE appointments SET status = :newStatus WHERE id = :appointmentId")
//    void updateStatus(int appointmentId, String newStatus);
//
//    @Query("SELECT * FROM appointments WHERE id = :id")
//    Appointment getById(int id);
}
