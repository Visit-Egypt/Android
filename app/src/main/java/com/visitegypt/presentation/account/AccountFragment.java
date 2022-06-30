package com.visitegypt.presentation.account;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.ChipGroup;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.visitegypt.R;
import com.visitegypt.domain.model.Badge;
import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.PlaceActivity;
import com.visitegypt.domain.model.Post;
import com.visitegypt.domain.model.Tag;
import com.visitegypt.presentation.callBacks.OnFilterUpdate;
import com.visitegypt.presentation.gamification.BadgesSliderViewAdapter;
import com.visitegypt.presentation.gamification.CitiesActivity;
import com.visitegypt.presentation.home.parent.Home;
import com.visitegypt.presentation.tripmateRequest.TripMateRequest;
import com.visitegypt.utils.Chips;
import com.visitegypt.utils.GamificationRules;
import com.visitegypt.utils.GeneralUtils;
import com.visitegypt.utils.MergeObjects;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AccountFragment extends Fragment implements OnFilterUpdate {

    private static final String TAG = "Account Fragment";

    private TextView userName, country;
    private TextView likesNumberTextView, followingNumberTextView, followersNumberTextView;
    private TextView levelTextView, currentLevelTextView, nextLevelTextView,
            xpRemainingTextView, xpProgressTextView;
    private TextView userTitleTextView;
    private LinearProgressIndicator xpLinearProgressIndicator;
    private AccountViewModel accountViewModel;
    private Button gamificationStartPlayingButton, tripMateRequestsButton;
    private CircularImageView circularAccountImageView, changeInterestIcon;
    private RecyclerView badgesRecyclerView;
    private BadgesSliderViewAdapter badgesSliderViewAdapter;
    private ChipGroup chipGroup;
    private ChipGroup editChipGroup;
    private View dialogLayout;
    private MaterialTextView myInterests;
    private ArrayList<Badge> userBadges;
    private ArrayList<Badge> placeBadges;
    private HashSet<String> userTags;
    private HashSet<String> addNewTags;
    private HashSet<String> removedTags;
    private AlertDialog dialog;
    private int generatedXp;

    private PostsRecyclerViewAdapter postsRecyclerViewAdapter;
    private RecyclerView postsRecyclerView;
    private MaterialTextView noPostsMaterialTextView;


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
        country = accountView.findViewById(R.id.titleTextViewAccountFragment);
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
        userTitleTextView = accountView.findViewById(R.id.titleTextViewAccountFragment);
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

        userBadges = new ArrayList<>();
        placeBadges = new ArrayList<>();
        badgesRecyclerView = accountView.findViewById(R.id.userBadgesRecyclerViewAccountFragment);
        badgesRecyclerView.setLayoutManager(new LinearLayoutManager(accountView.getContext(), RecyclerView.HORIZONTAL, false));
        badgesSliderViewAdapter = new BadgesSliderViewAdapter(placeBadges, accountView.getContext());
        badgesRecyclerView.setAdapter(badgesSliderViewAdapter);


        noPostsMaterialTextView = accountView.findViewById(R.id.noPostsMaterialTextView);
        postsRecyclerView = accountView.findViewById(R.id.postsRecyclerView);
        postsRecyclerViewAdapter = new PostsRecyclerViewAdapter(accountView.getContext());
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(accountView.getContext()));
        postsRecyclerView.setAdapter(postsRecyclerViewAdapter);
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
                dialog.dismiss();

            }
        });
        accountViewModel.getUserBadges();
        accountViewModel.getAllBadges();
        accountViewModel.allBadgesMutableLiveData.observe(getViewLifecycleOwner(), placeBadges -> {
            this.placeBadges = placeBadges;
            GeneralUtils.LiveDataUtil.observeOnce(accountViewModel.userBadgesMutableLiveData, userBadges -> {
                GamificationRules.mergeTwoBadges(placeBadges, userBadges);

                ArrayList<Badge> progressBadges = new ArrayList<>();
                for (Badge badge : placeBadges) {
                    if (badge.getProgress() != 0) {
                        progressBadges.add(badge);
                        if (badge.isOwned()) {
                            Log.d(TAG, "initViewModel: found an owned badge" +
                                    badge.getTitle() + ": " + badge.getXp() + " xp");
                            generatedXp += badge.getXp();
                        }
                    }
                }
                badgesSliderViewAdapter.setBadges(progressBadges);
                setUserXp(generatedXp);
            });
        });

        accountViewModel.getPlaceActivitiesOfUser();
        accountViewModel.userPlaceActivityMutableLiveData.observe(getViewLifecycleOwner(), placeActivities -> {
            List<String> placeActivitiesIds = new ArrayList<>();
            for (PlaceActivity placeActivity : placeActivities) {
                placeActivitiesIds.add(placeActivity.getId());
                //generatedXp += placeActivity.getXp();
                Log.d(TAG, "initViewModel: place activity generatedXp: " + generatedXp);
            }
///////
            accountViewModel.getUserPosts();
            accountViewModel.mutableLiveDataMyPosts.observe(this, new Observer<List<Post>>() {
                @Override
                public void onChanged(List<Post> posts) {
                    if (posts.size() != 0) {
                        noPostsMaterialTextView.setVisibility(View.GONE);
                        Log.d(TAG, "posts: " + posts.get(0).toString());
                        postsRecyclerViewAdapter.setPostsArrayList(posts);
                        Log.d(TAG, "posts available");
                    } else {
                        Log.d(TAG, "no posts available ");
                    }
                }
            });


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