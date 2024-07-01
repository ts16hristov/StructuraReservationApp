package nbu.f104260.structurestudioreservationapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import nbu.f104260.structurestudioreservationapp.Service.FetchMusicUrlService;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {

    private final Handler handler = new Handler();
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseHelper = new DatabaseHelper(this);

        if (!NetworkUtil.isNetworkConnected(this)) {
            Toast.makeText(this, "No internet connection. App will close.", Toast.LENGTH_LONG).show();
            finish();  // Close the app or activity
        } else {
            Intent serviceIntent = new Intent(this, FetchMusicUrlService.class);
            startService(serviceIntent);


            // Perform the check in the background
            new Thread(new Runnable() {
                @Override
                public void run() {
                    checkLoginAndRedirect();
                }
            }).start();
        }
    }

    private void checkLoginAndRedirect() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginData", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("IsLoggedIn", false);
        String email = sharedPreferences.getString("Email", "");
        String password = sharedPreferences.getString("Password", "");

        if (isLoggedIn && databaseHelper.checkEmailPassword(email, password)) {
            navigateToHome();
        } else {
            navigateToMain();
        }
    }

    private void navigateToHome() {
        Intent intent = new Intent(SplashScreen.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToMain() {
        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}