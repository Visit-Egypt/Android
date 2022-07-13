package com.visitegypt.presentation.badges;

import static com.visitegypt.utils.GeneralUtils.LiveDataUtil.observeOnce;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.visitegypt.R;
import com.visitegypt.domain.model.Badge;
import com.visitegypt.domain.model.BadgeTask;
import com.visitegypt.domain.model.FullBadge;
import com.visitegypt.presentation.gamification.BadgesSliderViewAdapter;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class BadgeActivity extends AppCompatActivity {

    private static final String TAG = "badges activity";

    private BadgesSliderViewAdapter badgesSliderViewAdapter;
    private RecyclerView badgesRecyclerView;
    private ProgressBar progressBar;

    private ArrayList<Badge> badgesArrayList;

    private BadgesActivityViewModel badgesActivityViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badge);

        initViews();
        initViewModel();
    }

    private void initViewModel() {
        badgesActivityViewModel = new ViewModelProvider(this).get(BadgesActivityViewModel.class);
        badgesActivityViewModel.getBadges();
        badgesActivityViewModel.getFullBadges();

        observeOnce(badgesActivityViewModel.badgesMutableLiveData, badges -> {
            badgesArrayList.clear();
            observeOnce(badgesActivityViewModel.fullBadgesMutableLiveData, fullBadges -> {
                if (fullBadges != null) {
                    for (FullBadge fullBadge : fullBadges) {
                        for (Badge badge : badges) {
                            if (fullBadge.getBadge().getId().equals(badge.getId())) {
                                badge.setProgress(fullBadge.getProgress());
                                badge.setOwned(fullBadge.isOwned());
                                badge.setPinned(fullBadge.isPinned());
                                for (BadgeTask badgeTask : badge.getBadgeTasks()) {
                                    for (BadgeTask fullBadgeTask : fullBadge.getBadgeTasks()) {
                                        if (badgeTask.getTaskTitle().equals(fullBadgeTask.getTaskTitle())) {
                                            badgeTask.setProgress(fullBadgeTask.getProgress());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Log.d(TAG, "initViewModel: finished: " + new Gson().toJson(badges));
                badgesArrayList.addAll(badges);
                badgesSliderViewAdapter.setBadges((ArrayList<Badge>) badges);
                progressBar.setVisibility(View.GONE);
            });
        });

    }

    private void initViews() {
        badgesArrayList = new ArrayList<>();
        badgesRecyclerView = findViewById(R.id.allBadgesActivityRecyclerView);
        badgesRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        badgesSliderViewAdapter = new BadgesSliderViewAdapter(badgesArrayList, this);
        badgesRecyclerView.setAdapter(badgesSliderViewAdapter);
        progressBar = findViewById(R.id.badgeActivityProgressBar);
    }
}