package com.example.firstapplication;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class APICALL {

    ArrayList<String[]> BusRouteInfoCall (String entryText, String BUS_ROUTE_API,String API_KEY, ArrayList<String[]> BusRouteInfo){

        int counter=0;
        int endcounter=0;

        if(entryText.length()==0 ||entryText==null){

            return BusRouteInfo;
        }
        switch (entryText.charAt(0)){
            case '1':
                counter=0;
                endcounter=19;
                break;
            case '2':
                counter=19;
                endcounter=24;
                break;
            case '3':
                counter=24;
                endcounter=27;
                break;
            case '4':
                counter=27;
                endcounter=28;
                break;
            case '5':
                counter=28;
                endcounter=29;
                break;
            case '6':
                counter=29;
                endcounter=36;
                break;
            case '7':
                counter=36;
                endcounter=39;
                break;
            case '8':
                counter=39;
                endcounter=44;
                break;
            case '9':
                counter=44;
                endcounter=100;
                break;

        }

        while (true) {
            try {
                URL url = new URL(BUS_ROUTE_API + "?$skip=" + String.valueOf(counter*500));

                System.out.println(counter);
                counter++;

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("AccountKey", API_KEY);

                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();

                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }

                    bufferedReader.close();

                    JSONObject mainReader = new JSONObject(stringBuilder.toString());
                    JSONArray values = mainReader.getJSONArray("value");

                    //break condition
                    if ((counter==(endcounter+1))||(values.length() == 0)) {
                        break;
                    }


                    for (int i = 0; i < values.length(); i++) {

                        JSONObject busRoute = values.getJSONObject(i);
                        if(busRoute.getString("ServiceNo").equals(entryText)){

                            BusRouteInfo.add(new String[5]);

                            BusRouteInfo.get(BusRouteInfo.size()-1)[0] = busRoute.getString("ServiceNo")+"\n" ;
                            BusRouteInfo.get(BusRouteInfo.size()-1)[1] = busRoute.getString("Direction") ;
                            BusRouteInfo.get(BusRouteInfo.size()-1)[2] = busRoute.getString("BusStopCode") ;
                            BusRouteInfo.get(BusRouteInfo.size()-1)[3] = busRoute.getString("BusStopCode") ;
                        }
                    }

                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }
        return BusRouteInfo;
    }

    Map<String,String> BusStopHashMap (String entryText, String BUS_STOPS, String API_KEY, Map<String,String> busStopHashMap, ArrayList<String[]> BusRouteInfo){

        int counter =0;

        while(true){
            try{
                URL url = new URL(BUS_STOPS + "?$skip=" + String.valueOf(counter*500));
                counter++;

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("AccountKey", API_KEY);

                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;

                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }

                    JSONObject mainReader = new JSONObject(stringBuilder.toString());
                    JSONArray values = mainReader.getJSONArray("value");

                    bufferedReader.close();
                    //break condition
                    if (values.length() == 0){
                        break;
                    }

                    for(int i=0;i<values.length();i++){
                        JSONObject busStop = values.getJSONObject(i);
                        busStopHashMap.put(busStop.getString("BusStopCode"),busStop.getString("Description"));
                    }
                }
                finally {
                    urlConnection.disconnect();
                }
            }
            catch (Exception e){
                System.out.println("ERROR OCCUR");
                e.printStackTrace();
            }
        }

        System.out.println("Total number of bus stops: "+busStopHashMap.size());
        return busStopHashMap;
    }

    ArrayList<String[]> BusArrivalCall (String entryText, String BUS_ARRIVAL_API,String API_KEY, ArrayList<String[]> BusRouteInfo){

        for(int i=0;i<BusRouteInfo.size();i++){
            try{

                URL url = new URL(BUS_ARRIVAL_API + "?BusStopCode=" +BusRouteInfo.get(i)[3]+"&ServiceNo="+ entryText);


                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("AccountKey", API_KEY);


                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();

                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }

                    JSONObject mainReader = new JSONObject(stringBuilder.toString());
                    JSONArray services = mainReader.getJSONArray("Services");
                    JSONObject allBusData= services.getJSONObject(0);
                    JSONObject nextBus= allBusData.getJSONObject("NextBus");


                    //get current date time.
                    Date currentDate= Calendar.getInstance().getTime();
                    DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    Date nextTiming= dateFormat.parse(nextBus.getString("EstimatedArrival"));

                    long difference= nextTiming.getTime()-currentDate.getTime();
                    if(difference<0)
                        difference =0;
                    BusRouteInfo.get(i)[4]=String.valueOf(difference/1000/60);

                    bufferedReader.close();
                }
                finally {
                    urlConnection.disconnect();
                }
            }
            catch (Exception e){
                System.out.println("ERROR OCCUR");
            }
        }
        return BusRouteInfo;
    }

}
