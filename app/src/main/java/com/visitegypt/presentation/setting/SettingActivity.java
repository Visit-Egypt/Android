package com.visitegypt.presentation.setting;

import static com.visitegypt.utils.UploadUtils.checkAndRequestPermissions;
import static com.visitegypt.utils.UploadUtils.getRealPathFromUri;
import static com.visitegypt.utils.UploadUtils.setContext;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.visitegypt.R;
import com.visitegypt.domain.model.UserUpdateRequest;
import com.visitegypt.presentation.log.LogActivity;

import java.io.File;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SettingActivity extends AppCompatActivity {
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    private static final String TAG = "Setting Activity";
    private static final int PHOTO_SELECTED = 1;
    private static final int PICK_FROM_GALLERY = 0;
    TextInputEditText firstName, lastName, email, phone, password;
    AppCompatButton saveButton, logOutButton;
    CircularImageView changeImageView;
    CircularImageView userImageView;
    View settingFragment;
    SettingViewModel settingViewModel;
    File file = null;
    private String userImage;

    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setContext(this);
        settingViewModel = new ViewModelProvider(this).get(SettingViewModel.class);
        init();
        settingViewModel.initCallBack();
        getUserData();
        changeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAndRequestPermissions(SettingActivity.this)) {
                    chooseImage(SettingActivity.this);
                }
            }
        });
        saveButton.setOnClickListener(view -> {
            UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
            String userFirstName = firstName.getText().toString();
            String userLastName = lastName.getText().toString();
            String userPhoneNumber = phone.getText().toString();
            String userEmail = email.getText().toString();
            String userPassword = password.getText().toString();


            if (userFirstName != null && !userFirstName.isEmpty())
                userUpdateRequest.setFirstName(userFirstName);
            if (userLastName != null && !userLastName.isEmpty())
                userUpdateRequest.setLastName(userLastName);
            if (userPhoneNumber != null && !userPhoneNumber.isEmpty())
                userUpdateRequest.setPhoneNumber(userPhoneNumber);
            if (userPassword != null && !userPassword.isEmpty())
                userUpdateRequest.setPassword(userPassword);
            if (userEmail != null && !userEmail.isEmpty())
                userUpdateRequest.setEmail(userEmail);
            if (userImage != null && !userImage.isEmpty())
                userUpdateRequest.setPhotoLink(userImage);

            settingViewModel.updateUser(userUpdateRequest);
        });
        settingViewModel.url.observe(this, s -> {
            if (s != null) {
                userImage = s;
                Picasso.get().load(s).into(userImageView);
            }

        });
    }


    private void init() {
        firstName = findViewById(R.id.firstNameTextInputEditText);
        lastName = findViewById(R.id.lastNameTextInputEditText);
        email = findViewById(R.id.emailTextInputEditText);
        phone = findViewById(R.id.phoneTextInputEditText);
        password = findViewById(R.id.passwordTextInputEditText);
        saveButton = findViewById(R.id.saveButton);
//        logOutButton = findViewById(R.id.logOutButton);
        changeImageView = findViewById(R.id.changeImageView);
        userImageView = findViewById(R.id.userImageView);
    }

    private void getUserData() {
        settingViewModel.getUserData();
        settingViewModel.mutableLiveDataUser.observe(this, user -> {
            firstName.setText(user.getFirstName());
            lastName.setText(user.getLastName());
            email.setText(user.getEmail());
            phone.setText(user.getPhoneNumber());
            if (user.getPhotoUrl() != null && !user.getPhotoUrl().isEmpty()) {
                Glide.with(this)
                        .load(user.getPhotoUrl())
                        .fitCenter()
                        .placeholder(R.drawable.ic_baseline_account_circle_24)
                        .error(R.drawable.ic_baseline_account_circle_24)
                        .into(userImageView);
            }
        });

    }

    /***************************************************************************************************/

    private void redirect() {
        Intent intent = new Intent(this, LogActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void chooseImage(Context context) {

        final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit"}; // create a menuOption Array

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setItems(optionsMenu, (dialogInterface, i) -> {

            if (optionsMenu[i].equals("Take Photo")) {
                Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);
            } else if (optionsMenu[i].equals("Choose from Gallery")) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);

            } else if (optionsMenu[i].equals("Exit")) {
                dialogInterface.dismiss();
            }

        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,
                            "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT)
                            .show();

                } else if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,
                            "FlagUp Requires Access to Your Storage.",
                            Toast.LENGTH_SHORT).show();

                } else {
                    chooseImage(this);
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        if (selectedImage != null) {
                            Log.d(TAG, selectedImage.toString());
                            String filePath = getRealPathFromUri(selectedImage);
                            String mimeType = this.getContentResolver().getType(selectedImage);
                            if (filePath != null && !filePath.isEmpty())
                                file = new File(filePath);
                            if (file.exists() && file != null)
                                settingViewModel.uploadUserProfilePhoto(file, mimeType);
                        }

                    }
                    break;
            }
        }
    }
}
