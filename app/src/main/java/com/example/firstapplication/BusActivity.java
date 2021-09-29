package com.example.firstapplication;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BusActivity extends AppCompatActivity {

    LinearLayout scrolllinearLayout;
    EditText entryText;

    static final String BUS_STOPS= "http://datamall2.mytransport.sg/ltaodataservice/BusStops";
    static final String BUS_ROUTE_API="http://datamall2.mytransport.sg/ltaodataservice/BusRoutes";
    static final String BUS_ARRIVAL_API="http://datamall2.mytransport.sg/ltaodataservice/BusArrivalv2";

    static final String API_KEY="vrJ4e/28RUKoH09HOP85yQ==";

    Map<String,String> busStopHashMap= new HashMap<String,String>();
    ArrayList<String[]> BusRouteInfo = new ArrayList<String[]>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_main);

        scrolllinearLayout=findViewById(R.id.scrollLinearView);
        entryText= findViewById(R.id.entryText);


        Button queryButton=findViewById(R.id.send_button);


        if(isOnline()){
            new RetrieveFeedTask(busStopHashMap, BusRouteInfo).execute();
            queryButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    new RetrieveFeedTask(busStopHashMap, BusRouteInfo).execute();
                }
            });
        }
        else{
            scrolllinearLayout.removeAllViews();

            Button internetError= new Button(scrolllinearLayout.getContext());
            internetError.setText("Network connection error. Please check your network connection and try again.");
            scrolllinearLayout.addView(internetError);

        }


    }

    //Boolean function to check the internet connection
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }


    class RetrieveFeedTask extends AsyncTask<String,Void,ArrayList<String[]>> {

        Map<String,String> busStopHashMap;
        ArrayList<String[]> BusRouteInfo;

        RetrieveFeedTask(Map<String,String>busStopHashMap,ArrayList<String[]> BusRouteInfo){
            this.busStopHashMap=busStopHashMap;
            this.BusRouteInfo = BusRouteInfo;
        }
        @Override
        protected ArrayList<String[]> doInBackground(String... strings) {

            APICALL mainBusCall = new APICALL();

            BusRouteInfo.clear();
            //This is me calling the api
            BusRouteInfo = mainBusCall.BusRouteInfoCall(entryText.getText().toString(), BUS_ROUTE_API, API_KEY, BusRouteInfo);
            System.out.println("First phase complete");

            //Second, bus stop API call
            if(busStopHashMap.size()==0){
                busStopHashMap = mainBusCall.BusStopHashMap(entryText.getText().toString(), BUS_STOPS, API_KEY, busStopHashMap, BusRouteInfo);
                System.out.println("Second phase complete");
            }

            //Matching process after phase 1 and 2
            for (int i = 0; i < BusRouteInfo.size(); i++) {
                String key = BusRouteInfo.get(i)[2];
                BusRouteInfo.get(i)[2] = busStopHashMap.get(key);
            }

            //Finally, the bus arrival API call
            BusRouteInfo = mainBusCall.BusArrivalCall(entryText.getText().toString(), BUS_ARRIVAL_API, API_KEY, BusRouteInfo);
            System.out.println("Third phase complete");

            return BusRouteInfo;

        }

        protected void onPostExecute(final ArrayList<String[]> BusRouteInfo) {


            scrolllinearLayout.removeAllViews();
            if(BusRouteInfo.size()==0){
                Button noBus= new Button(scrolllinearLayout.getContext());
                noBus.setText("Please enter a valid bus number");
                scrolllinearLayout.addView(noBus);
                return;
            }

            try{

                Button route1= new Button(scrolllinearLayout.getContext());
                route1.setTag("route1");

                Button route2= new Button(scrolllinearLayout.getContext());
                route2.setTag("route2");

                if(BusRouteInfo.get(BusRouteInfo.size()-1)[1].compareTo("2")==0){

                    route1.setText(BusRouteInfo.get(0)[2]+"\n"+"to\n"+BusRouteInfo.get((BusRouteInfo.size()/2))[2]);
                    scrolllinearLayout.addView(route1);
                    route2.setText(BusRouteInfo.get((BusRouteInfo.size()/2))[2]+"\n"+"to\n"+BusRouteInfo.get(BusRouteInfo.size()-1)[2]);
                    scrolllinearLayout.addView(route2);

                    route1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent= new Intent(scrolllinearLayout.getContext(), BusRoute.class);
                            intent.putExtra("BusRouteInfo",BusRouteInfo);
                            intent.putExtra("routeDirection",1);
                            startActivity(intent);
                        }
                    });

                    route2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent= new Intent(scrolllinearLayout.getContext(), BusRoute.class);
                            intent.putExtra("BusRouteInfo",BusRouteInfo);
                            intent.putExtra("routeDirection",2);
                            startActivity(intent);
                        }
                    });
                }
                else{
                    route1.setText(BusRouteInfo.get(0)[2]+"\n"+"to\n"+BusRouteInfo.get((BusRouteInfo.size()-1))[2]);
                    scrolllinearLayout.addView(route1);

                    route1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent= new Intent(getApplicationContext(), BusRoute.class);
                            intent.putExtra("BusRouteInfo",BusRouteInfo);
                            intent.putExtra("routeDirection",0);
                            startActivity(intent);
                        }
                    });
                }

            }
            catch(NullPointerException e){
                System.out.println("Exception");
            }
            //responseView.setText(response);

        }

    }
}
