package com.visitegypt.presentation.home.child.activity.ARCharacter;

import static com.visitegypt.utils.Constants.REQUEST_ID_MULTIPLE_PERMISSIONS;
import static com.visitegypt.utils.Constants.SHARED_PREF_USER_ID;
import static com.visitegypt.utils.UploadUtils.checkAndRequestPermissions;
import static com.visitegypt.utils.UploadUtils.getRealPathFromUri;

import android.Manifest;
import android.app.DownloadManager;
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
    private String genderFlag = "male";
    private DownloadManager downloadManager;
    private MaterialButton createCharacterMaterialButton, cancelMaterialButton, test, testt;
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
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        liveDataObserve();
        maleRadioButton.setOnClickListener(v -> genderFlag = "male");
        femaleRadioButton.setOnClickListener(v -> genderFlag = "female");
        createCharacterMaterialButton.setOnClickListener(v -> {
            characterViewModel.getARResponse();
            startActivity(new Intent(this, Home.class));

        });
        cancelMaterialButton.setOnClickListener(v -> {
            startActivity(new Intent(this, Home.class));
        });

    }

    public void initViews() {
        characterViewModel = new ViewModelProvider(this).get(CharacterViewModel.class);
        characterViewModel.initCallBack();
        UploadUtils.setContext(this);
        createCharacterMaterialButton = findViewById(R.id.createCharacterMaterialButton);
        cancelMaterialButton = findViewById(R.id.cancelMaterialButton);
        test = findViewById(R.id.test);
        testt = findViewById(R.id.testt);
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
                GeneralUtils.showSnackError(this, cancelMaterialButton, "Failed to upload image");
            } else {
                nameOfUploadedTextView.setText("Custom Image");
                uploadImageButton.setImageURI(selectedImage);
                createCharacterMaterialButton.setEnabled(true);
                GeneralUtils.showSnackInfo(this, cancelMaterialButton, "Image successfully uploaded");
            }

        });
        characterViewModel.arMTLFilesMutableLiveData.observe(this, v -> {
//            Log.d(TAG, "liveDataObserve:arMTLFilesMutableLiveData " + v);
            if (v != null) {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(v));
                File arFile = new File("/storage/emulated/0/Android/data/com.visitegypt/files", sharedPreferences.getString(SHARED_PREF_USER_ID, "") + ".mtl");
                if (arFile.exists()) {
                    Log.d(TAG, "arMTLFilesMutableLiveData: file exist");
                    boolean deleted = arFile.delete();
                }
                try {
                    request.setDestinationInExternalFilesDir(this, File.separator, sharedPreferences.getString(SHARED_PREF_USER_ID, "") + ".mtl");
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    Long reference = downloadManager.enqueue(request);
                } catch (Exception e) {
                    Log.e(TAG, "arMTLFilesMutableLiveData: " + e.getMessage());
                }

            }

        });
        characterViewModel.arPNGFilesMutableLiveData.observe(this, v -> {
            if (!v.equals("null")) {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(v));
                File arFile = new File("/storage/emulated/0/Android/data/com.visitegypt/files", sharedPreferences.getString(SHARED_PREF_USER_ID, "") + ".texture.png");
                if (arFile.exists()) {
                    Log.d(TAG, "arPNGFilesMutableLiveData: file exist");
                    boolean deleted = arFile.delete();
                }
                Log.d(TAG, "arPNGFilesMutableLiveData: file isn't exist");
                try {
                    request.setDestinationInExternalFilesDir(this, File.separator, sharedPreferences.getString(SHARED_PREF_USER_ID, "") + ".texture.png");
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    Long reference = downloadManager.enqueue(request);
                } catch (Exception e) {
                    Log.e(TAG, "arPNGFilesMutableLiveData: " + e.getMessage());
                }
            }

        });
        characterViewModel.arOBJFilesMutableLiveData.observe(this, v -> {
//            Log.d(TAG, "liveDataObserve:arOBJFilesMutableLiveData " + v);
            if (!v.toString().equals("null")) {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(v));
                File arFile = new File("/storage/emulated/0/Android/data/com.visitegypt/files", sharedPreferences.getString(SHARED_PREF_USER_ID, "") + "-" + genderFlag + ".obj");
                if (arFile.exists()) {
                    Log.d(TAG, "arOBJFilesMutableLiveData: file exist");
                    boolean deleted = arFile.delete();
                }
                try {
                    request.setDestinationInExternalFilesDir(this, File.separator, sharedPreferences.getString(SHARED_PREF_USER_ID, "") + "-" + genderFlag + ".obj");
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    Long reference = downloadManager.enqueue(request);
                } catch (Exception e) {
                    Log.e(TAG, "arOBJFilesMutableLiveData: " + e.getMessage());
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                case 1:
                    Log.d(TAG, "onActivityResult: image on activity result called");
                    if (resultCode == RESULT_OK && data != null) {
                        selectedImage = data.getData();
                        if (selectedImage != null) {
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
                    Toast.makeText(this, "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT).show();

                } else if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "FlagUp Requires Access to Your Storage.", Toast.LENGTH_SHORT).show();

                } else {
                    chooseImage(this);
                }
                break;
        }
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
    public void onBackPressed() {
        super.onBackPressed();
    }
}
