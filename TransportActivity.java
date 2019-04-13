package com.example.firstapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class TransportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport);
    }

    public void MrtPage(View view){
        Intent intent= new Intent(this, MrtActivity.class);
        startActivity(intent);
    }
    public void BusPage(View view){
        Intent intent= new Intent(this, BusActivity.class);
        startActivity(intent);
    }

    public void TaxiPage(View view){
        Intent intent= new Intent(this, TaxiActivity.class);
        startActivity(intent);
    }

    public void CarparkPage(View view){
        Intent intent= new Intent(this, CarparkActivity.class);
        startActivity(intent);
    }
}
