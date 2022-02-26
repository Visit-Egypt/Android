package com.visitegypt.presentation.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.visitegypt.R;
import com.visitegypt.domain.model.Post;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AccountFragment extends Fragment {
    TextView userName, country;
    TextView likesNumberTextView, followingNumberTextView, followersNumberTextView;
    TextView postNameTextView, postDateTextView, postCaptionTextView, postImageView;
    AccountViewModel accountViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View accountView = inflater.inflate(R.layout.fragment_account, container, false);
        userName = accountView.findViewById(R.id.nameTextView);
        country = accountView.findViewById(R.id.countryTextView);
        likesNumberTextView = accountView.findViewById(R.id.likesNumberTextView);
        followingNumberTextView = accountView.findViewById(R.id.followingNumberTextView);
        followersNumberTextView = accountView.findViewById(R.id.followersNumberTextView);
        postNameTextView = accountView.findViewById(R.id.postNameTextView);
        postCaptionTextView = accountView.findViewById(R.id.postCaptionTextView);


        accountViewModel =
                new ViewModelProvider(this).get(AccountViewModel.class);
        //in this section we will get these data from db
        country.setText("UK");
        likesNumberTextView.setText("100");
        followingNumberTextView.setText("20");
        followersNumberTextView.setText("100");
        accountViewModel.getUserInformation();
        accountViewModel.mutableLiveDataName.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                userName.setText(s);
            }
        });
        accountViewModel.mutableLiveDataMyPosts.observe(getViewLifecycleOwner(), new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                if (posts.get(0).getId() == null) {
                    ;
                } else {
                    postNameTextView.setText(posts.get(0).getUserName());
                    postCaptionTextView.setText(posts.get(0).getCaption());
                }
            }
        });


        return accountView;
    }
}