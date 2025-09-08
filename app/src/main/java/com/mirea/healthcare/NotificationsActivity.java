package com.mirea.healthcare;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.Executors;

public class NotificationsActivity extends AppCompatActivity {
    private RecyclerView notificationsRecyclerView;
    private LinearLayout emptyView;
    private AppointmentDao appointmentDao;
    private AppointmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notifications);

//        notificationsRecyclerView = findViewById(R.id.notifications_recycler_view);
//        emptyView = findViewById(R.id.empty_view);
//        TextView emptyTextView = findViewById(R.id.empty_notifications_text);
//
//        // Настройка RecyclerView
//        notificationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        adapter = new AppointmentAdapter(); // Убедитесь, что у вас есть конструктор по умолчанию
//        notificationsRecyclerView.setAdapter(adapter);
//
//        AppDatabase db = AppDatabase.getDatabase(this);
//        appointmentDao = db.appointmentDao();
//
//        loadNotifications();
    }

//    private int getCurrentUserId() {
//        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
//        return prefs.getInt("user_id", -1);
//    }
//
//    private void loadNotifications() {
//        int userId = getCurrentUserId();
//        if (userId == -1) return;
//
//        // Загрузка в фоне (без LiveData)
//        Executors.newSingleThreadExecutor().execute(() -> {
//            List<Appointment> appointments = appointmentDao.getAllForUser(userId);
//
//            runOnUiThread(() -> {  // Обновляем UI в главном потоке
//                if (appointments == null || appointments.isEmpty()) {
//                    showEmptyView();
//                } else {
//                    showNotifications(appointments);
//                }
//            });
//        });
//    }

//    private void showEmptyView() {
//        notificationsRecyclerView.setVisibility(View.GONE);
//        emptyView.setVisibility(View.VISIBLE);
//    }
//
//    private void showNotifications(List<Appointment> appointments) { // Исправляем тип параметра
//        emptyView.setVisibility(View.GONE);
//        notificationsRecyclerView.setVisibility(View.VISIBLE);
//
//        adapter.updateData(appointments);
//    }
}