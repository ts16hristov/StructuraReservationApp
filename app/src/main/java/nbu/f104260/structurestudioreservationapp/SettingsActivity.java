package nbu.f104260.structurestudioreservationapp;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import nbu.f104260.structurestudioreservationapp.Service.BackgroundMusicService;
import nbu.f104260.structurestudioreservationapp.Service.FetchMusicUrlService;
import nbu.f104260.structurestudioreservationapp.databinding.ActivityHomeBinding;
import nbu.f104260.structurestudioreservationapp.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActivitySettingsBinding binding;
    DrawerLayout drawerLayout;

    NavigationView navigationView;

    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        drawerLayout = findViewById(R.id.main);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        navigationView.bringToFront();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open_navigation_view, R.string.close_navigation_view
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch switchMusic = findViewById(R.id.switchMusic);
        switchMusic.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                BackgroundMusicService.resumeMusic();
            } else {
                BackgroundMusicService.pauseMusic();
            }
        });
        switchMusic.setChecked(isMyServiceRunning());

        TextView deleteTextView = findViewById(R.id.delete);
        deleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog();
            }
        });

    }

    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (BackgroundMusicService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if(menuItem.getItemId() == R.id.main) {
            Intent intent = new Intent(SettingsActivity.this, MakeReservationActivity.class);
            startActivity(intent);
        } else if(menuItem.getItemId() == R.id.history) {
            Intent intent = new Intent(SettingsActivity.this, HistoryActivity.class);
            startActivity(intent);
        } else if(menuItem.getItemId() == R.id.profile) {
            Intent intent = new Intent(SettingsActivity.this, ProfileActivity.class);
            startActivity(intent);
        } else if(menuItem.getItemId() == R.id.home) {
            Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);
            startActivity(intent);
        } else if(menuItem.getItemId() == R.id.logout) {
            SharedPreferences sharedPreferences = getSharedPreferences("LoginData", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("IsLoggedIn", false);
            editor.apply();
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intent);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Delete");
        builder.setMessage("Are you sure you want to delete this account? This action cannot be undone.");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SharedPreferences sharedPreferences = getSharedPreferences("LoginData", MODE_PRIVATE);
                String email = sharedPreferences.getString("Email","");
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if(databaseHelper.deleteUser(email)){
                    editor.putBoolean("IsLoggedIn", false);
                    editor.remove("Email");
                    editor.remove("Password");
                    editor.apply();
                    Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(SettingsActivity.this, "Try again!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}