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

import java.sql.Timestamp;
import java.text.DateFormat;
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
import java.util.TimeZone;


public class activity_AddTimer extends AppCompatActivity {


    RelativeLayout RelativeLayout;
    FloatingActionButton Refresh;
    TextView tvDisplayDate;
    TextView tvDisplayTime,tvDisplayTemporTime;
    DatePicker dpResult;
    Button btnChangeDate, btnChangeTime,btnTimerSave, btnChangeTimeTempor;
    RadioButton Repeat, onetime,timerOn, timerOff;
    RadioGroup timerActivate, timerChoice,Tempor_Device;
    RadioButton device_on, device_off, Tempor_On,Tempor_Off,Tempor_Device_On,Tempor_Device_Off;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    long Timer_timestamp;
    boolean[] checkedValues;
    String Tag="";
    String Tag_Timer="";
    String Tag_Dev_Changed="";
    String device="";
    String Timer_ID="";
    String ID="";//ID del Timmer
    String timersid="";
    String timersidchanged="";
    String TimSingledate, Date_DatePicker, Time_Timepicker, Repeat_Days,TimRepeatday,Time_TimepickerTempor;
    String timeron,timerepeat,timdevstate,tempordevstate,temporon,timsigledate,timrepeatday,timrepeattime,temportime;
    Long TimeRepeattime, Temportime;
    private String m_Text = "";
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mDatabaseReference_devices;
    Long num_timers;
    Map<String,Object> light_map;
    Map<Long,Object> light_devices;
    Map<Boolean,Object> TemporOn, TimeRepeat, TimSingle, TimerChanged, TimerOn, TimerRepeat, TimDevState, TemporDevState;
    Map<Long,Object> TemporTime, TimRepeatTime;
    Map<String,Object> TimRepeatDay, TimSingleDate;
    AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timers);
        Bundle extras = getIntent().getExtras();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.home50);
        btnChangeTimeTempor=(Button) findViewById(R.id.get_temportime);
        btnChangeDate =(Button) findViewById(R.id.get_date);
        btnChangeTime =(Button) findViewById(R.id.get_time);
        btnTimerSave =(Button) findViewById(R.id.Timer_Save);
        tvDisplayDate = (TextView) findViewById(R.id.date);
        tvDisplayTime = (TextView) findViewById(R.id.time);
        tvDisplayTemporTime=(TextView) findViewById(R.id.txvtemportime);
        addListenerOnButtonDate();
        addListenerOnButtonTime();
        addListenerOnButtonTimeTempor();
        addListenerOnButtonTimerSave();
        Repeat=(RadioButton) findViewById(R.id.radio_repeat);
        onetime=(RadioButton) findViewById(R.id.radio_onetime);
        timerChoice=(RadioGroup) findViewById(R.id.Radio_timer);
        Tempor_Device=(RadioGroup) findViewById(R.id.accionTimer);
        Tempor_Device_Off=(RadioButton) findViewById(R.id.Tempor_Device_Off);
        Tempor_Device_On=(RadioButton) findViewById(R.id.Tempor_Device_On);
        device_on=(RadioButton) findViewById(R.id.Device_On);
        device_off=(RadioButton) findViewById(R.id.Device_Off);
        Tempor_On=(RadioButton) findViewById(R.id.radio_temporon);
        Tempor_Off=(RadioButton) findViewById(R.id.radio_temporoff);
        timerOn=(RadioButton) findViewById(R.id.radio_timeron);
        timerOff=(RadioButton) findViewById(R.id.radio_timeroff);
        timerActivate = (RadioGroup) findViewById(R.id.Radio_ActivarTimer);
        Tag = extras.getString("Tag_Id");
        device=extras.getString("device");
        Timer_ID=extras.getString("Timer_ID");
        StringBuilder sb = new StringBuilder(Tag);
        sb.deleteCharAt(6);
        Tag_Timer=Timer_ID+"_"+sb.toString();
        Tag_Dev_Changed=sb.toString()+"_Changed";
        timersid=extras.getString("TimersID");
        num_timers=extras.getLong("NumTimers");
        timersidchanged=extras.getString("TimersIDChanged");
        Refresh = (FloatingActionButton) findViewById(R.id.floatingRefresh);
        Refresh.setOnClickListener(handleOnClickBtnRefresh(Refresh));
        //initializing database reference
        ID=Timer_ID.substring(5);
        timeron="Timer"+ID+"On";timerepeat="Tim"+ID+"Repeat";timdevstate="Tim"+ID+"DevState";tempordevstate="Tempor"+ID+"DevState";temporon="Tempor"+ID+"On";timsigledate="Tim"+ID+"SingleDate";
        timrepeatday="Tim"+ID+"RepeatDay";timrepeattime="Tim"+ID+"RepeatTime";temportime="Tempor"+ID+"Time";
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mDatabaseReference_devices = FirebaseDatabase.getInstance().getReference();
        // Capture button clicks



        mDatabaseReference_devices.child(device).addListenerForSingleValueEvent(new ValueEventListener(){

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                light_devices = (HashMap<Long,Object>) snapshot.getValue();
            }

            @Override public void onCancelled(DatabaseError error) {

            }
        });

        mDatabaseReference.child(device+"/"+Tag_Timer).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {





                           //String[] Button_List = light_map.values().toArray(new String[0]);//reparar el error de tipo de variable para el primer valor Devices es Long



            }
            @Override public void onCancelled(DatabaseError error) {

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

    public void addListenerOnButtonTimeTempor() {

        btnChangeTimeTempor = (Button) findViewById(R.id.get_temportime);

        btnChangeTimeTempor.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                timePickerTempor();


            }

        });

    }

    public void addListenerOnButtonTimerSave() {

        btnTimerSave = (Button) findViewById(R.id.Timer_Save);

        btnTimerSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Code Here for save the data to firebase
                if(onetime.isChecked()&&timerOn.isChecked())
                {
                    if(Date_DatePicker==null || Time_Timepicker==null){
                        Toast.makeText(
                                activity_AddTimer.this,
                                "Debe Seleccionar Fecha y Hora",
                                Toast.LENGTH_SHORT)
                                .show();
                        return;
                    }
                    String dateTime = Date_DatePicker+" "+Time_Timepicker;
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.getDefault());
                    format.setTimeZone(TimeZone.getTimeZone("GMT"));
                    try {
                        Date date = format.parse(dateTime);
                        Timer_timestamp = date.getTime()/1000;
                        String timestamp=String.valueOf(Timer_timestamp);
                        mDatabaseReference.child(device+"/"+Tag_Timer+"/Tim"+ID+"Single").setValue(true);
                        mDatabaseReference.child(device+"/"+Tag_Timer+"/Tim"+ID+"Repeat").setValue(false);
                        mDatabaseReference.child(device+"/"+Tag_Timer+"/Tim"+ID+"SingleDate").setValue(timestamp);
                        mDatabaseReference.child(device+"/"+Tag_Timer+"/Timer"+ID+"State").setValue(true);
                        //mDatabaseReference.child(device+"/"+Tag+"/Timer"+ID+"Changed").setValue(true);
                        mDatabaseReference.child(device+"/"+Tag_Dev_Changed+"/DataChanged").setValue(true);

                        StringBuilder sb = new StringBuilder(timersidchanged);
                        int index=sb.indexOf(ID);

                        if(timersidchanged.equals("Empty"))
                        {
                            timersidchanged=ID;
                        }
                        else if(index<0)
                        {
                            timersidchanged=timersidchanged+","+ID;
                        }
                        mDatabaseReference.child(device+"/"+Tag+"/TimersIDChanged").setValue(timersidchanged);
                        if(device_on.isChecked())
                        {
                            mDatabaseReference.child(device+"/"+Tag_Timer+"/Tim"+ID+"DevState").setValue(true);
                        }
                        if(device_off.isChecked())
                        {
                            mDatabaseReference.child(device+"/"+Tag_Timer+"/Tim"+ID+"DevState").setValue(false);
                        }
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if(Repeat.isChecked()&&timerOn.isChecked())
                {

                    if(Time_Timepicker==null||Repeat_Days==null||checkedValues.length==0){
                        Toast.makeText(
                                activity_AddTimer.this,
                                "Debe Seleccionar Dia(s) y Hora",
                                Toast.LENGTH_SHORT)
                                .show();
                        return;

                    }
                    for(int i=0;i<7;i++)
                    {
                        if(checkedValues[i])
                        {
                            Repeat_Days=Repeat_Days+Integer.toString(i);
                        }
                    }

                    Date_DatePicker="1970-1-1";
                    String dateTime = Date_DatePicker+" "+Time_Timepicker;
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.getDefault());
                    format.setTimeZone(TimeZone.getTimeZone("GMT"));
                    try {
                        Date date = format.parse(dateTime);
                        Timer_timestamp = date.getTime()/1000;
                        mDatabaseReference.child(device+"/"+Tag_Timer+"/Tim"+ID+"Single").setValue(false);
                        mDatabaseReference.child(device+"/"+Tag_Timer+"/Tim"+ID+"Repeat").setValue(true);
                        mDatabaseReference.child(device+"/"+Tag_Timer+"/Tim"+ID+"RepeatTime").setValue(Timer_timestamp);
                        mDatabaseReference.child(device+"/"+Tag_Timer+"/Tim"+ID+"RepeatDay").setValue(Repeat_Days);
                        mDatabaseReference.child(device+"/"+Tag_Timer+"/Timer"+ID+"State").setValue(true);
                        //mDatabaseReference.child(device+"/"+Tag+"/Timer"+ID+"Changed").setValue(true);
                        StringBuilder sb = new StringBuilder(timersidchanged);
                        int index=sb.indexOf(ID);

                        if(timersidchanged.equals("Empty"))
                        {
                            timersidchanged=ID;
                        }
                        else if(index<0)
                        {
                            timersidchanged=timersidchanged+","+ID;
                        }
                        mDatabaseReference.child(device+"/"+Tag+"/TimersIDChanged").setValue(timersidchanged);

                        mDatabaseReference.child(device+"/"+Tag_Dev_Changed+"/DataChanged").setValue(true);
                        if(device_on.isChecked())
                        {
                            mDatabaseReference.child(device+"/"+Tag_Timer+"/Tim"+ID+"DevState").setValue(true);
                        }
                        if(device_off.isChecked())
                        {
                            mDatabaseReference.child(device+"/"+Tag_Timer+"/Tim"+ID+"DevState").setValue(false);
                        }

                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if(Tempor_On.isChecked())
                {
                    String dateTime = "1970-1-1"+" "+Time_TimepickerTempor;
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.getDefault());
                    format.setTimeZone(TimeZone.getTimeZone("GMT"));
                    try {
                        Date date = format.parse(dateTime);
                        Timer_timestamp = date.getTime()/1000;
                        mDatabaseReference.child(device+"/"+Tag_Timer+"/Tempor"+ID+"Time").setValue(Timer_timestamp);
                        mDatabaseReference.child(device+"/"+Tag_Timer+"/Tempor"+ID+"On").setValue(true);
                        mDatabaseReference.child(device+"/"+Tag_Timer+"/Timer"+ID+"State").setValue(true);
                       // mDatabaseReference.child(device+"/"+Tag+"/Timer"+ID+"Changed").setValue(true);
                        StringBuilder sb = new StringBuilder(timersidchanged);
                        int index=sb.indexOf(ID);

                        if(timersidchanged.equals("Empty"))
                        {
                            timersidchanged=ID;
                        }
                        else if(index<0)
                        {
                            timersidchanged=timersidchanged+","+ID;
                        }
                        mDatabaseReference.child(device+"/"+Tag+"/TimersIDChanged").setValue(timersidchanged);
                        mDatabaseReference.child(device+"/"+Tag_Dev_Changed+"/DataChanged").setValue(true);
                        if(Tempor_Device_On.isChecked())
                        {
                            mDatabaseReference.child(device+"/"+Tag_Timer+"/Tempor"+ID+"DevState").setValue(true);

                        }
                        if(Tempor_Device_Off.isChecked())
                        {
                            mDatabaseReference.child(device+"/"+Tag_Timer+"/Tempor"+ID+"DevState").setValue(false);
                        }
                    }
                    catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
                else
                {
                    mDatabaseReference.child(device+"/"+Tag_Timer+"/Tempor"+ID+"On").setValue(false);
                    //mDatabaseReference.child(device+"/"+Tag+"/Timer"+ID+"Changed").setValue(true);
                    StringBuilder sb = new StringBuilder(timersidchanged);
                    int index=sb.indexOf(ID);

                    if(timersidchanged.equals("Empty"))
                    {
                        timersidchanged=ID;
                    }
                    else if(index<0)
                    {
                        timersidchanged=timersidchanged+","+ID;
                    }
                    mDatabaseReference.child(device+"/"+Tag+"/TimersIDChanged").setValue(timersidchanged);
                }


                Long numtimers = num_timers+1;
                //String appendstring=timersid+","+ID;
                //mDatabaseReference.child(device+"/"+Tag+"/Timer"+ID+"Changed").setValue(true);
                mDatabaseReference.child(device+"/"+Tag_Timer+"/Timer"+ID+"State").setValue(true);
                mDatabaseReference.child(device+"/"+Tag_Timer+"/Timer"+ID+"Name").setValue("Empty");
                mDatabaseReference.child(device+"/"+Tag+"/Timers").setValue(numtimers);
                //mDatabaseReference.child(device+"/"+Tag+"/TimersID").setValue(appendstring);
                StringBuilder sb = new StringBuilder(timersidchanged);
                int index=sb.indexOf(ID);

                if(timersidchanged.equals("Empty"))
                {
                    timersidchanged=ID;
                }
                else if(index<0)
                {
                    timersidchanged=timersidchanged+","+ID;
                }
                mDatabaseReference.child(device+"/"+Tag+"/TimersIDChanged").setValue(timersidchanged);


                if(Tempor_Off.isChecked()&&timerOff.isChecked())
                {
                    mDatabaseReference.child(device+"/"+Tag_Timer+"/Tim"+ID+"Single").setValue(true);
                    mDatabaseReference.child(device+"/"+Tag_Timer+"/Tim"+ID+"SingleDate").setValue("0");
                    mDatabaseReference.child(device+"/"+Tag_Timer+"/Tim"+ID+"Repeat").setValue(false);
                    mDatabaseReference.child(device+"/"+Tag_Timer+"/Tim"+ID+"RepeatTime").setValue(0);
                    mDatabaseReference.child(device+"/"+Tag_Timer+"/Tim"+ID+"RepeatDay").setValue("Empty");
                    mDatabaseReference.child(device+"/"+Tag_Timer+"/Tim"+ID+"DevState").setValue(false);
                    //mDatabaseReference.child(device+"/"+Tag+"/Timer"+ID+"Changed").setValue(true);
                    mDatabaseReference.child(device+"/"+Tag_Timer+"/Tempor"+ID+"Time").setValue(0);
                    mDatabaseReference.child(device+"/"+Tag_Timer+"/Tempor"+ID+"On").setValue(false);
                    mDatabaseReference.child(device+"/"+Tag_Timer+"/Tempor"+ID+"DevState").setValue(false);
                    mDatabaseReference.child(device+"/"+Tag_Dev_Changed+"/DataChanged").setValue(true);
                    mDatabaseReference.child(device+"/"+Tag_Timer+"/Timer"+ID+"State").setValue(false);
                    StringBuilder sbu = new StringBuilder(timersidchanged);
                    int ind=sbu.indexOf(ID);

                    if(timersidchanged.equals("Empty"))
                    {
                        timersidchanged=ID;
                    }
                    else if(ind<0)
                    {
                        timersidchanged=timersidchanged+","+ID;
                    }
                    mDatabaseReference.child(device+"/"+Tag+"/TimersIDChanged").setValue(timersidchanged);

                }
                mDatabaseReference.child(device+"/"+Tag_Dev_Changed+"/DataChanged").setValue(true);
                Toast.makeText(
                        activity_AddTimer.this,
                        "Se Guardo la informacion con exito",
                        Toast.LENGTH_SHORT)
                        .show();
                String Tag_Id=Tag;
                //String Timer_ID="Timer"+numtimers;
                Intent myIntent = new Intent(activity_AddTimer.this,activity_MyTimers.class);
                myIntent.putExtra("device",device);
                myIntent.putExtra("Tag_Id",Tag_Id);
                //myIntent.putExtra("Timer_ID",Timer_ID);
                startActivity(myIntent);
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

    private void timePickerTempor(){
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
                        Time_TimepickerTempor = Integer.toString(hour)+":"+Integer.toString(minute);
                        tvDisplayTemporTime.setText(new StringBuilder().append(hour)
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

    public void onRadioTemporButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_temporon:
                if (checked)

                    Tempor_Off.setChecked(false);
                Tempor_Device.setEnabled(true);
                Tempor_Device_On.setEnabled(true);
                Tempor_Device_Off.setEnabled(true);
                btnChangeTimeTempor.setEnabled(true);
                break;
            case R.id.radio_temporoff:
                if (checked)
                    Tempor_On.setChecked(false);
                Tempor_Device.setEnabled(false);
                Tempor_Device_On.setEnabled(false);
                Tempor_Device_Off.setEnabled(false);
                btnChangeTimeTempor.setEnabled(false);
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
                //btnTimerSave.setEnabled(true);
                tvDisplayTime.setEnabled(true);
                tvDisplayDate.setEnabled(true);
                mDatabaseReference.child(device+"/"+Tag_Timer+"/Timer"+ID+"On").setValue(true);
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
                //btnTimerSave.setEnabled(false);
                tvDisplayTime.setEnabled(false);
                tvDisplayDate.setEnabled(false);
                mDatabaseReference.child(device+"/"+Tag_Timer+"/Timer"+ID+"On").setValue(false);
                break;
        }
    }


    public AlertDialog createMultipleListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder( activity_AddTimer.this);
        Repeat_Days="";
        final ArrayList itemsSeleccionados = new ArrayList();
        checkedValues = new boolean[7];
        CharSequence[] items = new CharSequence[7];
       // String[] day = TimRepeatday.split("");
        int indice =0;

       /* for(int i=1;i<day.length;i++)
        {
            indice=Integer.parseInt(day[i]);
            checkedValues[indice]=true;
        }*/

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
                                    activity_AddTimer.this,
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





    View.OnClickListener handleOnClickBtnRefresh(final ImageButton button) {
        return new View.OnClickListener() {
            public void onClick(View v) {

                finish();
                startActivity(getIntent());
                Toast.makeText(
                        activity_AddTimer.this,
                        "Se actualizo la informacion",
                        Toast.LENGTH_SHORT)
                        .show();

            }
        };
    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Display display = ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();
        if(rotation==0 || rotation==2)
        {
            RelativeLayout =(RelativeLayout) findViewById(R.id.activity_timers);
            RelativeLayout.setBackgroundResource(R.drawable.natureopt);
        }
        if(rotation==1 || rotation==3)
        {
            RelativeLayout =(RelativeLayout) findViewById(R.id.activity_timers);
            RelativeLayout.setBackgroundResource(R.drawable.cropped_opt);
        }
        super.onConfigurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);
    }
    public void onBackPressed(){
        String Tag_Id=Tag;
        //String Timer_ID="Timer"+numtimers;
        Intent myIntent = new Intent(activity_AddTimer.this,activity_MyTimers.class);
        myIntent.putExtra("device",device);
        myIntent.putExtra("Tag_Id",Tag_Id);
        //myIntent.putExtra("Timer_ID",Timer_ID);
        startActivity(myIntent);

    }
}
