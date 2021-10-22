package com.visitegypt.presentation.detailplace;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.data.repository.PlaceClient;
import com.visitegypt.domain.model.Place;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailViewModel extends ViewModel {
    private static final String TAG = "Detail View Model";
    MutableLiveData<Place> mutableLiveData=new MutableLiveData<>();
String place_id;
    public void getPlace(){
        PlaceClient.getPlaceRepo().getPlace(place_id).enqueue(new Callback<Place>() {
            @Override
            public void onResponse(Call<Place> call, Response<Place> response) {
                mutableLiveData.setValue(response.body());
                
            }

            @Override
            public void onFailure(Call<Place> call, Throwable t) {

            }
        });
    }
}
