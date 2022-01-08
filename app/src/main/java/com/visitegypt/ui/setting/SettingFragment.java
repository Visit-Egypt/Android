package com.visitegypt.ui.setting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.visitegypt.R;
import com.visitegypt.databinding.FragmentSettingBinding;
import com.visitegypt.domain.model.User;
import com.visitegypt.ui.account.AccountViewModel;

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
    View settingFragment;
    SettingViewModel settingViewModel;
    private static final int PHOTO_SELECTED = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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

                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, PHOTO_SELECTED);

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
        logOutButton = settingFragment.findViewById(R.id.logOutButton);
        changeImageView = settingFragment.findViewById(R.id.changeImageView);

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

            }
        });
    }

}