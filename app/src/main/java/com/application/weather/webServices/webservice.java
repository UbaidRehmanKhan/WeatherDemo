package com.application.weather.webServices;


import com.application.weather.constant.AppConstant;
import com.application.weather.webhelpers.models.WeatherModelWrapper;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface webservice {

    @GET(AppConstant.ServerAPICalls.WEATHER)
    Call<WeatherModelWrapper> getWeather(
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("appid") String appid);
}
