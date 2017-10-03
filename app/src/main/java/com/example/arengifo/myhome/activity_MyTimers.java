package com.example.arengifo.myhome;

import android.content.Context;
import android.content.res.Configuration;
import android.provider.SyncStateContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Button;
import android.app.Activity;
import android.view.Menu;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.graphics.Color;
import android.app.Activity;
import android.content.Intent;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.View;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

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


public class activity_MyTimers extends AppCompatActivity {


    LinearLayout Linear;
    LinearLayout Linear_state;
    int Count=0;
    Button [] button;
    View.OnClickListener listener;
    String Tag="";
    String device="";
    String Timer_ID="";
    Long Num_Buttons;
    Long numtimers;
    Map<String, Object> Device_Map;
    Map<String, Map<String, Object>> Timer_Name;
    Map<Boolean, Map<Boolean, Object>> Timer_State;
    Map<Long, Map<Long, Object>> Num_Timers;
    private DatabaseReference mDatabaseReference;
    RelativeLayout RelativeLayout;
    FloatingActionButton Refresh;
    FloatingActionButton addTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mytimers);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.home50);
        Bundle extras = getIntent().getExtras();
        Refresh = (FloatingActionButton) findViewById(R.id.floatingRefresh);
        Refresh.setOnClickListener(handleOnClickBtnRefresh(Refresh));
        addTimer=(FloatingActionButton) findViewById(R.id.floatingAddTimer);
        addTimer.setOnClickListener(handleOnClickBtnAddTimer(addTimer));
        Tag = extras.getString("Tag_Id");
        device=extras.getString("device");
        //initializing database reference
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        // Capture button clicks

        mDatabaseReference.child(device).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {



                Device_Map = (HashMap<String,Object>) snapshot.getValue();
                Timer_Name = (Map<String, Map<String, Object>>) snapshot.getValue();
                Timer_State =(Map<Boolean, Map<Boolean, Object>>) snapshot.getValue();
                Num_Timers=(Map<Long, Map<Long, Object>>) snapshot.getValue();
                // Long value=(Long) Device_Map.get("Devices");
               // Num_Buttons =value;
                Count =0;
                TableLayout Table = (TableLayout) findViewById(R.id.TableLayout);

                //String[] Button_List = Device_Map.values().toArray(new String[0]);//reparar el error de tipo de variable para el primer valor Devices es Long
                Iterator myVeryOwnIterator = Device_Map.keySet().iterator();
                while(myVeryOwnIterator.hasNext()) {
                    String key=(String)myVeryOwnIterator.next();

                    if (key.equals("Devices"))
                    {
                        //Long value=(Long) Device_Map.get(key);
                        //Num_Buttons =value;
                    }
                    else
                    {


                        numtimers=(long)  Num_Timers.get(key).get("Timers");
                        String name="";
                        Boolean state=false;
                        //String name2=name+state.toString();
                        for(int i=1;i<=numtimers;i++)
                        {
                            if(i==1)
                            {
                                Timer_ID ="Timer";
                                 name= (String) Timer_Name.get(key).get(Timer_ID+"Name");
                                 state = (Boolean) Timer_State.get(key).get(Timer_ID+"State");

                            }
                            else
                            {
                                Timer_ID ="Timer"+(i-1);
                                name= (String) Timer_Name.get(key).get(Timer_ID+"Name");
                                state = (Boolean) Timer_State.get(key).get(Timer_ID+"State");
                            }
                            TableRow row= new TableRow(getApplicationContext());
                            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);

                            row.setLayoutParams(lp);
                            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            param.weight = 1;

                            //Button  btn = new Button(getApplicationContext());
                            Button   btn=(Button)getLayoutInflater().inflate(R.layout.buttonstyle, null);
                            btn.setTag("btn_"+Timer_ID);//Set ID in String
                            if(name.equals("Empty"))
                            {
                                btn.setText(Timer_ID);
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
                            State.setTag("swi_"+Timer_ID);
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
                                    String TagTimer=buttonView.getTag().toString().substring(4);
                                    String ID=TagTimer.substring(5);
                                    if(isChecked){

                                        mDatabaseReference.child(device+"/"+Tag+"/"+TagTimer+"State").setValue(true);
                                        mDatabaseReference.child(device+"/"+Tag+"/DataChanged").setValue(true);
                                        mDatabaseReference.child(device+"/"+Tag+"/Timer"+ID+"On").setValue(true);
                                        mDatabaseReference.child(device+"/"+Tag+"/Timer"+ID+"Changed").setValue(true);
                                    }else{

                                        mDatabaseReference.child(device+"/"+Tag+"/"+TagTimer+"State").setValue(false);
                                        mDatabaseReference.child(device+"/"+Tag+"/DataChanged").setValue(true);
                                        mDatabaseReference.child(device+"/"+Tag+"/Tempor"+ID+"On").setValue(false);
                                        mDatabaseReference.child(device+"/"+Tag+"/Timer"+ID+"On").setValue(false);
                                        mDatabaseReference.child(device+"/"+Tag+"/Timer"+ID+"Changed").setValue(true);
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


            }
            @Override public void onCancelled(DatabaseError error) {

            }
        });




    }



    View.OnClickListener handleOnClick(final Button button) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                // Start NewActivity.class
                String Tag_Id=Tag;
                String Timer_ID=v.getTag().toString();
                Intent myIntent = new Intent(activity_MyTimers.this,activity_timers.class);
                myIntent.putExtra("device",device);
                myIntent.putExtra("Tag_Id",Tag_Id);
                myIntent.putExtra("Timer_ID",Timer_ID);
                startActivity(myIntent);
            }
        };
    }

    View.OnClickListener handleOnClickBtnRefresh(final ImageButton button) {
        return new View.OnClickListener() {
            public void onClick(View v) {

                finish();
                startActivity(getIntent());
                Toast.makeText(
                        activity_MyTimers.this,
                        "Se actualizo la informacion",
                        Toast.LENGTH_SHORT)
                        .show();

            }
        };
    }

    View.OnClickListener handleOnClickBtnAddTimer(final ImageButton button) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                long numerotimers=0;
                String Tag_Id=Tag;
                if(numtimers==1)
                {
                    numerotimers=numtimers;
                }
                else
                {
                    numerotimers=numtimers-1;
                }

                String Timer_ID="Timer"+numerotimers;
                Intent myIntent = new Intent(activity_MyTimers.this,activity_AddTimer.class);
                myIntent.putExtra("device",device);
                myIntent.putExtra("Tag_Id",Tag_Id);
                myIntent.putExtra("Timer_ID",Timer_ID);
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
            RelativeLayout =(RelativeLayout) findViewById(R.id.activity_light__devices);
            RelativeLayout.setBackgroundResource(R.drawable.maderaopt);
        }
        if(rotation==1 || rotation==3)
        {
            RelativeLayout =(RelativeLayout) findViewById(R.id.activity_light__devices);
            RelativeLayout.setBackgroundResource(R.drawable.maderacropped_opt);
        }
        super.onConfigurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);
    }

}
