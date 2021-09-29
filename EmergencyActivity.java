package com.example.firstapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class EmergencyActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);


        TextView newText2=(TextView)findViewById(R.id.text_view2);
        newText2.setMovementMethod(LinkMovementMethod.getInstance());

        TextView newText3=(TextView)findViewById(R.id.text_view3);
        newText3.setMovementMethod(LinkMovementMethod.getInstance());

        TextView newText4=(TextView)findViewById(R.id.text_view4);
        newText4.setMovementMethod(LinkMovementMethod.getInstance());

        TextView newText5=(TextView)findViewById(R.id.text_view5);
        newText5.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
