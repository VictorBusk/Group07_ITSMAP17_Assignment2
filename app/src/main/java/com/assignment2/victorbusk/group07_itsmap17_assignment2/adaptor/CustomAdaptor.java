package com.assignment2.victorbusk.group07_itsmap17_assignment2.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

import com.assignment2.victorbusk.group07_itsmap17_assignment2.R;
import com.assignment2.victorbusk.group07_itsmap17_assignment2.model.WeatherItemModel;

public class CustomAdaptor extends BaseAdapter {

    private final Context context;
    private ArrayList<WeatherItemModel> weather;
    private WeatherItemModel weatherItemModel;

    public CustomAdaptor(Context c, ArrayList<WeatherItemModel> weatherItemModels) {
        this.context = c;
        this.weather = weatherItemModels;
    }

    @Override
    public int getCount() {
        if(weather!=null) {
            return weather.size();
        } else {
            return 0;
        }    }

    @Override
    public Object getItem(int position) {
        if(weather!=null) {
            return weather.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View customView, ViewGroup parent) {
        if (customView == null) {
            LayoutInflater demoInflator = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            customView = demoInflator.inflate(R.layout.custom_row, null);
        }

        weatherItemModel = weather.get(position);
        if(weatherItemModel!=null){
            TextView txtCityName = customView.findViewById(R.id.tvCityName);
            txtCityName.setText(weatherItemModel.getName());

            String tempString = weatherItemModel.getTemperature() + "°C";
            TextView txtTemperature = customView.findViewById(R.id.tvTemp);
            txtTemperature.setText(tempString);

            String humidString = weatherItemModel.getHumidity() + "%";
            TextView txtHumid = customView.findViewById(R.id.tvHumidity);
            txtHumid.setText(humidString);

            ImageView image = customView.findViewById(R.id.img);
            image.setImageResource(R.drawable.mrpbh);
        }
        return customView;
    }

    public void clearData() {
        weather.clear();
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
//    }
}
