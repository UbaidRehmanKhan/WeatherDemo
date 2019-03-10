package com.application.weather.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.application.weather.R;
import com.application.weather.activities.WeatherActivity;
import com.application.weather.helpers.DateFormatHelper;
import com.application.weather.interfaces.WeatherListener;
import com.application.weather.model.ListModel;

import java.util.ArrayList;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.Holder> {

    WeatherActivity weatherActivity;
    LayoutInflater inflater;
    ArrayList<ListModel> updatedList;
    double celsiusMax, celsiusMin;
    WeatherListener weatherListener;;

    public WeatherAdapter(WeatherActivity weatherActivity, ArrayList<ListModel> updatedList, WeatherListener weatherListener) {
        this.weatherActivity = weatherActivity;
        this.weatherListener=weatherListener;
        this.updatedList=updatedList;
        inflater = LayoutInflater.from(weatherActivity);
    }


    @NonNull
    @Override
    public WeatherAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_weather, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final WeatherAdapter.Holder holder, final int position) {

        celsiusMax = updatedList.get(position).getMain().getTempMax();
        celsiusMin = updatedList.get(position).getMain().getTempMin();



        holder.tvDay.setText(DateFormatHelper.datetoDay(updatedList.get(position).getDtTxt()));
        holder.tvMax.setText(String.format("%.0f", Double.valueOf((celsiusMax - 273.15))) + weatherActivity.getString(R.string.c));
        holder.tvMin.setText(String.format("%.0f", Double.valueOf((celsiusMin - 273.15))) + weatherActivity.getString(R.string.c));

        holder.llWeatherItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weatherListener.weatherItemOnClick(updatedList.get(position));
            }
        });
    }


    @Override
    public int getItemCount() {
        return updatedList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        TextView tvDay, tvMax, tvMin;
        LinearLayout llWeatherItem;

        public Holder(View itemView) {
            super(itemView);

            tvDay = itemView.findViewById(R.id.tvDay);
            tvMax = itemView.findViewById(R.id.tvMax);
            tvMin = itemView.findViewById(R.id.tvMin);
            llWeatherItem = itemView.findViewById(R.id.llItemWeather);
        }
    }
}
