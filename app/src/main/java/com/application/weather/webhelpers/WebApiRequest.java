package com.application.weather.webhelpers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.webkit.MimeTypeMap;

import com.application.weather.constant.AppConstant;
import com.application.weather.helpers.NetworkUtils;
import com.application.weather.webServices.WebServiceFactory;
import com.application.weather.webServices.webservice;
import com.application.weather.webhelpers.models.WeatherModelWrapper;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebApiRequest {

    private static webservice apiService;
    private static ProgressDialog mDialog;
    private static Activity currentActivity;
    private static WebApiRequest ourInstance = new WebApiRequest();

    private WebApiRequest() {
    }

    public static WebApiRequest getInstance(Activity activity, boolean isShow) {
        apiService = WebServiceFactory.getInstance(activity);
        currentActivity = activity;

//        if (isShow) {
//
//            if(mDialog != null && !mDialog.isShowing()){
//                mDialog = new ProgressDialog(currentActivity);
//                mDialog.setMessage(activity.getString(R.string.loading));
//                mDialog.setTitle(activity.getString(R.string.please_wait));
//                mDialog.setCancelable(false);
//            }
//
//            if (!currentActivity.isFinishing()) {
//                if(mDialog != null && !mDialog.isShowing())
//                    mDialog.show();
//
//            }
//        }

        return ourInstance;
    }

    // url = file path or whatever suitable URL you want.
    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public RequestBody getStringRequestBody(String value) {
        return RequestBody.create(MediaType.parse("text/plain"), value);
    }

    public RequestBody getImageRequestBody(File value) {
        //return RequestBody.create(MediaType.parse(getMimeType(value.getAbsolutePath())), value);
        return RequestBody.create(MediaType.parse("image/*"), value);
    }

    public void GetWeather(double lat,double lon,String appId,final APIArrayRequestDataCallBack apiRequestDataCallBack) {
        if (!NetworkUtils.isNetworkAvailable(currentActivity)) {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }

            apiRequestDataCallBack.onNoNetwork();
            return;
        }

        Call<WeatherModelWrapper> call = apiService.getWeather(lat,lon,appId);
        call.enqueue(new Callback<WeatherModelWrapper>() {
            @Override
            public void onResponse(Call<WeatherModelWrapper> pCall, Response<WeatherModelWrapper> response) {

                if (response != null && response.body().getCod() != null) {
                    if (response.body().getCod()==(AppConstant.ServerAPICalls.SUCCESS_CODE)) {
                        apiRequestDataCallBack.onSuccess(response.body());
//                        Utils.showSnackBar(currentActivity, currentActivity.findViewById(R.id.main_container),
//                                response.body().getMessage(), ContextCompat.getColor(currentActivity, R.color.color_chatHeader));
                    } else {

                        apiRequestDataCallBack.onError();
                    }
                } else {
                    apiRequestDataCallBack.onError();
                }
            }

            @Override
            public void onFailure(Call<WeatherModelWrapper> pCall, Throwable throwable) {
                throwable.printStackTrace();
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                }
                apiRequestDataCallBack.onError();
            }
        });
    }

    public interface APIRequestDataCallBack {
        void onSuccess(int response);

        void onError();

        void onNoNetwork();
    }

    public interface APIArrayRequestDataCallBack {
        void onSuccess(WeatherModelWrapper response);

        void onError();

        void onNoNetwork();
    }

}
