package com.visitegypt.utils;

public class Constants {
    public static final String BASE_URL = "https://visit-egypt.herokuapp.com/";
    public static final String SHARED_PREF_USER_ACCESS_TOKEN = "accessToken";
    public static final String SHARED_PREF_USER_ID = "userId";
    public static final String SHARED_PREF_TOKEN_TYPE = "tokenType";
    public static final String SHARED_PREF_USER_REFRESH_TOKEN = "refreshToken";
    public static final String SHARED_PREF_FULL_NAME = "fullName";

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
