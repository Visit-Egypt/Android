package com.visitegypt.utils;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.visitegypt.domain.model.Token;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Base64;

@RequiresApi(api = Build.VERSION_CODES.O)
public class JWT extends Token {
    private static final String TAG = "Cannot invoke method length() on null object";

    public JWT(String accessToken, String refreshToken) {
        super(accessToken, refreshToken);
    }

    private static String TokenPayLoad;
    private static String refreshTokenPayLoad;
    private static String tokenExp = null;
    private static String refreshTokenExp = null;
    private static Base64.Decoder decoder = Base64.getDecoder();

    private void jwtDecode() {
        String[] tokenChunks = super.getAccessToken().split("\\.");
        String[] refreshTokenChunks = this.getRefreshToken().split("\\.");
        TokenPayLoad = new String(decoder.decode(tokenChunks[1]));
        refreshTokenPayLoad = new String(decoder.decode(refreshTokenChunks[1]));
    }

    private static void getExpiryDate() {
        JSONObject tokenJsonObject = null;
        JSONObject refreshTokenJsonObject = null;

        try {
            tokenJsonObject = new JSONObject(TokenPayLoad);
            refreshTokenJsonObject = new JSONObject(refreshTokenPayLoad);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            tokenExp = tokenJsonObject.getString("exp");
            refreshTokenExp = refreshTokenJsonObject.getString("exp");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public  Boolean isValidToken() {
        jwtDecode();
        getExpiryDate();
        Log.d(TAG, "isValidToken:  " +Long.parseLong(tokenExp) );
        Log.d(TAG, "isValidToken:  " +java.time.Instant.now().getEpochSecond() );
        Boolean isValid = Long.parseLong(tokenExp) - java.time.Instant.now().getEpochSecond() >=0 ? true : false;
        return isValid;

    }

}
