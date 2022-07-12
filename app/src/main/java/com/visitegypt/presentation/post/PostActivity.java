package com.visitegypt.presentation.post;

import static com.visitegypt.utils.Constants.REQUEST_ID_MULTIPLE_PERMISSIONS;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.imageview.ShapeableImageView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.visitegypt.R;
import com.visitegypt.domain.model.User;
import com.visitegypt.utils.Constants;
import com.visitegypt.utils.GeneralUtils;

import java.io.File;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PostActivity extends AppCompatActivity {
    private static final String TAG = "Posts Activity";

    private PostsViewModel postsViewModel;

    private File file;
    private String placeId;

    private CircularImageView userImageView;
    private EditText postTxt;
    private Button postButton;
    private ShapeableImageView  postImageView;
    private User user;
    private Uri selectedImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                placeId = null;
            } else {
                placeId = extras.getString(Constants.PLACE_ID);
            }
        } else {
            placeId = (String) savedInstanceState.getSerializable(Constants.PLACE_ID);
        }

        setContentView(R.layout.activity_post);
        setContext(this);

        initViews();
        setUserImageImageView();
        liveDataObsereve();
    }

    private void initViews() {
        postTxt = findViewById(R.id.postTextView);
        postsViewModel = new ViewModelProvider(this).get(PostsViewModel.class);
        postsViewModel.initCallBack();
        userImageView = findViewById(R.id.userImageView);
        postButton = findViewById(R.id.postButtonPostActivity);
        postImageView = findViewById(R.id.postImageView);

        postButton.setOnClickListener(view -> {
            postTxt.getText().toString();
            if (postTxt.getText().toString().isEmpty()) {
                Toast.makeText(PostActivity.this, "You can't make empty post", Toast.LENGTH_LONG).show();
            } else {
                postsViewModel.setPostData(postTxt.getText().toString(), placeId);
                postsViewModel.addPost();
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
    public void selectPhotoOnClick(View view) {
        if (checkAndRequestPermissions(PostActivity.this)) {
            chooseImage(this);

        }
    }

    private void liveDataObsereve() {
        postsViewModel.getUser();
        GeneralUtils.LiveDataUtil.observeOnce(postsViewModel.userMutableLiveData, user1 -> {
            postButton.setEnabled(true);
            user = user1;
        });
        postsViewModel.isImageUploaded.observe(this, aBoolean -> {
            if (!aBoolean)
                Toast.makeText(PostActivity.this, "Upload Failed", Toast.LENGTH_LONG).show();
            else
            {
               postImageView.setImageURI(selectedImage);
               postImageView.setVisibility(View.VISIBLE);
            }

        });
        postsViewModel.isPostUploaded.observe(this, aBoolean -> {
            if (aBoolean) {
                Toast.makeText(PostActivity.this, "Your Post is Created ", Toast.LENGTH_LONG).show();
                onBackPressed();
                postTxt.getText().clear();
                GeneralUtils.LiveDataUtil.observeOnce(postsViewModel.userMutableLiveData, newUser -> {
                    updateUserXP(newUser);
                });
            } else {
                Toast.makeText(PostActivity.this, "Please,try again ", Toast.LENGTH_LONG).show();
            }
        });


    }

    private void updateUserXP(User newUser) {
        User prevUser = user;
        user = newUser;

        GeneralUtils.showUserProgress(this,
                postTxt,
                null,
                null,
                prevUser.getXp(),
                newUser.getXp()
        );
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

    private void setUserImageImageView() {
        String url = postsViewModel.getUserImage();
        if (url != null)
            if (!url.isEmpty())
                Picasso.get().load(url).into(userImageView);
    }

}