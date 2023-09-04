package com.example.weatherapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView temp,conditiontv,cname;
    private TextInputEditText cityname;
    private ImageView back,icon,search;
    private RelativeLayout homerl;
    private ProgressBar loading;
    private RecyclerView weatherRv;
    private ArrayList<weatherRVmodel> weatherRVmodelArrayList;
    private weatherRVadapter weatherRVadapter;
    private LocationManager locationManager ;
    private int PERMISSION_CODE= 1;
    private String Cityname;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS ,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main);

        temp = findViewById(R.id.idtemp);
        conditiontv = findViewById(R.id.condition);
        cityname = findViewById(R.id.enter_city);
        icon = findViewById(R.id.weatherimg);
        search = findViewById(R.id.idsearch);
        homerl = findViewById(R.id.rlhome);
        weatherRv= findViewById(R.id.idrvweather);
        cname = findViewById(R.id.city_name);
        back =findViewById(R.id.image);


        weatherRVmodelArrayList = new ArrayList<>();
        weatherRVadapter = new weatherRVadapter(this,weatherRVmodelArrayList);
        weatherRv.setAdapter(weatherRVadapter);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED  && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
        }

        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        Cityname = getcityname(location.getLongitude(), location.getLatitude());
        getweatherinfo(Cityname);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String city= cityname.getText().toString();

                 if (city.isEmpty())
                 {
                     Toast.makeText(MainActivity.this, "Please enter the city name", Toast.LENGTH_SHORT).show();
                 }
                 else
                 {
                     cityname.setText(Cityname);
                     getweatherinfo(city);
                 }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE)
        {
            if (grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "permition granted", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "Please provide the permition", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    }

    private String getcityname(double lonitude, double latitude)
    {
        String cityname = "not found";

        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(latitude,lonitude,10);

            for(Address adr : addresses)
            {
                if(adr!= null)
                {
                    String city = adr.getLocality();
                    if(city!= null && !city.equals(""))
                    {
                        cityname =city;
                    }
                    else
                    {
                        Log.d("TAG", "CITY NOT FOUND");
                        Toast.makeText(this, "User City Not Found ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

         return cityname;
    }

    private void getweatherinfo (String cityname)
    {
        String url = "http://api.weatherapi.com/v1/current.json?key=9b7908bdfe60485c83a71126232503&q="+cityname+"&aqi=no";
        cname.setText(cityname);
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest jsonObjectRequest =new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                loading.setVisibility(View.GONE);
                homerl.setVisibility(View.VISIBLE);
                weatherRVmodelArrayList.clear();

                try {
                    String temperature = response.getJSONObject("current").getString("temp_c");

                    temp.setText(temperature + "°c");
                    int isDay = response.getJSONObject("current").getInt("is_day");
                    String condition = response.getJSONObject("current").getJSONObject("condition").getString("text");
                    String conditionicon = response.getJSONObject("current").getJSONObject("condition").getString("icon");
                    Picasso.get().load("http:".concat(conditionicon)).into(icon);

                    conditiontv.setText(condition);

                    if (isDay == 1) {
                        Picasso.get().load("https://images.pexels.com/photos/3560044/pexels-photo-3560044.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1").into(back);

                    } else {
                        Picasso.get().load("https://tse4.mm.bing.net/th?id=OIP.bhgaEYN9hAWGFqQHJUkFcwHaLH&pid=Api&P=0").into(back);
                    }

                    JSONObject forcastobj = response.getJSONObject("forcast");
                    JSONObject forcast = response.getJSONArray("forcast").getJSONObject(0);

                    JSONArray hourArray = response.getJSONArray("hour");

                    for (int i = 0; i < hourArray.length(); i++) {
                        JSONObject hourobj = hourArray.getJSONObject(i);

                        String time = hourobj.getString("time");
                        String wind = hourobj.getString("wind_kph");
                        String temper = hourobj.getString("temp_c");
                        String img = hourobj.getJSONObject("condition").getString("icon");

                        weatherRVmodelArrayList.add(new weatherRVmodel(time, temper, img, wind));
                    }

                    weatherRVadapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "please enter valic city name...", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);


    }
}