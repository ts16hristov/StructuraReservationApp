package nbu.f104260.structurestudioreservationapp;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import nbu.f104260.structurestudioreservationapp.databinding.BarberCardBinding;
import nbu.f104260.structurestudioreservationapp.entities.Appointment;
import nbu.f104260.structurestudioreservationapp.entities.Barber;

public class BarberAdapter extends RecyclerView.Adapter<BarberAdapter.ViewHolder>{

    private final ArrayList<Barber> barbers;
    private OnClickListener onClickListener;

    public BarberAdapter(ArrayList<Barber> barbers) {
        this.barbers = barbers;
    }

    // Method to set the click listener
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BarberCardBinding binding = BarberCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Barber item = barbers.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return barbers.size();
    }

    // Interface for the click listener
    public interface OnClickListener {
        void onClick(int position, Appointment model);
    }

    // ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder {
        public BarberCardBinding binding;

        public ViewHolder(BarberCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            // Set click listener on the ViewHolder's item view
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onClickListener != null) {
                    // Create an Appointment object and set barberId
                    Barber selectedBarber = barbers.get(position);
                    Appointment appointment = new Appointment(selectedBarber.getId(), "", "", "", "", 0);
                    onClickListener.onClick(position, appointment);
                }
            });
        }


        // Bind method to bind data to ViewHolder
        public void bind(Barber barber) {
            binding.name.setText(barber.getName());
            binding.position.setText(barber.getPosition());
        }
    }
}
