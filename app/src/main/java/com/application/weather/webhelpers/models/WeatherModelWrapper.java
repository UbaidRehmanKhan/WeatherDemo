package com.application.weather.webhelpers.models;

import com.application.weather.model.CityModel;
import com.application.weather.model.ListModel;

import java.util.ArrayList;

/**
 * Created by ahsanali on 9/6/2017.
 */
public class WeatherModelWrapper {

    private String Message;
    private Integer cod;
    private int cnt;
    private ArrayList<ListModel> list;

    private CityModel city;

    public CityModel getCity() {
        return city;
    }

    public void setCity(CityModel city) {
        this.city = city;
    }


    public Integer getCod() {
        return cod;
    }

    public void setCod(Integer cod) {
        this.cod = cod;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public String getMessage() {
        return Message;
    }


    public ArrayList<ListModel> getList() {
        return list;
    }

    public void setList(ArrayList<ListModel> list) {
        this.list = list;
    }

    public void setMessage(String message) {
        if (!message.equals(null))
            Message = message;
        else
            Message = "Something went wrong";

    }



}
