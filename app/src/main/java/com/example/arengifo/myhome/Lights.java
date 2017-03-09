package com.example.arengifo.myhome;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.app.Activity;
import android.view.Menu;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.graphics.Color;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class Lights extends AppCompatActivity {

    Switch light;
    TextView switchStatus;
    private DatabaseReference mDatabaseReference;
    Map<String,Object> light_map;
    Map<String,Object> Light_Name;
    Map<Boolean,Object> Light_State;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lights);
        Bundle extras = getIntent().getExtras();
        final String Tag = extras.getString("Tag_Id").substring(4);
        switchStatus = (TextView) findViewById(R.id.switchStatus);
        //initializing database reference
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        // Capture button clicks
        light = (Switch) findViewById(R.id.light);// initiate Switch
        light.setTag("swi_"+Tag);



        mDatabaseReference.child("Light_SW/"+Tag).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {



                light_map = (HashMap<String,Object>) snapshot.getValue();
                Light_Name = (HashMap<String, Object>) snapshot.getValue();
                Light_State =(HashMap<Boolean,Object>) snapshot.getValue();

                //String[] Button_List = light_map.values().toArray(new String[0]);//reparar el error de tipo de variable para el primer valor Devices es Long
                Iterator myVeryOwnIterator = light_map.keySet().iterator();
                while(myVeryOwnIterator.hasNext()) {
                    String key=(String)myVeryOwnIterator.next();

                    if (key.equals("State"))
                    {
                        Boolean state = (Boolean) Light_State.get(key);
                        if(state)
                        {
                            light.setChecked(state);
                        }
                        else
                        {
                            light.setChecked(state);
                        }

                    }
                    else
                    {


                        String name= (String) Light_Name.get(key);

                        String name2=name.toString();


                        if(name.equals("Empty"))
                        {
                            light.setText(Tag);
                        }
                        else
                        {
                            light.setText(name);
                        }



                    }

                }


            }
            @Override public void onCancelled(DatabaseError error) {

            }
        });





        light.setOnCheckedChangeListener(new OnCheckedChangeListener() {


            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){

                    switchStatus.setText("ON");
                    switchStatus.setTextColor(Color.parseColor("#FF66CC"));
                    mDatabaseReference.child("Light_SW/"+Tag+"/State").setValue(true);
                }else{
                    switchStatus.setText("OFF");
                    switchStatus.setTextColor(Color.parseColor("#999999"));
                    mDatabaseReference.child("Light_SW/"+Tag+"/State").setValue(false);
                }

            }
        });


    }
}
