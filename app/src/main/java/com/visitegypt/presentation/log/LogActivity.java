package com.visitegypt.presentation.log;

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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.shobhitpuri.custombuttons.GoogleSignInButton;
import com.visitegypt.R;
import com.visitegypt.domain.model.User;
import com.visitegypt.domain.usecase.UserValidation;
import com.visitegypt.presentation.home.parent.Home;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class LogActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "Log Activity";
    private static final String USER_NAME = "user_name";
    private static final String USER_EMAIL = "user_email";
    private static final int RC_SIGN_IN = 1;
    public GoogleSignInClient mGoogleSignInClient;
    public GoogleApiClient googleApiClient;
    public ConstraintLayout signInConstraintLayout, signUpConstraintLayout;
    GoogleSignInButton googleSignInButton, googleSignUpButton;
    Dialog forgetPasswordDialog;
    boolean checkSignUp;
    TextInputLayout emailTextField, passwordSignInTextField, firstNameSignUpTextField, lastNameSignUpTextField, emailSignUpTextField, phoneNumber, password;
    MaterialButton signInMaterialButton, signUpMaterialButton;
    View loadingLayout;
    String token, emaill, passwordd;

    MaterialTextView forgetPasswordMaterialTextView, signInTransferMaterialButton, signUpTransferMaterialButton;
    LogViewModel logViewModel;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        forgetPasswordMaterialTextView = findViewById(R.id.forgetPasswordMaterialTextView);
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        googleSignInButton = findViewById(R.id.googleSignInButton);
        googleSignUpButton = findViewById(R.id.googleSignUpButton);

        googleSignInButton.setOnClickListener(v -> {
            checkSignUp = false;
            signIn();

        });
        googleSignUpButton.setOnClickListener(v -> {
            checkSignUp = true;

            signIn();
        });
        logViewModel = new ViewModelProvider(this).get(LogViewModel.class);
        signInConstraintLayout = findViewById(R.id.signInConstraintLayout);
        signUpConstraintLayout = findViewById(R.id.signUpConstraintLayout);
        if (logViewModel.checkUser()) {
            Log.d(TAG, "onCreate: user signed in");
            redirectHome();
        }
        loadingLayout = findViewById(R.id.loadingLayout);
        emailTextField = findViewById(R.id.emailTextField);
        passwordSignInTextField = findViewById(R.id.passwordSignInTextField);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        forgetPasswordMaterialTextView.setOnClickListener(
                v -> forgetPassword()
        );
        logViewModel.msgMutableLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals("Your login done")) {
                    redirectHome();
                    Toast.makeText(LogActivity.this, s, Toast.LENGTH_LONG).show();

                } else if (s.equals("Your google login done")) {
                    redirectHome();
                    logOut();
                    Toast.makeText(LogActivity.this, s, Toast.LENGTH_LONG).show();
                } else {
                    hideLoading();
                    Toast.makeText(LogActivity.this, s, Toast.LENGTH_LONG).show();
                }
            }
        });
        logViewModel.userMutable.observe(this, user -> {
            Intent intent = new Intent(LogActivity.this, Home.class);
            intent.putExtra(USER_NAME, user.getFirstName() + " " + user.getLastName());
            intent.putExtra(USER_EMAIL, user.getEmail());
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
        logViewModel.forgotPasswordResponse.observe(this, a -> {

            if (a.equals("reset done")) {
                Toast.makeText(LogActivity.this, "Reset password done.", Toast.LENGTH_LONG).show();
                redirectSignup();
            } else {
                Toast.makeText(LogActivity.this, "Not found", Toast.LENGTH_LONG).show();
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        loadingLayout = findViewById(R.id.loadingLayout);
        signUpMaterialButton = findViewById(R.id.signUpMaterialButton);
        firstNameSignUpTextField = findViewById(R.id.firstNameSignUpTextField);
        lastNameSignUpTextField = findViewById(R.id.lastNameSignUpTextField);
        emailSignUpTextField = findViewById(R.id.emailSignUpTextField);
        phoneNumber = findViewById(R.id.txtPhoneNumber);
        password = findViewById(R.id.passwordSignUpTextField);
        logViewModel = new ViewModelProvider(this).get(LogViewModel.class);
        logViewModel.mutableLiveDataErrors.observe(this, new Observer<String[]>() {
            @Override
            public void onChanged(String[] strings) {
                if (!strings[0].isEmpty()) {
                    firstNameSignUpTextField.setError(strings[0]);
                }
                if (!strings[1].isEmpty()) {
                    lastNameSignUpTextField.setError(strings[1]);
                }
                if (!strings[2].isEmpty()) {
                    emailSignUpTextField.setError(strings[2]);
                }
                if (!strings[3].isEmpty()) {
                    password.setError(strings[3]);
                }
            }
        });
        logViewModel.mutableLiveDataResponse.observe(this, s -> {
            Log.d(TAG, "account change observed");
            if (s.equals("Your account was created successfully")) {
                Toast.makeText(LogActivity.this, "Verfication email is sent, please verify your email", Toast.LENGTH_LONG).show();
                Toast.makeText(LogActivity.this, s, Toast.LENGTH_LONG).show();
                redirectSignIn();
            } else if (s.equals("Your google account was created successfully")) {
                Toast.makeText(LogActivity.this, s, Toast.LENGTH_LONG).show();
                logOut();
                redirectHome();
            } else {
                Toast.makeText(LogActivity.this, s, Toast.LENGTH_LONG).show();
            }
            hideLoading();
        });
        signUpMaterialButton.setOnClickListener(v -> {
            if (firstNameSignUpTextField.getEditText().getText().toString().isEmpty() ||
                    lastNameSignUpTextField.getEditText().getText().toString().isEmpty() ||
                    emailSignUpTextField.getEditText().getText().toString().isEmpty() ||
                    phoneNumber.getEditText().getText().toString().isEmpty() ||
                    password.getEditText().getText().toString().isEmpty()) {
                checkValidations();
            } else {
                UserValidation userValidation = new UserValidation(firstNameSignUpTextField.getEditText().getText().toString().trim(), lastNameSignUpTextField.getEditText().getText().toString(),
                        emailSignUpTextField.getEditText().getText().toString().trim(),
                        phoneNumber.getEditText().getText().toString().trim(),
                        password.getEditText().getText().toString());
                logViewModel.setUserValidation(userValidation);
                if (logViewModel.checkUserValidation()) {
                    showLoading();
                    logViewModel.getUser();
                }
            }
        });

    }


    public void checkValidations() {
        if (firstNameSignUpTextField.getEditText().getText().toString().isEmpty()) {
            firstNameSignUpTextField.setError("Enter your first name");
            firstNameSignUpTextField.requestFocus();
            firstNameSignUpTextField.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (firstNameSignUpTextField.getEditText().getText().toString().isEmpty()) {
                        firstNameSignUpTextField.setError("Enter your first name");

                    } else {
                        firstNameSignUpTextField.setError(null);
                    }
                }
            });
        }
        if (lastNameSignUpTextField.getEditText().getText().toString().isEmpty()) {
            lastNameSignUpTextField.setError("Enter your last name");
            lastNameSignUpTextField.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (lastNameSignUpTextField.getEditText().getText().toString().isEmpty()) {
                    } else {
                        lastNameSignUpTextField.setError(null);
                    }
                }
            });
        }
        if (emailSignUpTextField.getEditText().getText().toString().isEmpty()) {
            emailSignUpTextField.setError("Enter your email");
            emailSignUpTextField.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (emailSignUpTextField.getEditText().getText().toString().isEmpty()) {
                        emailSignUpTextField.setError("Enter your email");
                    } else {
                        emailSignUpTextField.setError(null);
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


    public void signInButtonOnClick(View view) {
        emaill = emailTextField.getEditText().getText().toString();
        passwordd = passwordSignInTextField.getEditText().getText().toString();
        if (emaill.isEmpty() || passwordd.isEmpty()) {
            if (emaill.isEmpty()) {
                emailTextField.setError("Please Enter Your Email");
                emailTextField.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (emailTextField.getEditText().getText().toString().isEmpty()) {
                            emailTextField.setError("Please Enter Your Email");
                        } else {
                            emailTextField.setError(null);
                        }
                    }
                });
            }
            if (passwordd.isEmpty()) {
                passwordSignInTextField.setError("Please,enter your password");
                passwordSignInTextField.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (passwordSignInTextField.getEditText().getText().toString().isEmpty()) {
                            passwordSignInTextField.setError("Please,enter your password");
                        } else {
                            passwordSignInTextField.setError(null);
                        }
                    }
                });
            }
        } else {
            User myUser = new User(emaill, passwordd);
            showLoading();
            logViewModel.login(myUser);
        }
    }

    private void showLoading() {
        signInMaterialButton.setVisibility(View.GONE);
        passwordSignInTextField.setVisibility(View.GONE);
        emailTextField.setVisibility(View.GONE);
        googleSignInButton.setVisibility(View.GONE);

        firstNameSignUpTextField.setVisibility(View.GONE);
        lastNameSignUpTextField.setVisibility(View.GONE);
        emailSignUpTextField.setVisibility(View.GONE);
        phoneNumber.setVisibility(View.GONE);
        password.setVisibility(View.GONE);
        signUpMaterialButton.setVisibility(View.GONE);
        signUpTransferMaterialButton.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);
    }


    private void hideLoading() {
        firstNameSignUpTextField.setVisibility(View.VISIBLE);
        lastNameSignUpTextField.setVisibility(View.VISIBLE);
        emailSignUpTextField.setVisibility(View.VISIBLE);
        phoneNumber.setVisibility(View.VISIBLE);
        password.setVisibility(View.VISIBLE);
        signUpMaterialButton.setVisibility(View.VISIBLE);
        signUpMaterialButton.setVisibility(View.VISIBLE);

        signInMaterialButton.setVisibility(View.VISIBLE);

        passwordSignInTextField.setVisibility(View.VISIBLE);
        googleSignInButton.setVisibility(View.VISIBLE);
        emailTextField.setVisibility(View.VISIBLE);
        loadingLayout.setVisibility(View.GONE);

    }

    public void redirectSignup(View view) {
        signInConstraintLayout.setVisibility(View.GONE);
        signUpConstraintLayout.setVisibility(View.VISIBLE);

    }

    public void redirectSignup() {
        signInConstraintLayout.setVisibility(View.GONE);
        signUpConstraintLayout.setVisibility(View.VISIBLE);

    }

    private void redirectHome() {
        Intent intent = new Intent(LogActivity.this, Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//sign in
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(runnable -> {
            String token = runnable.getResult();
            Log.d(TAG, "redirectHome: woow" + token);
            logViewModel.registerDeviceToNotification(token);
        });
        startActivity(intent);
        finish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        hideLoading();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
            Log.d(TAG, "onActivityResult: requesting login");

        } else {
            Log.d(TAG, "onActivityResult: not equal RC_SIGN_IN");
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        Log.d(TAG, "onActivityResult: handleSignInResult");

        try {
            Log.d(TAG, "onActivityResult:done");
            GoogleSignInAccount acct = completedTask.getResult(ApiException.class);

            if (acct != null) {
                String personName = acct.getDisplayName();

                String idToken = acct.getIdToken();
                Log.d(TAG, "handleSignInResult: " + personName);
                Log.d(TAG, "handleSignInResult: " + idToken);
                if (checkSignUp) {
                    logViewModel.signUpWithGoogle(idToken, acct.getEmail());
                } else {
                    logViewModel.signInWithGoogle(idToken, acct.getEmail());
                }
            } else {
                Log.e(TAG, "handleSignInResult: account is null");
            }

        } catch (ApiException e) {
            Log.e(TAG, "signInResult:failed code=" + e.getStatusCode());
        }

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void logOut() {
        mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> Log.d(TAG, "onComplete: logout from google acc done successfully"));
    }

    private void forgetPassword() {
        View dialogLayout = LayoutInflater.from(LogActivity.this).inflate(R.layout.dialog_forget_password, null);
        forgetPasswordDialog = new Dialog(this);
        forgetPasswordDialog.setContentView(dialogLayout);
        forgetPasswordDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        forgetPasswordDialog.show();
        TextInputEditText textInputEditText = forgetPasswordDialog.findViewById(R.id.forgetPasswordTextInputEditText);

        forgetPasswordDialog.findViewById(R.id.forgetPasswordMaterialButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emaill = textInputEditText.getText().toString().trim();
                Log.d(TAG, "onClick:  " + emaill);
                if (emaill.isEmpty()) {
                    textInputEditText.setError("Enter your E-mail.");
                } else {
                    logViewModel.forgotPassword(emaill);
                }
            }

        });
    }

    public void redirectSignIn(View view) {
        signUpConstraintLayout.setVisibility(View.GONE);
        signInConstraintLayout.setVisibility(View.VISIBLE);
    }

    public void redirectSignIn() {
        signUpConstraintLayout.setVisibility(View.GONE);
        signInConstraintLayout.setVisibility(View.VISIBLE);
    }
}