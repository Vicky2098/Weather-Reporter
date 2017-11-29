package com.example.android.weather;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;

import static com.example.android.weather.R.id.minmax;
import static com.example.android.weather.R.id.temp;

/**
 * Created by VICKY on 05-07-2017.
 */

public class CustomAdapter extends BaseAdapter {
    Context context;
    List<RowItem> rowItems;
    URL url1;
    Bitmap bmp;
    String IMG_URL = "http://openweathermap.org/img/w/";
    DecimalFormat form = new DecimalFormat("0.0");
    public CustomAdapter(Context context, List<RowItem> rowItems) {

        this.context = context;
        this.rowItems = rowItems;

    }

    @Override
    public int getCount() {

        return rowItems.size();
    }

    @Override
    public Object getItem(int position) {

        return rowItems.get(position);
    }

    @Override
    public long getItemId(int position) {

        return rowItems.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, null);
        }
        final TextView fmin_max, ftemp, fdesc, fhumid, fwindspeed, fdate;
        final ImageView ficon;

        fmin_max = (TextView) convertView.findViewById(R.id.fmin_max);
        ftemp = (TextView) convertView.findViewById(R.id.ftemp);
        fdesc = (TextView) convertView.findViewById(R.id.fdesc);
        fhumid = (TextView) convertView.findViewById(R.id.fhumid);
        fwindspeed = (TextView) convertView.findViewById(R.id.fwindspeed);
        fdate = (TextView) convertView.findViewById(R.id.fdate);
        ficon = (ImageView) convertView.findViewById(R.id.ficon);

        final RowItem item = rowItems.get(position);

                double temp=item.getTemp();
                double mintemp=item.getTemp_min();
                double maxtemp=item.getTemp_max();
                ftemp.setText(String.valueOf(form.format(temp)) + " °C");
                fmin_max.setText(String.valueOf(form.format(mintemp)) + " °C" + "/"
                        + String.valueOf(form.format(maxtemp)) + " °C");
                fdesc.setText(item.getCondition() + "");
                fdate.setText(item.getDate() + "");
                fhumid.setText(item.getHumidity() + "%");
                fwindspeed.setText(item.getWindspeed() + " m/s");
                ficon.setImageBitmap(item.getImage());

        return convertView;
    }
}