package nbu.f104260.structurestudioreservationapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import nbu.f104260.structurestudioreservationapp.Service.FetchMusicUrlService;

public class MainActivity extends AppCompatActivity {


    private final Handler handler = new Handler();

    private final DatabaseHelper databaseHelper = new DatabaseHelper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

     ImageButton signInButton = findViewById(R.id.main_sign_in);
     ImageButton signUpButton = findViewById(R.id.signup_button);

     if (!NetworkUtil.isNetworkConnected(this)) {
         Toast.makeText(this, "No internet connection. App will close.", Toast.LENGTH_LONG).show();
         finish();  // Close the app or activity
     } else {
         Intent serviceIntent = new Intent(this, FetchMusicUrlService.class);
         startService(serviceIntent);
     }

        new Thread(new Runnable() {
            @Override
            public void run() {
                checkLoginAndRedirect();
            }
        }).start();

            signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void checkLoginAndRedirect() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginData", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("IsLoggedIn", false);
        String email = sharedPreferences.getString("Email", "");
        String password = sharedPreferences.getString("Password", "");

        if (isLoggedIn && databaseHelper.checkEmailPassword(email, password)) {
            navigateToHome();
        }
    }

    private void navigateToHome() {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }




}