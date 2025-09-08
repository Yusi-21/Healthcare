package com.mirea.healthcare;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {
    private List<Appointment> appointments;
    private Context context;  // Добавляем контекст
    private OnAppointmentClickListener appointmentClickListener;
    private OnAppointmentDeletedListener deletionListener;
//    private NotificationHelper notificationHelper; // Добавляем хелпер уведомлений

    public interface OnAppointmentClickListener {
        void onAppointmentClick(Appointment appointment);
    }

    // Новый интерфейс для обработки удаления
    public interface OnAppointmentDeletedListener {
        void onAppointmentDeleted();
    }

    public AppointmentAdapter(List<Appointment> appointments, OnAppointmentClickListener appointmentClickListener) {
        this.appointments = appointments;
        this.appointmentClickListener = appointmentClickListener;
    }

//    public AppointmentAdapter() {}

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvPatientName, tvDateTime, tvStatus, tvSpecialization;// new added

        public ViewHolder(View itemView) {
            super(itemView);
            tvPatientName = itemView.findViewById(R.id.tv_patient_name);
            tvSpecialization = itemView.findViewById(R.id.tv_specialization); // new added
            tvDateTime = itemView.findViewById(R.id.tv_date_time);
            tvStatus = itemView.findViewById(R.id.tv_status);
        }
    }

    // Существующие конструкторы (не изменяем)
    public AppointmentAdapter(List<Appointment> appointments) {
        this.appointments = appointments != null ? appointments : new ArrayList<>();;
    }

//    public void setNotificationHelper(NotificationHelper helper) {
//        this.notificationHelper = helper;
//    }

    // Новый метод для установки контекста
    public void setContext(Context context) {
        this.context = context;
    }

    // Новый метод для установки слушателя удаления
    public void setOnAppointmentDeletedListener(OnAppointmentDeletedListener listener) {
        this.deletionListener = listener;
    }

    public void updateData(List<Appointment> newAppointments) {
        this.appointments.clear();
        this.appointments.addAll(newAppointments != null ? newAppointments : new ArrayList<>());
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_appointment, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Appointment appointment = appointments.get(position);
        holder.tvPatientName.setText(appointment.doctorName);

        // zapisler DoctorEntity ya DoctorAdapter tarapyndan sohranit edilya dalmi ?
        // eger sheyle bolsa bashga yere yazylan zapisi nireden tapyp alsyn ol ?
        holder.tvSpecialization.setText(appointment.specialization);// new added

        String dateTime = String.format("%s " + context.getString(R.string.at) + " %s",
                appointment.date,
                appointment.time);
        holder.tvDateTime.setText(dateTime);

        holder.tvStatus.setText(R.string.status_confirmed); // тут нужно добавить switch-case (planned, booked, closed)

        // Добавляем обработчик долгого нажатия
        holder.itemView.setOnLongClickListener(v -> {
            showContextMenu(v, appointment, position);
            return true;
        });

        holder.itemView.setOnClickListener(v -> {
            if (appointmentClickListener != null) {
                appointmentClickListener.onAppointmentClick(appointment);
            }
        });

        // Проверяем статус и дату
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        if (appointment.date.compareTo(currentDate) < 0) {
            appointment.status = AppointmentStatus.CLOSED;
        }

        switch (appointment.status) {
            case AppointmentStatus.BOOKED:
                holder.tvStatus.setText(R.string.status_booked);
                holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.orange));
                break;
            case AppointmentStatus.CLOSED:
                holder.tvStatus.setText(R.string.status_closed);
                holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.red));
                break;
            case AppointmentStatus.OPEN:
            default:
                holder.tvStatus.setText(R.string.status_open);
                holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.green));
        }

        // Добавляем обработку уведомлений при изменении статуса
//        if (appointment.status.equals(AppointmentStatus.BOOKED)) {
//            sendNotification(appointment, "Новая запись");
//        } else if (appointment.status.equals(AppointmentStatus.CLOSED)){
//            sendNotification(appointment, "Запись завершена");
//        }
    }

//    private void sendNotification(Appointment appointment, String title) {
//        if (notificationHelper != null) {
//            notificationHelper.sendAppointmentNotification(appointment, title);
//        }
//    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    private void showContextMenu(View view, Appointment appointment, int position) {
        if (context == null) return;

        // Создаем PopupMenu с кастомным стилем
        PopupMenu popupMenu = new PopupMenu(context, view);

        // Применяем наш стиль
        try {
            @SuppressLint("DiscouragedPrivateApi") Field field = popupMenu.getClass().getDeclaredField("mPopup");
            field.setAccessible(true);
            Object menuPopupHelper = field.get(popupMenu);
            assert menuPopupHelper != null;
            Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
            Method setForceShowIcon = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
            setForceShowIcon.invoke(menuPopupHelper, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Надуваем меню
        popupMenu.inflate(R.menu.appointment_context_menu);

        // Стилизуем элементы меню
        Menu menu = popupMenu.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            SpannableString spanString = new SpannableString(item.getTitle());
            spanString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.red)),
                    0, spanString.length(), 0);
            item.setTitle(spanString);
        }

        // Обработка кликов
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_delete) {
                deleteAppointment(appointment, position);
                return true;
            }
            return false;
        });

        // Показываем меню с анимацией
        popupMenu.show();
    }

    private void deleteAppointment(Appointment appointment, int position) {
        if (context == null) return;

        // Используем MaterialAlertDialogBuilder для единого стиля
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context, R.style.MyDatePickerDialogTheme)
                .setTitle(R.string.confirm_delete)
                .setMessage(R.string.delete_appointment)
                .setPositiveButton(R.string.delete_alert, (dialog, which) -> {
                    new Thread(() -> {
                        AppDatabase.getDatabase(context).appointmentDao().deleteById(appointment.id);

                        // Обновляем UI в главном потоке
                        ((Activity) context).runOnUiThread(() -> {
                            appointments.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, appointments.size());

                            if (deletionListener != null) {
                                deletionListener.onAppointmentDeleted();
                            }

                            Toast.makeText(context, R.string.confirmed_delete, Toast.LENGTH_SHORT).show();
                        });
                    }).start();
                })
                .setNegativeButton(R.string.cancel, null);

        AlertDialog dialog = builder.create();

        // Кастомизация кнопок при показе
        dialog.setOnShowListener(d -> {
            Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);

            if (positiveButton != null) {
                positiveButton.setTextColor(ContextCompat.getColor(context, R.color.blue)); // Красный для опасных действий
                positiveButton.setAllCaps(false); // Отключаем автоматический CAPS
            }
            if (negativeButton != null) {
                negativeButton.setTextColor(ContextCompat.getColor(context, R.color.red));
                negativeButton.setAllCaps(false);
            }
        });

        dialog.show();
    }
}