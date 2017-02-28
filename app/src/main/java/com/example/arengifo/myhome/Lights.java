package com.example.arengifo.myhome;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Switch;
import android.app.Activity;
import android.view.Menu;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.graphics.Color;

public class Lights extends AppCompatActivity {

    Switch light;
    TextView switchStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lights);

        switchStatus = (TextView) findViewById(R.id.switchStatus);
        light = (Switch) findViewById(R.id.light);// initiate Switch
        light.setText("Iluminacion Salon");


        light.setOnCheckedChangeListener(new OnCheckedChangeListener() {


            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){

                    switchStatus.setText("ON");
                    switchStatus.setTextColor(Color.parseColor("#FF66CC"));
                }else{
                    switchStatus.setText("OFF");
                    switchStatus.setTextColor(Color.parseColor("#999999"));
                }

            }
        });


    }
}
