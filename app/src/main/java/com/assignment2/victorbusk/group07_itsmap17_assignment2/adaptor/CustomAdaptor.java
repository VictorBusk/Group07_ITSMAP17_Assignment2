package com.assignment2.victorbusk.group07_itsmap17_assignment2.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.assignment2.victorbusk.group07_itsmap17_assignment2.R;

public class CustomAdaptor extends ArrayAdapter<String> {

    private static LayoutInflater inflater=null;

    public CustomAdaptor(Context context, String[] cityNames) {
        super(context, R.layout.custom_row, cityNames);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE );
        View customView = convertView;
        if(customView == null){
            customView = inflater.inflate(R.layout.custom_row, parent, false);
        }

        String singleCityName = getItem(position);

        TextView cityNameTV = (TextView) customView.findViewById(R.id.tvCityName);
        ImageView image = (ImageView) customView.findViewById(R.id.img);

        cityNameTV.setText(singleCityName);
        image.setImageResource(R.drawable.mrpbh);

        return customView;
    }

//    private void setWeatherPicture(View convertView, String weatherDesc) {
//        WeatherIconView weatherIconView;
//        weatherIconView = (WeatherIconView) convertView.findViewById(R.id.weatherIcon);
//
//        if (weatherDesc != null) {
//            switch (weatherDesc) {
//                case "clear sky":
//                    weatherIconView.setIconResource(this.context.getString(R.string.wi_day_sunny));
//                    break;
//                case "few clouds":
//                    weatherIconView.setIconResource(this.context.getString(R.string.wi_day_cloudy));
//                    break;
//                case "scattered clouds":
//                    weatherIconView.setIconResource(this.context.getString(R.string.wi_cloud));
//                    break;
//                case "broken clouds":
//                    weatherIconView.setIconResource(this.context.getString(R.string.wi_cloudy));
//                    break;
//                case "shower rain":
//                    weatherIconView.setIconResource(this.context.getString(R.string.wi_showers));
//                    break;
//                case "rain":
//                    weatherIconView.setIconResource(this.context.getString(R.string.wi_rain));
//                    break;
//                case "thunderstorm":
//                    weatherIconView.setIconResource(this.context.getString(R.string.wi_thunderstorm));
//                    break;
//                case "snow":
//                    weatherIconView.setIconResource(this.context.getString(R.string.wi_snow));
//                    break;
//                case "mist":
//                    weatherIconView.setIconResource(this.context.getString(R.string.wi_fog));
//                    break;
//                default:
//                    weatherIconView.setIconResource(this.context.getString(R.string.wi_na));
//                    break;
//            }
//        } else {
//            weatherIconView.setIconResource(this.context.getString(R.string.wi_na));
//        }
//
//    }
}
