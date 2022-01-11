package com.visitegypt.presentation.signin;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.visitegypt.R;
import com.visitegypt.domain.model.User;
import com.visitegypt.presentation.home.HomeActivity;
import com.visitegypt.presentation.signup.SignUpActivity;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class SignInActivity extends AppCompatActivity {
    private static final String TAG = "Cannot invoke method length() on null object";
    TextInputLayout txtEmail, txtPassword;
    AppCompatButton signInButton;
    View loadingLayout;
    String password, email;
    SignInViewModel signInViewModel;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        signInViewModel = new ViewModelProvider(this).get(SignInViewModel.class);
        if (signInViewModel.checkUser()) {
            redirectHome();
        }
        signInButton = findViewById(R.id.signInButton);
        loadingLayout = findViewById(R.id.loadingLayout);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        signInViewModel.msgMutableLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals("Your login done")) {
                    Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    hideLoading();
                    Toast.makeText(SignInActivity.this, s, Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public void buttonOnClick(View view) {
        email = txtEmail.getEditText().getText().toString();
        password = txtPassword.getEditText().getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            if (email.isEmpty()) {
                txtEmail.setError("Please Enter Your Email");
                txtEmail.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (txtEmail.getEditText().getText().toString().isEmpty()) {
                            txtEmail.setError("Please Enter Your Email");
                        } else {
                            txtEmail.setError(null);
                        }
                    }
                });
            }
            if (password.isEmpty()) {
                txtPassword.setError("Please,enter your password");
                txtPassword.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (txtPassword.getEditText().getText().toString().isEmpty()) {
                            txtPassword.setError("Please,enter your password");
                        } else {
                            txtPassword.setError(null);
                        }
                    }
                });
            }
        } else {

            User myUser = new User(email, password);
            showLoading();
            signInViewModel.login(myUser);

        }
    }

    public void signUpButton(View view) {
        redirectSignup();
    }

    private void showLoading() {
        signInButton.setVisibility(View.GONE);
        txtPassword.setVisibility(View.GONE);
        txtEmail.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        signInButton.setVisibility(View.VISIBLE);
        txtPassword.setVisibility(View.VISIBLE);
        txtEmail.setVisibility(View.VISIBLE);
        loadingLayout.setVisibility(View.GONE);
    }

    private void redirectSignup() {
        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void redirectHome() {
        Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

}