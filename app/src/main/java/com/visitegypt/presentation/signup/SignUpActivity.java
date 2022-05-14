package com.visitegypt.presentation.signup;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.shobhitpuri.custombuttons.GoogleSignInButton;
import com.visitegypt.R;
import com.visitegypt.domain.usecase.UserValidation;
import com.visitegypt.presentation.home.parent.Home;
import com.visitegypt.presentation.signin.SignInActivity;
import com.visitegypt.presentation.signin.SignInViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SignUpActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "Sign Up Activity";
    private static final int RC_SIGN_IN = 1;
    GoogleSignInButton googleSignInButton;
    TextInputLayout firstName, lastName, email, phoneNumber, password;
    SignUpViewModel signUpViewModel;
    SignInViewModel signInViewModel;
    MaterialTextView btnSignInTransfer;
    AppCompatButton btnSignUp;
    View loadingLayout;
    GoogleSignInClient mGoogleSignInClient;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        googleSignInButton = findViewById(R.id.googleSignInButton);
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        loadingLayout = findViewById(R.id.loadingLayout);
        btnSignUp = findViewById(R.id.btnSignUp);
        firstName = findViewById(R.id.txtFirstName);
        lastName = findViewById(R.id.txtLastName);
        email = findViewById(R.id.emailTextView);
        phoneNumber = findViewById(R.id.txtPhoneNumber);
        password = findViewById(R.id.txtPassword);
        btnSignInTransfer = findViewById(R.id.btnSignInTransfer);
        signUpViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);
        signInViewModel = new ViewModelProvider(this).get(SignInViewModel.class);
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
        signUpViewModel.mutableLiveDataResponse.observe(this, s -> {
            Log.d(TAG, "account change observed");
            if (s.equals("Your account was created successfully")) {
                Toast.makeText(SignUpActivity.this, "Verfication email is sent, please verify your email", Toast.LENGTH_LONG).show();
                Toast.makeText(SignUpActivity.this, s, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else if (s.equals("Your google account was created successfully")) {
                Toast.makeText(SignUpActivity.this, s, Toast.LENGTH_LONG).show();
                logOut();
                redirectHome();
            } else {
                Toast.makeText(SignUpActivity.this, s, Toast.LENGTH_LONG).show();
            }
            hideLoading();
        });
        btnSignUp.setOnClickListener(v -> {
            if (firstName.getEditText().getText().toString().isEmpty() ||
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
                    signUpViewModel.getUser();
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

    private void showLoading() {
        firstName.setVisibility(View.GONE);
        lastName.setVisibility(View.GONE);
        email.setVisibility(View.GONE);
        phoneNumber.setVisibility(View.GONE);
        password.setVisibility(View.GONE);
        btnSignUp.setVisibility(View.GONE);
        btnSignInTransfer.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        firstName.setVisibility(View.VISIBLE);
        lastName.setVisibility(View.VISIBLE);
        email.setVisibility(View.VISIBLE);
        phoneNumber.setVisibility(View.VISIBLE);
        password.setVisibility(View.VISIBLE);
        btnSignInTransfer.setVisibility(View.VISIBLE);
        btnSignUp.setVisibility(View.VISIBLE);
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

    private void redirectHome() {
        Intent intent = new Intent(SignUpActivity.this, Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
            Log.d(TAG, "onActivityResult:done ");

        } else {
            Log.d(TAG, "onActivityResult: not equal RC_SIGN_IN");
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        Log.d(TAG, "onActivityResult:Start task ");
        try {
            GoogleSignInAccount acct = completedTask.getResult(ApiException.class);
            if (acct != null) {
                String personName = acct.getDisplayName();
                String idToken = acct.getIdToken();
                Log.d(TAG, "handleSignInResult: " + personName);
                Log.d(TAG, "handleSignInResult: " + idToken);
                signUpViewModel.signUpWithGoogle(idToken, acct.getEmail());

            }

        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }

    }

    public void logOut() {
        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG, "onComplete: logout from google acc done successfully");
            }
        });

    }
}