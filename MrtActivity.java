package com.example.firstapplication;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MrtActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mrt);

        Date currentTime= Calendar.getInstance().getTime();

        Date d[]= new Date[10];
        final String stationDir[]= new String[10];
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String currentStringTime= sdf.format(currentTime);

        try {
            currentTime= sdf.parse(currentStringTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
                //circle
                d[0] = sdf.parse("23:03:00");
                stationDir[0]="Harbourfront to Dhoby Ghaut";
                d[1] = sdf.parse("22:48:00");
                stationDir[1]="Dhoby Ghaut to Harbourfront";

                //NEL
                d[2] = sdf.parse("23:55:00");
                stationDir[2]="Harbourfront to Punggol";
                d[3] = sdf.parse("23:28:00");
                stationDir[3]="Punggol to Harbourfront";


                //NSL
                d[4] = sdf.parse("22:46:00");
                stationDir[4]="Jurong East to Marina South Pier";
                d[5] = sdf.parse("23:48:00");
                stationDir[5]="Marina South Pier to Jurong East";

                //EWL
                d[6] = sdf.parse("23:20:00");
                stationDir[6]="Tuas Link to Pasir Ris";
                d[7] = sdf.parse("23:23:00");
                stationDir[7]="Pasir Ris to Tuas Link";

                //Downtown
                d[8] = sdf.parse("23:04:00");
                stationDir[8]="Expo to Bukit Panjang";
                d[9] = sdf.parse("23:35:00");
                stationDir[9]="Bukit Panjang to Expo";

            } catch (Exception ex) {
                ex.printStackTrace();
            }



            new CountDownTimer(d[0].getTime()-currentTime.getTime(),60000){
                TextView circle=(TextView)findViewById(R.id.circle_view);
                public void onTick(long millisUntilFinished) {
                    circle.setText(stationDir[0]+":\n"+
                            "Time remaining: " + millisUntilFinished/3600000+
                                    ":" + (millisUntilFinished/60000)%60+"\n"

                    );
                }
                public void onFinish() {
                    circle.setText("CLOSED :(");
                }
            }.start();

        new CountDownTimer(d[1].getTime()-currentTime.getTime(),60000){
            TextView circle=(TextView)findViewById(R.id.circle_view);
            public void onTick(long millisUntilFinished) {
                circle.append(stationDir[1]+":\n"+
                        "Time remaining: " + millisUntilFinished/3600000+
                        ":" + (millisUntilFinished/60000)%60+"\n");
            }
            public void onFinish() {
                circle.append("CLOSED :(");
            }
        }.start();

        new CountDownTimer(d[2].getTime()-currentTime.getTime(),60000){
            TextView northeast=(TextView)findViewById(R.id.north_east_view);
            public void onTick(long millisUntilFinished) {
                northeast.setText(stationDir[2]+":\n"+
                        "Time remaining: " + millisUntilFinished/3600000+
                        ":" + (millisUntilFinished/60000)%60+"\n"

                );
            }
            public void onFinish() {
                northeast.setText("CLOSED :(");
            }
        }.start();

        new CountDownTimer(d[3].getTime()-currentTime.getTime(),60000){
            TextView northeast=(TextView)findViewById(R.id.north_east_view);
            public void onTick(long millisUntilFinished) {
                northeast.append(stationDir[3]+":\n"+
                        "Time remaining: " + millisUntilFinished/3600000+
                        ":" + (millisUntilFinished/60000)%60+"\n");
            }
            public void onFinish() {
                northeast.append("CLOSED :(");
            }
        }.start();

        new CountDownTimer(d[4].getTime()-currentTime.getTime(),60000){
            TextView downtown=(TextView)findViewById(R.id.downtown_view);
            public void onTick(long millisUntilFinished) {
                downtown.setText(stationDir[4]+":\n"+
                        "Time remaining: " + millisUntilFinished/3600000+
                        ":" + (millisUntilFinished/60000)%60+"\n"

                );
            }
            public void onFinish() {
                downtown.setText("CLOSED :(");
            }
        }.start();

        new CountDownTimer(d[5].getTime()-currentTime.getTime(),60000){
            TextView downtown=(TextView)findViewById(R.id.downtown_view);
            public void onTick(long millisUntilFinished) {
                downtown.append(stationDir[5]+":\n"+
                        "Time remaining: " + millisUntilFinished/3600000+
                        ":" + (millisUntilFinished/60000)%60+"\n");
            }
            public void onFinish() {
                downtown.append("CLOSED :(");
            }
        }.start();
        new CountDownTimer(d[6].getTime()-currentTime.getTime(),60000){
            TextView eastwest=(TextView)findViewById(R.id.east_west_view);
            public void onTick(long millisUntilFinished) {
                eastwest.setText(stationDir[6]+":\n"+
                        "Time remaining: " + millisUntilFinished/3600000+
                        ":" + (millisUntilFinished/60000)%60+"\n"

                );
            }
            public void onFinish() {
                eastwest.setText("CLOSED :(");
            }
        }.start();

        new CountDownTimer(d[7].getTime()-currentTime.getTime(),60000){
            TextView eastwest=(TextView)findViewById(R.id.east_west_view);
            public void onTick(long millisUntilFinished) {
                eastwest.append(stationDir[7]+":\n"+
                        "Time remaining: " + millisUntilFinished/3600000+
                        ":" + (millisUntilFinished/60000)%60+"\n");
            }
            public void onFinish() {
                eastwest.append("CLOSED :(");
            }
        }.start();
        new CountDownTimer(d[8].getTime()-currentTime.getTime(),60000){
            TextView northsouth=(TextView)findViewById(R.id.north_south_view);
            public void onTick(long millisUntilFinished) {
                northsouth.setText(stationDir[8]+":\n"+
                        "Time remaining: " + millisUntilFinished/3600000+
                        ":" + (millisUntilFinished/60000)%60+"\n"

                );
            }
            public void onFinish() {
                northsouth.setText("CLOSED :(");
            }
        }.start();

        new CountDownTimer(d[9].getTime()-currentTime.getTime(),60000){
            TextView northsouth=(TextView)findViewById(R.id.north_south_view);
            public void onTick(long millisUntilFinished) {
                northsouth.append(stationDir[9]+":\n"+
                        "Time remaining: " + millisUntilFinished/3600000+
                        ":" + (millisUntilFinished/60000)%60+"\n");
            }
            public void onFinish() {
                northsouth.append("CLOSED :(");
            }
        }.start();


    }
}