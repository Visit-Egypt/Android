package com.visitegypt.presentation.signup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.visitegypt.R;
import com.visitegypt.domain.usecase.UserValidation;
import com.visitegypt.presentation.signin.SignInActivity;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SignUpActivity extends AppCompatActivity {
    TextInputLayout firstName, lastName, email, phoneNumber, password;
    SignUpViewModel signUpViewModel;
    private static final String TAG = "Cannot invoke method length() on null object";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        firstName = findViewById(R.id.txtFisrtName);
        lastName = findViewById(R.id.txtLastName);
        email = findViewById(R.id.txtEmail);
        phoneNumber = findViewById(R.id.txtPhoneNumber);
        password = findViewById(R.id.txtPassword);
        signUpViewModel = new  ViewModelProvider(this).get(SignUpViewModel.class);
        signUpViewModel.mutableLiveDataErrors.observe(this, new Observer<String[]>() {
            @Override
            public void onChanged(String[] strings) {

                if(!strings[0].isEmpty()) {
                    firstName.setError(strings[0]);
                }
                if(!strings[1].isEmpty()) {

                    lastName.setError(strings[1]);
                }
                if(!strings[2].isEmpty()) {
                    email.setError(strings[2]);
                }
                if(!strings[3].isEmpty()) {
                    password.setError(strings[3]);
                }
            }
        });
        signUpViewModel.mutableLiveDataResponse.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(SignUpActivity.this,s,Toast.LENGTH_LONG).show();
            }
        });
    }

    public void signInButton(View view) {
        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void signupOnclick(View view) {
        Log.d("TAG", "signupOnclick: 1  " + firstName.getEditText().getText().toString());
        Log.d("TAG", "signupOnclick: 2  " + firstName.getEditText().toString());
        if(
                firstName.getEditText().getText().toString().isEmpty() ||
                        lastName.getEditText().getText().toString().isEmpty() ||
                        email.getEditText().getText().toString().isEmpty() ||
                        phoneNumber.getEditText().getText().toString().isEmpty() ||
                        password.getEditText().getText().toString().isEmpty()) {
            if (firstName.getEditText().getText().toString().isEmpty()) {
                firstName.setError("Enter your first name");
            }
            if ( lastName.getEditText().getText().toString().isEmpty()) {
                lastName.setError("Enter your last name");
            }
            if (email.getEditText().getText().toString().isEmpty()) {
                email.setError("Enter your email");
            }
            if (phoneNumber.getEditText().getText().toString().isEmpty()) {
                phoneNumber.setError("Enter your phone number");
            }
            if (password.getEditText().getText().toString().isEmpty()) {
                password.setError("Enter your password");
            }

        } else {
            UserValidation userValidation = new UserValidation(firstName.getEditText().getText().toString(),lastName.getEditText().getText().toString(),
                    email.getEditText().getText().toString(),
                    phoneNumber.getEditText().getText().toString(),
                    password.getEditText().getText().toString());
            signUpViewModel.getUser(userValidation);

        }
    }
}