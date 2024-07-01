package nbu.f104260.structurestudioreservationapp;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import nbu.f104260.structurestudioreservationapp.databinding.HourCardBinding;
import nbu.f104260.structurestudioreservationapp.entities.Time;

public class HoursAdapter extends RecyclerView.Adapter<HoursAdapter.ViewHolder> {
    private ArrayList<Time> times;

    private OnClickListener onClickListener;

    public HoursAdapter(ArrayList<Time> times){
        this.times = times;
    }

    public void setOnClickListener(HoursAdapter.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position, Time model);
    }


    @NonNull
    @Override
    public HoursAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HourCardBinding binding = HourCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new HoursAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Time time = times.get(position);
        holder.bind(time);
    }



    @Override
    public int getItemCount() {
        return times.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public HourCardBinding binding;

        public ViewHolder(HourCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            // Set click listener on the ViewHolder's item view
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onClickListener != null) {
                    if (position != RecyclerView.NO_POSITION && onClickListener != null) {
                        onClickListener.onClick(position, times.get(position)); // Pass Service object
                    }
                }
            });
        }


        public void bind(Time time) {
            binding.hour.setText(time.getTime());
        }
    }

}
