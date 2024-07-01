package nbu.f104260.structurestudioreservationapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Calendar;

import nbu.f104260.structurestudioreservationapp.databinding.ActivityChooseServiceBinding;
import nbu.f104260.structurestudioreservationapp.databinding.ActivityChooseTimeBinding;
import nbu.f104260.structurestudioreservationapp.entities.Appointment;
import nbu.f104260.structurestudioreservationapp.entities.Service;
import nbu.f104260.structurestudioreservationapp.entities.Time;

public class ChooseTimeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActivityChooseTimeBinding binding;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    androidx.appcompat.widget.Toolbar toolbar;

    Long barberId;
    Appointment appointment;

    DatabaseHelper databaseHelper;
    HoursAdapter hoursAdapter; // Declare adapter at class level

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChooseTimeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize views and components
        drawerLayout = findViewById(R.id.main);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView.bringToFront();

        if (getIntent().hasExtra("APPOINTMENT")) {
            appointment = (Appointment) getIntent().getSerializableExtra("APPOINTMENT");
        } else {
            Toast.makeText(this, "No Appointment object found", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ChooseTimeActivity.this, MakeReservationActivity.class);
            startActivity(intent);
            finish();
        }

        String[] dateParts = appointment.getDate().split("-");
        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]) - 1; // Calendar.MONTH is zero-based
        int dayOfMonth = Integer.parseInt(dateParts[2]);

        Calendar currentDate = Calendar.getInstance();
        int currentYear = currentDate.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH);
        int currentDayOfMonth = currentDate.get(Calendar.DAY_OF_MONTH);

        boolean isToday = (year == currentYear && month == currentMonth && dayOfMonth == currentDayOfMonth);

        databaseHelper = new DatabaseHelper(this);
        ArrayList<Time> time = new ArrayList<>();
        ArrayList<Time> times = databaseHelper.getAvailableWorkingHours(appointment.getBarberId(), appointment.getDate());

        if (!isToday) {
            time.addAll(times);
        } else {
            int currentHour = currentDate.get(Calendar.HOUR_OF_DAY);
            int currentMinute = currentDate.get(Calendar.MINUTE);

            for (Time t : times) {
                String[] parts = t.getTime().split(":");
                int hour = Integer.parseInt(parts[0]);
                int minute = Integer.parseInt(parts[1]);

                // Include hours only if they are after the current time
                if (hour > currentHour || (hour == currentHour && minute > currentMinute)) {
                    time.add(t);
                }
            }
        }

        // Set up RecyclerView
        binding.listHours.setLayoutManager(new LinearLayoutManager(this));
        hoursAdapter = new HoursAdapter(time);
        binding.listHours.setAdapter(hoursAdapter);

        hoursAdapter.setOnClickListener(new HoursAdapter.OnClickListener() {
            @Override
            public void onClick(int position, Time time) {
                SharedPreferences sharedPreferences = getSharedPreferences("LoginData", MODE_PRIVATE);
                appointment.setClientEmail(sharedPreferences.getString("Email", null));
                appointment.setTime(time.getTime());
                if(databaseHelper.insertAppointment(appointment)){
                    Intent intent = new Intent(ChooseTimeActivity.this, MadedReservationActivity.class);
                    intent.putExtra("APPOINTMENT", appointment);
                    startActivity(intent);
                }  else {
                    Toast.makeText(ChooseTimeActivity.this, "Try again! SomethingWentWrong!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseTimeActivity.this, ChooseDateActivity.class);
                intent.putExtra("APPOINTMENT", appointment);
                startActivity(intent);
                finish();
            }
        });

        // Set up navigation drawer toggle
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
            startActivity(new Intent(ChooseTimeActivity.this, HomeActivity.class));
            finish();
        } else if (id == R.id.history) {
            startActivity(new Intent(ChooseTimeActivity.this, HistoryActivity.class));
            finish();
        } else if (id == R.id.profile) {
            startActivity(new Intent(ChooseTimeActivity.this, ProfileActivity.class));
            finish();
        } else if (id == R.id.more) {
            startActivity(new Intent(ChooseTimeActivity.this, SettingsActivity.class));
            finish();
        } else if (id == R.id.logout) {
            SharedPreferences sharedPreferences = getSharedPreferences("LoginData", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("IsLoggedIn", false);
            editor.apply();
            startActivity(new Intent(ChooseTimeActivity.this, MainActivity.class));
            finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}