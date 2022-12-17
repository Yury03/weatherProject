
package com.example.weatherproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    String defaultCity;
    TextView temp;
    TextView discrip;
    Bundle b;
    public static final String APP_CITY = "CITY";
    int c = 0;
    String cityVar;
    SharedPreferences sp;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id., ContentFragment.class, null)
//                    .commit();
//        }

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

        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            cityVar = arguments.get("cityVar").toString();
            cityText.setText(cityVar);
            String api = "188293f79c0695b76bd873ec916a0f2f";
            String url = "https://api.openweathermap.org/data/2.5/weather?q=" + cityVar + "&appid=" + api + "&units=metric&lang=ru";
            new getData().execute(url);
            SharedPreferences.Editor e = sp.edit();
            e.putString(APP_CITY, cityVar);
            e.commit();
        } else if (!cityVar.equals(String.valueOf(R.string.cityNotChanged))) {
            cityText.setText(cityVar);
            String api = "188293f79c0695b76bd873ec916a0f2f";
            String url = "https://api.openweathermap.org/data/2.5/weather?q=" + cityVar + "&appid=" + api + "&units=metric&lang=ru";
            new getData().execute(url);
        }
        //Toast.makeText(this, debug, Toast.LENGTH_LONG).show();
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
                    c++;
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
                    discrip.setText(descrip);
//                    switch (descrip){
//                        case "пасмурно":
//                            break;
//                        case "небольшая облачность":
//                            break;
//                        case "переменная облачность":
//                            break;
//                        case "облачно с прояснениями":
//                            break;
//
//                    }
                    //feels.setText(String.valueOf((int)jsonObject.getJSONObject("main").getDouble("feels_like")));
                    //humidity.setText(String.valueOf((int)jsonObject.getJSONObject("main").getDouble("humidity"))+" %");

                    double t = jsonObject.getJSONObject("main").getDouble("temp");
                    temp.setText(String.valueOf((int) t));
                    cityText.setText(cityVar);
                } else {
                    cityText.setText("Somewhere Nowhere");
                    discrip.setText("SORRY\nCITY NOT FOUND");
                    temp.setText("");
                    ImageView imageView=findViewById(R.id.rainy_day);//перенести в глобальные
                    imageView.setVisibility(View.INVISIBLE);
                    //обработка кнопок
                    ImageButton imageButton=findViewById(R.id.butLeft);
                    changeFr(imageButton);
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
