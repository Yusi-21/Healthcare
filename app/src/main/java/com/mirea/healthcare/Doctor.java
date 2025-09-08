package com.mirea.healthcare;

public class Doctor {
    private int iconResId;
    private String name;
    private String profession;

    private String specializationKey;

    public Doctor(int iconResId, String name, String profession, String specializationKey) {
        this.iconResId = iconResId;
        this.name = name;
        this.profession = profession;
        this.specializationKey = specializationKey;

    }

    public int getIconResId() {
        return iconResId;
    }

    public String getName() {
        return name;
    }

    public String getProfession() {
        return profession;
    }

    public String getSpecializationKey() { return specializationKey; }
}