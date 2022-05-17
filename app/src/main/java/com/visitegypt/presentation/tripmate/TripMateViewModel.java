package com.visitegypt.presentation.tripmate;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.data.source.local.dao.TagDao;
import com.visitegypt.domain.model.Tag;
import com.visitegypt.domain.model.User;
import com.visitegypt.domain.usecase.GetAllUserTagUseCase;
import com.visitegypt.domain.usecase.GetUserUseCase;
import com.visitegypt.utils.Constants;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class TripMateViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private GetUserUseCase getUserUseCase;
    private TagDao tagDao;
    private SharedPreferences sharedPreferences;
    private GetAllUserTagUseCase getAllUserTagUseCase;
    MutableLiveData<List<Tag>> mutableLiveDataTagNames = new MutableLiveData<>();
    MutableLiveData<List<User>> mutableLiveDataUsers = new MutableLiveData<>();
    private static final String TAG = "Trip Mate View Model";

    @Inject
    public TripMateViewModel(GetUserUseCase getUserUseCase,
                             TagDao tagDao,
                             SharedPreferences sharedPreferences,
                             GetAllUserTagUseCase getAllUserTagUseCase
    ) {
        this.getUserUseCase = getUserUseCase;
        this.tagDao = tagDao;
        this.sharedPreferences = sharedPreferences;
        this.getAllUserTagUseCase = getAllUserTagUseCase;
    }

    public void getUserTags() {
        String userId = sharedPreferences.getString(Constants.SHARED_PREF_USER_ID, "");
        getUserUseCase.setUser(userId, "");
        getUserUseCase.getUserInterest()
                .subscribe(tags -> {
                    if (tags != null && tags.size() != 0) {
                        tagDao.getTagsNameByIds(tags)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe((tagList, throwable) -> {
                                    mutableLiveDataTagNames.setValue(tagList);
                                });
                    }
                }, throwable -> {

                });

    }
    public void getAllUserTag(List<String> id)
    {
        getAllUserTagUseCase.setIds(id);
        getAllUserTagUseCase.execute(users -> {
            mutableLiveDataUsers.setValue(users);
        },throwable -> {
            Log.d(TAG, "getAllUserTag: " + throwable.getMessage());
        });
    }
}