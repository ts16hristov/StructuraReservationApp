package nbu.f104260.structurestudioreservationapp;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import nbu.f104260.structurestudioreservationapp.Validators.EmailValidator;
import nbu.f104260.structurestudioreservationapp.Validators.PasswordValidator;
import nbu.f104260.structurestudioreservationapp.Validators.PhoneValidator;
import nbu.f104260.structurestudioreservationapp.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ImageButton signInButton = findViewById(R.id.sign_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

        databaseHelper = new DatabaseHelper(this);

        binding.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.signupEmail.getText().toString();
                String firstName = binding.firstName.getText().toString();
                String lastName = binding.lastName.getText().toString();
                String password = binding.signupPassword.getText().toString();
                String confirmPassword = binding.signupConfirmPassword.getText().toString();
                String phone = binding.signupPhone.getText().toString();

                if(email.equals("") || firstName.equals("") || lastName.equals("") || password.equals("") || phone.equals("") || confirmPassword.equals("")) {
                    Toast.makeText(SignUpActivity.this, "All fields are mandatory!", Toast.LENGTH_SHORT).show();
                } else if (EmailValidator.isValidEmail(email)){
                    Toast.makeText(SignUpActivity.this, "Insert correct email address!", Toast.LENGTH_SHORT).show();
                } else if (PasswordValidator.isValidPassword(password)) {
                    Toast.makeText(SignUpActivity.this, "Password needs to include: 1 UPPER KEY, 1 lower key, 1 number and 1 symbol and to be 8-16 length!", Toast.LENGTH_SHORT).show();
                } else if (PhoneValidator.isValidPhoneNumber(phone)){
                    Toast.makeText(SignUpActivity.this, "Insert correct phone number!", Toast.LENGTH_SHORT).show();
                } else {
                    if(!password.equals(confirmPassword) ){
                        Toast.makeText(SignUpActivity.this, "The two passwords did not match!", Toast.LENGTH_SHORT).show();
                    } else {
                        if(!databaseHelper.checkEmail(email)) {
                            Boolean insert = databaseHelper.insertData(email,firstName,lastName, password,phone);

                            if (insert) {
                                Toast.makeText(SignUpActivity.this, "Signup successfully! You need to sign in!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class );
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(SignUpActivity.this, "Sign up failed! Try again!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SignUpActivity.this, "Already exists user with this email!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }


}