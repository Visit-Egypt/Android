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

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.visitegypt.R;
import com.visitegypt.domain.model.Badge;
import com.visitegypt.presentation.gamification.BadgesSliderViewAdapter;
import com.visitegypt.presentation.gamification.CitiesActivity;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AccountFragment extends Fragment {

    private static final String TAG = "Account Fragment";

    private TextView userName, country;
    private TextView likesNumberTextView, followingNumberTextView, followersNumberTextView;
    private TextView postNameTextView, postDateTextView, postCaptionTextView, postImageView;
    private AccountViewModel accountViewModel;
    private Button gamificationStartPlayingButton;
    private CircularImageView circularAccountImageView;
    private RecyclerView badgesRecyclerView;
    private BadgesSliderViewAdapter badgesSliderViewAdapter;
    private ArrayList<Badge> userBadges;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View accountView = inflater.inflate(R.layout.fragment_account, container, false);
        initViews(accountView);
        initViewModel();
        // TODO fillData();
        return accountView;
    }

    private void initViews(View accountView) {
        userName = accountView.findViewById(R.id.nameTextView);
        country = accountView.findViewById(R.id.countryTextView);
        likesNumberTextView = accountView.findViewById(R.id.likesNumberTextView);
        followingNumberTextView = accountView.findViewById(R.id.followingNumberTextView);
        followersNumberTextView = accountView.findViewById(R.id.followersNumberTextView);
        postNameTextView = accountView.findViewById(R.id.userNamePostTextView);
        postCaptionTextView = accountView.findViewById(R.id.postCaptionTextView);
        circularAccountImageView = accountView.findViewById(R.id.circularAccountImageView);

        gamificationStartPlayingButton = accountView.findViewById(R.id.startPlayingGamificationButtonAccountFragment);
        gamificationStartPlayingButton.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), CitiesActivity.class);
            startActivity(intent);
        });

        userBadges = new ArrayList<>();
        badgesRecyclerView = accountView.findViewById(R.id.userBadgesRecyclerViewAccountFragment);
        badgesRecyclerView.setLayoutManager(new LinearLayoutManager(accountView.getContext(), RecyclerView.HORIZONTAL, false));
        badgesSliderViewAdapter = new BadgesSliderViewAdapter(userBadges, accountView.getContext());
        badgesRecyclerView.setAdapter(badgesSliderViewAdapter);
    }

    private void fillData() {
        // TODO String userLevel = GamificationRules.getTitleFromLevel(user.getLevel());
        //userName.setText(userLevel);

    }

    private void initViewModel() {
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        accountViewModel.mutableLiveDataName.observe(getViewLifecycleOwner(), s -> userName.setText(s));

        accountViewModel.getUserInformation();
        accountViewModel.mutableLiveDataMyPosts.observe(getViewLifecycleOwner(), posts -> {
            if (posts.get(0).getId() == null) {
                ;
            } else {
                postNameTextView.setText(posts.get(0).getUserName());
                postCaptionTextView.setText(posts.get(0).getCaption());
            }
        });
        accountViewModel.mutableLiveDataUserImage.observe(getViewLifecycleOwner(),s -> {
            if (s != null)
                Picasso.get().load(s).into(circularAccountImageView);
        });

        accountViewModel.getUserBadges();
        accountViewModel.badgesMutableLiveData.observe(getViewLifecycleOwner(),
                badges -> {
                    Log.d(TAG, "initViewModel: ");
                    badgesSliderViewAdapter.setBadges(badges);
                }
        );
    }
}