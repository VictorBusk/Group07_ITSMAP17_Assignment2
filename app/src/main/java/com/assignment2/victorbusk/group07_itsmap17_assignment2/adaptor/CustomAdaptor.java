package com.assignment2.victorbusk.group07_itsmap17_assignment2.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.assignment2.victorbusk.group07_itsmap17_assignment2.R;
import com.assignment2.victorbusk.group07_itsmap17_assignment2.model.WeatherItemModel;
import com.assignment2.victorbusk.group07_itsmap17_assignment2.utils.Connector;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

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
        if (weather != null) {
            return weather.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if (weather != null) {
            return weather.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //Set current listview item in accordance to this custom setup
    public View getView(int position, View customView, ViewGroup parent) {
        if (customView == null) {
            LayoutInflater demoInflator = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            customView = demoInflator.inflate(R.layout.weather_list_item, null);
        }

        weatherItemModel = weather.get(position); //Get current position in the listview
        if (weatherItemModel != null) {
            //Insert data from the model into our custom listview item (weather_list_item.xml)
            TextView txtCityName = customView.findViewById(R.id.tvCityName);
            txtCityName.setText(weatherItemModel.getName());

            String tempPostfix = context.getString(R.string.temp_symbol);
            String tempString = weatherItemModel.getTemperature() + tempPostfix;
            TextView txtTemperature = customView.findViewById(R.id.tvTemp);
            txtTemperature.setText(tempString);

            String humPostfix = context.getString(R.string.humidity_symbol);
            String humidString = weatherItemModel.getHumidity() + humPostfix;
            TextView txtHumid = customView.findViewById(R.id.tvHumidity);
            txtHumid.setText(humidString);

            String imageString = weatherItemModel.getImage();
            ImageView image = customView.findViewById(R.id.img);
            //Loading images using Picasso: http://square.github.io/picasso/
            Picasso.with(context).load(Connector.iconURL(imageString)).resize(50, 50).into(image);
        }
        return customView;
    }

    public void clearData() {
        weather.clear();
    }
}
