package com.example.firstapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

public class CarparkActivity extends AppCompatActivity implements GoogleMap.OnMyLocationButtonClickListener,OnMapReadyCallback {


    private static final String TAG = "MyMap";
    private static final int REQUEST_LOCATION_PERMISSION = 100;
    public GoogleMap mMap;


    static final String API_URL= "http://datamall2.mytransport.sg/ltaodataservice/CarParkAvailabilityv2";
    static final String API_KEY= "vrJ4e/28RUKoH09HOP85yQ==";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carpark_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        if(isOnline()){
            new RetrieveFeedTask().execute(API_URL);
        }
        else{
            Toast.makeText(getApplicationContext(),"Network connection error. Please check your network connection and try again.",Toast.LENGTH_LONG).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_options, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.normal_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            case R.id.hybrid_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            case R.id.satellite_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            case R.id.terrain_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
    private void setPoiClick(final GoogleMap map) {
        LatLng home = new LatLng(1.358348, 103.762598);
        final String snippet = String.format(Locale.getDefault(), "Lat: %1$.3f, Long: %2$.3f", home.latitude, home.longitude);

        map.setOnPoiClickListener(new GoogleMap.OnPoiClickListener() {
            @Override
            public void onPoiClick(PointOfInterest poi) { Marker poiMarker = mMap.addMarker(new MarkerOptions().position(poi.latLng).title(poi.name).snippet(snippet));
                poiMarker.showInfoWindow();
            }

        });

    }
    //short click to place a marker
    private void setLongMapClick(final GoogleMap map) {
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            //tells us what to display when the marker is clicked
            @Override
            public void onMapLongClick(LatLng latLng) {
                String snippet = String.format(Locale.getDefault(),
                        "Lat: %1$.3f, Long: %2$.3f",
                        latLng.latitude,
                        latLng.longitude);

                map.addMarker(new MarkerOptions().position(latLng).title(getString(R.string.dropped_pin)).snippet(snippet));
            }

        });

    }
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationButtonClickListener(this);


        } else {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // Check if location permissions are granted and if so enable the
        // location data layer.
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocation();
                    break;
                }
        }
    }



    public void onLocationChanged() {
        LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if(location!= null) {

            double latitude = location.getLatitude();
            double longitude= location.getLongitude();

            System.out.println("Current Long: "+longitude+"\n");
            System.out.println("Current Lat: "+latitude+"\n");
            Toast.makeText(this,("Lat: "+latitude+"\n"
                    +"Lon: "+longitude),Toast.LENGTH_SHORT).show();

        }
        else{
            Toast.makeText(this,"No Location detected. Please check your GPS.",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onMyLocationButtonClick() {
        onLocationChanged();
        return false;
    }

    public void clearAllMarkers(View view){
        mMap.clear();
    }

    public void createAllMarker(View view){
        //onMapReady(mMap);
        new RetrieveFeedTask().execute(API_URL);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        float zoom= 15;


        enableMyLocation();

        LatLng home = new LatLng(1.358348, 103.762598);
        String snippet = String.format(Locale.getDefault(), "Lat: %1$.3f, Long: %2$.3f", home.latitude, home.longitude);
        //mMap.addMarker(new MarkerOptions().position(home).title("Marker in home").snippet(snippet));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(home,zoom));

        setLongMapClick(mMap);
        setPoiClick(mMap);

        try{
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.map_style));
            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        }
        catch(Resources.NotFoundException e){
            Log.e(TAG,"Can't find style",e);
        }


    }


    //This entire fkin class is merely meant to process thr API data as well as process the
    //retrieved data to put as markers on the UI screen.


    //AsyncTask is a method to specifically allow the use of certain actions on the main thread.
    //An alternative method would be to create and switch to a seperate thread but it's more tedious and needs thread management control.


    //AsyncTask has 3 parameters namely: params, loadingType and returnType
    public class RetrieveFeedTask extends AsyncTask<String,Void, ArrayList<String[]>>{

        //doInBackground is a predefiedfunction that allows porcesses to be run in the background(networking, general processes)
        //Note that UI actions cannot be done in the background thread (things like placing markers).
        @Override
        protected ArrayList<String[]> doInBackground(String... strings) {
            ArrayList<String[]> CarparkInfo = new ArrayList<String[]>();
            while (true) {
                try {
                    URL url = new URL(API_URL + "?$skip=" + String.valueOf( CarparkInfo.size()));
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
                        if (values.length() == 0) {
                            break;
                        }

                        int currentSize;
                        if(CarparkInfo==null){
                            currentSize=0;
                        }
                        else{
                            currentSize=CarparkInfo.size();
                        }

                        for (int i = 0; i < values.length(); i++) {
                            CarparkInfo.add(new String[4]);
                            JSONObject carpark = values.getJSONObject(i);

                            try {

                                String coordinates[] = (carpark.getString("Location")).split(" ");

                                //latitude
                                CarparkInfo.get(i+currentSize)[0] = coordinates[0];
                                //Longitude
                                CarparkInfo.get(i+currentSize)[1] = coordinates[1];

                            } catch (ArrayIndexOutOfBoundsException e) {
                                System.out.println("Caught");
                                continue;
                            }

                            CarparkInfo.get(i+currentSize)[2] = carpark.getString("Development") + "\n";
                            CarparkInfo.get(i+currentSize)[3] = carpark.getString("AvailableLots") + "\n";


                        }

                    } finally {
                        urlConnection.disconnect();
                    }
                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage(), e);
                    return null;
                }
            }
            return CarparkInfo;
        }


        //On post execute we can perform UI processes. Note that the parameter for on Post execute can be the
        //return value for the onBackground
        @Override
        protected void onPostExecute(ArrayList<String[]> CarparkInfo) {

            try{
                int taxiNum= CarparkInfo.size();

                System.out.println("Total no of carparks: "+taxiNum );
                Toast.makeText(getApplicationContext(),"Total Carparks : "+String.valueOf(taxiNum),Toast.LENGTH_LONG).show();

            }
            catch(NullPointerException e){
                System.out.println("OK");
            }

            for(int i=0; i<CarparkInfo.size();i++){
                try{
                    LatLng test= new LatLng(Double.parseDouble(CarparkInfo.get(i)[0]),Double.parseDouble(CarparkInfo.get(i)[1]));
                    //String info = String.format(Locale.getDefault(), "Lat: %1$.3f, Long: %2$.3f", test.latitude, test.longitude);
                    String lots= CarparkInfo.get(i)[3];
                    mMap.addMarker(new MarkerOptions().position(test).title(CarparkInfo.get(i)[2]).snippet(lots));
                }
                catch(NumberFormatException e){
                    continue;
                }
                catch(NullPointerException e){
                    continue;
                }
            }
        }
    }
}
