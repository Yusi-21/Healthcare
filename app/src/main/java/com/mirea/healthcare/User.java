package com.mirea.healthcare;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "password")
    public String password;

    @ColumnInfo(name = "username")
    public String username;

    @ColumnInfo(name = "phone")  // Добавляем телефон
    public String phone;

    @ColumnInfo(name = "address") // Добавляем адрес
    public String address;

    @ColumnInfo(name = "user_type") // "doctor" или "patient"
    public String userType;

    @ColumnInfo(name = "profile_image")
    public String profileImage;
    

    // Конструктор, геттеры и сеттеры
    public User(String email, String password, String username, String userType, String profileImage) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.userType = userType;
        this.profileImage = profileImage;
        this.phone = "";
        this.address = "";
    }

    // Геттеры
    public int getId() { return id; }
    public String getEmail() { return email; }

    public String getPassword() { return password; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getUserType() { return userType; }
    public String getProfileImage() { return profileImage; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

}

