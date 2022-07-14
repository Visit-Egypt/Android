package com.visitegypt.presentation.userProfile;

import static com.visitegypt.utils.Constants.CHOSEN_USER_ID;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.visitegypt.R;
import com.visitegypt.domain.model.Badge;
import com.visitegypt.domain.model.FullBadge;
import com.visitegypt.domain.model.Tag;
import com.visitegypt.domain.model.TripMateRequest;
import com.visitegypt.presentation.account.PostsRecyclerViewAdapter;
import com.visitegypt.presentation.gamification.BadgesSliderViewAdapter;
import com.visitegypt.utils.Chips;
import com.visitegypt.utils.GamificationRules;

import java.text.MessageFormat;
import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UserProfile extends Fragment {

    private static final String TAG = "User Profile";
    private UserProfileViewModel userProfileViewModel;
    private View userProfileFragment;
    private CircularImageView userImage;
    private MaterialButton btnFollow, btnRequest;
    private LinearProgressIndicator xpLinearProgressIndicator;
    private Dialog requestMateDialog;
    private BadgesSliderViewAdapter badgesSliderViewAdapter;
    private TextView userName, followingNumberTextView, followersNumberTextView;
    private RecyclerView badgesRecyclerView;
    private View loadingLayout;
    private ChipGroup chipGroup;
    private MaterialTextView myInterests;
    private LinearLayout userProfileLayout;
    private ArrayList<Badge> userBadges, placeBadges;
    private String id;
    private MaterialTextView noPostsMaterialTextView;
    private ImageView profileFrameImageView;
    private TextView levelTextView, currentLevelTextView, nextLevelTextView,
            xpRemainingTextView, xpProgressTextView;
    private Chip userTitleChipView;
    private PostsRecyclerViewAdapter postsRecyclerViewAdapter;
    private RecyclerView postsRecyclerView;
    private int level;

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
        userProfileViewModel.getPostsByUserId(id);
        userProfileViewModel.getUserFullBadges();
        mutableLiveDataObserve();
    }

    private void initView() {
        userImage = userProfileFragment.findViewById(R.id.circularAccountImageView);
        userName = userProfileFragment.findViewById(R.id.nameTextView);
        followingNumberTextView = userProfileFragment.findViewById(R.id.followingNumberTextView);
        followersNumberTextView = userProfileFragment.findViewById(R.id.followersNumberTextView);
        btnFollow = userProfileFragment.findViewById(R.id.followMaterialButton);
        loadingLayout = userProfileFragment.findViewById(R.id.loadingLayout);
        userProfileLayout = userProfileFragment.findViewById(R.id.userProfile);
        btnRequest = userProfileFragment.findViewById(R.id.requestMaterialButton);
        myInterests = userProfileFragment.findViewById(R.id.myInterests);
        chipGroup = userProfileFragment.findViewById(R.id.chipGroup);
        profileFrameImageView = userProfileFragment.findViewById(R.id.profileFrameImageView);
        levelTextView = userProfileFragment.findViewById(R.id.levelTextViewAccountFragment);
        currentLevelTextView = userProfileFragment.findViewById(R.id.userLevelLinearProgressIndicationTextViewAccountFragment);
        nextLevelTextView = userProfileFragment.findViewById(R.id.nextLevelProgressIndicatorTextViewAccountFragment);
        xpProgressTextView = userProfileFragment.findViewById(R.id.xpProgressTextViewAccountFragment);
        xpRemainingTextView = userProfileFragment.findViewById(R.id.remainingXpTextViewAccountFragment);
        noPostsMaterialTextView = userProfileFragment.findViewById(R.id.noPostsMaterialTextView);
        userTitleChipView = userProfileFragment.findViewById(R.id.titleChipViewAccountFragment);
        xpLinearProgressIndicator = userProfileFragment.findViewById(R.id.userLevelLinearProgressIndicationAccountFragment);
        placeBadges = new ArrayList<>();
        badgesRecyclerView = userProfileFragment.findViewById(R.id.userBadgesRecyclerViewAccountFragment);
        badgesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        badgesSliderViewAdapter = new BadgesSliderViewAdapter(placeBadges, userProfileFragment.getContext());
        badgesRecyclerView.setAdapter(badgesSliderViewAdapter);
        postsRecyclerView = userProfileFragment.findViewById(R.id.postsRecyclerView);
        postsRecyclerViewAdapter = new PostsRecyclerViewAdapter(getContext());
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        postsRecyclerView.setAdapter(postsRecyclerViewAdapter);
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
            setUserXp(user.getXp());
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
        userProfileViewModel.mutableLiveDataTagNames.observe(getViewLifecycleOwner(), tags -> {
            if ((tags != null) && (tags.size() != 0)) {
                myInterests.setVisibility(View.GONE);
                for (Tag tag : tags) {
                    chipGroup.addView(Chips.createChipsLabel(tag.getName()));
                }
            }
        });

        userProfileViewModel.fullBadgesMutableLiveData.observe(getViewLifecycleOwner(), fullBadges -> {
            ArrayList<Badge> badges = new ArrayList<>();
            for (FullBadge fullBadge : fullBadges) {
                badges.add(GamificationRules.fullBadgeToBadge(fullBadge));
            }
            badgesSliderViewAdapter.setBadges(badges);
        });
        userProfileViewModel.userPostsMutableLiveData.observe(getViewLifecycleOwner(), posts -> {
            if (posts.size() != 0) {
                noPostsMaterialTextView.setVisibility(View.GONE);
                Log.d(TAG, "setting posts to recycler view...");
                postsRecyclerViewAdapter.setPostsArrayList(posts);
                Log.d(TAG, "posts available  " + posts.size());
            } else {
                Log.d(TAG, "no posts available ");
            }
        });
    }

    private void showDialog() {
        View dialogLayout = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_trip_mate, null);
        requestMateDialog = new Dialog(getContext());
        requestMateDialog.setContentView(dialogLayout);
        requestMateDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        requestMateDialog.show();
        MaterialButton requrstTripMate = requestMateDialog.findViewById(R.id.requestTripMate);

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

    private void setUserXp(int xp) {
        Log.d(TAG, "setUserXp: user with xp: " + xp);
        level = GamificationRules.getLevelFromXp(xp);
        Log.d(TAG, "setUserXp: level of user: " + level);

        levelTextView.setText(MessageFormat.format("LVL {0}", level));
        currentLevelTextView.setText(Integer.toString(level));
        nextLevelTextView.setText(Integer.toString(level + 1));

        String title = GamificationRules.getTitleFromLevel(level);

        xpRemainingTextView.setText(GamificationRules.getRemainingXPToNextLevel(xp) + "XP remaining to next level");
        xpProgressTextView.setText(xp - GamificationRules.getLevelXp(level) + "/" +
                ((GamificationRules.getLevelXp(level + 1)) - GamificationRules.getLevelXp(level)));
        userTitleChipView.setText(title);
        xpLinearProgressIndicator.setMax(((GamificationRules.getLevelXp(level + 1)) - GamificationRules.getLevelXp(level)));
        xpLinearProgressIndicator.setProgress(xp - GamificationRules.getLevelXp(level), true);

        Animation fade = new AlphaAnimation(0, 1);
        fade.setDuration(500);
        fade.setInterpolator(new DecelerateInterpolator());

        profileFrameImageView.setBackground(GamificationRules.getProfileFrameDrawable(getActivity(), title));
//
//        if (title.equals(GamificationRules.ALL_TITLES[0])) {
//            profileFrameImageView.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.rank1, null));
//        } else if (title.equals(GamificationRules.ALL_TITLES[1])) {
//            profileFrameImageView.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.rank2, null));
//        } else if (title.equals(GamificationRules.ALL_TITLES[2])) {
//            profileFrameImageView.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.rank3, null));
//        } else if (title.equals(GamificationRules.ALL_TITLES[3])) {
//            profileFrameImageView.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.rank4, null));
//        } else if (title.equals(GamificationRules.ALL_TITLES[4])) {
//            profileFrameImageView.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.rank5, null));
//        } else if (title.equals(GamificationRules.ALL_TITLES[5])) {
//            profileFrameImageView.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.rank6, null));
//        }
        profileFrameImageView.startAnimation(fade);

    }


}