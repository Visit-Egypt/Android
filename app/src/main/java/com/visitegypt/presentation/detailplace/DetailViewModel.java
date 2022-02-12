package com.visitegypt.presentation.detailplace;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.domain.model.Item;
import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.Review;
import com.visitegypt.domain.usecase.GetItemsUseCase;
import com.visitegypt.domain.usecase.GetPlaceDetailUseCase;
import com.visitegypt.domain.usecase.SubmitReviewUseCase;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DetailViewModel extends ViewModel {
    private static final String TAG = "Detail View Model";

    MutableLiveData<Place> placesMutableLiveData = new MutableLiveData<>();
    MutableLiveData<List<Item>> itemMutableLiveData = new MutableLiveData<>();
    MutableLiveData<List<Review>> reviewMutableLiveData = new MutableLiveData<>();
    private GetPlaceDetailUseCase getPlaceDetailUseCase;
    private GetItemsUseCase getItemsUseCase;
    private SubmitReviewUseCase submitReviewUseCase;

    @Inject
    public DetailViewModel(GetPlaceDetailUseCase getPlaceDetailUseCase, GetItemsUseCase getItemsUseCase, SubmitReviewUseCase submitReviewUseCase) {
        this.getPlaceDetailUseCase = getPlaceDetailUseCase;
        this.getItemsUseCase = getItemsUseCase;
        this.submitReviewUseCase = submitReviewUseCase;
    }

    /****************************************************************************************/
    //This function is used to get the details of place by passing place ID
    public void getPlace(String placeId) {
        getPlaceDetailUseCase.setPlaceId(placeId);
        getPlaceDetailUseCase.execute(
                place -> {
                    placesMutableLiveData.setValue(place);
                }
                , throwable -> {
                    Log.e(TAG, "place detail retrieve failed", throwable);
                }
        );
    }

    /*******************************************************************************/
    //This function is used to get place items
    public void getItemsByPlaceId(String placeId) {
        getItemsUseCase.setPlaceId(placeId);
        getItemsUseCase.execute(itemPageResponse -> {
                    itemMutableLiveData.setValue(itemPageResponse.getItems());
                }
                ,
                throwable -> {
                    Log.e(TAG, "error retrieving items: " + throwable.getMessage());
                });
    }

    /*********************************************************************************/

    public void submitReview(String placeId, Review review) {
        submitReviewUseCase.setPlaceId(placeId);
        submitReviewUseCase.setReview(review);
        submitReviewUseCase.execute(reviews -> {
            Log.d(TAG, "submitReview: Review is submitted");
            reviewMutableLiveData.setValue(reviews);

        },throwable -> {
            Log.d(TAG, "submitReview: "+ throwable.getMessage());
        });

    }


}