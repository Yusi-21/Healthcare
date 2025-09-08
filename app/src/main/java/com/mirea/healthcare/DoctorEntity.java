package com.mirea.healthcare;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "doctors")
public class DoctorEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "user_id")
    public int userId;  // Связь с User.id

    @ColumnInfo(name = "specialization")
    public String specialization; // "surgeon", "cardiologist" (ключи из strings.xml)

    // Опционально: рейтинг, описание и т.д.
    @ColumnInfo(name = "description")
    public String description = "";

//    public DoctorEntity(int userId, String specialization, String description) {
//        this.userId = userId;
//        this.specialization = specialization;
//        this.description = description;
//
//    }
}