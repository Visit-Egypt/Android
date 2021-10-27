package com.visitegypt.presentation.signup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.visitegypt.R;
import com.visitegypt.domain.usecase.UserValidation;
import com.visitegypt.presentation.signin.SignInActivity;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SignUpActivity extends AppCompatActivity {
    MaterialAutoCompleteTextView firstName, lastName, email, phoneNumber, password;
    SignUpViewModel signUpViewModel;
    private static final String TAG = "Cannot invoke method length() on null object";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        firstName = findViewById(R.id.fistTextField);
        lastName = findViewById(R.id.lastTextField);
        email = findViewById(R.id.emailTxtField);
        phoneNumber = findViewById(R.id.phoneNumbertxtField);
        password = findViewById(R.id.passwordTxtField);
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
        if(
                firstName.getText().toString().isEmpty() ||
                        lastName.getText().toString().isEmpty() ||
                        email.getText().toString().isEmpty() ||
                        phoneNumber.getText().toString().isEmpty() ||
                        password.getText().toString().isEmpty()) {
            if (firstName.getText().toString().isEmpty()) {
                firstName.setError("Enter your first name");
            }
            if (lastName.getText().toString().isEmpty()) {
                lastName.setError("Enter your last name");
            }
            if (email.getText().toString().isEmpty()) {
                email.setError("Enter your email");
            }
            if (phoneNumber.getText().toString().isEmpty()) {
                phoneNumber.setError("Enter your phone number");
            }
            if (password.getText().toString().isEmpty()) {
                password.setError("Enter your password");
            }
        } else {
            UserValidation userValidation = new UserValidation(firstName.getText().toString(),lastName.getText().toString(),
                    email.getText().toString(),
                    phoneNumber.getText().toString(),
                    password.getText().toString());
            signUpViewModel.getUser(userValidation);

        }
    }
}