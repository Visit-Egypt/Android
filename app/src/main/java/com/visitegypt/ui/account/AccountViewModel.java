package com.visitegypt.ui.account;

import static com.visitegypt.utils.Constants.SHARED_PREF_FIRST_NAME;

import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.data.repository.PostRepositoryImp;
import com.visitegypt.domain.model.Post;
import com.visitegypt.domain.usecase.GetPostUseCase;
import com.visitegypt.domain.usecase.GetPostsByUser;
import com.visitegypt.utils.Constants;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AccountViewModel extends ViewModel {
    SharedPreferences sharedPreferences;
    MutableLiveData<List<Post>> myPosts = new MutableLiveData<>();
    MutableLiveData<String> mutableLiveDataName = new MutableLiveData<>();
     GetPostsByUser getPostsByUser;
    @Inject
    public AccountViewModel(SharedPreferences sharedPreferences, GetPostsByUser getPostsByUser) {
        this.sharedPreferences = sharedPreferences;
        this.getPostsByUser = getPostsByUser;
    }

    public void getUserInformation() {
        mutableLiveDataName.setValue(sharedPreferences.getString(Constants.SHARED_PREF_FIRST_NAME, null));
        //
        getUserPosts();

    }

    private void getUserPosts() {
        //when backend finishes there work start to implement
    }

}
