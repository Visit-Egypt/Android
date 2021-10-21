package com.visitegypt.presentation.signup;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.visitegypt.data.repository.UserRepositoryImp;
import com.visitegypt.domain.model.User;
import com.visitegypt.domain.usecase.RegisterUseCase;
import com.visitegypt.domain.usecase.UserValidation;

import org.json.JSONObject;

import java.util.List;

import io.reactivex.rxjava3.functions.Consumer;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

public class SignUpViewModel extends ViewModel {
    MutableLiveData<String[]> mutableLiveDataErrors = new MutableLiveData<>();
    MutableLiveData<String> mutableLiveDataResponse = new MutableLiveData<>();
    public void getUser (UserValidation userValidation)
    {
        mutableLiveDataErrors.postValue(userValidation.checkValidations());
        if(userValidation.isValidUser())
        {
            Log.d("TAG", "getUser: This is valid User");
            UserRepositoryImp userRepositoryImp = new UserRepositoryImp() ;
            User myUser = userValidation;
            Log.d("TAG", "getUser: " + myUser.getFirstName() + myUser.getLastName());
            RegisterUseCase registerUseCase = new RegisterUseCase(userRepositoryImp,myUser);
            try
            {
                registerUseCase.execute(new Consumer<User>() {
                    @Override
                    public void accept(User user) throws Throwable {
                        mutableLiveDataResponse.setValue("Your Account created successfully,please Sign in");

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        try
                        {
                            ResponseBody body = ((HttpException) throwable).response().errorBody();
                            JSONObject jObjectError  = new JSONObject(body.string());
                            Log.d("TAG", "accept try : " + jObjectError.getJSONArray("errors").toString());
                            if(jObjectError.getJSONArray("errors").toString().contains("msg"))
                            {

                                mutableLiveDataResponse.setValue( jObjectError.getJSONArray("errors").getJSONObject(0).getString("msg"));
                            }
                            else
                            {
                                mutableLiveDataResponse.setValue( jObjectError.getJSONArray("errors").toString());
                            }
                        }catch (Exception e)
                        {
                            Log.d("TAG", "accept catch: " + e.toString());
                        }
                    }
                });
            }catch (Exception e)
            {
                Log.d("TAG", "getUser: " + e);
            }

        }



    }
}