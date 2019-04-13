package com.example.firstapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;



import java.util.ArrayList;

public class BusRoute extends AppCompatActivity {
    ArrayList<String[]> BusRouteInfo = new ArrayList<String[]>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_route);

        this.BusRouteInfo= (ArrayList<String[]>) getIntent().getSerializableExtra("BusRouteInfo");
        int direction= (int) getIntent().getSerializableExtra("routeDirection");

        LinearLayout scrolllinearLayout=findViewById(R.id.scrollLinearView);
        display(scrolllinearLayout, BusRouteInfo, direction);
    }

    protected void display(LinearLayout scrolllinearLayout,ArrayList<String[]> BusRouteInfo, int direction){

        StringBuilder response= new StringBuilder();
        scrolllinearLayout.removeAllViews();

        switch(direction){
            case 0:
                for(int i=0; i< (BusRouteInfo.size()-1);i++){

                    response.append(BusRouteInfo.get(i)[0]);
                    response.append(BusRouteInfo.get(i)[2]);


                    TextView textView= new TextView(scrolllinearLayout.getContext());
                    Button nextBus= new Button(textView.getContext());

                    textView.setPadding(10,0,0,0);
                    textView.setText(response);
                    textView.setTextSize(20);
                    textView.setBackgroundColor(0xFFFFFF);
                    textView.setBackgroundResource(R.drawable.outline);

                    nextBus.setText(BusRouteInfo.get(i)[4]);
                    nextBus.setTextSize(20);
                    nextBus.setWidth(30);
                    nextBus.setGravity(Gravity.LEFT);

                    scrolllinearLayout.addView(textView);
                    scrolllinearLayout.addView(nextBus);

                    response.setLength(0);
                }
            case 1:
                for(int i=0; i< (BusRouteInfo.size()/2)-1;i++){

                    response.append(BusRouteInfo.get(i)[0]);
                    response.append(BusRouteInfo.get(i)[2]);

                    TextView textView= new TextView(scrolllinearLayout.getContext());
                    Button nextBus= new Button(textView.getContext());

                    textView.setPadding(10,0,0,0);
                    textView.setText(response);
                    textView.setTextSize(20);
                    textView.setTextColor(getResources().getColor(R.color.colorWhite));
                    textView.setBackgroundColor(0xFFFFFF);
                    textView.setBackgroundResource(R.drawable.outline);

                    nextBus.setText(BusRouteInfo.get(i)[4]);
                    nextBus.setTextSize(20);
                    nextBus.setWidth(30);
                    nextBus.setGravity(Gravity.LEFT);

                    scrolllinearLayout.addView(textView);
                    scrolllinearLayout.addView(nextBus);

                    response.setLength(0);
                }
            case 2:
                for(int i=(BusRouteInfo.size()/2); i< (BusRouteInfo.size()-1);i++){

                    response.append(BusRouteInfo.get(i)[0]);
                    response.append(BusRouteInfo.get(i)[2]);

                    TextView textView= new TextView(scrolllinearLayout.getContext());
                    Button nextBus= new Button(textView.getContext());

                    textView.setPadding(10,0,0,0);
                    textView.setText(response);
                    textView.setTextSize(20);
                    textView.setTextColor(getResources().getColor(R.color.colorWhite));
                    textView.setBackgroundColor(0xFFFFFF);
                    textView.setBackgroundResource(R.drawable.outline);

                    nextBus.setText(BusRouteInfo.get(i)[4]);
                    nextBus.setTextSize(20);
                    textView.setTextColor(getResources().getColor(R.color.colorWhite));
                    nextBus.setWidth(30);
                    nextBus.setGravity(Gravity.LEFT);

                    scrolllinearLayout.addView(textView);
                    scrolllinearLayout.addView(nextBus);

                    response.setLength(0);
                }
        }

    }
}
