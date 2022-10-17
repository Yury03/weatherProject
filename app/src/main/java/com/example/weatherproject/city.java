package com.example.weatherproject;

import android.content.Intent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class city extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);


    }
    public void finishThisActivity(View view){
        Intent intent = new Intent(this, MainActivity.class);
        finish();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }
}