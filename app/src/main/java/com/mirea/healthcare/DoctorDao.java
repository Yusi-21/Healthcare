package com.mirea.healthcare;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DoctorDao {
    @Insert
    void insert(DoctorEntity doctor);

    @Query("SELECT * FROM doctors WHERE user_id = :userId")
    DoctorEntity getByUserId(int userId);

    @Query("SELECT * FROM doctors WHERE specialization = :specialization")
    List<DoctorEntity> getBySpecialization(String specialization);

    @Query("SELECT * FROM doctors")
    List<DoctorEntity> getAll();
}
