package com.visitegypt.utils.error;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;


public class Error {
    private static final String TAG = "errorrrr class";
    public Throwable throwable;
    public ResponseBody body;
    public JSONObject jsonObject;
    public Response response;
    MutableLiveData<String> msgMutableLiveData = new MutableLiveData<>();

    public Error() {
        Log.e(TAG, "checking for error ");
    }

    public String errorType(Throwable throwable) {
        this.throwable = throwable;
        try {
            ResponseBody body = ((HttpException) throwable).response().errorBody();
            JSONObject jObjectError = new JSONObject(body.string());
            Log.d(TAG, "accept try : " + jObjectError.getJSONArray("errors").toString());
            response = ((HttpException) throwable).response();
            if (response.code() == 422) {
                Log.e(TAG, "Validation Error ");
                return "Validation Error";
            } else if (response.code() == 500) {
                Log.e(TAG, "Internal Server Error ");
                return "Internal Server Error";
            } else if (response.code() == 404) {
                Log.e(TAG, "was not found ");
                return " was not found";
            } else if (response.code() == 401) {
                return "was not authenticated";
            } else {
                return throwable.getMessage();
            }
        } catch (Exception e) {

            return throwable.getMessage();
        }

    }
}
