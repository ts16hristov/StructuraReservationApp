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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import nbu.f104260.structurestudioreservationapp.databinding.ActivityMakeReservationBinding;
import nbu.f104260.structurestudioreservationapp.entities.Appointment;
import nbu.f104260.structurestudioreservationapp.entities.Barber;

public class MakeReservationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ActivityMakeReservationBinding binding;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    androidx.appcompat.widget.Toolbar toolbar;

    DatabaseHelper databaseHelper;
    BarberAdapter barberAdapter; // Declare adapter at class level

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMakeReservationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize views and components
        drawerLayout = findViewById(R.id.main);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView.bringToFront();


        databaseHelper = new DatabaseHelper(this);
        ArrayList<Barber> barbers = databaseHelper.getAllBarbers();
//
//        Barber barber1 = new Barber(1,"Vasko","CHIEF");
//        Barber barber2 = new Barber(2,"Tsetsp","BARBER");
//        ArrayList<Barber> barbers = new ArrayList<>();
//        barbers.add(barber1);
//        barbers.add(barber2);

        // Initialize RecyclerView and Adapter
        binding.listBarbers.setLayoutManager(new LinearLayoutManager(this));
        barberAdapter = new BarberAdapter(barbers);
        binding.listBarbers.setAdapter(barberAdapter);

        // Set OnClickListener for the adapter
        barberAdapter.setOnClickListener(new BarberAdapter.OnClickListener() {
            @Override
            public void onClick(int position, Appointment appointment) {
                // Handle click event here, for example start new activity
                Intent intent = new Intent(MakeReservationActivity.this, ChooseServiceActivity.class);
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
            startActivity(new Intent(MakeReservationActivity.this, HomeActivity.class));
        } else if (id == R.id.history) {
            startActivity(new Intent(MakeReservationActivity.this, HistoryActivity.class));
        } else if (id == R.id.profile) {
            startActivity(new Intent(MakeReservationActivity.this, ProfileActivity.class));
        } else if (id == R.id.more) {
            startActivity(new Intent(MakeReservationActivity.this, SettingsActivity.class));
        } else if (id == R.id.logout) {
            SharedPreferences sharedPreferences = getSharedPreferences("LoginData", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("IsLoggedIn", false);
            editor.apply();
            startActivity(new Intent(MakeReservationActivity.this, MainActivity.class));
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Example of stopping any ongoing operations
        if (barberAdapter != null) {
            barberAdapter.setOnClickListener(null); // Clear click listener
        }
    }


}
