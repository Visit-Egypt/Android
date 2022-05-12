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
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.visitegypt.R;
import com.visitegypt.domain.model.Badge;
import com.visitegypt.domain.model.BadgeTask;
import com.visitegypt.presentation.gamification.BadgesSliderViewAdapter;
import com.visitegypt.presentation.gamification.CitiesActivity;
import com.visitegypt.presentation.home.parent.Home;
import com.visitegypt.presentation.tripmateRequest.TripMateRequest;
import com.visitegypt.utils.GamificationRules;
import com.visitegypt.utils.MergeObjects;

import java.text.MessageFormat;
import java.util.ArrayList;

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
    private Button gamificationStartPlayingButton,tripMateRequestsButton;
    private CircularImageView circularAccountImageView;
    private RecyclerView badgesRecyclerView;
    private BadgesSliderViewAdapter badgesSliderViewAdapter;

    private ArrayList<Badge> userBadges;
    private ArrayList<Badge> placeBadges;

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

    private void initViewModel() {
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        accountViewModel.mutableLiveDataName.observe(getViewLifecycleOwner(), s -> userName.setText(s));

        accountViewModel.getUser();
        accountViewModel.userMutableLiveData.observe(getViewLifecycleOwner(), user -> {
            Log.d(TAG, "initViewModel: filling user data... " + user.getUserId());
            int xp = user.getXp();
            Log.d(TAG, "initViewModel: user with xp: " + xp);
            int level = GamificationRules.getLevelFromXp(xp);
            Log.d(TAG, "initViewModel: level of user: " + level);
            levelTextView.setText(MessageFormat.format("LVL {0}", level));
            currentLevelTextView.setText(Integer.toString(level));
            nextLevelTextView.setText(Integer.toString(level + 1));
            xpRemainingTextView.setText((GamificationRules.getLevelXp(level + 1) - (GamificationRules.getLevelXp(level))) + "XP remaining to next level");
            xpProgressTextView.setText(GamificationRules.getLevelXp(level) + "/" + GamificationRules.getLevelXp(level + 1));
            userTitleTextView.setText(GamificationRules.getTitleFromLevel(level));
            xpLinearProgressIndicator.setMax(GamificationRules.getLevelXp(level + 1));
            xpLinearProgressIndicator.setProgress(GamificationRules.getLevelXp(level), true);
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
            //Log.d(TAG, "initViewModel: BOOM" + new Gson().toJson(placeBadges.get(1).getBadgeTasks()));
            accountViewModel.userBadgesMutableLiveData.observe(getViewLifecycleOwner(),
                    userBadges -> {
                        //Log.d(TAG, "initViewModel: BOOM" + new Gson().toJson(userBadges.get(1).getBadgeTasks()));
                        Log.d(TAG, "initViewModel: ");
                        ArrayList<Badge> realBadges = new ArrayList<>();
                        for (Badge badge : userBadges) {
                            for (Badge placeBadge : placeBadges) {
                                if (badge.getId().equals(placeBadge.getId())) {
                                    MergeObjects.MergeTwoObjects.merge(placeBadge, badge);
                                    //placeBadge.setProgress(badge.getProgress());
                                    //placeBadge.setOwned(badge.isOwned());
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
                    }

            );
        });


    }
    public void initOnClick()
    {
        tripMateRequestsButton.setOnClickListener(v -> {
            ((Home)getParentFragment().getActivity()).changeFragment(new TripMateRequest());
        });
    }
}