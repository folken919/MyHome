package com.example.arengifo.myhome;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class Socket_Devices extends AppCompatActivity {


    LinearLayout Linear;
    LinearLayout Linear_state;
    int Count=0;
    Button [] button;
    OnClickListener listener;
    Long Num_Buttons;
    Map<String, Object> light_map;
    Map<String, Map<String, Object>> Light_Name;
    Map<Boolean, Map<Boolean, Object>> Light_State;
    private DatabaseReference mDatabaseReference;
    RelativeLayout RelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket__devices);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.home50);

        //initializing database reference
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        // Capture button clicks

         mDatabaseReference.child("Socket_PW").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {



                light_map = (HashMap<String,Object>) snapshot.getValue();
                Light_Name = (Map<String, Map<String, Object>>) snapshot.getValue();
                Light_State =(Map<Boolean, Map<Boolean, Object>>) snapshot.getValue();
                Long value=(Long) light_map.get("Devices");
                Num_Buttons =value;
                Count =0;
                TableLayout Table = (TableLayout) findViewById(R.id.TableLayout);

                //String[] Button_List = light_map.values().toArray(new String[0]);//reparar el error de tipo de variable para el primer valor Devices es Long
                Iterator myVeryOwnIterator = light_map.keySet().iterator();
                while(myVeryOwnIterator.hasNext()) {
                    String key=(String)myVeryOwnIterator.next();

                    if (key.equals("Devices"))
                    {
                        //Long value=(Long) light_map.get(key);
                        //Num_Buttons =value;
                    }
                    else if(key.contains("Device_"))
                    {

                        TableRow row= new TableRow(getApplicationContext());
                        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);

                        row.setLayoutParams(lp);
                        String name= (String) Light_Name.get(key).get("Name");
                        Boolean state = (Boolean) Light_State.get(key).get("State");

                        //String name2=name+state.toString();


                        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        param.weight = 1;

                        //Button  btn = new Button(getApplicationContext());
                        Button   btn=(Button)getLayoutInflater().inflate(R.layout.buttonstyle, null);
                            btn.setTag("btn_"+key);//Set ID in String
                            if(name.equals("Empty"))
                            {
                                btn.setText(key);
                            }
                            else
                            {
                                btn.setText(name);
                            }
                            //btn.setTextColor(Color.parseColor("#303F9F"));
                            btn.setTextSize(20);
                            btn.setHeight(100);
                           // btn.setLayoutParams(param);
                            btn.setPadding(15, 5, 15, 5);
                            //Linear.addView(btn);
                            btn.setOnClickListener(handleOnClick(btn));


                        final Switch State=(Switch) getLayoutInflater().inflate(R.layout.swbtnstyle, null);
                        State.setTag("swi_"+key);
                        State.setTextColor(Color.parseColor("#3399FF"));
                        State.setGravity(Gravity.RIGHT);
                        State.setPadding(15, 5, 15, 5);
                        if(state)
                        {
                            State.setChecked(state);
                        }
                        else
                        {
                            State.setChecked(state);
                        }
                        //State.setLayoutParams(param);
                        //Linear.addView(State);
                        State.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                String Tag=buttonView.getTag().toString().substring(4);
                                if(isChecked){

                                    mDatabaseReference.child("Socket_PW/"+Tag+"/State").setValue(true);
                                    mDatabaseReference.child("Socket_PW/"+Tag+"/DataChanged").setValue(true);
                                }else{

                                    mDatabaseReference.child("Socket_PW/"+Tag+"/State").setValue(false);
                                    mDatabaseReference.child("Socket_PW/"+Tag+"/DataChanged").setValue(true);
                                }
                                // do something, the isChecked will be
                                // true if the switch is in the On position
                            }
                        });
                        row.setGravity(Gravity.CENTER);
                        row.addView(btn);
                        row.addView(State);
                        Table.addView(row,Count);
                        Count=Count +1;

                    }

                }


            }
            @Override public void onCancelled(DatabaseError error) {

            }
        });




    }



    OnClickListener handleOnClick(final Button button) {
        return new OnClickListener() {
            public void onClick(View v) {
                // Start NewActivity.class
                String Tag_Id=v.getTag().toString();
                Intent myIntent = new Intent(Socket_Devices.this,Sockets.class);
                myIntent.putExtra("Tag_Id",Tag_Id);
                startActivity(myIntent);
            }
        };
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Display display = ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();
        if(rotation==0 || rotation==2)
        {
            RelativeLayout =(RelativeLayout) findViewById(R.id.activity_socket__devices);
            RelativeLayout.setBackgroundResource(R.drawable.maderaopt);
        }
        if(rotation==1 || rotation==3)
        {
            RelativeLayout =(RelativeLayout) findViewById(R.id.activity_socket__devices);
            RelativeLayout.setBackgroundResource(R.drawable.maderacropped_opt);
        }
        super.onConfigurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);
    }
    public void onBackPressed(){
        //String Timer_ID="Timer"+numtimers;
        Intent myIntent = new Intent(Socket_Devices.this,MainActivity.class);
        //myIntent.putExtra("Timer_ID",Timer_ID);
        startActivity(myIntent);

    }
}
