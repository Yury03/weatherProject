package com.example.weatherproject;
//188293f79c0695b76bd873ec916a0f2f api ключ
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;

public class city extends AppCompatActivity {
    String cityVar;
    Button chCityBut;
    //EditText CityText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        chCityBut=findViewById(R.id.butChangeCity);
        //CityText=findViewById(R.id.editTextCity);

    }
    public void findCityBut(View view){
        EditText editTextCity = findViewById(R.id.editTextCity);
        cityVar = editTextCity.getText().toString();
        //if(CityText.getText().toString().trim().equals("")){
        if(cityVar.trim().equals("")){
            Toast.makeText(city.this, R.string.noUserInput, Toast.LENGTH_LONG).show();
        }else{
            finishThisActivity(chCityBut);
        }

    }
    public void finishThisActivity(View view){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("cityVar", cityVar);
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
        finish();
    }
}