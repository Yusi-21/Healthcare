package com.mirea.healthcare;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PatientAppointmentAdapter extends RecyclerView.Adapter<PatientAppointmentAdapter.ViewHolder> {
    private final List<Appointment> appointments;

    public PatientAppointmentAdapter(List<Appointment> appointments) {

        this.appointments = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.patientId != 0 && appointment.patientName != null) {
                this.appointments.add(appointment);
            }
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_patient_appointment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appointment appointment = appointments.get(position);
        holder.patientName.setText(appointment.patientName);


        String formattedDateTime = formatDateTime(appointment.date, appointment.time);
        holder.appointmentDate.setText(formattedDateTime);
    }

    private String formatDateTime(String date, String time) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date parsedDate = inputFormat.parse(date);

            SimpleDateFormat outputFormat = new SimpleDateFormat("d MMMM yyyy", Locale.getDefault());
            String formattedDate = outputFormat.format(parsedDate);

            return String.format("%s, %s", formattedDate, time);
        } catch (Exception e) {
            return String.format("%s, %s", date, time); // Возвращаем как есть, если ошибка
        }
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView patientName;
        TextView appointmentDate;

        public ViewHolder(View itemView) {
            super(itemView);
            patientName = itemView.findViewById(R.id.textPatientName);
            appointmentDate = itemView.findViewById(R.id.textAppointmentDateTime);
        }
    }
}