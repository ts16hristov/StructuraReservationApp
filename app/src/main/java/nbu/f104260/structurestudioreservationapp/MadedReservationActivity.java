package nbu.f104260.structurestudioreservationapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
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

import nbu.f104260.structurestudioreservationapp.databinding.ActivityChooseTimeBinding;
import nbu.f104260.structurestudioreservationapp.databinding.ActivityMadedReservationBinding;
import nbu.f104260.structurestudioreservationapp.entities.Appointment;

public class MadedReservationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActivityMadedReservationBinding binding;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    androidx.appcompat.widget.Toolbar toolbar;

    Long barberId;
    Appointment appointment;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMadedReservationBinding.inflate(getLayoutInflater());
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
            Toast.makeText(this, "Something went wrong go check All reservations!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MadedReservationActivity.this, MakeReservationActivity.class);
            startActivity(intent);
            finish();
        }

        databaseHelper = new DatabaseHelper(this);

        binding.barberName.setText(databaseHelper.getBarberNameById(appointment.getBarberId()));
        binding.dateD.setText(appointment.getDate());
        binding.timeT.setText(appointment.getTime());
        binding.serviceS.setText(appointment.getService());
        binding.priceP.setText(appointment.getPrice()+"BGN");

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
            startActivity(new Intent(MadedReservationActivity.this, HomeActivity.class));
            finish();
        } else if (id == R.id.history) {
            startActivity(new Intent(MadedReservationActivity.this, HistoryActivity.class));
            finish();
        } else if (id == R.id.profile) {
            startActivity(new Intent(MadedReservationActivity.this, ProfileActivity.class));
            finish();
        } else if (id == R.id.more) {
            startActivity(new Intent(MadedReservationActivity.this, SettingsActivity.class));
            finish();
        } else if (id == R.id.logout) {
            SharedPreferences sharedPreferences = getSharedPreferences("LoginData", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("IsLoggedIn", false);
            editor.apply();
            startActivity(new Intent(MadedReservationActivity.this, MainActivity.class));
            finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}