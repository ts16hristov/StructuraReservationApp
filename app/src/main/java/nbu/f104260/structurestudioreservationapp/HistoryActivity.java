package nbu.f104260.structurestudioreservationapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Calendar;

import nbu.f104260.structurestudioreservationapp.databinding.ActivityHistoryBinding;
import nbu.f104260.structurestudioreservationapp.entities.Appointment;
import nbu.f104260.structurestudioreservationapp.entities.AppointmentHistory;

public class HistoryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActivityHistoryBinding binding;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    androidx.appcompat.widget.Toolbar toolbar;

    DatabaseHelper databaseHelper;
    private ArrayList<Appointment> oldAppointments;
    private ArrayList<Appointment> newAppointments;
    private AppointmentAdapter newAppointmentAdapter;
    private AppointmentAdapter oldAppointmentAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        drawerLayout = findViewById(R.id.main);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView.bringToFront();

        databaseHelper = new DatabaseHelper(this);
        SharedPreferences sharedPreferences = getSharedPreferences("LoginData", MODE_PRIVATE);

        ArrayList<Appointment> appointments = databaseHelper.getAppointmentsByEmail(sharedPreferences.getString("Email", null));
        separateAppointments(appointments);

        // Create lists for old and new appointments
        ArrayList<AppointmentHistory> oldAppointmentList = new ArrayList<>();
        ArrayList<AppointmentHistory> newAppointmentList = new ArrayList<>();

        // Populate old appointments list
        for (Appointment appointment : oldAppointments) {
            AppointmentHistory appointmentHistory = new AppointmentHistory();
            appointmentHistory.setBarberId(appointment.getBarberId());
            appointmentHistory.setBarberName(databaseHelper.getBarberNameById(appointment.getBarberId()));
            appointmentHistory.setDate(appointment.getDate());
            appointmentHistory.setTime(appointment.getTime());
            appointmentHistory.setService(appointment.getService());
            appointmentHistory.setPrice(appointment.getPrice());
            oldAppointmentList.add(appointmentHistory);
        }

        // Populate new appointments list
        for (Appointment appointment : newAppointments) {
            AppointmentHistory appointmentHistory = new AppointmentHistory();
            appointmentHistory.setBarberId(appointment.getBarberId());
            appointmentHistory.setBarberName(databaseHelper.getBarberNameById(appointment.getBarberId()));
            appointmentHistory.setDate(appointment.getDate());
            appointmentHistory.setTime(appointment.getTime());
            appointmentHistory.setService(appointment.getService());
            appointmentHistory.setPrice(appointment.getPrice());
            newAppointmentList.add(appointmentHistory);
        }

        // Set up RecyclerView for old appointments
        binding.historyAppointments.setLayoutManager(new LinearLayoutManager(this));
        oldAppointmentAdapter = new AppointmentAdapter(oldAppointmentList);
        binding.historyAppointments.setAdapter(oldAppointmentAdapter);

        // Set up RecyclerView for new appointments
        binding.futureAppointments.setLayoutManager(new LinearLayoutManager(this));
        newAppointmentAdapter = new AppointmentAdapter(newAppointmentList);
        binding.futureAppointments.setAdapter(newAppointmentAdapter);

        // Handle click listener for old appointments
        oldAppointmentAdapter.setOnClickListener(new AppointmentAdapter.OnClickListener() {
            @Override
            public void onClick(int position, AppointmentHistory appointment) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("APPOINTMENT", appointment);
                ReservationInfo fragment = new ReservationInfo();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.main, fragment, "YourFragmentTag");
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        newAppointmentAdapter.setOnClickListener(new AppointmentAdapter.OnClickListener() {
            @Override
            public void onClick(int position, AppointmentHistory appointment) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("APPOINTMENT", appointment);
                ReservationInfo fragment = new ReservationInfo();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.main, fragment, "YourFragmentTag");
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open_navigation_view, R.string.close_navigation_view
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.home) {
            startActivity(new Intent(HistoryActivity.this, HomeActivity.class));
            finish();
        } else if (id == R.id.main) {
            startActivity(new Intent(HistoryActivity.this, MakeReservationActivity.class));
            finish();
        } else if (id == R.id.profile) {
            finish();
        } else if (id == R.id.more) {
            startActivity(new Intent(HistoryActivity.this, SettingsActivity.class));
            finish();
        } else if (id == R.id.logout) {
            SharedPreferences sharedPreferences = getSharedPreferences("LoginData", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("IsLoggedIn", false);
            editor.apply();
            startActivity(new Intent(HistoryActivity.this, MainActivity.class));
            finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void separateAppointments(ArrayList<Appointment> allAppointments) {
        oldAppointments = new ArrayList<>();
        newAppointments = new ArrayList<>();

        Calendar currentCalendar = Calendar.getInstance();
        long soonestTime = Long.MAX_VALUE; // Initialize to a very large value
        long currentTimeInMillis = currentCalendar.getTimeInMillis();

        Appointment soonestNewAppointment = null;

        for (Appointment appointment : allAppointments) {
            try {
                // Parse appointment date
                String[] dateParts = appointment.getDate().split("-");
                int year = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]);
                int dayOfMonth = Integer.parseInt(dateParts[2]);

                // Parse appointment time
                String[] timeParts = appointment.getTime().split(":");
                int hourOfDay = Integer.parseInt(timeParts[0]);
                int minute = Integer.parseInt(timeParts[1]);

                // Create Calendar instance for appointment date and time
                Calendar appointmentCalendar = Calendar.getInstance();
                appointmentCalendar.set(year, month - 1, dayOfMonth, hourOfDay, minute);

                long appointmentTimeInMillis = appointmentCalendar.getTimeInMillis();

                if (appointmentTimeInMillis < currentTimeInMillis) {
                    oldAppointments.add(appointment);
                } else {
                    // Check if it's the soonest new appointment
                    if (appointmentTimeInMillis < soonestTime) {
                        soonestTime = appointmentTimeInMillis;
                        soonestNewAppointment = appointment;
                    }
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException | NullPointerException e) {
                e.printStackTrace(); // Handle or log parsing errors if needed
            }
        }

        // Add the soonest new appointment to the newAppointments list if found
        if (soonestNewAppointment != null) {
            newAppointments.add(soonestNewAppointment);
        }
    }


}