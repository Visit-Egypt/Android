package com.visitegypt.presentation.detailplace;

import android.accounts.AccountManager;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.domain.model.Item;
import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.Review;
import com.visitegypt.domain.model.response.ItemPageResponse;
import com.visitegypt.domain.usecase.GetItemsUseCase;
import com.visitegypt.domain.usecase.GetPlaceDetailUseCase;
import com.visitegypt.domain.usecase.SubmitReviewUseCase;
import com.visitegypt.utils.Error;

import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.BlockingDeque;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.functions.Consumer;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

@HiltViewModel
public class DetailViewModel extends ViewModel {
    private static final String TAG = "Detail View Model";

    MutableLiveData<Place> placesMutableLiveData = new MutableLiveData<>();
    MutableLiveData<List<Item>> itemMutableLiveData = new MutableLiveData<>();
    MutableLiveData<Boolean> reviewSuccessState = new MutableLiveData<>();

    private GetPlaceDetailUseCase getPlaceDetailUseCase;
    private GetItemsUseCase getItemsUseCase;
    private SubmitReviewUseCase submitReviewUseCase;

    @Inject
    public DetailViewModel(GetPlaceDetailUseCase getPlaceDetailUseCase, GetItemsUseCase getItemsUseCase, SubmitReviewUseCase submitReviewUseCase) {
        this.getPlaceDetailUseCase = getPlaceDetailUseCase;
        this.getItemsUseCase = getItemsUseCase;
        this.submitReviewUseCase = submitReviewUseCase;
    }

    public void getPlace(String placeId) {
        getPlaceDetailUseCase.setPlaceId(placeId);
        getPlaceDetailUseCase.execute(new Consumer<Place>() {
            @Override
            public void accept(Place place) throws Throwable {
                Log.d(TAG, "Place details retrieved!");
                placesMutableLiveData.setValue(place);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.e(TAG, "place detail retrieve failed", throwable);
            }
        });
    }

    public void getItemsByPlaceId(String placeId) {
        getItemsUseCase.setPlaceId(placeId);
        getItemsUseCase.execute(new Consumer<ItemPageResponse>() {
            @Override
            public void accept(ItemPageResponse itemPageResponse) throws Throwable {
                Log.d(TAG, "items retrieved!");
                itemMutableLiveData.setValue(itemPageResponse.getItems());
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {

                Log.e(TAG, "error retrieving items: " + throwable.getMessage());

            }
        });
    }

    public void submitReview(String placeId, Review review) {
        reviewSuccessState.setValue(false);
        submitReviewUseCase.setPlaceId(placeId);
        submitReviewUseCase.setReview(review);
        Log.d(TAG, "submitReview:  enter");
        /*submitReviewUseCase.execute(new Consumer<Void>() {
            @Override
            public void accept(Void unused) throws Throwable {
                Log.d(TAG, "Review successfully added");
                reviewSuccessState.setValue(true);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                try {
                    ResponseBody body = ((HttpException) throwable).response().errorBody();
                    JSONObject jObjectError = new JSONObject(body.string());
                    Log.d(TAG, "accept: submit error " +jObjectError.getJSONArray("errors").toString());
                    if (jObjectError.getJSONArray("errors").toString().contains("msg")) {


                    }
                } catch (Exception e) {
                    Log.d("TAG", "accept catch: " + e.toString());
                }
            }
        });*/
//        submitReviewUseCase.execute(new Consumer<Void>() {
//            @Override
//            public void accept(Void unused) throws Throwable {
//                Log.d(TAG, "accept: there's no error");
//                reviewSuccessState.setValue(true);
//
//            }
//        }, new Consumer<Throwable>() {
//            @Override
//            public void accept(Throwable throwable) throws Throwable {
//                Log.d(TAG, "accept: i found error");
//                try {
//                    ResponseBody body = ((HttpException) throwable).response().errorBody();
//                    JSONObject jObjectError = new JSONObject(body.string());
//                    Log.d(TAG, "accept: submit error " +jObjectError.getJSONArray("errors").toString());
//                    if (jObjectError.getJSONArray("errors").toString().contains("msg")) {
//
//
//                    }
//                } catch (Exception e) {
//                    Log.d("TAG", "accept catch: " + e.toString());
//                }
//            }
//        });
        submitReviewUseCase.execute(new Consumer<String>() {
            @Override
            public void accept(String s) throws Throwable {

                Log.d(TAG, "accept: error free");
                reviewSuccessState.setValue(true);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                reviewSuccessState.setValue(true);
//                Log.d(TAG, "accept: i found error");
//                try {
//                    ResponseBody body = ((HttpException) throwable).response().errorBody();
//                    JSONObject jObjectError = new JSONObject(body.string());
//                    Log.d(TAG, "accept: submit error " +jObjectError.getJSONArray("errors").toString());
//                    if (jObjectError.getJSONArray("errors").toString().contains("msg")) {
//                    }
//                } catch (Exception e) {
//                    Log.d("TAG", "accept catch: " + e.toString());
//                }
                Error error = new Error();
                String errorMsg = error.errorType(throwable);
                Log.d(TAG,"error is:"+errorMsg);
            }
        });
    }
}
