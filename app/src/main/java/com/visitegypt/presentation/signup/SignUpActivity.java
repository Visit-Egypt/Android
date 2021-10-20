package com.visitegypt.presentation.signup;

import android.app.ActionBar;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.visitegypt.R;

public class SignUpActivity extends AppCompatActivity {
    MaterialAutoCompleteTextView firstName,lastName,email,phoneNumber,passwoed;
    MaterialButton signUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

    }
}