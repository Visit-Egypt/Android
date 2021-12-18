package com.visitegypt.utils;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Base64;

@RequiresApi(api = Build.VERSION_CODES.O)
public class JWT {
    public static String getHeader() {
        return header;
    }

    public static String getPayLoad() {
        return payLoad;
    }

    private static String header;
    private static String payLoad;
    private static Base64.Decoder decoder = Base64.getDecoder();

    public static void jwtDecode(String token) {
        String[] chunks = token.split("\\.");
        header = new String(decoder.decode(chunks[0]));
        payLoad = new String(decoder.decode(chunks[1]));
    }

    public static String getExpiryDate() {
        JSONObject myJsonObj = null;
        String exp = null;
        try {
            myJsonObj = new JSONObject(payLoad);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            exp = myJsonObj.getString("exp");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return exp;
    }
    public static Boolean isValidToken(String expToken,String expRefreshToken)
    {
        Boolean isValid = Integer.parseInt(expToken) <= Integer.parseInt(expRefreshToken) ? true : false;
        return isValid;
    }

}
