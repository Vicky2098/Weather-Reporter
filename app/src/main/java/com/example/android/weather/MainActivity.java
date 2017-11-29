package com.example.android.weather;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;


import static android.R.attr.id;
import static android.R.attr.key;
import static android.R.attr.name;

public class MainActivity extends AppCompatActivity {


    static double TEMPERATURE, HUMIDITY, WINDSPEED, MINTEMP, MAXTEMP;
    static String CONDITION, CITY;



    static boolean flag = false;
    static String REQUEST_URL, icon;
   static String IMG_URL = "http://openweathermap.org/img/w/";
    EditText nameBox;
    ImageView condIcon;
    TextView city, condDescription, temperature, pressure, humid, windSpeed, minmax;
    LinearLayout lLayout;
    Button searchButton;
    private static final String TAG = "MyActivity";
   static URL url1;
    static Bitmap bmp;
    static String COUNTRY;
    String url;
    private static final String TAG_RESULT = "predictions";



    String browserKey = "API";
 static String name,id;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.SplashTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        lLayout = (LinearLayout) findViewById(R.id.lLayout);
        condIcon = (ImageView) findViewById(R.id.condIcon);
        searchButton = (Button) findViewById(R.id.searchButton);
        nameBox = (EditText) findViewById(R.id.cityName);
        condDescription = (TextView) findViewById(R.id.condDescription);
        temperature = (TextView) findViewById(R.id.temp);

        windSpeed = (TextView) findViewById(R.id.windSpeed);
        humid = (TextView) findViewById(R.id.humidity);
        city = (TextView) findViewById(R.id.city);
        minmax = (TextView) findViewById(R.id.minmax);
    }

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
   public void Autocomplete(View view){
       AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
               .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
               .build();

       try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).setFilter(typeFilter)
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place: " + place.getName());
                nameBox.setText(place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    public void Asynctask(View view) {
        if (nameBox.getText().toString().equals(""))
            Toast.makeText(this, "Enter City Name", Toast.LENGTH_LONG).show();

        else {
            lLayout.setVisibility(View.VISIBLE);
            REQUEST_URL= "http://api.openweathermap.org/data/2.5/weather?q=";
            String s = nameBox.getText().toString();
            REQUEST_URL += s;
            REQUEST_URL += "&mode=json&APPID=0ce24eb51f440b272e21d44e8f62e8a7";
            nameBox.setText("");
            WeatherTask W = new WeatherTask();
            W.execute();
        }
    }

    public void Forecast(View view){
        Intent forecast_intent = new Intent(getApplicationContext(),
                ForecastActivity.class);

        forecast_intent.putExtra("id", id);
        forecast_intent.putExtra("name", CITY);

        startActivity(forecast_intent);

    }

    public static String StreamReader(InputStream inputStream) throws IOException {
        StringBuilder builder = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);

            try {
                String line = reader.readLine();

                while (line != null) {
                    builder.append(line);
                    line = reader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        return builder.toString();
    }

    public static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";

        if (url == null)
            return jsonResponse;


        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;


        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {

                inputStream = urlConnection.getInputStream();
                jsonResponse = StreamReader(inputStream);

            } else {
                flag = true;
                Log.e(TAG, "Error response code: " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();

            if (inputStream != null)
                inputStream.close();
        }

        return jsonResponse;
    }

    public static void extractFromJSON(String JSONWeather)
    {
        try {
            JSONObject jsonObj = new JSONObject(JSONWeather);
            JSONArray weatherArray = jsonObj.getJSONArray("weather");
            JSONObject weather1 = weatherArray.getJSONObject(0);

            icon = weather1.getString("icon");
            CONDITION = weather1.getString("description");

            HUMIDITY = jsonObj.getJSONObject("main").getDouble("humidity");
            WINDSPEED = jsonObj.getJSONObject("wind").getDouble("speed");
            TEMPERATURE = (jsonObj.getJSONObject("main").getDouble("temp")) - 273.15;
            MINTEMP=(jsonObj.getJSONObject("main").getDouble("temp_min")) - 273.15;
            MAXTEMP=(jsonObj.getJSONObject("main").getDouble("temp_max")) - 273.15;
            CITY= jsonObj.getString("name");
            id=jsonObj.getString("id");
            COUNTRY=jsonObj.getJSONObject("sys").getString("country");
            try {
                url1 = new URL(IMG_URL+icon+".png");
            } catch (MalformedURLException e) {
            }


            try {
                bmp = BitmapFactory.decodeStream(url1.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    DecimalFormat form = new DecimalFormat("0.0");

    public void screenUpdate()
    {
        lLayout.setVisibility(View.GONE);

        condIcon.setImageBitmap(bmp);
        city.setText(CITY+", "+COUNTRY);
        temperature.setText(String.valueOf(form.format(TEMPERATURE))+" °C");
        minmax.setText(String.valueOf(form.format(MINTEMP))+" °C/"+String.valueOf(form.format(MINTEMP))+" °C");

        humid.setText(String.valueOf(HUMIDITY)+ " %");
        condDescription.setText(CONDITION);
        windSpeed.setText(String.valueOf(WINDSPEED)+ " m/s");

    }


    public class WeatherTask extends AsyncTask<URL,Void,Void> {

        @Override
        protected Void doInBackground(URL... urls) {


            URL url = null;
            String jsonResponse = "";
            try {
                url = new URL(REQUEST_URL);
            } catch (MalformedURLException e) {
            }

            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            extractFromJSON(jsonResponse);

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            if(flag==false)
                screenUpdate();}
    }
}




