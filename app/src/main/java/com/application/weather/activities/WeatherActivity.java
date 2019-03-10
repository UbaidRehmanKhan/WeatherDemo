package com.application.weather.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.application.weather.R;
import com.application.weather.adapters.WeatherAdapter;
import com.application.weather.databinding.ActivityMainBinding;
import com.application.weather.helpers.DateFormatHelper;
import com.application.weather.interfaces.WeatherListener;
import com.application.weather.model.ListModel;
import com.application.weather.webhelpers.WebApiRequest;
import com.application.weather.webhelpers.models.WeatherModelWrapper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class WeatherActivity extends AppCompatActivity implements WeatherListener, View.OnClickListener {

    ActivityMainBinding binding;
    WeatherAdapter weatherAdapter;
    WeatherModelWrapper updatedModel;
    ArrayList<ListModel> updatedList;
    LocationManager locationManager;
    double lat, lon;

    FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        //  getWeather();

        requestPermision();
        binding.tvCurrentLocation.setOnClickListener(this);
        client = LocationServices.getFusedLocationProviderClient(this);
        updatedList = new ArrayList<>();
        binding.rvWeather.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }



    }

    private void setData(ListModel lm) {


        binding.tvDescription.setText(lm.getWeather().get(0).getDescription());
        binding.tvDay.setText(DateFormatHelper.datetoDay(lm.getDtTxt()));
        double celsius = lm.getMain().getTemp();
        binding.tvCenti.setText((int) (celsius - 273.15) + "");
        double humPercent = ((lm.getMain().getHumidity() * 100) / 100);
        double speedPercent = ((lm.getWind().getSpeed() * 100) / 100);
        binding.tvSpeed.setText(speedPercent + "km/h");
        binding.tvHumidity.setText(humPercent + "%");
        binding.tvPressure.setText(lm.getMain().getPressure() + "%");
    }

    public void hideLoader() {
        binding.progressBar.setVisibility(View.GONE);
    }

    public void showLoader() {
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    public void getWeather(double lat, double lon) {

        showLoader();
        WebApiRequest.getInstance(this, true).GetWeather(
                lat,
                lon,
                getString(R.string.AppId),
                new WebApiRequest.APIArrayRequestDataCallBack() {
                    @Override
                    public void onSuccess(final WeatherModelWrapper response) {

                        hideLoader();

                        updatedModel = response;
                        binding.tvCity.setText(updatedModel.getCity().getName());

                        setData(updatedModel.getList().get(0));
                        ArrayList<ListModel> listModels = response.getList();


                        for (int i = 0; i < listModels.size(); i++) {

                            ListModel model = listModels.get(i);
                            String strDate = model.getDtTxt();
                            String newDate = DateFormatHelper.formatTime(strDate);
                            if (i > 0) {
                                ListModel updatedModel = listModels.get(i - 1);
                                String strupdatedDate = updatedModel.getDtTxt();
                                String updatednewDate = DateFormatHelper.formatTime(strupdatedDate);

                                if (newDate.equals(updatednewDate)) {
                                    continue;
                                }
                            }
                            updatedList.add(model);

                        }
                        weatherAdapter = new WeatherAdapter(WeatherActivity.this, updatedList, WeatherActivity.this);
                        binding.rvWeather.setAdapter(weatherAdapter);

                    }

                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onNoNetwork() {


                    }
                }
        );
    }

    private void requestPermision()
    {
        ActivityCompat.requestPermissions(WeatherActivity.this,new String []{ACCESS_FINE_LOCATION},1);
    }

    @Override
    public void weatherItemOnClick(ListModel listModel) {
        setData(listModel);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCurrentLocation:

                if (updatedList.size()>0)
                {
                    return;
                }
                if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                client.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            lat = location.getLatitude();
                            lon = location.getLongitude();
                            getWeather(lat, lon);
                        }
                        else
                        {
                            Toast.makeText(WeatherActivity.this,R.string.turnon_gps,Toast.LENGTH_LONG).show();
                        }

                    }
                });
                break;
        }
    }
}
