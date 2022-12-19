
package com.example.weatherproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import org.json.JSONException;
import org.json.JSONObject;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    TextView cityText;
    ImageView imageView;
    ImageButton leftButton;
    ImageButton rightButton;
    TextView temp;
    TextView discrip;
    Bundle b;
    public static final String APP_CITY = "CITY";
    String cityVar;
    SharedPreferences sp;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragment fragment = new fr_1();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment, fragment);
        ft.commit();
        sp = getSharedPreferences(APP_CITY, Context.MODE_PRIVATE);
        cityVar = sp.getString(APP_CITY, String.valueOf(R.string.cityNotChanged));
        cityText = findViewById(R.id.cityText);
        temp = findViewById(R.id.temp);
        discrip = findViewById(R.id.discrip);
        imageView = findViewById(R.id.rainy_day);
        leftButton = findViewById(R.id.butLeft);
        rightButton = findViewById(R.id.butRight);
        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {//запуск активити с аргументами, принятыми из другой активити
            cityVar = arguments.get("cityVar").toString();
            cityText.setText(cityVar);
            if (isOnline()) {
                String api = "188293f79c0695b76bd873ec916a0f2f";
                String url = "https://api.openweathermap.org/data/2.5/weather?q=" + cityVar + "&appid=" + api + "&units=metric&lang=ru";
                new getData().execute(url);
            } else {
                discrip.setText(R.string.noNetwork);
            }
            SharedPreferences.Editor e = sp.edit();
            e.putString(APP_CITY, cityVar);
            e.commit();
        } else if (!cityVar.equals(R.string.cityNotChanged)) {//запуск активити без аргумента, с сохранными настройками
            cityText.setText(cityVar);
            if (isOnline()) {
                String api = "188293f79c0695b76bd873ec916a0f2f";
                String url = "https://api.openweathermap.org/data/2.5/weather?q=" + cityVar + "&appid=" + api + "&units=metric&lang=ru";
                new getData().execute(url);
            } else {
                discrip.setText(R.string.noNetwork);
                cityText.setText("");
                temp.setText("");
                leftButton.setVisibility(View.INVISIBLE);
                rightButton.setVisibility(View.INVISIBLE);
            }
        } else {//запуск активити без аргументов и без настроек
            cityText.setText(R.string.cityNotChanged);
            leftButton.setVisibility(View.INVISIBLE);
            rightButton.setVisibility(View.INVISIBLE);
            discrip.setText("");
            temp.setText("");
            b = new Bundle();
            b.putString("identifier", "{\"cod\":\"404\"}");
            changeFr(leftButton);

        }
    }

    public void changeFr(View view) {
        Fragment fragment = null;
        switch (view.getId()) {
            case R.id.butRight:
                fragment = new fr_1();
                break;
            case R.id.butLeft:
                fragment = new fr_2();
                break;
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        fragment.setArguments(b);
        ft.replace(R.id.fragment, fragment);
        ft.commit();
    }

    public void onClick(View view) {
        Intent intent = new Intent(this, city.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
        finish();
    }

    protected boolean isOnline() {
        boolean online = false;
        String cs = Context.CONNECTIVITY_SERVICE;
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(cs);
        if (cm.getActiveNetworkInfo() == null) {
            online = false;
        } else {
            online = true;
        }
        return online;
    }

    private class getData extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cityText.setText("Ожидайте...");
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            StringBuffer buffer = new StringBuffer();
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append(("\n"));
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            b = new Bundle();
            if (buffer.toString().length() > 70) {
                b.putString("identifier", buffer.toString());
                return buffer.toString();
            }
            b.putString("identifier", "{\"cod\":\"404\"}");
            return "{\"cod\":\"404\"}";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {//{"cod":"404","message":"city not found"}
                JSONObject jsonObject = new JSONObject(s);
                int cod = jsonObject.getInt("cod");
                if (cod == 200) {
                    String descrip = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
                    descrip = descrip.toUpperCase();
                    discrip.setText(descrip);
                    double t = jsonObject.getJSONObject("main").getDouble("temp");
                    String icon = jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon");
                    icon = icon.substring(0, 2);
                    switch (icon) {
                        case "01":
                            imageView.setImageResource(R.drawable.hot_sunny);
                            break;
                        case "02":
                        case "13":
                            imageView.setImageResource(R.drawable.snowy_day);
                            break;
                        case "03":
                        case "04":
                            if (t < 0) {
                                imageView.setImageResource(R.drawable.snow2_1);
                            } else {
                                imageView.setImageResource(R.drawable.snow4);
                            }
                            break;
                        case "09":
                            imageView.setImageResource(R.drawable.snow_rain);
                            break;
                        case "10":
                            imageView.setImageResource(R.drawable.rainy_day);
                            break;
                        case "11":
                            imageView.setImageResource(R.drawable.thunderstormday);
                            break;
                        case "50":
                            if (t < 0) {
                                imageView.setImageResource(R.drawable.snow33);
                            } else {
                                imageView.setImageResource(R.drawable.snow33);
                            }
                            break;
                    }
                    if (t > 0) {
                        temp.setText("+" + String.valueOf((int) t) + "\u00B0C");
                    } else {
                        temp.setText(String.valueOf((int) t)+ "\u00B0C");
                    }
                    cityText.setText(cityVar);
                } else if (cod == 404) {
                    cityText.setText("Somewhere Nowhere");
                    discrip.setText("SORRY\nCITY NOT FOUND");
                    temp.setText("");
                    imageView.setVisibility(View.INVISIBLE);
                    leftButton.setVisibility(View.INVISIBLE);
                    rightButton.setVisibility(View.INVISIBLE);
                    ImageButton imageButton = findViewById(R.id.butLeft);
                    changeFr(imageButton);
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
