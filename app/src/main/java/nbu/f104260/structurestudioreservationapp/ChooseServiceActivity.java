package nbu.f104260.structurestudioreservationapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import nbu.f104260.structurestudioreservationapp.databinding.ActivityChooseServiceBinding;
import nbu.f104260.structurestudioreservationapp.databinding.ActivityMakeReservationBinding;
import nbu.f104260.structurestudioreservationapp.entities.Appointment;
import nbu.f104260.structurestudioreservationapp.entities.Service;

public class ChooseServiceActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ActivityChooseServiceBinding binding;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    androidx.appcompat.widget.Toolbar toolbar;

    Long barberId;
    Appointment appointment;

    DatabaseHelper databaseHelper;
    ServiceAdapter serviceAdapter; // Declare adapter at class level


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChooseServiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize views and components
        drawerLayout = findViewById(R.id.main);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView.bringToFront();

        if (getIntent().hasExtra("APPOINTMENT")) {
            appointment = (Appointment) getIntent().getSerializableExtra("APPOINTMENT");
            barberId = appointment.getBarberId();
        } else {
            Toast.makeText(this, "No Appointment object found", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ChooseServiceActivity.this, MakeReservationActivity.class);
            startActivity(intent);
            finish();
        }


        databaseHelper = new DatabaseHelper(this);
        ArrayList<Service> services = databaseHelper.getAllServicesById(barberId);

        // Initialize RecyclerView and Adapter
        binding.listServices.setLayoutManager(new LinearLayoutManager(this));
        serviceAdapter = new ServiceAdapter(services);
        binding.listServices.setAdapter(serviceAdapter);

        // Set OnClickListener for the adapter
        serviceAdapter.setOnClickListener(new ServiceAdapter.OnClickListener() {
            @Override
            public void onClick(int position, Service service) {
                // Handle click event here, for example start new activity
                Intent intent = new Intent(ChooseServiceActivity.this, ChooseDateActivity.class);
                appointment.setService(service.getName());
                appointment.setPrice(service.getPrice());
                intent.putExtra("APPOINTMENT", appointment);  // Pass the Appointment object
                startActivity(intent);
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
            startActivity(new Intent(ChooseServiceActivity.this, HomeActivity.class));
            finish();
        } else if (id == R.id.history) {
            startActivity(new Intent(ChooseServiceActivity.this, HistoryActivity.class));
            finish();
        } else if (id == R.id.profile) {
            startActivity(new Intent(ChooseServiceActivity.this, ProfileActivity.class));
            finish();
        } else if (id == R.id.more) {
            startActivity(new Intent(ChooseServiceActivity.this, SettingsActivity.class));
            finish();
        } else if (id == R.id.logout) {
            SharedPreferences sharedPreferences = getSharedPreferences("LoginData", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("IsLoggedIn", false);
            editor.apply();
            startActivity(new Intent(ChooseServiceActivity.this, MainActivity.class));
            finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Example of stopping any ongoing operations
        if (serviceAdapter != null) {
            serviceAdapter.setOnClickListener(null); // Clear click listener
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}