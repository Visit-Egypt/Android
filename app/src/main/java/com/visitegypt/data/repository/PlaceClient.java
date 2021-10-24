package com.visitegypt.data.repository;

import com.visitegypt.data.source.remote.RetrofitService;
import com.visitegypt.domain.model.Place;

import retrofit2.Call;

public class PlaceClient {
    private static PlaceClient placeClient;
    private RetrofitService retrofitService;
//    private static final String BaseUrl = "";
//    private RetrofitService retrofitService;


//    public PlaceClient() {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BaseUrl)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        retrofitService = retrofit.create(RetrofitService.class);
//
//    }
//
    public static PlaceClient getPlaceRepo() {
        if (placeClient == null)
            placeClient = new PlaceClient();
        return placeClient;
    }

    public Call<Place> getPlace(String id) {
        return retrofitService.getPlace(id);
    }
}
