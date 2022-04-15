package com.visitegypt.presentation.setting;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import static com.visitegypt.utils.UploadUtils.checkAndRequestPermissions;
import static com.visitegypt.utils.UploadUtils.getRealPathFromUri;
import static com.visitegypt.utils.UploadUtils.setContext;

import android.Manifest;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.visitegypt.R;
import com.visitegypt.domain.model.User;
import com.visitegypt.domain.model.UserUpdateRequest;
import com.visitegypt.presentation.home.parent.Home;
import com.visitegypt.presentation.signin.SignInActivity;

import com.visitegypt.utils.UploadUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint

public class SettingFragment extends Fragment {
    TextInputEditText firstName,
            lastName,
            email,
            phone,
            password;
    AppCompatButton saveButton, cancelButton, logOutButton;
    CircularImageView changeImageView;
    CircularImageView userImageView;
    View settingFragment;
    SettingViewModel settingViewModel;
    private  String userImage ;
    File file = null;
    private static final int PHOTO_SELECTED = 1;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    private static final int PICK_FROM_GALLERY = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        settingFragment = inflater.inflate(R.layout.fragment_setting, container, false);
        setContext(getContext());
        settingViewModel =
                new ViewModelProvider(this).get(SettingViewModel.class);
        init();
        settingViewModel.initCallBack();
        getUserData();
        changeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkAndRequestPermissions(getActivity())){
                    chooseImage(getContext());
                }
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        settingViewModel.url.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null)
                {
                    userImage = s;
                    Picasso.get().load(s).into(userImageView);

                }

            }
        });
        return settingFragment;
    }



    private void init() {
        firstName = settingFragment.findViewById(R.id.firstNameTextInputEditText);
        lastName = settingFragment.findViewById(R.id.lastNameTextInputEditText);
        email = settingFragment.findViewById(R.id.emailTextInputEditText);
        phone = settingFragment.findViewById(R.id.phoneTextInputEditText);
        password = settingFragment.findViewById(R.id.passwordTextInputEditText);
        saveButton = settingFragment.findViewById(R.id.saveButton);
        cancelButton = settingFragment.findViewById(R.id.cancelButton);
//        logOutButton = settingFragment.findViewById(R.id.logOutButton);
        changeImageView = settingFragment.findViewById(R.id.changeImageView);
        userImageView = settingFragment.findViewById(R.id.userImageView);
    }

    private void getUserData() {
        settingViewModel.getUserData();
        settingViewModel.mutableLiveDataUser.observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                firstName.setText(user.getFirstName());
                lastName.setText(user.getLastName());
                email.setText(user.getEmail());
                phone.setText(user.getPhoneNumber());
                if (user.getPhotoUrl() != null && !user.getPhotoUrl().isEmpty()) {
                    Glide.with(requireContext())
                            .load(user.getPhotoUrl())
                            .fitCenter()
                            .placeholder(R.drawable.ic_baseline_account_circle_24)
                            .error(R.drawable.ic_baseline_account_circle_24)
                            .into(userImageView);
                }
            }
        });

    }
/***************************************************************************************************/


    /********************************************************/
    private void redirect() {

        Intent intent = new Intent(getContext(), SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

    @Override
    public void onResume() {
        super.onResume();
        ((Home) getActivity()).setActionBarTitle("Setting");
        ((Home) getActivity()).hideChatBot();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((Home) getActivity()).showChatBot();
    }
    private void chooseImage(Context context){

        final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit" }; // create a menuOption Array

        // create a dialog for showing the optionsMenu

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // set the items in builder

        builder.setItems(optionsMenu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(optionsMenu[i].equals("Take Photo")){

                    // Open the camera and get the photo

                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                }
                else if(optionsMenu[i].equals("Choose from Gallery")){

                    // choose from  external storage

                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);

                }
                else if (optionsMenu[i].equals("Exit")) {
                    dialogInterface.dismiss();
                }

            }
        });
        builder.show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(),
                            "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT)
                            .show();

                } else if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(),
                            "FlagUp Requires Access to Your Storage.",
                            Toast.LENGTH_SHORT).show();

                } else {
                    chooseImage(getContext());
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
                            Log.d("TAG", selectedImage.toString());
                            String filePath = getRealPathFromUri(selectedImage);
                            String mimeType = requireActivity().getContentResolver().getType(selectedImage);
                            if (filePath != null && !filePath.isEmpty())
                                file = new File(filePath);
                                if (file.exists() && file != null)
                                settingViewModel.uploadUserProfilePhoto(file,mimeType);
                        }

                    }
                    break;
            }
        }
    }
}