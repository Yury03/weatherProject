package com.example.weatherproject;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;

public class city extends AppCompatActivity {
    String cityVar;
    Button chCityBut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        chCityBut=findViewById(R.id.butChangeCity);

    }
    public void findCityBut(View view){
        EditText editTextCity = findViewById(R.id.editTextCity);
        cityVar = editTextCity.getText().toString();
        finishThisActivity(chCityBut);
    }
    public void finishThisActivity(View view){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("cityVar", cityVar);
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
        finish();
    }
}