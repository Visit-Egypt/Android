package com.visitegypt.utils;

public class Constants {
    public static final String BASE_URL = "https://visit-egypt.herokuapp.com/";

    public enum CustomerType {
        FOREIGNER_STUDENT("Foreigner Student"),
        FOREIGNER_ADULT("Foreigner Adult"),
        EGYPTIAN_STUDENT("Egyptian Student"),
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

    public enum weekDays {Saturday, Sunday, Monday, Tuesday, Wednesday, Thursday, Friday}
}
