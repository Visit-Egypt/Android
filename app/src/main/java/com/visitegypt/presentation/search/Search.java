package com.visitegypt.presentation.search;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.visitegypt.R;
import com.visitegypt.domain.model.SearchPlace;
import com.visitegypt.presentation.home.parent.SearchRecyclerViewAdapter;
import com.visitegypt.presentation.home.parent.SearchViewModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Search extends AppCompatActivity {
    private SearchViewModel searchViewModel;
    private TextInputLayout txtSearch;
    private RecyclerView recyclerView;
    private ArrayList<SearchPlace> searchPlaces;
    private SearchRecyclerViewAdapter searchRecyclerViewAdapter;
    private static final String TAG = "Search";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initViews();
        searchViewModel.webSocketConnet();
        startSearch();
        searchResult();
    }

    private void searchResult() {
//        searchViewModel.mutableLiveDataText.observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(String s) {
//                Log.d(TAG, "Web sockets Test onChanged: " + s);
//                if (!s.contains("errors")) {
//                    Gson gson = new Gson();
//                    Type listType = new TypeToken<List<SearchPlace>>() {
//                    }.getType();
//                    searchPlaces = gson.fromJson(s, listType);
//                    searchRecyclerViewAdapter.updatePlacesList(searchPlaces);
//                }
//                if (txtSearch.getEditText().getText().toString().isEmpty())
//                {
//                    searchPlaces.clear();
//                    searchRecyclerViewAdapter.updatePlacesList(searchPlaces);
//                }
//
//
//            }
//        });
    }

    private void startSearch() {
        txtSearch.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!txtSearch.getEditText().getText().toString().isEmpty())
                    searchViewModel.search(txtSearch.getEditText().getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void initViews() {
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
//        txtSearch = findViewById(R.id.search);
        recyclerView = findViewById(R.id.searchRecyclerView);
        searchRecyclerViewAdapter = new SearchRecyclerViewAdapter(searchPlaces, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(searchRecyclerViewAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}