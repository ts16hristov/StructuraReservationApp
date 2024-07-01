package nbu.f104260.structurestudioreservationapp;

import static nbu.f104260.structurestudioreservationapp.Validators.PhoneValidator.isValidPhoneNumber;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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

import nbu.f104260.structurestudioreservationapp.Validators.EmailValidator;
import nbu.f104260.structurestudioreservationapp.Validators.PasswordValidator;
import nbu.f104260.structurestudioreservationapp.Validators.PhoneValidator;
import nbu.f104260.structurestudioreservationapp.databinding.ActivityHomeBinding;
import nbu.f104260.structurestudioreservationapp.databinding.ActivityProfileBinding;
import nbu.f104260.structurestudioreservationapp.databinding.ActivitySettingsBinding;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    ActivityProfileBinding binding;
    DrawerLayout drawerLayout;

    NavigationView navigationView;

    androidx.appcompat.widget.Toolbar toolbar;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
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

        databaseHelper = new DatabaseHelper(this);


        SharedPreferences sharedPreferences = getSharedPreferences("LoginData", MODE_PRIVATE);
        populateUserData(sharedPreferences.getString("Email", ""));

        binding.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserData(sharedPreferences.getString("Email",""));
            }
        });

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if(menuItem.getItemId() == R.id.main) {
            Intent intent = new Intent(ProfileActivity.this, MakeReservationActivity.class);
            startActivity(intent);
        } else if(menuItem.getItemId() == R.id.history) {
            Intent intent = new Intent(ProfileActivity.this, HistoryActivity.class);
            startActivity(intent);
        } else if(menuItem.getItemId() == R.id.home) {
            Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
            startActivity(intent);
        } else if(menuItem.getItemId() == R.id.more) {
            Intent intent = new Intent(ProfileActivity.this, SettingsActivity.class);
            startActivity(intent);
        } else if(menuItem.getItemId() == R.id.logout) {
            SharedPreferences sharedPreferences = getSharedPreferences("LoginData", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("IsLoggedIn", false);
            editor.apply();
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(intent);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void populateUserData(String email) {
        Cursor cursor = databaseHelper.getUserData(email);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String firstName = cursor.getString(cursor.getColumnIndex("first_name"));
            @SuppressLint("Range")String lastName = cursor.getString(cursor.getColumnIndex("last_name"));
            @SuppressLint("Range")String userEmail = cursor.getString(cursor.getColumnIndex("email"));
            @SuppressLint("Range")String phoneNumber = cursor.getString(cursor.getColumnIndex("phoneNumber"));

            binding.firstName.setHint(firstName);
            binding.lastName.setHint(lastName);
            binding.email.setHint(userEmail);
            binding.phone.setHint(phoneNumber);
            binding.oldPassword.setHint("Old Password");
            binding.newPassword.setHint("New Password");
            binding.confirmPassword.setHint("Confirm Password");

            cursor.close();
        } else {
            Toast.makeText(this, "User data not found.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUserData(String email1) {
        String email = binding.email.getText().toString();
        String firstName = binding.firstName.getText().toString();
        String lastName = binding.lastName.getText().toString();
        String password = binding.newPassword.getText().toString();
        String confirmPassword = binding.confirmPassword.getText().toString();
        String phone = binding.phone.getText().toString();


        if (!email.isEmpty() && EmailValidator.isValidEmail(email)) {
            Toast.makeText(ProfileActivity.this, "Insert correct email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.isEmpty() && PasswordValidator.isValidPassword(password)) {
            Toast.makeText(ProfileActivity.this, "Password needs to include: 1 UPPER KEY, 1 lower key, 1 number and 1 symbol and to be 8-16 length!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!phone.isEmpty() && PhoneValidator.isValidPhoneNumber(phone)) {
            Toast.makeText(ProfileActivity.this, "Insert correct phone number!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(ProfileActivity.this, "The two passwords did not match!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Retrieve current user data to check password
        Cursor cursor = databaseHelper.getUserData(email1);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String storedPassword = cursor.getString(cursor.getColumnIndex("password"));

            if (!binding.oldPassword.getText().toString().isEmpty() && !storedPassword.equals(binding.oldPassword.getText().toString())) {
                Toast.makeText(ProfileActivity.this, "Old password is incorrect!", Toast.LENGTH_SHORT).show();
                cursor.close();
                return;
            }

            cursor.close();

            // Update only the fields that are provided
            boolean isUpdated = databaseHelper.updateUser(email1,
                    email.isEmpty() ? null:email,
                    firstName.isEmpty() ? null : firstName,
                    lastName.isEmpty() ? null : lastName,
                    password.isEmpty() ? null : password,
                    phone.isEmpty() ? null : phone);

            if (isUpdated) {
                Toast.makeText(ProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

                SharedPreferences sharedPreferences = getSharedPreferences("LoginData", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if(!email1.equals(email)){
                    editor.putString("Email", email);
                }
                editor.remove("Password");
                editor.putBoolean("isLoggedIn", false);
                editor.apply();
                Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(ProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "User data not found.", Toast.LENGTH_SHORT).show();
        }
    }
}