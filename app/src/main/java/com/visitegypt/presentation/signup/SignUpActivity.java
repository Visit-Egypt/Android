package com.visitegypt.presentation.signup;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.visitegypt.R;
import com.visitegypt.domain.usecase.UserValidation;
import com.visitegypt.presentation.home.HomeActivity;
import com.visitegypt.presentation.signin.SignInActivity;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SignUpActivity extends AppCompatActivity {
    TextInputLayout firstName, lastName, email, phoneNumber, password;
    
    SignUpViewModel signUpViewModel;
    MaterialTextView signInButton;
    AppCompatButton signUpButton;
    View loadingLayout;
    private static final String TAG = "Cannot invoke method length() on null object";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        loadingLayout = findViewById(R.id.loadingLayout);
        signUpButton = findViewById(R.id.signUpButton);
        firstName = findViewById(R.id.txtFisrtName);
        lastName = findViewById(R.id.txtLastName);
        email = findViewById(R.id.txtEmail);
        phoneNumber = findViewById(R.id.txtPhoneNumber);
        password = findViewById(R.id.txtPassword);
        signInButton = findViewById(R.id.signInTransferButton);
        signUpViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);
        signUpViewModel.mutableLiveDataErrors.observe(this, new Observer<String[]>() {
            @Override
            public void onChanged(String[] strings) {
                if (!strings[0].isEmpty()) {
                    firstName.setError(strings[0]);
                }
                if (!strings[1].isEmpty()) {

                    lastName.setError(strings[1]);
                }
                if (!strings[2].isEmpty()) {
                    email.setError(strings[2]);
                }
                if (!strings[3].isEmpty()) {
                    password.setError(strings[3]);
                }
            }
        });
        signUpViewModel.mutableLiveDataResponse.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {

                if (s.equals("Your account was created successfully")) {
                    Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                hideLoading();
                Toast.makeText(SignUpActivity.this, s, Toast.LENGTH_LONG).show();
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        if (
                firstName.getEditText().getText().toString().isEmpty() ||
                        lastName.getEditText().getText().toString().isEmpty() ||
                        email.getEditText().getText().toString().isEmpty() ||
                        phoneNumber.getEditText().getText().toString().isEmpty() ||
                        password.getEditText().getText().toString().isEmpty()) {
            checkValidations();
        } else {
            UserValidation userValidation = new UserValidation(firstName.getEditText().getText().toString().trim(), lastName.getEditText().getText().toString(),
                    email.getEditText().getText().toString().trim(),
                    phoneNumber.getEditText().getText().toString().trim(),
                    password.getEditText().getText().toString());
            signUpViewModel.setUserValidation(userValidation);
            if (signUpViewModel.checkUserValidation()) {
                showLoading();
                signUpViewModel.getUser();
            }
        }
            }
        });
        
    }

    public void signInButton(View view) {
        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

//    public void signupOnclick(View view) {
//        if (
//                firstName.getEditText().getText().toString().isEmpty() ||
//                        lastName.getEditText().getText().toString().isEmpty() ||
//                        email.getEditText().getText().toString().isEmpty() ||
//                        phoneNumber.getEditText().getText().toString().isEmpty() ||
//                        password.getEditText().getText().toString().isEmpty()) {
//            checkValidations();
//        } else {
//            UserValidation userValidation = new UserValidation(firstName.getEditText().getText().toString().trim(), lastName.getEditText().getText().toString(),
//                    email.getEditText().getText().toString().trim(),
//                    phoneNumber.getEditText().getText().toString().trim(),
//                    password.getEditText().getText().toString());
//            signUpViewModel.setUserValidation(userValidation);
//            if (signUpViewModel.checkUserValidation()) {
//                showLoading();
//                signUpViewModel.getUser();
//            }
//        }
//    }


    private void showLoading() {
        firstName.setVisibility(View.GONE);
        lastName.setVisibility(View.GONE);
        email.setVisibility(View.GONE);
        phoneNumber.setVisibility(View.GONE);
        password.setVisibility(View.GONE);
        signUpButton.setVisibility(View.GONE);
        signInButton.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        firstName.setVisibility(View.VISIBLE);
        lastName.setVisibility(View.VISIBLE);
        email.setVisibility(View.VISIBLE);
        phoneNumber.setVisibility(View.VISIBLE);
        password.setVisibility(View.VISIBLE);
        signInButton.setVisibility(View.VISIBLE);
        signUpButton.setVisibility(View.VISIBLE);
        loadingLayout.setVisibility(View.GONE);
    }

    public void checkValidations() {
        if (firstName.getEditText().getText().toString().isEmpty()) {

            firstName.setError("Enter your first name");
            firstName.requestFocus();
            firstName.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (firstName.getEditText().getText().toString().isEmpty()) {
                        firstName.setError("Enter your first name");

                    } else {
                        firstName.setError(null);
                    }
                }
            });
        }
        if (lastName.getEditText().getText().toString().isEmpty()) {
            lastName.setError("Enter your last name");
            lastName.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (lastName.getEditText().getText().toString().isEmpty()) {
                    } else {
                        lastName.setError(null);
                    }
                }
            });
        }
        if (email.getEditText().getText().toString().isEmpty()) {
            email.setError("Enter your email");
            email.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (email.getEditText().getText().toString().isEmpty()) {
                        email.setError("Enter your email");
                    } else {
                        email.setError(null);
                    }

                }
            });
        }
        if (phoneNumber.getEditText().getText().toString().isEmpty()) {
            phoneNumber.setError("Enter your phone number");
            phoneNumber.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (phoneNumber.getEditText().getText().toString().isEmpty()) {
                        phoneNumber.setError("Enter your phone number");
                    } else {
                        phoneNumber.setError(null);
                    }
                }
            });
        }
        if (password.getEditText().getText().toString().isEmpty()) {
            password.setError("Enter your password");
            password.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (password.getEditText().getText().toString().isEmpty()) {
                        password.setError("Enter your password");
                    } else {
                        password.setError(null);
                    }
                }
            });
        }
    }
    
}