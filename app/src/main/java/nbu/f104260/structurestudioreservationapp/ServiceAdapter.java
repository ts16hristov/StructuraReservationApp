package nbu.f104260.structurestudioreservationapp;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import nbu.f104260.structurestudioreservationapp.databinding.ServiceCardBinding;
import nbu.f104260.structurestudioreservationapp.entities.Service;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {
    private final ArrayList<Service> services;
    private OnClickListener onClickListener;

    public ServiceAdapter(ArrayList<Service> services) {
        this.services = services;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position, Service model);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ServiceCardBinding binding = ServiceCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ServiceAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Service service = services.get(position);
        holder.bind(service);
    }


    @Override
    public int getItemCount() {

        return services.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ServiceCardBinding binding;

        public ViewHolder(ServiceCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            // Set click listener on the ViewHolder's item view
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onClickListener != null) {
                    if (position != RecyclerView.NO_POSITION && onClickListener != null) {
                        onClickListener.onClick(position, services.get(position)); // Pass Service object
                    }
                }
            });
        }


        public void bind(Service service) {
            binding.service.setText(service.getName());
            binding.price.setText(String.valueOf(service.getPrice())+"BGN");
        }
    }
}
