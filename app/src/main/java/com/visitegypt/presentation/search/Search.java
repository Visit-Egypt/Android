package com.visitegypt.presentation.search;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.visitegypt.R;
import com.visitegypt.domain.model.SearchPlace;
import com.visitegypt.domain.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Search extends AppCompatActivity {
    private SearchViewModel searchViewModel;
    private TextInputEditText text;
    private static final String TAG = "Search";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        TextInputLayout txtEmail = findViewById(R.id.emailTextView);
        txtEmail.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!txtEmail.getEditText().getText().toString().isEmpty())
                searchViewModel.search(txtEmail.getEditText().getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        searchViewModel.start();
        searchViewModel.mutableLiveDataText.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String  s) {
                Log.d(TAG, "Web sockets Test onChanged: "+s);
                //String userJson = "{\"id\": \"616f2746b817807a7a6c7167\", \"title\": \"The Egyptian Museum In Cairo\", \"default_image\": \"https://res.cloudinary.com/zagazig/image/upload/t1hj3r6todq6qdwz1iwp.jpg\"}";
                Gson gson = new Gson();
//                Type userListType = new TypeToken<ArrayList<SearchPlace>>(){}.getType();

//                try
//                {
////                    ArrayList<SearchPlace> userArray = gson.fromJson(userJson, userListType);
//                    SearchPlace userArray = gson.fromJson(userJson,SearchPlace.class);
//                    Log.d(TAG, "onChanged: "+userArray.getTitle());
//                }
//                catch (IllegalStateException | JsonSyntaxException exception)
//                {
//                    exception.printStackTrace();
//                }
//                try {
//                    JSONArray jsonarray = new JSONArray(s);
//                    JSONObject jsonobject = jsonarray.getJSONObject(0);
//                    String code = jsonobject.getString("title");
//                    Log.d(TAG, "onChanged: code" + code);
//
//                    } catch (JSONException jsonException) {
//                    jsonException.printStackTrace();
//                }

            }
        });
    }


}