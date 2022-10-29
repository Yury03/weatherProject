
package com.example.weatherproject;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private Button changeCity;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        Bundle arguments = getIntent().getExtras();
        if(arguments!=null){
            String cityVar = arguments.get("cityVar").toString();
            textView.setText("Город: " + cityVar);
        }

    }
    public void onClick(View view) {
        Intent intent = new Intent(this, city.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
        finish();
    }

}