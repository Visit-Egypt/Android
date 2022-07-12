package com.visitegypt.presentation.Reward;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.domain.model.Reward;
import com.visitegypt.domain.repository.UserRepository;

import java.util.List;

public class RewardViewModel extends ViewModel {
    MutableLiveData<List<Reward>> mutableLiveDataRewards = new MutableLiveData<>();
    private UserRepository userRepository;

    public RewardViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void getAllRewards() {
        // TODO
    }
}
