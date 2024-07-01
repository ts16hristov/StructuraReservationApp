package nbu.f104260.structurestudioreservationapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.SharedMemory;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.CompoundButtonCompat;

import nbu.f104260.structurestudioreservationapp.databinding.ActivitySignInBinding;

public class SignInActivity extends AppCompatActivity {

    ActivitySignInBinding binding;
    DatabaseHelper databaseHelper;

    RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ImageButton signUpButton = findViewById(R.id.signup_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        databaseHelper = new DatabaseHelper(this);
        radioButton = findViewById(R.id.radioButton);
        int color = ContextCompat.getColor(this, android.R.color.white);
        CompoundButtonCompat.setButtonTintList(radioButton, ColorStateList.valueOf(color));

        binding.signButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.signinEmail.getText().toString();
                String password = binding.signinPassword.getText().toString();

                if (email.equals("") || password.equals("")){
                    Toast.makeText(SignInActivity.this, "All fields are mandatory!", Toast.LENGTH_SHORT).show();
                } else {
                    boolean check = databaseHelper.checkEmailPassword(email, password);
                    if(check){
                        Toast.makeText(SignInActivity.this, "Sign in is successfully!", Toast.LENGTH_SHORT).show();
                        SharedPreferences sharedPreferences = getSharedPreferences("LoginData", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        if(radioButton.isChecked()){
                            editor.putBoolean("IsLoggedIn", true);
                        } else {
                            editor.putBoolean("IsLoggedIn", false);
                        }
                        editor.putString("Email", email);
                        editor.putString("Password", password);
                        editor.apply();
                        Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(SignInActivity.this, "Try again! Wring credentials!", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
}