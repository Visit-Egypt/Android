package com.visitegypt.utils;

import java.util.SplittableRandom;

public class Constants {
    public static final String BASE_URL = "http://129.146.61.141/";
    public static final String S3_URL = "https://visitegypt-media-bucket.s3.amazonaws.com/";
    public static final String WEB_SOCKET_URL = "ws://129.146.61.141/api/place/ws";
    public static String CHOSEN_USER_ID = "userId";
    public static final String SHARED_PREF_USER_ACCESS_TOKEN = "accessToken";
    public static final String SHARED_PREF_USER_ID = "userId";
    public static final String SHARED_PREF_TOKEN_TYPE = "tokenType";
    public static final String SHARED_PREF_USER_IMAGE = "userImage";
    public static final String SHARED_PREF_EMAIL = "email";
    public static final String SHARED_PREF_USER_REFRESH_TOKEN = "refreshToken";
    public static final String SHARED_PREF_FIRST_NAME = "firstName";
    public static final String SHARED_PREF_LAST_NAME = "lastName";
    public static final String SHARED_PREF_PHONE_NUMBER = "phoneNumber";
    public static  final String SHARED_PREF_NAME = "secret_shared_prefs";
    private static final int PHOTO_SELECTED = 1;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    public static final String PLACE_ID = "placeId";
    private static final int PICK_FROM_GALLERY = 0;
    public static final int MAXIMNumberOfCities = 5;

    public enum CustomerType {
        FOREIGNER_STUDENT("Foreigner Student"),
        FOREIGNER_ADULT("Foreigner Adult"),
        FOREIGNER_ADULT_VIDEO("Foreigner Adult Personal Video"),
        FOREIGNER_ADULT_PHOTO("Foreigner Adult Personal Photography"),
        EGYPTIAN_STUDENT("Egyptian Student"),
        EGYPTIAN_PHOTO("Egyptian Personal Photography"),
        EGYPTIAN_VIDEO("Egyptian Personal Video"),
        CHILDREN("Children Under 6 Years"),
        EGYPTIAN_ADULT("Egyptian Adult");

        private final String text;

        CustomerType(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum Days {
        SATURDAY("Saturday"),
        SUNDAY("Sunday"),
        MONDAY("Monday"),
        TUESDAY("Tuesday"),
        WEDNESDAY("Wednesday"),
        THURSDAY("Thursday"),
        FRIDAY("Friday");

        private final String text;

        Days(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }
}
