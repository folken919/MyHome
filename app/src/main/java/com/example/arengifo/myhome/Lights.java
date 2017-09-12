package com.example.arengifo.myhome;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.app.Activity;
import android.view.Menu;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.graphics.Color;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;



public class Lights extends AppCompatActivity {

    String progressChan;
    Switch light;
    SeekBar DimmerSeekBar;
    RelativeLayout RelativeLayout;
    FloatingActionButton Edit;
    FloatingActionButton Delete;
    TextView switchStatus;
    private String m_Text = "";
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mDatabaseReference_devices;
    Map<String,Object> light_map;
    Map<String,Object> Light_Name;
    Map<Boolean,Object> Light_State;
    Map<Long,Object> Light_Dimmer;
    Map<Long,Object> light_devices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lights);
        Bundle extras = getIntent().getExtras();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.home50);
        final String Tag = extras.getString("Tag_Id").substring(4);
        switchStatus = (TextView) findViewById(R.id.switchStatus);
        Edit = (FloatingActionButton) findViewById(R.id.floatingEditBtn);
        Edit.setTag("edi_"+Tag);
        Edit.setOnClickListener(handleOnClick(Edit));
        Delete = (FloatingActionButton) findViewById(R.id.floatingDelete);
        Delete.setTag("del_"+Tag);
        Delete.setOnClickListener(handleOnClickBtnDel(Delete));
                //initializing database reference
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mDatabaseReference_devices = FirebaseDatabase.getInstance().getReference();
        // Capture button clicks
        light = (Switch) findViewById(R.id.light);// initiate Switch
        light.setTag("swi_"+Tag);


        mDatabaseReference_devices.child("Light_SW").addListenerForSingleValueEvent(new ValueEventListener(){

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                light_devices = (HashMap<Long,Object>) snapshot.getValue();
            }

            @Override public void onCancelled(DatabaseError error) {

            }
        });

        mDatabaseReference.child("Light_SW/"+Tag).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {



                light_map = (HashMap<String,Object>) snapshot.getValue();
                Light_Name = (HashMap<String, Object>) snapshot.getValue();
                Light_State =(HashMap<Boolean,Object>) snapshot.getValue();
                Light_Dimmer=(HashMap<Long,Object>) snapshot.getValue();


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
                    if (key.equals("Name"))
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
                    if (key.equals("Dimmer"))
                    {
                        Long dimmer_value= 128 - (Long) Light_Dimmer.get(key);
                        Integer dim_int = (int) (long) dimmer_value;
                        DimmerSeekBar.setProgress(dim_int);
                        DecimalFormat oneDigit = new DecimalFormat("#,##0.0");//format to 1 decimal place
                        Double progressChanged=new Double(dimmer_value);
                        progressChanged= progressChanged/128*100;
                        Long dimmervalue= Math.round(progressChanged);
                        //progressChanged = Double.valueOf(oneDigit.format(progressChanged));
                        progressChan = "Intensidad de Luz :" +new Long(dimmervalue).toString()+"%";
                        Toast.makeText(Lights.this, progressChan,
                               Toast.LENGTH_SHORT).show();


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
                    mDatabaseReference.child("Light_SW/"+Tag+"/DataChanged").setValue(true);
                }else{
                    switchStatus.setText("OFF");
                    switchStatus.setTextColor(Color.parseColor("#FFFFFF"));
                    mDatabaseReference.child("Light_SW/"+Tag+"/State").setValue(false);
                    mDatabaseReference.child("Light_SW/"+Tag+"/DataChanged").setValue(true);
                }

            }
        });


        DimmerSeekBar=(SeekBar)findViewById(R.id.simpleSeekBar);
        // perform seek bar change listener event used for getting the progress value
        DimmerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                mDatabaseReference.child("Light_SW/"+Tag+"/Dimmer").setValue(128-progressChangedValue);
                mDatabaseReference.child("Light_SW/"+Tag+"/DataChanged").setValue(true);
                DecimalFormat oneDigit = new DecimalFormat("#,##0.0");//format to 1 decimal place
                Double progressChanged=new Double(progressChangedValue);
                progressChanged= progressChanged/128*100;
                Long dimmervalue= Math.round(progressChanged);
                //progressChanged = Double.valueOf(oneDigit.format(progressChanged));
                progressChan = "Intensidad de Luz :" +new Long(dimmervalue).toString()+"%";
                Toast.makeText(Lights.this, progressChan,
                        Toast.LENGTH_SHORT).show();



            }
        });

    }

    View.OnClickListener handleOnClick(final FloatingActionButton button) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                // Start NewActivity.class
               final String tag=button.getTag().toString().substring(4);
                AlertDialog.Builder builder = new AlertDialog.Builder(Lights.this);
                builder.setTitle("Nombre del Dispositivo");

// Set up the input
                final EditText input = new EditText(Lights.this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        if(m_Text.equals(null)||m_Text.equals(""))
                        {
                            dialog.cancel();
                        }
                        else
                        {
                            mDatabaseReference.child("Light_SW/"+tag+"/Name").setValue(m_Text);
                        }


                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        };
    }


    View.OnClickListener handleOnClickBtnDel(final ImageButton button) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                // Start NewActivity.class
                final String tag=button.getTag().toString().substring(4);
                AlertDialog.Builder builder = new AlertDialog.Builder(Lights.this);
                builder.setTitle("Esta Seguro de Eliminar el Dispositivo?");

// Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //Obtenr valor con numero de dispositivos restarle el dispositivo a eliminar
                        final Long value=(Long) light_devices.get("Devices");
                        final Long Num_Devices =value -1;
                        mDatabaseReference.child("Light_SW/Devices").setValue(Num_Devices);
                        //Eliminar la entrada de la base de datos
                        mDatabaseReference.child("Light_SW/"+tag).removeValue();

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        };
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Display display = ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();
        if(rotation==0 || rotation==2)
        {
            RelativeLayout =(RelativeLayout) findViewById(R.id.activity_lights);
            RelativeLayout.setBackgroundResource(R.drawable.natureopt);
        }
        if(rotation==1 || rotation==3)
        {
            RelativeLayout =(RelativeLayout) findViewById(R.id.activity_lights);
            RelativeLayout.setBackgroundResource(R.drawable.cropped_opt);
        }
        super.onConfigurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);
    }
}
