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
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaxiActivity extends AppCompatActivity implements GoogleMap.OnMyLocationButtonClickListener,OnMapReadyCallback {


    private static final String TAG = "MyMap";
    private static final int REQUEST_LOCATION_PERMISSION = 100;
    public GoogleMap mMap;


    //Get the current date time in this format
    Date currentDate= Calendar.getInstance().getTime();
    DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    String dateString= dateFormat.format(currentDate);

    final String API_URL= "https://api.data.gov.sg/v1/transport/taxi-availability?date_time="+dateString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taxi_maps);

        TextView newText=(TextView)findViewById(R.id.taxi_button);
        newText.setMovementMethod(LinkMovementMethod.getInstance());

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

    public void callTaxi(View view){

    }

    public void createAllMarker(View view){
        //onMapReady(mMap);
        mMap.clear();
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
    public class RetrieveFeedTask extends AsyncTask<String,Void, List<LatLng>>{

        //doInBackground is a predefiedfunction that allows porcesses to be run in the background(networking, general processes)
        //Note that UI actions cannot be done in the background thread (things like placing markers).
        @Override
        protected List<LatLng> doInBackground(String... strings) {
            List<LatLng> TaxiCoordinates= new ArrayList<LatLng>();
            try{
                URL url= new URL(API_URL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();

                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();

                    //first JSONObject; scope is the entire JSON file
                    JSONObject reader= new JSONObject(stringBuilder.toString());
                    JSONArray features= reader.getJSONArray("features");
                    JSONObject objects= features.getJSONObject(0);
                    JSONObject geometry=objects.getJSONObject("geometry");

                    JSONArray coordinates=geometry.getJSONArray("coordinates");

                    for(int i=0; i<coordinates.length();i++){

                        JSONArray singleCoordinate=coordinates.getJSONArray(i);
                        Double truncatedLon= BigDecimal.valueOf(singleCoordinate.getDouble(0))
                                .setScale(5, RoundingMode.HALF_UP)
                                .doubleValue();

                        Double truncatedLat= BigDecimal.valueOf(singleCoordinate.getDouble(1))
                                .setScale(5, RoundingMode.HALF_UP)
                                .doubleValue();
                        LatLng location= new LatLng(truncatedLat,truncatedLon);
                        TaxiCoordinates.add(location);

                    }
                    return TaxiCoordinates;

                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch (Exception e) {
                Log.e("ERROR",e.getMessage(), e);
                return null;
            }
        }


        //On post execute we can perform UI processes. Note that the parameter for on Post execute can be the
        //return value for the onBackground
        @Override
        protected void onPostExecute(List<LatLng> latLngs) {

            int taxiNum= latLngs.size();
            Toast.makeText(getApplicationContext(),"Total Taxis now: "+String.valueOf(taxiNum),Toast.LENGTH_LONG).show();

            for(int i=0; i<latLngs.size();i++){
                LatLng test= new LatLng(latLngs.get(i).latitude,latLngs.get(i).longitude);
                String info = String.format(Locale.getDefault(), "Lat: %1$.3f, Long: %2$.3f", test.latitude, test.longitude);
                mMap.addMarker(new MarkerOptions().position(test).title("Taxi Marker").snippet(info));
            }
        }
    }


}
