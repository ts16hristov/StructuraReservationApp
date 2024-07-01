package nbu.f104260.structurestudioreservationapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import nbu.f104260.structurestudioreservationapp.databinding.ShortAppointmentBinding;
import nbu.f104260.structurestudioreservationapp.entities.AppointmentHistory;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {
    ArrayList<AppointmentHistory> appointments;

    DatabaseHelper databaseHelper;

    private OnClickListener onClickListener;

    public AppointmentAdapter(ArrayList<AppointmentHistory> appointments){
        this.appointments = appointments;
    }
    public void setOnClickListener(AppointmentAdapter.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ShortAppointmentBinding binding = ShortAppointmentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }



    @Override
    public void onBindViewHolder(@NonNull AppointmentAdapter.ViewHolder holder, int position) {
        AppointmentHistory item = appointments.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    // Interface for the click listener
    public interface OnClickListener {
        void onClick(int position, AppointmentHistory model);
    }

    // ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ShortAppointmentBinding binding;
        private Context context;

        public ViewHolder(ShortAppointmentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            // Set click listener on the ViewHolder's item view
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onClickListener != null) {
                    AppointmentHistory appointmentHistory = appointments.get(position);
                    onClickListener.onClick(position, appointmentHistory);
                }
            });
        }


        // Bind method to bind data to ViewHolder
        public void bind(AppointmentHistory appointment) {
            binding.BarberName.setText(appointment.getBarberName());
            binding.dateD.setText(appointment.getDate());
            binding.timeT.setText(appointment.getTime());
        }
    }
}
