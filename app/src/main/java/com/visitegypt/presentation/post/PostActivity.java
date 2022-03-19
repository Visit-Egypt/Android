package com.visitegypt.presentation.post;

import static com.visitegypt.utils.Constants.REQUEST_ID_MULTIPLE_PERMISSIONS;
import static com.visitegypt.utils.UploadUtils.checkAndRequestPermissions;
import static com.visitegypt.utils.UploadUtils.getRealPathFromUri;
import static com.visitegypt.utils.UploadUtils.setContext;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.visitegypt.R;
import com.visitegypt.domain.usecase.GetAllCitiesUseCase;
import com.visitegypt.domain.usecase.UploadUserPhotoUseCase;
import com.visitegypt.presentation.signin.SignInActivity;

import java.io.File;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PostActivity extends AppCompatActivity {
    private static final String TAG = "Posts Activity";
    PostsViewModel postsViewModel;
    private File file;
    private EditText postTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        setContext(this);
        postTxt = findViewById(R.id.postTextView);
        postsViewModel = new ViewModelProvider(this).get(PostsViewModel.class);
        postsViewModel.initCallBack();
        postsViewModel.isImageUploaded.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (!aBoolean)
                    Toast.makeText(PostActivity.this, "Upload Failed", Toast.LENGTH_LONG).show();

            }
        });
        postsViewModel.isPostUploaded.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean)
                {
                    Toast.makeText(PostActivity.this, "Your Post is Created ", Toast.LENGTH_LONG).show();
                    postTxt.getText().clear();
                }
                else
                {
                    Toast.makeText(PostActivity.this, "Please,try again ", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    public void cancelOnClick(View view) {
        onBackPressed();
    }


    /*******************************************/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void postOnClick(View view) {
        if (postTxt.getText().toString() == null || postTxt.getText().toString().isEmpty()) {
            Toast.makeText(PostActivity.this, "You can't make empty post", Toast.LENGTH_LONG).show();
        } else {
            String placeId = "616f2746b817807a7a6c7167";
            postsViewModel.setPostData(postTxt.getText().toString(), placeId);
            postsViewModel.addPost();
        }

    }

    public void selectPhotoOnClick(View view) {
        if (checkAndRequestPermissions(PostActivity.this)) {
            chooseImage(this);

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
                            String mimeType = getContentResolver().getType(selectedImage);
                            if (filePath != null && !filePath.isEmpty())
                                file = new File(filePath);
                            if (file.exists() && file != null)
                                postsViewModel.uploadUserProfilePhoto(file, mimeType);
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

        builder.setItems(optionsMenu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

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

            }
        });
        builder.show();
    }
}