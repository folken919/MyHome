package com.example.arengifo.myhome;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;



public class Lights extends AppCompatActivity {

    String progressChan;
    Switch light;
    SeekBar DimmerSeekBar;
    RelativeLayout RelativeLayout;
    FloatingActionButton Edit;
    FloatingActionButton Delete;
    TextView switchStatus;
    TextView tvDisplayDate;
    TextView tvDisplayTime;
    DatePicker dpResult;
    Button btnChangeDate, btnChangeTime,btnTimerSave;
    RadioButton Repeat, onetime,timerOn, timerOff;
    RadioGroup timerActivate, timerChoice;
    RadioButton device_on, device_off;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    long Timer_timestamp;
    boolean[] checkedValues;
    String Tag="";
    String DateTime, Date_DatePicker, Time_Timepicker, Repeat_Days;
    private String m_Text = "";
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mDatabaseReference_devices;
    Map<String,Object> light_map;
    Map<String,Object> Light_Name;
    Map<Boolean,Object> Light_State;
    Map<Long,Object> Light_Dimmer;
    Map<Long,Object> light_devices;
    Map<Boolean,Object> TemporOn, TimeRepeat, TimSingle, TimerChanged, TimerOn, TimerRepeat, TimDevState;
    Map<Long,Object> TemporTime, TimRepeatTime, TimSingleDate;
    Map<String,Object> TimRepeatDay;
    AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lights);
        Bundle extras = getIntent().getExtras();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.home50);
        btnChangeDate =(Button) findViewById(R.id.get_date);
        btnChangeTime =(Button) findViewById(R.id.get_time);
        btnTimerSave =(Button) findViewById(R.id.Timer_Save);
        tvDisplayDate = (TextView) findViewById(R.id.date);
        tvDisplayTime = (TextView) findViewById(R.id.time);
        addListenerOnButtonDate();
        addListenerOnButtonTime();
        addListenerOnButtonTimerSave();
        Repeat=(RadioButton) findViewById(R.id.radio_repeat);
        onetime=(RadioButton) findViewById(R.id.radio_onetime);
        timerChoice=(RadioGroup) findViewById(R.id.Radio_timer);
        device_on=(RadioButton) findViewById(R.id.Device_On);
        device_off=(RadioButton) findViewById(R.id.Device_Off);
        timerOn=(RadioButton) findViewById(R.id.radio_timeron);
        timerOff=(RadioButton) findViewById(R.id.radio_timeroff);
        timerActivate = (RadioGroup) findViewById(R.id.Radio_ActivarTimer);
        Tag = extras.getString("Tag_Id").substring(4);
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
                TemporOn = (HashMap<Boolean,Object>) snapshot.getValue();
                TimeRepeat =(HashMap<Boolean,Object>) snapshot.getValue();
                TimSingle = (HashMap<Boolean,Object>) snapshot.getValue();
                TimerChanged = (HashMap<Boolean,Object>) snapshot.getValue();
                TimDevState = (HashMap<Boolean,Object>) snapshot.getValue();
                TimerOn = (HashMap<Boolean,Object>) snapshot.getValue();
                TimerRepeat = (HashMap<Boolean,Object>) snapshot.getValue();
                TemporTime =(HashMap<Long,Object>) snapshot.getValue();
                TimRepeatDay=(HashMap<String,Object>) snapshot.getValue();
                TimRepeatTime=(HashMap<Long,Object>) snapshot.getValue();
                TimSingleDate=(HashMap<Long,Object>) snapshot.getValue();
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

                    if (key.equals("TimerOn"))
                    {
                        Boolean timerstate=(Boolean) TimerOn.get(key);
                        if(timerstate)
                        {
                            timerOn.setChecked(true);
                            timerChoice.setEnabled(true);
                            onetime.setEnabled(true);
                            Repeat.setEnabled(true);
                            device_on.setEnabled(true);
                            device_off.setEnabled(true);
                            btnChangeTime.setEnabled(true);
                            btnChangeDate.setEnabled(true);
                            btnTimerSave.setEnabled(true);
                            tvDisplayTime.setEnabled(true);
                            tvDisplayDate.setEnabled(true);
                        }
                        else
                        {
                            timerOff.setChecked(true);
                            timerChoice.setEnabled(false);
                            device_on.setEnabled(false);
                            device_off.setEnabled(false);
                            onetime.setEnabled(false);
                            Repeat.setEnabled(false);
                            btnChangeTime.setEnabled(false);
                            btnChangeDate.setEnabled(false);
                            btnTimerSave.setEnabled(false);
                            tvDisplayTime.setEnabled(false);
                            tvDisplayDate.setEnabled(false);
                        }
                    }

                    if(key.equals("TimRepeat"))
                    {
                        Boolean timrepeat=(Boolean) TimeRepeat.get(key);
                        if(timrepeat)
                        {
                            Repeat.setChecked(true);
                            onetime.setChecked(false);
                        }
                        else
                        {
                            Repeat.setChecked(false);
                            onetime.setChecked(true);
                        }

                    }

                    if(key.equals("TimDevState"))
                    {
                        Boolean timdevstate=(Boolean) TimDevState.get(key);
                        if(timdevstate)
                        {
                            device_on.setChecked(true);
                            device_off.setChecked(false);
                        }
                        else
                        {
                            device_on.setChecked(false);
                            device_off.setChecked(true);
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

    public void addListenerOnButtonDate() {

        btnChangeDate = (Button) findViewById(R.id.get_date);

        btnChangeDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                datePicker();

            }

        });

    }
    public void addListenerOnButtonTime() {

        btnChangeTime = (Button) findViewById(R.id.get_time);

        btnChangeTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                timePicker();


            }

        });

    }

    public void addListenerOnButtonTimerSave() {

        btnTimerSave = (Button) findViewById(R.id.Timer_Save);

        btnTimerSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Code Here for save the data to firebase
               if(onetime.isChecked())
               {
                   if(Date_DatePicker==null || Time_Timepicker==null){
                       Toast.makeText(
                               Lights.this,
                               "Debe Seleccionar Fecha y Hora",
                               Toast.LENGTH_SHORT)
                               .show();
                       return;
                   }
                   String dateTime = Date_DatePicker+" "+Time_Timepicker;
                   SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.getDefault());
                   try {
                       Date date = format.parse(dateTime);
                       Timer_timestamp = date.getTime()/1000;
                       mDatabaseReference.child("Light_SW/"+Tag+"/TimSingle").setValue(true);
                       mDatabaseReference.child("Light_SW/"+Tag+"/TimRepeat").setValue(false);
                       mDatabaseReference.child("Light_SW/"+Tag+"/TimSingleDate").setValue(Timer_timestamp);
                       mDatabaseReference.child("Light_SW/"+Tag+"/TimerChanged").setValue(true);
                       if(device_on.isChecked())
                       {
                           mDatabaseReference.child("Light_SW/"+Tag+"/TimDevState").setValue(true);
                       }
                       if(device_off.isChecked())
                       {
                           mDatabaseReference.child("Light_SW/"+Tag+"/TimDevState").setValue(false);
                       }
                   } catch (ParseException e) {
                       // TODO Auto-generated catch block
                       e.printStackTrace();
                   }
               }

                if(Repeat.isChecked())
                {
                    for(int i=0;i<7;i++)
                    {
                        if(checkedValues[i])
                        {
                            Repeat_Days=Repeat_Days+Integer.toString(i);
                        }
                    }
                    if(Time_Timepicker==null||Repeat_Days==null){
                        Toast.makeText(
                                Lights.this,
                                "Debe Seleccionar Dia(s) y Hora",
                                Toast.LENGTH_SHORT)
                                .show();
                        return;

                    }
                    Date_DatePicker="1970-1-1";
                    String dateTime = Date_DatePicker+" "+Time_Timepicker;
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.getDefault());
                    try {
                        Date date = format.parse(dateTime);
                        Timer_timestamp = date.getTime()/1000;
                        mDatabaseReference.child("Light_SW/"+Tag+"/TimSingle").setValue(false);
                        mDatabaseReference.child("Light_SW/"+Tag+"/TimRepeat").setValue(true);
                        mDatabaseReference.child("Light_SW/"+Tag+"/TimRepeatTime").setValue(Timer_timestamp);
                        mDatabaseReference.child("Light_SW/"+Tag+"/TimRepeatDay").setValue(Repeat_Days);
                        mDatabaseReference.child("Light_SW/"+Tag+"/TimerChanged").setValue(true);
                        if(device_on.isChecked())
                        {
                            mDatabaseReference.child("Light_SW/"+Tag+"/TimDevState").setValue(true);
                        }
                        if(device_off.isChecked())
                        {
                            mDatabaseReference.child("Light_SW/"+Tag+"/TimDevState").setValue(false);
                        }

                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }


                }

        });

    }
    private void datePicker(){


        // Get Current Date
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int selectedYear,int selectedMonth, int selectedDay) {

                        year = selectedYear;
                        month = selectedMonth;
                        day = selectedDay;
                        Date_DatePicker = Integer.toString(year)+"-"+Integer.toString(month+1)+"-"+Integer.toString(day);
                        // set selected date into textview
                        tvDisplayDate.setText(new StringBuilder().append(month + 1)
                                .append("-").append(day).append("-").append(year)
                                .append(" "));

                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    private void timePicker(){
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int selectedHour,int selectedMinute) {

                        hour = selectedHour;
                        minute = selectedMinute;
                        Time_Timepicker = Integer.toString(hour)+":"+Integer.toString(minute);
                        tvDisplayTime.setText(new StringBuilder().append(hour)
                                .append(":").append(minute).append(" "));
                    }
                }, hour, minute, true);
        timePickerDialog.show();
    }



    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_onetime:
                if (checked)
                    btnChangeDate.setEnabled(true);
                    break;
            case R.id.radio_repeat:
                if (checked)
                    btnChangeDate.setEnabled(false);
                     alert = createMultipleListDialog();
                alert.show();
                    break;

        }
    }

    public void onRadioButtonClickedON(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.Device_On:
                if (checked)

                break;
            case R.id.Device_Off:
                if (checked)

                break;

        }
    }

    public void onRadioTimerButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_timeron:
                if (checked)
                    timerChoice.setEnabled(true);
                    onetime.setEnabled(true);
                    Repeat.setEnabled(true);
                    device_on.setEnabled(true);
                    device_off.setEnabled(true);
                    btnChangeTime.setEnabled(true);
                    btnChangeDate.setEnabled(true);
                    btnTimerSave.setEnabled(true);
                    tvDisplayTime.setEnabled(true);
                    tvDisplayDate.setEnabled(true);
                    mDatabaseReference.child("Light_SW/"+Tag+"/TimerOn").setValue(true);
                    break;
            case R.id.radio_timeroff:
                if (checked)
                    timerChoice.setEnabled(false);
                    device_on.setEnabled(false);
                    device_off.setEnabled(false);
                    onetime.setEnabled(false);
                    Repeat.setEnabled(false);
                    btnChangeTime.setEnabled(false);
                    btnChangeDate.setEnabled(false);
                    btnTimerSave.setEnabled(false);
                    tvDisplayTime.setEnabled(false);
                    tvDisplayDate.setEnabled(false);
                    mDatabaseReference.child("Light_SW/"+Tag+"/TimerOn").setValue(false);
                break;
        }
    }


    public AlertDialog createMultipleListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder( Lights.this);
        Repeat_Days="";
        final ArrayList itemsSeleccionados = new ArrayList();
        checkedValues = new boolean[7];
        CharSequence[] items = new CharSequence[7];
        checkedValues[0] = true;
        items[0] = "Domingo";
        items[1] = "Lunes";
        items[2] = "Martes";
        items[3] = "Miercoles";
        items[4] = "Jueves";
        items[5] = "Viernes";
        items[6] = "Sabado";

        builder.setTitle("Dia: ")
                .setMultiChoiceItems(items, checkedValues, new DialogInterface.OnMultiChoiceClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            // Guardar indice seleccionado
                            itemsSeleccionados.add(which);
                            checkedValues[which]=true;
                            Toast.makeText(
                                    Lights.this,
                                    "Dias Seleccionados:(" + itemsSeleccionados.size() + ")",
                                    Toast.LENGTH_SHORT)
                                    .show();
                        } else if (itemsSeleccionados.contains(which)) {
                            // Remover indice sin selección
                            checkedValues[which]=false;
                            itemsSeleccionados.remove(Integer.valueOf(which));
                        }

                    }

                });

        return builder.create();
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
