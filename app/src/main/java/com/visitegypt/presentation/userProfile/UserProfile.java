package com.visitegypt.presentation.userProfile;

import static com.visitegypt.utils.Constants.CHOSEN_USER_ID;

import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.visitegypt.R;
import com.visitegypt.domain.model.TripMateRequest;
import com.visitegypt.presentation.home.HomeRecyclerViewAdapter;
import com.visitegypt.utils.Chips;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UserProfile extends Fragment {

    private UserProfileViewModel userProfileViewModel;
    private View userProfileFragment;
    private CircularImageView userImage;
    private MaterialButton btnFollow, btnRequest;
    private Dialog requestMateDialog;
    private TextView userName, followingNumberTextView, followersNumberTextView;
    private View loadingLayout;
    private ChipGroup chipGroup;
    private MaterialTextView myInterests;
    private LinearLayout userProfileLayout;
    private String id ;

    public static UserProfile newInstance() {
        return new UserProfile();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        userProfileFragment = inflater.inflate(R.layout.user_profile_fragment, container, false);
        if (savedInstanceState == null) {
            Bundle extras = this.getArguments();
            if (extras == null) {
                id = null;
            } else {
                id = extras.getString(CHOSEN_USER_ID);
            }
        } else {
            id = (String) savedInstanceState.getSerializable(CHOSEN_USER_ID);
        }
        initView();
        Chips.setContext(getContext());
        setOnClickListeners();
        return userProfileFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userProfileViewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);
        // TODO: Use the ViewModel
        userProfileViewModel.setUserId(id);
        userProfileViewModel.getUser();
        mutableLiveDataObserve();
    }

    private void initView() {
        userImage = userProfileFragment.findViewById(R.id.circularAccountImageView);
        userName = userProfileFragment.findViewById(R.id.nameTextView);
        followingNumberTextView = userProfileFragment.findViewById(R.id.followingNumberTextView);
        followersNumberTextView = userProfileFragment.findViewById(R.id.followersNumberTextView);
        btnFollow = userProfileFragment.findViewById(R.id.follow);
        loadingLayout = userProfileFragment.findViewById(R.id.loadingLayout);
        userProfileLayout = userProfileFragment.findViewById(R.id.userProfile);
        btnRequest = userProfileFragment.findViewById(R.id.request);
        myInterests = userProfileFragment.findViewById(R.id.myInterests);
        chipGroup = userProfileFragment.findViewById(R.id.chipGroup);

    }

    private void setOnClickListeners() {
        btnFollow.setOnClickListener(view -> {
            if (btnFollow.getText().equals("Follow"))
                userProfileViewModel.followUser();
            else
                userProfileViewModel.unFollow();


        });
        btnRequest.setOnClickListener(v -> {
            userProfileViewModel.setUserId(id);
            showDialog();
        });
    }

    private void mutableLiveDataObserve() {
        userProfileViewModel.mutableLiveDataUser.observe(getViewLifecycleOwner(), user -> {
            userName.setText(user.getFirstName() + " " + user.getLastName());
            if ((user.getInterests() != null) && (user.getInterests().size() != 0)) {
                myInterests.setVisibility(View.GONE);
                for (String name : user.getInterests()) {
                    chipGroup.addView(Chips.createChipsLabel(name));
                }

            } else {
                myInterests.setVisibility(View.GONE);
                List<String> myLabel = new ArrayList<>();
                myLabel.add("Travelling");
                myLabel.add("Beach");
                myLabel.add("Reda");
                myLabel.add("Reda");
                myLabel.add("Reda");
                myLabel.add("Reda");
                myLabel.add("Reda");
                myLabel.add("Reda");
                myLabel.add("Reda");
                myLabel.add("Reda");
                for (String name : myLabel) {
                    chipGroup.addView(Chips.createChipsLabel(name));
                }
            }
            if ((user.getFollowers() != null) && (user.getFollowers().size() != 0))
                followersNumberTextView.setText(String.valueOf(user.getFollowers().size()));
            if ((user.getFollowing() != null) && (user.getFollowing().size() != 0))
                followingNumberTextView.setText(String.valueOf(user.getFollowing().size()));
            if (user.getPhotoUrl() != null)
                if (!user.getPhotoUrl().isEmpty())
                    Picasso.get().load(user.getPhotoUrl()).into(userImage);

        });
        userProfileViewModel.mutableLiveDataFollow.observe(getViewLifecycleOwner(), s -> {
            if (s.equals("Error")) {
                Toast.makeText(getContext(), "Sorry,Try again", Toast.LENGTH_LONG).show();
            } else {
                btnFollow.setText("UnFollow");
                followersNumberTextView.setText(s);

            }
        });
        userProfileViewModel.mutableLiveDataUnFollow.observe(getViewLifecycleOwner(), s -> {
            if (s.equals("Error")) {
                Toast.makeText(getContext(), "Sorry,Try again", Toast.LENGTH_LONG).show();

            } else {
                btnFollow.setText("Follow");
                followersNumberTextView.setText(s);
            }

        });
        userProfileViewModel.mutableLiveDataIsFollowing.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean)
                btnFollow.setText("UnFollow");
        });
        userProfileViewModel.mutableLiveDataIsRequested.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                hideLoading();
                Toast.makeText(getContext(), "Your Request is sent successfully", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showDialog() {
        View dialogLayout = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_trip_mate, null);
        requestMateDialog = new Dialog(getContext());
        requestMateDialog.setContentView(dialogLayout);
        requestMateDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        requestMateDialog.show();
        MaterialButton requrstTripMate = requestMateDialog.findViewById(R.id.requrstTripMate);

        requrstTripMate.setOnClickListener(view -> {
            TextInputEditText titleTextInput = requestMateDialog.findViewById(R.id.titleEditText);
            String title = titleTextInput.getText().toString().trim();
            TextInputEditText descriptionTextInput = requestMateDialog.findViewById(R.id.descriptionEditText);
            String description = descriptionTextInput.getText().toString().trim();
            if (title.isEmpty()) {
                titleTextInput.setError("Title can't be empty");
            } else {
                if (description.isEmpty()) {
                    descriptionTextInput.setError("Description can't be empty");
                } else {
                    userProfileViewModel.setRequestMateBody(new TripMateRequest(title, description));
                    requestMateDialog.dismiss();
                    showLoading();
                    userProfileViewModel.requestTripMate();

                }
            }
        });
    }

    private void showLoading() {
        userProfileLayout.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {

        userProfileLayout.setVisibility(View.VISIBLE);
        loadingLayout.setVisibility(View.GONE);
    }


}