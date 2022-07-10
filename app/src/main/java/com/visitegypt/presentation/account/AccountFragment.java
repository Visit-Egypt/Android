package com.visitegypt.presentation.account;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textview.MaterialTextView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.visitegypt.R;
import com.visitegypt.domain.model.Badge;
import com.visitegypt.domain.model.FullBadge;
import com.visitegypt.domain.model.Tag;
import com.visitegypt.presentation.badges.BadgeActivity;
import com.visitegypt.presentation.callBacks.OnFilterUpdate;
import com.visitegypt.presentation.gamification.BadgesSliderViewAdapter;
import com.visitegypt.presentation.gamification.CitiesActivity;
import com.visitegypt.presentation.gamification.UserTitlesRecyclerViewAdapter;
import com.visitegypt.presentation.home.parent.Home;
import com.visitegypt.presentation.tripmateRequest.TripMateRequest;
import com.visitegypt.utils.Chips;
import com.visitegypt.utils.GamificationRules;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AccountFragment extends Fragment implements OnFilterUpdate {

    private static final String TAG = "Account Fragment";

    private TextView nameTextView, titleTextViewAccountFragment, userName, country;
    private TextView likesNumberTextView, followingNumberTextView, followersNumberTextView;
    private TextView levelTextView, currentLevelTextView, nextLevelTextView,
            xpRemainingTextView, xpProgressTextView;
    private Chip userTitleChipView;
    private LinearProgressIndicator xpLinearProgressIndicator;
    private AccountViewModel accountViewModel;
    private Button gamificationStartPlayingButton, tripMateRequestsButton, seeAllBadgesButton;
    private CircularImageView circularAccountImageView, changeInterestIcon;
    private RecyclerView badgesRecyclerView;
    private BadgesSliderViewAdapter badgesSliderViewAdapter;
    private ChipGroup chipGroup;
    private ChipGroup editChipGroup;
    private View dialogLayout;
    private MaterialTextView myInterests;
    private ArrayList<Badge> userBadges, placeBadges;
    private HashSet<String> userTags, addNewTags, removedTags;
    private AlertDialog dialog;

    private int level;

    private PostsRecyclerViewAdapter postsRecyclerViewAdapter;
    private RecyclerView postsRecyclerView;
    private MaterialTextView noPostsMaterialTextView;

    private ImageView profileFrameImageView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View accountView = inflater.inflate(R.layout.fragment_account, container, false);
        initViews(accountView);
        initViewModel();
        initOnClick();
        return accountView;
    }

    private void initViews(View accountView) {
        userName = accountView.findViewById(R.id.nameTextView);
        country = accountView.findViewById(R.id.titleChipViewAccountFragment);
        nameTextView = accountView.findViewById(R.id.nameTextView);
        titleTextViewAccountFragment = accountView.findViewById(R.id.titleTextViewAccountFragment);
        likesNumberTextView = accountView.findViewById(R.id.likesNumberTextView);
        followingNumberTextView = accountView.findViewById(R.id.followingNumberTextView);
        followersNumberTextView = accountView.findViewById(R.id.followersNumberTextView);
        circularAccountImageView = accountView.findViewById(R.id.circularAccountImageView);
        tripMateRequestsButton = accountView.findViewById(R.id.tripMateRequestsAccountFragmentButton);
        levelTextView = accountView.findViewById(R.id.levelTextViewAccountFragment);
        currentLevelTextView = accountView.findViewById(R.id.userLevelLinearProgressIndicationTextViewAccountFragment);
        nextLevelTextView = accountView.findViewById(R.id.nextLevelProgressIndicatorTextViewAccountFragment);
        xpProgressTextView = accountView.findViewById(R.id.xpProgressTextViewAccountFragment);
        xpRemainingTextView = accountView.findViewById(R.id.remainingXpTextViewAccountFragment);
        userTitleChipView = accountView.findViewById(R.id.titleChipViewAccountFragment);
        myInterests = accountView.findViewById(R.id.myInterests);
        changeInterestIcon = accountView.findViewById(R.id.changeInterestIcon);
        chipGroup = accountView.findViewById(R.id.chipGroup);
        Chips.setContext(getContext());
        Chips.setOnFilterUpdate(this::onFilterUpdate);
        Chips.setLayoutInflater(getLayoutInflater());
        userTags = new HashSet<>();
        addNewTags = new HashSet<>();
        removedTags = new HashSet<>();
        xpLinearProgressIndicator = accountView.findViewById(R.id.userLevelLinearProgressIndicationAccountFragment);

        gamificationStartPlayingButton = accountView.findViewById(R.id.startPlayingGamificationButtonAccountFragment);
        gamificationStartPlayingButton.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), CitiesActivity.class);
            startActivity(intent);
        });

        seeAllBadgesButton = accountView.findViewById(R.id.badgesSeeAllAccountFragmentButton);
        seeAllBadgesButton.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), BadgeActivity.class));
        });

        userBadges = new ArrayList<>();
        placeBadges = new ArrayList<>();
        badgesRecyclerView = accountView.findViewById(R.id.userBadgesRecyclerViewAccountFragment);
        badgesRecyclerView.setLayoutManager(new LinearLayoutManager(accountView.getContext(), RecyclerView.HORIZONTAL, false));
        badgesSliderViewAdapter = new BadgesSliderViewAdapter(placeBadges, accountView.getContext());
        badgesRecyclerView.setAdapter(badgesSliderViewAdapter);

        noPostsMaterialTextView = accountView.findViewById(R.id.noPostsMaterialTextView);
        postsRecyclerView = accountView.findViewById(R.id.postsRecyclerView);
        postsRecyclerViewAdapter = new PostsRecyclerViewAdapter(accountView.getContext());
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        postsRecyclerView.setAdapter(postsRecyclerViewAdapter);

        userTitleChipView.setOnClickListener(view -> {
            showTitleDialog(getContext());
        });

        profileFrameImageView = accountView.findViewById(R.id.profileFrameImageView);
    }

    private void showTitleDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_titles);

        RecyclerView recyclerView = dialog.findViewById(R.id.userTitleDialogRecyclerView);
        UserTitlesRecyclerViewAdapter userTitlesRecyclerViewAdapter = new UserTitlesRecyclerViewAdapter(GamificationRules.getAllUserTitles(level));
        recyclerView.setAdapter(userTitlesRecyclerViewAdapter);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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

        if (title.equals(GamificationRules.ALL_TITLES[0])) {
            profileFrameImageView.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.rank1, null));
        } else if (title.equals(GamificationRules.ALL_TITLES[1])) {
            profileFrameImageView.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.rank2, null));
        } else if (title.equals(GamificationRules.ALL_TITLES[2])) {
            profileFrameImageView.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.rank3, null));
        } else if (title.equals(GamificationRules.ALL_TITLES[3])) {
            profileFrameImageView.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.rank4, null));
        } else if (title.equals(GamificationRules.ALL_TITLES[4])) {
            profileFrameImageView.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.rank5, null));
        } else if (title.equals(GamificationRules.ALL_TITLES[5])) {
            profileFrameImageView.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.rank6, null));
        }
        profileFrameImageView.startAnimation(fade);

    }

    private void initViewModel() {
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        accountViewModel.userMutableLiveData.observe(getViewLifecycleOwner(), user -> {
            nameTextView.setText(user.getFirstName() + " " + user.getLastName());
            if (user.getPhotoUrl() != null) {
                Log.d(TAG, "onChanged: " + user.getPhotoUrl());
                accountViewModel.saveUserImage(user.getPhotoUrl());
                Picasso.get().load(user.getPhotoUrl()).into(circularAccountImageView);

            }
        });

        accountViewModel.getUser();
        accountViewModel.userMutableLiveData.observe(getViewLifecycleOwner(), user -> {
            Log.d(TAG, "initViewModel: filling user data... " + user.getUserId());
            setUserXp(user.getXp());
        });
        //get user posts
        accountViewModel.getPostsByUserId();

        accountViewModel.userPostsMutableLiveData.observe(getViewLifecycleOwner(), posts -> {
            if (posts.size() != 0) {
                noPostsMaterialTextView.setVisibility(View.GONE);
                Log.d(TAG, "setting posts to recycler view...");
                postsRecyclerViewAdapter.setPostsArrayList(posts);
                Log.d(TAG, "posts available  " + posts.size());
            } else {
                Log.d(TAG, "no posts available ");
            }
        });
        accountViewModel.getUserInformation();
        accountViewModel.mutableLiveDataMyPosts.observe(getViewLifecycleOwner(), posts -> {
            if (posts.get(0).getId() == null) {
                // TODO fill user activities
            }
        });
        accountViewModel.mutableLiveDataUserImage.observe(getViewLifecycleOwner(), s -> {
            if (s != null)
                if (!s.isEmpty())
                    Picasso.get().load(s).into(circularAccountImageView);
        });
        accountViewModel.mutableLiveDataUserTagNames.observe(getViewLifecycleOwner(), tags -> {
            if ((tags != null) && (tags.size() != 0)) {
                myInterests.setVisibility(View.GONE);
                for (Tag tag : tags) {
                    userTags.add(tag.getId());
                    chipGroup.addView(Chips.createChipsLabel(tag.getName()));
                }
            }
        });
        accountViewModel.mutableLiveDataAllTags.observe(getViewLifecycleOwner(), tags -> {
            showDialog(tags);

        });
        accountViewModel.mutableLiveUpdateIsDone.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                accountViewModel.mutableLiveDataAllTags.observe(getViewLifecycleOwner(), this::showDialog);
            }
        });

        accountViewModel.getUserFullBadges();
        accountViewModel.fullBadgesMutableLiveData.observe(getViewLifecycleOwner(), fullBadges -> {
            ArrayList<Badge> badges = new ArrayList<>();
            for (FullBadge fullBadge : fullBadges) {
                badges.add(GamificationRules.fullBadgeToBadge(fullBadge));
            }
            badgesSliderViewAdapter.setBadges(badges);
        });
    }

    public void initOnClick() {
        tripMateRequestsButton.setOnClickListener(v -> {
            ((Home) getParentFragment().getActivity()).changeFragment(new TripMateRequest());
        });
        changeInterestIcon.setOnClickListener(view -> {
            accountViewModel.getAllTags();

        });
    }

    private void showDialog(List<Tag> allTags) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        dialogLayout = LayoutInflater.from(getContext()).inflate(R.layout.dialog_user_interests, null);
        editChipGroup = dialogLayout.findViewById(R.id.chipGroup);
        if (dialog == null) {
            Chips.createSelectChips(allTags, userTags, editChipGroup);
            builder.setView(dialogLayout);
            dialog = builder.create();
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogLayout.findViewById(R.id.saveButton).setOnClickListener(view -> {
                accountViewModel.updateYourInterest(addNewTags, removedTags);
                dialog.dismiss();
            });
            dialog.show();
        } else {
            dialog.show();
        }
    }

    @Override
    public void onFilterUpdate(List<String> filters) {
        if (filters.size() != 0 && filters != null) {
            Log.d(TAG, "onFilterUpdate: filter tags" + filters);
            for (String filter : filters) {
                if (!userTags.contains(filter) && !addNewTags.contains(filter)) {
                    addNewTags.add(filter);
                    Log.d(TAG, "onFilterUpdate: add to following list " + addNewTags);
                }
            }
            Iterator<String> userTagItr = userTags.iterator();
            while (userTagItr.hasNext()) {
                String userTag = userTagItr.next();
                if (!filters.contains(userTag)) {
                    removedTags.add(userTag);
                    Log.d(TAG, "onFilterUpdate: add to unfollow list " + removedTags);
                }

            }
            if (addNewTags.size() != 0) {
                Iterator<String> itr = addNewTags.iterator();

                while (itr.hasNext()) {
                    String tag = itr.next();
                    if (!filters.contains(tag)) {
                        itr.remove();
                    }
                }

            }
            Iterator<String> itr = removedTags.iterator();

            while (itr.hasNext()) {
                String tag = itr.next();
                if (filters.contains(tag)) {
                    itr.remove();
                }
            }


            Log.d(TAG, "onFilterUpdate: new follow  " + addNewTags);
            Log.d(TAG, "onFilterUpdate: unfollow  " + removedTags);
        }
    }
}