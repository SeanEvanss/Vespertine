package com.example.firstapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void foodSelectorPage(View view){
        Intent intent= new Intent(this, FoodSelectorActivity.class);
        startActivity(intent);
    }

    public void emergencyPage(View view){
        Intent intent= new Intent(this, EmergencyActivity.class);
        startActivity(intent);
    }

    public void transportPage(View view){
        Intent intent= new Intent(this, TransportActivity.class);
        startActivity(intent);
    }


}
