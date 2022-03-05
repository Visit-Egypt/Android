package com.visitegypt.presentation.item;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.domain.model.Item;
import com.visitegypt.domain.usecase.GetItemsUseCase;
import com.visitegypt.domain.usecase.GetPlaceDetailUseCase;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ItemViewModel extends ViewModel {

    private static final String TAG = "Item View Model";
    MutableLiveData<List<Item>> itemMutableLiveData = new MutableLiveData<>();
    private GetPlaceDetailUseCase getPlaceDetailUseCase;
    private GetItemsUseCase getItemsUseCase;

    @Inject
    public ItemViewModel(GetPlaceDetailUseCase getPlaceDetailUseCase, GetItemsUseCase getItemsUseCase) {
        this.getPlaceDetailUseCase = getPlaceDetailUseCase;
        this.getItemsUseCase = getItemsUseCase;

    }

}
