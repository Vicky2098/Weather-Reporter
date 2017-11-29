package com.example.android.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.android.weather.CustomAdapter;
import com.example.android.weather.R;
import com.example.android.weather.RowItem;

import static android.os.Build.VERSION_CODES.M;
import static com.example.android.weather.MainActivity.IMG_URL;
import static com.example.android.weather.MainActivity.bmp;
import static com.example.android.weather.MainActivity.url1;
import static com.example.android.weather.R.id.forecast;
import static com.example.android.weather.R.id.temp;

public class ForecastActivity extends AppCompatActivity {

    String forecast_url = "http://api.openweathermap.org/data/2.5/forecast?id=";
    String full_url;
    double temp, temp_max, temp_min, humid, windspeed;
    String icon,description,mdate;
    LinearLayout l2Layout;


    ListView lv;
    CustomAdapter adapter;
    List<RowItem> rowItems = new ArrayList<RowItem>();


    String place_id, place_name;
    private static final String TAG = "MyActivity";
    Handler messageHandler = new Handler();



    JSONObject mJsonObj;
    Bitmap bmp;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i("onCreate", "Executed");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        lv = (ListView) findViewById(forecast);



        Intent i = getIntent();
        place_id = i.getExtras().getString("id");
        place_name = i.getExtras().getString("name");
        getSupportActionBar().setSubtitle(place_name);
        l2Layout = (LinearLayout) findViewById(R.id.l2Layout);
        l2Layout.setVisibility(View.VISIBLE);

        full_url = forecast_url + place_id + "&APPID=0ce24eb51f440b272e21d44e8f62e8a7";
        new ForecastTask().execute();



    }

    public void ExtractFromJSON(String JSONForecast) {
        try {
            JSONObject jsonObj = new JSONObject(JSONForecast);
            JSONArray jlist = jsonObj.getJSONArray("list");

            String dt = null;
            String pre_dt = null;

            for (int i = 0; i <= jlist.length(); i++) {
                dt = jlist.getJSONObject(i).getString("dt_txt")
                        .substring(8, 10);

                if (i > 1 || i == 1) {
                    pre_dt = jlist.getJSONObject(i - 1)
                            .getString("dt_txt")
                            .substring(8, 10);

                    if (!(dt.equals(pre_dt))) {

                        mdate = jlist.getJSONObject(i)
                                .getString("dt_txt")
                                .substring(0, 10);

                        JSONObject main = jlist
                                .getJSONObject(i)
                                .getJSONObject("main");
                        JSONObject wind = jlist
                                .getJSONObject(i)
                                .getJSONObject("wind");

                        temp = (main.getDouble("temp") - 273.15);
                        temp_max = (main
                                .getDouble("temp_max") - 273.15);
                        temp_min = (main
                                .getDouble("temp_min") - 273.15);
                        humid = main.getDouble("humidity");
                        windspeed = wind.getDouble("speed");

                        JSONArray weather = jlist
                                .getJSONObject(i).getJSONArray(
                                        "weather");

                        icon = weather.getJSONObject(0)
                                .getString("icon");
                        try {
                            url1 = new URL(IMG_URL+icon+".png");
                        } catch (MalformedURLException e) {
                        }


                        try {
                            bmp = BitmapFactory.decodeStream(url1.openConnection().getInputStream());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        description = weather
                                .getJSONObject(0).getString(
                                        "description");

                        RowItem rItem = new RowItem(temp,
                                temp_max, temp_min, humid, windspeed,
                                description, bmp, mdate);

                        rowItems.add(rItem);

                    }
                }
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Hello", "NAsnsal");
    }


    public class ForecastTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... urls) {


            URL url = null;
            String jsonResponse = "";
            try {
                url = new URL(full_url);
            } catch (MalformedURLException e) {
            }

            try {
                jsonResponse = MainActivity.makeHttpRequest(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ExtractFromJSON(jsonResponse);
            if(jsonResponse==null)
                Log.e(TAG, "No response json " );

            Log.i("DIG", "EXEC");
            return null;

        }


        @Override
        protected void onPostExecute(Void aVoid) {
            adapter = new CustomAdapter(getApplicationContext(), rowItems);
            l2Layout.setVisibility(View.GONE);
            lv.setAdapter(adapter);

        }

    }
}




