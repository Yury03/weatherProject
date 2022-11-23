
package com.example.weatherproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    String defaultCity;
    TextView nameTextView;
    public static final String APP_CITY = "CITY";

    String cityVar;
    SharedPreferences sp;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sp = getSharedPreferences(APP_CITY, Context.MODE_PRIVATE);
        cityVar = sp.getString(APP_CITY, String.valueOf(R.string.cityNotChanged));

        //Toast.makeText(this, cityVar, Toast.LENGTH_LONG).show();

        textView = findViewById(R.id.textView);
        nameTextView=findViewById(R.id.textView2);

        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            cityVar = arguments.get("cityVar").toString();
            textView.setText("Город: " + cityVar);
            String api = "188293f79c0695b76bd873ec916a0f2f";
            String url = "https://api.openweathermap.org/data/2.5/weather?q=" + cityVar + "&appid=" + api + "&units=metric&lang=ru";
            new getData().execute(url);
            SharedPreferences.Editor e = sp.edit();
            e.putString(APP_CITY, cityVar);
            e.commit();
        }else if (!cityVar.equals(String.valueOf(R.string.cityNotChanged))){
            textView.setText("Город: " + cityVar);
            String api = "188293f79c0695b76bd873ec916a0f2f";
            String url = "https://api.openweathermap.org/data/2.5/weather?q=" + cityVar + "&appid=" + api + "&units=metric&lang=ru";
            new getData().execute(url);
        }

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
            nameTextView.setText("Ожидайте...");
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append(("\n"));
                }
                return buffer.toString();
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
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                nameTextView.setText("Температура: " + jsonObject.getJSONObject("main").getDouble("temp"));
                //nameTextView.setText(s);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }
    }
    protected void onStop() {
        super.onStop();
        //SharedPreferences.Editor e = sp.edit();
         // не забудьте подтвердить изменения
    }
}
