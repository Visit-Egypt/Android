package com.visitegypt.presentation.signin;


import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.android.material.textview.MaterialTextView;
import com.shobhitpuri.custombuttons.GoogleSignInButton;
import com.visitegypt.R;
import com.visitegypt.domain.model.User;
import com.visitegypt.presentation.home.parent.Home;
import com.visitegypt.presentation.signup.SignUpActivity;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class SignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, FacebookCallback<LoginResult> {
    private static final String TAG = "Sign In Activity";
    private static final String USER_NAME = "user_name";
    private static final String USER_EMAIL = "user_email";
    private static final int RC_SIGN_IN = 1;
    public int GoogleFlag = 0;
    public GoogleSignInClient mGoogleSignInClient;
    public GoogleApiClient googleApiClient;
    GoogleSignInButton googleSignInButton;
    Dialog forgetPasswordDialog;
    TextInputLayout txtEmail, txtPassword;
    AppCompatButton btnSignIn;
    View loadingLayout;
    String password, email, token;
    SignInViewModel signInViewModel;
    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    AccessToken accessToken;
    MaterialTextView forgetPasswordTextView;
    private LoginButton facebookSignInButton;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        forgetPasswordTextView = findViewById(R.id.forgetPasswordMaterialTextView);
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

        Log.d(TAG, "onCreate: tokennnnn   " + accessTokenTracker);
        signInViewModel = new ViewModelProvider(this).get(SignInViewModel.class);
        if (signInViewModel.checkUser()) {
            redirectHome();
        }
        btnSignIn = findViewById(R.id.btnSignIn);
        loadingLayout = findViewById(R.id.loadingLayout);
        txtEmail = findViewById(R.id.emailTextView);
        txtPassword = findViewById(R.id.txtPassword);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        forgetPasswordTextView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        forgetPassword();
                    }
                }
        );
        signInViewModel.msgMutableLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals("Your login done")) {

                } else {
                    hideLoading();
                    Toast.makeText(SignInActivity.this, s, Toast.LENGTH_LONG).show();
                }
            }
        });
        signInViewModel.userMutable.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                Intent intent = new Intent(SignInActivity.this, Home.class);
                intent.putExtra(USER_NAME, user.getFirstName() + " " + user.getLastName());
                intent.putExtra(USER_EMAIL, user.getEmail());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        signInViewModel.forgotPasswordResponse.observe(this, a -> {

            if (a.equals("reset done")) {
                Toast.makeText(SignInActivity.this, "Reset password done.", Toast.LENGTH_LONG).show();
                redirectSignup();


            } else {
                Toast.makeText(SignInActivity.this, "Not found", Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void onCancel() {
        Toast.makeText(this, "Login cancel, ", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onError(@NonNull FacebookException e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onError: " + e.getMessage());

    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        Toast.makeText(this, "Login successfullyy, " + loginResult.getAccessToken(), Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onSuccess: " + loginResult.getAccessToken());
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
        btnSignIn.setVisibility(View.GONE);
        txtPassword.setVisibility(View.GONE);
        txtEmail.setVisibility(View.GONE);
        googleSignInButton.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        btnSignIn.setVisibility(View.VISIBLE);
        txtPassword.setVisibility(View.VISIBLE);
        googleSignInButton.setVisibility(View.VISIBLE);
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
        Intent intent = new Intent(SignInActivity.this, Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(runnable -> {
            String token = runnable.getResult();
            Log.d(TAG, "redirectHome: woow" + token);
            signInViewModel.test(token);
        });
        startActivity(intent);
        finish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult:google " + GoogleFlag);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
            Log.d(TAG, "onActivityResult: ");
            logOut();
            redirectHome();

        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void logOut() {
        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG, "onComplete: logout from google acc done successfully");
            }
        });

    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        Log.d(TAG, "onActivityResult: ");

        try {
            GoogleSignInAccount acct = completedTask.getResult(ApiException.class);

            if (acct != null) {
                String personName = acct.getDisplayName();

                String idToken = acct.getIdToken();
                Log.d(TAG, "handleSignInResult: " + personName);
                Log.d(TAG, "handleSignInResult: " + idToken);
                signInViewModel.signInWithGoogle(idToken, acct.getEmail());
            }

        } catch (ApiException e) {
            Log.d(TAG, "signInResult:failed code=" + e.getStatusCode());
        }

    }

    private void forgetPassword() {
        View dialogLayout = LayoutInflater.from(SignInActivity.this).inflate(R.layout.dialog_forget_password, null);
        forgetPasswordDialog = new Dialog(this);
        forgetPasswordDialog.setContentView(dialogLayout);
        forgetPasswordDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        forgetPasswordDialog.show();
        TextInputEditText textInputEditText = forgetPasswordDialog.findViewById(R.id.forgetPasswordTextInputEditText);

        forgetPasswordDialog.findViewById(R.id.forgetPasswordMaterialButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emaill = textInputEditText.getText().toString().trim();
                Log.d(TAG, "onClick:aaaaaaaaaaaaaaaas " + emaill);
                if (emaill.isEmpty()) {
                    textInputEditText.setError("Enter your E-mail.");
                } else {
                    signInViewModel.forgotPassword(emaill);
                }
            }

        });
    }
}