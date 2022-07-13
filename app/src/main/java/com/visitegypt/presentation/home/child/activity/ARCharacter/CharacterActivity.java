package com.visitegypt.presentation.home.child.activity.ARCharacter;

import static com.visitegypt.utils.Constants.REQUEST_ID_MULTIPLE_PERMISSIONS;
import static com.visitegypt.utils.UploadUtils.checkAndRequestPermissions;
import static com.visitegypt.utils.UploadUtils.getRealPathFromUri;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.visitegypt.R;
import com.visitegypt.domain.model.User;
import com.visitegypt.presentation.home.parent.Home;
import com.visitegypt.utils.GeneralUtils;
import com.visitegypt.utils.UploadUtils;

import java.io.File;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class CharacterActivity extends AppCompatActivity {
    private static final String TAG = "Character Activity";
    @Inject
    public SharedPreferences sharedPreferences;
    Boolean genderFlag;
    private MaterialButton createCharacterMaterialButton, cancelMaterialButton;
    private CharacterViewModel characterViewModel;
    private File file;
    private String placeId;
    private CircularImageView userImageView;
    private User user;
    private Uri selectedImage;
    private ImageButton uploadImageButton;
    private ShapeableImageView uploadShapeableImageView;
    private TextView nameOfUploadedTextView;
    private RadioGroup genderRadioGroup;
    private RadioButton maleRadioButton, femaleRadioButton;


    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_character);
        initViews();
        liveDataObserve();

        createCharacterMaterialButton.setOnClickListener(v -> {

            if (femaleRadioButton.isChecked())
                genderFlag = true;
            else
                genderFlag = false;
            Log.d(TAG, "onCreate: dddd");
            Toast.makeText(this, "donnneee", Toast.LENGTH_LONG).show();
//            startActivity(new Intent(this, Home.class));

            //response to third endpoint
            characterViewModel.getARResponse();

        });
        cancelMaterialButton.setOnClickListener(v -> {
            startActivity(new Intent(CharacterActivity.this, Home.class));
        });

    }

    public void initViews() {
        characterViewModel = new ViewModelProvider(this).get(CharacterViewModel.class);
        characterViewModel.initCallBack();
        UploadUtils.setContext(this);
        createCharacterMaterialButton = findViewById(R.id.createCharacterMaterialButton);
        cancelMaterialButton = findViewById(R.id.cancelMaterialButton);
        uploadImageButton = findViewById(R.id.uploadImageButton);
        nameOfUploadedTextView = findViewById(R.id.nameOfUploadedTextView);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        femaleRadioButton = findViewById(R.id.femaleRadioButton);
        maleRadioButton = findViewById(R.id.maleRadioButton);


    }

    public void uploadPhotoOnClick(View view) {
        if (checkAndRequestPermissions(CharacterActivity.this)) {
            chooseImage(this);
        }
    }

    private void liveDataObserve() {
        characterViewModel.getUser();
        GeneralUtils.LiveDataUtil.observeOnce(characterViewModel.userMutableLiveData, user1 -> {
            user = user1;
        });
        characterViewModel.isImageUploaded.observe(this, aBoolean -> {
            if (!aBoolean) {
                Toast.makeText(this, "Upload Failed", Toast.LENGTH_LONG).show();
                Log.d(TAG, "liveDataObserve: aaa");
            } else {
                Log.d(TAG, "liveDataObserve: qqqqq");
                nameOfUploadedTextView.setText("Untitled Image");
                uploadImageButton.setImageURI(selectedImage);
                createCharacterMaterialButton.setEnabled(true);
            }

        });
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
                        selectedImage = data.getData();
                        if (selectedImage != null) {
                            Log.d(TAG, "  vvvvvvvvvvv  " + selectedImage.toString());
                            String filePath = getRealPathFromUri(selectedImage);
                            String mimeType = getContentResolver().getType(selectedImage);
                            if (filePath != null && !filePath.isEmpty())
                                file = new File(filePath);
                            if (file.exists() && file != null)
                                characterViewModel.uploadUserProfilePhoto(file, mimeType);
                        }

                    }
                    break;
            }
        }
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

    private void chooseImage(Context context) {

        final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit"}; // create a menuOption Array

        // create a dialog for showing the optionsMenu

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // set the items in builder

        builder.setItems(optionsMenu, (dialogInterface, i) -> {

            if (optionsMenu[i].equals("Take Photo")) {

                // Open the camera and get the photo

                Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);
            } else if (optionsMenu[i].equals("Choose from Gallery")) {

                // choose from  external storage

                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);

            } else if (optionsMenu[i].equals("Exit")) {
                dialogInterface.dismiss();
            }

        });
        builder.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
