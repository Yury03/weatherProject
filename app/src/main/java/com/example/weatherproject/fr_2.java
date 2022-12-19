package com.example.weatherproject;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.json.JSONException;
import org.json.JSONObject;


public class fr_2 extends Fragment {
    TextView feels;
    TextView rainChance;
    TextView humidity;
    TextView visibility;
    TextView wind;
    TextView uvIndex;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = this.getArguments();
        feels = (TextView) view.findViewById(R.id.__10_);
        rainChance = (TextView) view.findViewById(R.id._5_);
        humidity = (TextView) view.findViewById(R.id._98_);
        visibility = (TextView) view.findViewById(R.id._5_km);
        wind = (TextView) view.findViewById(R.id._5_m_s);
        uvIndex = (TextView) view.findViewById(R.id._3);
        try {
            JSONObject jsonObj = new JSONObject(bundle.getString("identifier"));
            int cod = jsonObj.getInt("cod");
            if (cod == 200) {
                int feelsLike = (int) jsonObj.getJSONObject("main").getDouble("feels_like");
                if (feelsLike > 0) {
                    feels.setText("+" + String.valueOf(feelsLike));
                }else {
                    feels.setText(String.valueOf(feelsLike));
                }
                rainChance.setText("-");
                humidity.setText((int) jsonObj.getJSONObject("main").getDouble("humidity") + " %");
                double vis = (jsonObj.getInt("visibility")) / 1000.0;
                String visResult = String.format("%.1f", vis);
                visibility.setText(visResult + " км");
                double win = jsonObj.getJSONObject("wind").getDouble("speed");
                String winResult = String.format("%.1f", win);
                wind.setText(winResult + " м/с");
                uvIndex.setText("-");
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fr_2, container, false);
    }
}