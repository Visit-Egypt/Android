package com.visitegypt.presentation.account;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.gson.Gson;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.visitegypt.R;
import com.visitegypt.domain.model.Badge;
import com.visitegypt.domain.model.BadgeTask;
import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.PlaceActivity;
import com.visitegypt.presentation.gamification.BadgesSliderViewAdapter;
import com.visitegypt.presentation.gamification.CitiesActivity;
import com.visitegypt.utils.GamificationRules;
import com.visitegypt.utils.GeneralUtils;
import com.visitegypt.utils.MergeObjects;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AccountFragment extends Fragment {

    private static final String TAG = "Account Fragment";

    private TextView userName, country;
    private TextView likesNumberTextView, followingNumberTextView, followersNumberTextView;
    private TextView levelTextView, currentLevelTextView, nextLevelTextView,
            xpRemainingTextView, xpProgressTextView;
    private TextView userTitleTextView;
    private LinearProgressIndicator xpLinearProgressIndicator;
    private AccountViewModel accountViewModel;
    private Button gamificationStartPlayingButton;
    private CircularImageView circularAccountImageView;
    private RecyclerView badgesRecyclerView;
    private BadgesSliderViewAdapter badgesSliderViewAdapter;

    private ArrayList<Badge> userBadges;
    private ArrayList<Badge> placeBadges;

    private int generatedXp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View accountView = inflater.inflate(R.layout.fragment_account, container, false);
        initViews(accountView);
        initViewModel();
        return accountView;
    }

    private void initViews(View accountView) {
        userName = accountView.findViewById(R.id.nameTextView);
        country = accountView.findViewById(R.id.titleTextViewAccountFragment);
        likesNumberTextView = accountView.findViewById(R.id.likesNumberTextView);
        followingNumberTextView = accountView.findViewById(R.id.followingNumberTextView);
        followersNumberTextView = accountView.findViewById(R.id.followersNumberTextView);
        circularAccountImageView = accountView.findViewById(R.id.circularAccountImageView);

        levelTextView = accountView.findViewById(R.id.levelTextViewAccountFragment);
        currentLevelTextView = accountView.findViewById(R.id.userLevelLinearProgressIndicationTextViewAccountFragment);
        nextLevelTextView = accountView.findViewById(R.id.nextLevelProgressIndicatorTextViewAccountFragment);
        xpProgressTextView = accountView.findViewById(R.id.xpProgressTextViewAccountFragment);
        xpRemainingTextView = accountView.findViewById(R.id.remainingXpTextViewAccountFragment);
        userTitleTextView = accountView.findViewById(R.id.titleTextViewAccountFragment);

        xpLinearProgressIndicator = accountView.findViewById(R.id.userLevelLinearProgressIndicationAccountFragment);

        gamificationStartPlayingButton = accountView.findViewById(R.id.startPlayingGamificationButtonAccountFragment);
        gamificationStartPlayingButton.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), CitiesActivity.class);
            startActivity(intent);
        });

        userBadges = new ArrayList<>();
        placeBadges = new ArrayList<>();
        badgesRecyclerView = accountView.findViewById(R.id.userBadgesRecyclerViewAccountFragment);
        badgesRecyclerView.setLayoutManager(new LinearLayoutManager(accountView.getContext(), RecyclerView.HORIZONTAL, false));
        badgesSliderViewAdapter = new BadgesSliderViewAdapter(placeBadges, accountView.getContext());
        badgesRecyclerView.setAdapter(badgesSliderViewAdapter);
    }

    private void setUserXp(int xp) {
        Log.d(TAG, "setUserXp: user with xp: " + xp);
        int level = GamificationRules.getLevelFromXp(xp);
        Log.d(TAG, "setUserXp: level of user: " + level);
        levelTextView.setText(MessageFormat.format("LVL {0}", level));
        currentLevelTextView.setText(Integer.toString(level));
        nextLevelTextView.setText(Integer.toString(level + 1));
        xpRemainingTextView.setText((GamificationRules.getLevelXp(level + 1) - xp + (GamificationRules.getLevelXp(level))) + "XP remaining to next level");
        xpProgressTextView.setText(xp - GamificationRules.getLevelXp(level) + "/" + GamificationRules.getLevelXp(level + 1));
        userTitleTextView.setText(GamificationRules.getTitleFromLevel(level));
        xpLinearProgressIndicator.setMax(GamificationRules.getLevelXp(level + 1));
        xpLinearProgressIndicator.setProgress(xp - GamificationRules.getLevelXp(level), true);
    }

    private void initViewModel() {
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        accountViewModel.mutableLiveDataName.observe(getViewLifecycleOwner(), s -> userName.setText(s));

        accountViewModel.getUser();
        accountViewModel.userMutableLiveData.observe(getViewLifecycleOwner(), user -> {
            Log.d(TAG, "initViewModel: filling user data... " + user.getUserId());
            int xp = user.getXp();
            //setUserXp(xp);
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

        accountViewModel.getUserBadges();
        accountViewModel.getAllBadges();
        accountViewModel.allBadgesMutableLiveData.observe(getViewLifecycleOwner(), placeBadges -> {
            this.placeBadges = placeBadges;
            accountViewModel.userBadgesMutableLiveData.observe(getViewLifecycleOwner(),
                    userBadges -> {
                        Log.d(TAG, "initViewModel: ");
                        ArrayList<Badge> realBadges = new ArrayList<>();
                        for (Badge badge : userBadges) {
                            Log.d(TAG, "initViewModel: badge generatedXp: " + generatedXp);
                            for (Badge placeBadge : placeBadges) {
                                if (badge.getId().equals(placeBadge.getId())) {

                                    MergeObjects.MergeTwoObjects.merge(placeBadge, badge);
                                    for (BadgeTask badgeTask : badge.getBadgeTasks()) {
                                        for (BadgeTask placeBadgeTask : placeBadge.getBadgeTasks()) {
                                            if (badgeTask.getTaskTitle().equals(placeBadgeTask.getTaskTitle())) {
                                                // same badgeTask -> merge them
                                                MergeObjects.MergeTwoObjects.merge(placeBadgeTask, badgeTask);
                                            }
                                        }
                                    }
                                    Log.d(TAG, "initViewModel: " + new Gson().toJson(placeBadge));
                                    if (placeBadge.isOwned()) {
                                        Log.d(TAG, "initViewModel: found an owned badge: " + placeBadge.getTitle());
                                        generatedXp += placeBadge.getXp();
                                        Log.d(TAG, "initViewModel: badge generatedXp: " + generatedXp);
                                    } else {
                                        Log.d(TAG, "initViewModel: not owned: " + placeBadge.getTitle());
                                    }
                                    ArrayList<BadgeTask> badgeTasks = new ArrayList<>();
                                    for (BadgeTask badgeTask : badge.getBadgeTasks()) {
                                        for (BadgeTask placeBadgeTask : placeBadge.getBadgeTasks()) {
                                            if (badgeTask.getTaskTitle().equals(placeBadgeTask.getTaskTitle())) {
                                                MergeObjects.MergeTwoObjects.merge(placeBadgeTask, badgeTask);
                                                badgeTasks.add(placeBadgeTask);
                                            }
                                        }
                                    }
                                    placeBadge.setBadgeTasks(badgeTasks);
                                    realBadges.add(placeBadge);
                                }
                            }
                        }
                        badgesSliderViewAdapter.setBadges(realBadges);
                        setUserXp(generatedXp);
                    }
            );
        });

        accountViewModel.getPlaceActivitiesOfUser();
        accountViewModel.userPlaceActivityMutableLiveData.observe(getViewLifecycleOwner(), placeActivities -> {
            List<String> placeActivitiesIds = new ArrayList<>();
            for (PlaceActivity placeActivity : placeActivities) {
                placeActivitiesIds.add(placeActivity.getId());
                //generatedXp += placeActivity.getXp();
                Log.d(TAG, "initViewModel: place activity generatedXp: " + generatedXp);
            }
            accountViewModel.setPlaceActivitiesId(placeActivitiesIds);
            accountViewModel.getPlacesByPlaceActivities();
            GeneralUtils.LiveDataUtil.observeOnce(accountViewModel.placesWithNeededPlaceActivities, places -> {
                Log.d(TAG, "initViewModel: finding the actual place activities");
                List<PlaceActivity> placeActivityList = new ArrayList<>();
                for (Place place : places) {
                    for (PlaceActivity placeActivity : place.getPlaceActivities()) {
                        Log.d(TAG, "initViewModel: " + new Gson().toJson(placeActivity));

                        for (PlaceActivity userPlaceActivity : placeActivities) {
                            if (userPlaceActivity.getId().equals(placeActivity.getId())) {
                                MergeObjects.MergeTwoObjects.merge(placeActivity, userPlaceActivity);
                                if (placeActivity.isFinished()) {
                                    Log.d(TAG, "initViewModel: " + new Gson().toJson(placeActivity));
                                    generatedXp += placeActivity.getXp();
                                }
                                placeActivityList.add(placeActivity);
                            }
                        }
                        Log.d(TAG, "initViewModel: place activity generatedXp: " + generatedXp);
                    }
                }
                setUserXp(generatedXp);
            });

        });


    }
}