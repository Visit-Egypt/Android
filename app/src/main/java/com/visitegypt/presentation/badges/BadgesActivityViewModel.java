package com.visitegypt.presentation.badges;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.domain.model.Badge;
import com.visitegypt.domain.model.FullBadge;
import com.visitegypt.domain.model.User;
import com.visitegypt.domain.usecase.GetAllBadgesUseCase;
import com.visitegypt.domain.usecase.GetFullBadgeUseCase;
import com.visitegypt.domain.usecase.GetUserUseCase;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class BadgesActivityViewModel extends ViewModel {

    private static final String TAG = "badges viewModel";
    MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
    MutableLiveData<List<Badge>> badgesMutableLiveData = new MutableLiveData<>();
    MutableLiveData<List<FullBadge>> fullBadgesMutableLiveData = new MutableLiveData<>();
    private GetAllBadgesUseCase getAllBadgesUseCase;
    private GetFullBadgeUseCase getFullBadgeUseCase;
    private GetUserUseCase getUserUseCase;
    private SharedPreferences sharedPreferences;

    @Inject
    public BadgesActivityViewModel(GetAllBadgesUseCase getAllBadgesUseCase,
                                   GetFullBadgeUseCase getFullBadgeUseCase,
                                   GetUserUseCase getUserUseCase,
                                   SharedPreferences sharedPreferences) {
        this.getAllBadgesUseCase = getAllBadgesUseCase;
        this.getFullBadgeUseCase = getFullBadgeUseCase;
        this.getUserUseCase = getUserUseCase;
        this.sharedPreferences = sharedPreferences;
    }

    public void getBadges() {
        getAllBadgesUseCase.execute(badgeResponse -> {
            badgesMutableLiveData.setValue(badgeResponse.getBadges());
        }, throwable -> {
            badgesMutableLiveData.setValue(null);
            Log.e(TAG, "getBadges: ", throwable);
        });
    }

    public void getFullBadges() {
        getFullBadgeUseCase.execute(fullBadges -> {
            fullBadgesMutableLiveData.setValue(fullBadges);
        }, throwable -> {
            fullBadgesMutableLiveData.setValue(null);
            Log.e(TAG, "getFullBadges: ", throwable);
        });
    }
}
