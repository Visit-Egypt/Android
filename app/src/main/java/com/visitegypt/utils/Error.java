package com.visitegypt.utils;
//
//import org.json.JSONObject;
//
public class Error {
//
//    private Throwable throwable;
//
//    private JSONObject jObjectError;
//
//    private ResponseBody body;
//
//    public Throwable getThrowable() {
//        return throwable;
//    }
//
//    public void setThrowable(Throwable throwable) {
//        this.throwable = throwable;
//    }
//
//
//    public Error(Throwable throwable) {
//        this.throwable = throwable;
//        body = ((HttpException) throwable).response().errorBody();
//        jObjectError = new JSONObject(body.string());
//    }
//
//
//    public boolean isError() {
//
//        if (jObjectError.getJSONArray("errors").toString().contains("msg")) {
//
//            return false;
//        } else {
//            return true;
//        }
//    }
//
////      try {
////        ResponseBody body = ((HttpException) throwable).response().errorBody();
////        JSONObject jObjectError = new JSONObject(body.string());
////        Log.d("TAG", "accept try : " + jObjectError.getJSONArray("errors").toString());
////        if (jObjectError.getJSONArray("errors").toString().contains("msg")) {
////
////            msgMutableLiveData.setValue(jObjectError.getJSONArray("errors").getJSONObject(0).getString("msg"));
////        } else {
////            msgMutableLiveData.setValue(jObjectError.getJSONArray("errors").toString());
////        }
////    } catch (Exception e) {
////        Log.d("TAG", "accept catch: " + e.toString());
////    }
//
//
}
