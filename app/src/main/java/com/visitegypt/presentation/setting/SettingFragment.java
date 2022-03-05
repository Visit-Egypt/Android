package com.visitegypt.ui.setting;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.visitegypt.R;
import com.visitegypt.databinding.FragmentSettingBinding;
import com.visitegypt.domain.model.User;
import com.visitegypt.domain.model.UserUpdateRequest;
import com.visitegypt.domain.model.response.UploadedFilesResponse;
import com.visitegypt.presentation.home.HomeActivity;
import com.visitegypt.presentation.signin.SignInActivity;
import com.visitegypt.presentation.signup.SignUpActivity;
import com.visitegypt.ui.account.AccountViewModel;
import com.visitegypt.utils.UploadUtils;

import java.io.File;
import java.util.Objects;

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
    private static final int PHOTO_SELECTED = 1;
    private static final int PICK_FROM_GALLERY = 0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        settingFragment = inflater.inflate(R.layout.fragment_setting, container, false);

        settingViewModel =
                new ViewModelProvider(this).get(SettingViewModel.class);
        init();
        getUserData();
        changeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPhoto();
            }
        });
//        logOutButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                settingViewModel.logOut();
//                redirect();
//
//            }
//        });
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
                if(userLastName != null && !userLastName.isEmpty())
                    userUpdateRequest.setLastName(userLastName);
                if(userPhoneNumber != null && !userPhoneNumber.isEmpty())
                    userUpdateRequest.setPhoneNumber(userPhoneNumber);
                if(userPassword != null && !userPassword.isEmpty())
                    userUpdateRequest.setPassword(userPassword);
                if(userEmail != null && !userEmail.isEmpty())
                    userUpdateRequest.setEmail(userEmail);

                settingViewModel.updateUser(userUpdateRequest);
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return settingFragment;
    }
    private void selectPhoto() {
        final CharSequence[] options = {"Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Upload Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Choose from Gallery")) {


                    if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
                    } else {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        // intent.setType("image/*");
                        startActivityForResult(intent, PHOTO_SELECTED);
                    }

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
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

    private void getUserData()
    {
        settingViewModel.getUserData();
        settingViewModel.mutableLiveDataUser.observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                firstName.setText(user.getFirstName());
                lastName.setText(user.getLastName());
                email.setText(user.getEmail());
                phone.setText(user.getPhoneNumber());
                if (user.getPhotoUrl() != null && !user.getPhotoUrl().isEmpty()){
                    Glide.with(requireContext())
                            .load(user.getPhotoUrl())
                            .fitCenter()
                            .placeholder(R.drawable.ic_baseline_account_circle_24)
                            .error(R.drawable.ic_baseline_account_circle_24)
                            .into(userImageView);
                }
            }
        });
/*
        settingViewModel.mutableLiveDataUploadedFiles.observe(getViewLifecycleOwner(), new Observer<UploadedFilesResponse>() {
            @Override
            public void onChanged(UploadedFilesResponse uploadedFilesResponse) {
                if(uploadedFilesResponse != null && uploadedFilesResponse.getFilesUrls().size() > 0){
                    if(!uploadedFilesResponse.getFilesUrls().get(0).isEmpty()){
                        Glide.with(requireContext())
                                .load(uploadedFilesResponse.getFilesUrls().get(0))
                                .fitCenter()
                                .placeholder(R.drawable.ic_baseline_account_circle_24)
                                .error(R.drawable.ic_baseline_account_circle_24)
                                .into(userImageView);
                    }
                }
            }
        });

        settingViewModel.error.observe(getViewLifecycleOwner(), s -> {
            Toast.makeText(requireContext(), s, Toast.LENGTH_LONG).show();
        });

 */
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == PHOTO_SELECTED && data != null){
            Uri selectedImage = data.getData();
            String filePath = UploadUtils.getPath(selectedImage, requireContext());
            String mimeType = requireActivity().getContentResolver().getType(selectedImage);
            File userPhotoFile = new File(selectedImage.getPath());
            Log.d("upload: picUri", selectedImage.toString());
            Log.d("upload: filePath", filePath);
            Log.d("upload: contetType", mimeType);
            // settingViewModel.uploadUserProfilePhoto(userPhotoFile, mimeType);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PICK_FROM_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // intent.setType("image/*");
                startActivityForResult(intent, PHOTO_SELECTED);
            } else {
                Toast.makeText(requireContext(), "You have to grant permissions to open the gallery", Toast.LENGTH_LONG).show();
            }
        }
    }
    private  void redirect()
    {

        Intent intent = new Intent(getContext(), SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }
}