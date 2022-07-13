package com.visitegypt.data.repository;

import com.visitegypt.data.source.remote.RetrofitService;
import com.visitegypt.domain.model.ARResponsePage;
import com.visitegypt.domain.repository.ARRepository;
import com.visitegypt.domain.repository.UserRepository;

import java.util.List;

import javax.inject.Inject;
import com.visitegypt.data.source.remote.RetrofitService;
import com.visitegypt.domain.model.Badge;
import com.visitegypt.domain.model.BadgeTask;
import com.visitegypt.domain.model.ConfirmUploadModel;
import com.visitegypt.domain.model.ConfirmUploadResponse;
import com.visitegypt.domain.model.FullBadge;
import com.visitegypt.domain.model.FullExplore;
import com.visitegypt.domain.model.FullPlaceActivity;
import com.visitegypt.domain.model.PlaceActivity;
import com.visitegypt.domain.model.Token;
import com.visitegypt.domain.model.TripMateRequest;
import com.visitegypt.domain.model.User;
import com.visitegypt.domain.model.UserUpdateRequest;
import com.visitegypt.domain.model.XPUpdate;
import com.visitegypt.domain.model.response.UploadResponse;
import com.visitegypt.domain.repository.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;

import io.reactivex.rxjava3.core.Single;

public class ARRepositoryImp implements ARRepository {

    private RetrofitService retrofitService;

    @Inject
    public ARRepositoryImp(RetrofitService retrofitService) {
        this.retrofitService = retrofitService;
    }

    @Override
    public Single<ARResponsePage> getARResponse(String userId) {
        return retrofitService.getARResponse(userId);
    }

}
