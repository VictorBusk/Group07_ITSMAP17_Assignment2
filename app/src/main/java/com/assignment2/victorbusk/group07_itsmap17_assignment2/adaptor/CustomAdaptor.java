package com.assignment2.victorbusk.group07_itsmap17_assignment2.adaptor;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.github.pwittchen.weathericonview.WeatherIconView;

import com.assignment2.victorbusk.group07_itsmap17_assignment2.R;
import com.assignment2.victorbusk.group07_itsmap17_assignment2.model.ListViewItem;

import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomAdaptor extends BaseAdapter {

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader;

    public LazyAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_row, null);

        TextView title = (TextView)vi.findViewById(R.id.title); // title
        TextView artist = (TextView)vi.findViewById(R.id.artist); // artist name
        TextView duration = (TextView)vi.findViewById(R.id.duration); // duration
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image

        HashMap<String, String> song = new HashMap<String, String>();
        song = data.get(position);

        // Setting all values in listview
        title.setText(song.get(CustomizedListView.KEY_TITLE));
        artist.setText(song.get(CustomizedListView.KEY_ARTIST));
        duration.setText(song.get(CustomizedListView.KEY_DURATION));
        imageLoader.DisplayImage(song.get(CustomizedListView.KEY_THUMB_URL), thumb_image);
        return vi;
    }

    private void setWeatherPicture(View convertView, String weatherDesc) {
        WeatherIconView weatherIconView;
        weatherIconView = (WeatherIconView) convertView.findViewById(R.id.weatherIcon);

        if (weatherDesc != null) {
            switch (weatherDesc) {
                case "clear sky":
                    weatherIconView.setIconResource(this.context.getString(R.string.wi_day_sunny));
                    break;
                case "few clouds":
                    weatherIconView.setIconResource(this.context.getString(R.string.wi_day_cloudy));
                    break;
                case "scattered clouds":
                    weatherIconView.setIconResource(this.context.getString(R.string.wi_cloud));
                    break;
                case "broken clouds":
                    weatherIconView.setIconResource(this.context.getString(R.string.wi_cloudy));
                    break;
                case "shower rain":
                    weatherIconView.setIconResource(this.context.getString(R.string.wi_showers));
                    break;
                case "rain":
                    weatherIconView.setIconResource(this.context.getString(R.string.wi_rain));
                    break;
                case "thunderstorm":
                    weatherIconView.setIconResource(this.context.getString(R.string.wi_thunderstorm));
                    break;
                case "snow":
                    weatherIconView.setIconResource(this.context.getString(R.string.wi_snow));
                    break;
                case "mist":
                    weatherIconView.setIconResource(this.context.getString(R.string.wi_fog));
                    break;
                default:
                    weatherIconView.setIconResource(this.context.getString(R.string.wi_na));
                    break;
            }
        } else {
            weatherIconView.setIconResource(this.context.getString(R.string.wi_na));
        }

    }
}
