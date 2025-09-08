package com.mirea.healthcare;

public class AppointmentStatus {
    public static final String OPEN = "open";
    public static final String BOOKED = "booked";
    public static final String CLOSED = "closed";
    public static final String CONFIRMED = "confirmed";
    public static final String COMPLETED = "completed";
    public static final String REMINDER_SENT = "reminder_sent";
    public static final String CANCELLED = "cancelled";


    private AppointmentStatus() {} // Запрещаем создание экземпляров

    public static boolean requiresNotification(String status) {
        return BOOKED.equals(status) || CLOSED.equals(status) || CONFIRMED.equals(status);
    }
}
