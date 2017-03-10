package com.example.arengifo.myhome;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
    ImageButton Edit;
    ImageButton Delete;
    TextView switchStatus;
    private String m_Text = "";
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mDatabaseReference_devices;
    Map<String,Object> light_map;
    Map<String,Object> Light_Name;
    Map<Boolean,Object> Light_State;
    Map<Long,Object> light_devices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lights);
        Bundle extras = getIntent().getExtras();
        final String Tag = extras.getString("Tag_Id").substring(4);
        switchStatus = (TextView) findViewById(R.id.switchStatus);
        Edit = (ImageButton) findViewById(R.id.btn_edit);
        Edit.setTag("edi_"+Tag);
        Edit.setOnClickListener(handleOnClick(Edit));
        Delete = (ImageButton) findViewById(R.id.btn_delete);
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

    View.OnClickListener handleOnClick(final ImageButton button) {
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

}
