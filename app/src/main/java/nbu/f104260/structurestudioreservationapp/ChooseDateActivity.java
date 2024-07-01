package nbu.f104260.structurestudioreservationapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
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

import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;

import nbu.f104260.structurestudioreservationapp.databinding.ActivityChooseDateBinding;
import nbu.f104260.structurestudioreservationapp.databinding.ActivityMakeReservationBinding;
import nbu.f104260.structurestudioreservationapp.entities.Appointment;

public class ChooseDateActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActivityChooseDateBinding binding;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    androidx.appcompat.widget.Toolbar toolbar;

    CalendarView calendar;
    TextView date_view;

    Appointment appointment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChooseDateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize views and components
        drawerLayout = findViewById(R.id.main);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        calendar =  findViewById(R.id.calendar);
        date_view = findViewById(R.id.date_view);

        if (getIntent().hasExtra("APPOINTMENT")) {
            appointment = (Appointment) getIntent().getSerializableExtra("APPOINTMENT");
        } else {
            Toast.makeText(this, "No Appointment object found", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ChooseDateActivity.this, MakeReservationActivity.class);
            startActivity(intent);
            finish();
        }

        calendar.setMinDate(System.currentTimeMillis() - 1000);
        calendar
                .setOnDateChangeListener(
                        new CalendarView
                                .OnDateChangeListener() {
                            @Override
                            public void onSelectedDayChange(
                                    @NonNull CalendarView view,
                                    int year,
                                    int month,
                                    int dayOfMonth)
                            {
                                Calendar calendarToday = Calendar.getInstance();
                                int currentYear = calendarToday.get(Calendar.YEAR);
                                int currentMonth = calendarToday.get(Calendar.MONTH);
                                int currentDay = calendarToday.get(Calendar.DAY_OF_MONTH);

                                if (year < currentYear || (year == currentYear && month < currentMonth) || (year == currentYear && month == currentMonth && dayOfMonth < currentDay)) {
                                    Toast.makeText(ChooseDateActivity.this, "Please select a date from today onwards", Toast.LENGTH_SHORT).show();
                                    calendar.setDate(System.currentTimeMillis());
                                } else {
                                    String Date
                                            = year + "-"
                                            + (month + 1) + "-" + dayOfMonth;
                                    date_view.setText(Date);
                                }
                            }
                        });

        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(date_view.getText().toString().equals("Set the Date")){
                    Toast.makeText(ChooseDateActivity.this, "Please select a date from today onwards", Toast.LENGTH_SHORT).show();
                } else {
                    appointment.setDate(date_view.getText().toString());
                    Intent intent = new Intent(ChooseDateActivity.this, ChooseTimeActivity.class);
                    intent.putExtra("APPOINTMENT", appointment);
                    startActivity(intent);
                }
            }
        }

        );


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
            startActivity(new Intent(ChooseDateActivity.this, HomeActivity.class));
            finish();
        } else if (id == R.id.history) {
            startActivity(new Intent(ChooseDateActivity.this, HistoryActivity.class));
            finish();
        } else if (id == R.id.profile) {
            startActivity(new Intent(ChooseDateActivity.this, ProfileActivity.class));
            finish();
        } else if (id == R.id.more) {
            startActivity(new Intent(ChooseDateActivity.this, SettingsActivity.class));
            finish();
        } else if (id == R.id.logout) {
            SharedPreferences sharedPreferences = getSharedPreferences("LoginData", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("IsLoggedIn", false);
            editor.apply();
            startActivity(new Intent(ChooseDateActivity.this, MainActivity.class));
            finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
