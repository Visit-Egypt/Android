package com.visitegypt.presentation.signin;


import android.os.Bundle;

import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.visitegypt.R;
import com.visitegypt.domain.model.User;


public class SignInActivity extends AppCompatActivity {
    private static final String TAG = "Cannot invoke method length() on null object";
    MaterialAutoCompleteTextView txtEmail, txtPassword;
    String password, email;
    SignInViewModel signInViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        txtEmail = findViewById(R.id.Email);
        txtPassword = findViewById(R.id.passowrd);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        signInViewModel = new ViewModelProvider(this).get(SignInViewModel.class);
        signInViewModel.msgMutableLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(SignInActivity.this,s,Toast.LENGTH_LONG).show();
            }
        });

    }

    public void buttonOnClick(View view) {
        email = txtEmail.getText().toString();
        password = txtPassword.getText().toString();
        User myUser = new User(email,password);
        signInViewModel.loginViewModel(myUser);
    }
}