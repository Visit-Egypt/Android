package com.visitegypt.presentation.signin;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.visitegypt.R;
import com.visitegypt.di.SharedPrefsModule;
import com.visitegypt.domain.model.User;
import com.visitegypt.presentation.home.HomeActivity;
import com.visitegypt.presentation.signup.SignUpActivity;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class SignInActivity extends AppCompatActivity {
    private static final String TAG = "Cannot invoke method length() on null object";
    TextInputLayout txtEmail, txtPassword;
    String password, email;
    SignInViewModel signInViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
       txtEmail = findViewById(R.id.txtEmail);
       txtPassword = findViewById(R.id.txtPassword);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        signInViewModel = new ViewModelProvider(this).get(SignInViewModel.class);
        signInViewModel.msgMutableLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals("Your login done")) {
                    Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SignInActivity.this, s, Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    public void buttonOnClick(View view) {
        email = txtEmail.getEditText().getText().toString();
        password = txtPassword.getEditText().getText().toString();

        if (email.isEmpty()|| password.isEmpty()) {

            if(email.isEmpty()) {
                txtEmail.setError("Please Enter Your Email");
            }
            if (password.isEmpty()) {
                txtPassword.setError("Please,enter your password");
            }
        } else {

            User myUser = new User(email, password);
            signInViewModel.login(myUser);
        }
    }

    public void signUpButton(View view) {
        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}