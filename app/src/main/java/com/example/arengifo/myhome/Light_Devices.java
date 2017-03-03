package com.example.arengifo.myhome;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Button;
import android.app.Activity;
import android.view.Menu;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.graphics.Color;
import android.app.Activity;
import android.content.Intent;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.View;
import android.os.Bundle;

import android.view.ViewGroup.LayoutParams;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Light_Devices extends AppCompatActivity {

    Button Light_1;
    LinearLayout Linear;
    Button [] button;
    View.OnClickListener listener;
    Long Num_Buttons;
    Map<String, Object> light_map;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light__devices);
        Light_1= (Button) findViewById(R.id.Light_1);

        //initializing database reference
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        // Capture button clicks

        mDatabaseReference.child("Light_SW").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {


                Num_Buttons= snapshot.getChildrenCount()-1;
                light_map = (HashMap<String,Object>) snapshot.getValue();
                String[] Button_List = light_map.values().toArray(new String[0]);//reparar el error de tipo de variable para el primer valor Devices es Long



            }
            @Override public void onCancelled(DatabaseError error) {

            }
        });
        Light_1.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {

                // Start NewActivity.class
                Intent myIntent = new Intent(Light_Devices.this,Lights.class);
                startActivity(myIntent);
            }
        });




        String[] num_array_name={"U123","U124","U125"};
        Linear = (LinearLayout) findViewById(R.id.Linear_Layout);

        LayoutParams param = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f);

        Button[] btn = new Button[num_array_name.length];
        for (int i = 0; i < num_array_name.length; i++) {
            btn[i] = new Button(getApplicationContext());
            btn[i].setId(i);
            btn[i].setText(num_array_name[i].toString());
            btn[i].setTextColor(Color.parseColor("#000000"));
            btn[i].setTextSize(20);
            btn[i].setHeight(100);
            btn[i].setLayoutParams(param);
            btn[i].setPadding(15, 5, 15, 5);
            Linear.addView(btn[i]);

            btn[i].setOnClickListener(handleOnClick(btn[i]));

        }
    }



    View.OnClickListener handleOnClick(final Button button) {
        return new View.OnClickListener() {
            public void onClick(View v) {
            }
        };
    }


}
