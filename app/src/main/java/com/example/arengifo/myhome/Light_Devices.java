package com.example.arengifo.myhome;

import android.provider.SyncStateContract;
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

import com.google.android.gms.common.api.BooleanResult;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.R.id.list;


public class Light_Devices extends AppCompatActivity {

    Button Light_1;
    LinearLayout Linear;
    LinearLayout Linear_state;

    Button [] button;
    View.OnClickListener listener;
    Long Num_Buttons;
    Map<String, Object> light_map;
    Map<String, Map<String, Object>> Light_Name;
    Map<Boolean, Map<Boolean, Object>> Light_State;
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



                light_map = (HashMap<String,Object>) snapshot.getValue();
                Light_Name = (Map<String, Map<String, Object>>) snapshot.getValue();
                Light_State =(Map<Boolean, Map<Boolean, Object>>) snapshot.getValue();
                Long value=(Long) light_map.get("Devices");
                Num_Buttons =value;

                //String[] Button_List = light_map.values().toArray(new String[0]);//reparar el error de tipo de variable para el primer valor Devices es Long
                Iterator myVeryOwnIterator = light_map.keySet().iterator();
                while(myVeryOwnIterator.hasNext()) {
                    String key=(String)myVeryOwnIterator.next();

                    if (key.equals("Devices"))
                    {
                        //Long value=(Long) light_map.get(key);
                        //Num_Buttons =value;
                    }
                    else
                    {
                        String name= (String) Light_Name.get(key).get("Name");
                        Boolean state = (Boolean) Light_State.get(key).get("State");
                        String name2=name+state.toString();

                        Linear = (LinearLayout) findViewById(R.id.Linear_Layout);
                        Linear_state=(LinearLayout) findViewById(R.id.Linear_Layout_state);

                        LayoutParams param = new LinearLayout.LayoutParams(
                              LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f);


                        Button  btn = new Button(getApplicationContext());
                            btn.setTag("btn_"+key);//Set ID in String
                            if(name.equals("Empty"))
                            {
                                btn.setText(key);
                            }
                            else
                            {
                                btn.setText(name);
                            }
                            btn.setTextColor(Color.parseColor("#000000"));
                            btn.setTextSize(20);
                            btn.setHeight(100);
                            btn.setLayoutParams(param);
                            btn.setPadding(15, 5, 15, 5);
                            Linear.addView(btn);

                            btn.setOnClickListener(handleOnClick(btn));


                    }

                }


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





    }



    View.OnClickListener handleOnClick(final Button button) {
        return new View.OnClickListener() {
            public void onClick(View v) {
            }
        };
    }


}
