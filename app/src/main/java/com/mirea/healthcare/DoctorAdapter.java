package com.mirea.healthcare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {
    private final List<Doctor> doctors;
    private OnDoctorClickListener listener;
    private final Context context;
    private final boolean isEditMode;
    private final int appointmentId;

    public interface OnDoctorClickListener {
        void onDoctorClick(Doctor doctor);
    }

    public DoctorAdapter(List<Doctor> doctors, Context context, boolean isEditMode,
                         int appointmentId, OnDoctorClickListener listener) {
        this.doctors = doctors;
        this.context = context;
        this.isEditMode = isEditMode;
        this.appointmentId = appointmentId;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor, parent, false);
        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        Doctor doctor = doctors.get(position);
        holder.bind(doctor);
        holder.doctorIcon.setImageResource(doctor.getIconResId());
        holder.doctorName.setText(doctor.getName());
        holder.doctorProfession.setText(doctor.getProfession());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDoctorClick(doctor);
            }
        });
    }

    @Override
    public int getItemCount() {
        return doctors != null ? doctors.size() : 0;
    }

    public static class DoctorViewHolder extends RecyclerView.ViewHolder {
        ImageView doctorIcon;
        TextView doctorName, doctorProfession;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            doctorIcon = itemView.findViewById(R.id.doctorIcon);
            doctorName = itemView.findViewById(R.id.doctorName);
            doctorProfession = itemView.findViewById(R.id.doctorProfession);
        }

        public void bind(Doctor doctor) {
            doctorIcon.setImageResource(doctor.getIconResId());
            doctorName.setText(doctor.getName());
            doctorProfession.setText(doctor.getProfession());
        }
    }
}