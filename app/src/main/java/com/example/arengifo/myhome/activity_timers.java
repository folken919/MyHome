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


public class activity_timers extends AppCompatActivity {


    RelativeLayout RelativeLayout;
    FloatingActionButton Edit;
    FloatingActionButton Delete;
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
    String TimSingledate, Date_DatePicker, Time_Timepicker, Repeat_Days,TimRepeatday,Time_TimepickerTempor;
    Long TimeRepeattime, Temportime;
    private String m_Text = "";
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mDatabaseReference_devices;
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
        Edit = (FloatingActionButton) findViewById(R.id.floatingEditBtn);
        Edit.setTag("edi_"+Tag);
        Edit.setOnClickListener(handleOnClick(Edit));
        Delete = (FloatingActionButton) findViewById(R.id.floatingDelete);
        Delete.setTag("del_"+Tag);
        Delete.setOnClickListener(handleOnClickBtnDel(Delete));
        Refresh = (FloatingActionButton) findViewById(R.id.floatingRefresh);
        Refresh.setOnClickListener(handleOnClickBtnRefresh(Refresh));
        //initializing database reference
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mDatabaseReference_devices = FirebaseDatabase.getInstance().getReference();
        // Capture button clicks



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
                TimSingleDate=(HashMap<String,Object>) snapshot.getValue();
                TemporDevState=(HashMap<Boolean,Object>) snapshot.getValue();;
                //String[] Button_List = light_map.values().toArray(new String[0]);//reparar el error de tipo de variable para el primer valor Devices es Long
                Iterator myVeryOwnIterator = light_map.keySet().iterator();
                while(myVeryOwnIterator.hasNext()) {
                    String key=(String)myVeryOwnIterator.next();


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
                    if(key.equals("TemporDevState"))
                    {
                        Boolean tempdevstate=(Boolean) TemporDevState.get(key);
                        if(tempdevstate)
                        {
                            Tempor_Device_On.setChecked(true);
                            Tempor_Device_Off.setChecked(false);
                        }
                        else
                        {
                            Tempor_Device_On.setChecked(false);
                            Tempor_Device_Off.setChecked(true);
                        }

                    }
                    if(key.equals("TemporOn"))
                    {
                        Boolean tempon=(Boolean) TemporOn.get(key);
                        if(tempon)
                        {
                            Tempor_On.setChecked(true);
                            Tempor_Off.setChecked(false);
                            Tempor_Device.setEnabled(true);
                            Tempor_Device_On.setEnabled(true);
                            Tempor_Device_Off.setEnabled(true);
                            btnChangeTimeTempor.setEnabled(true);

                        }
                        else
                        {
                            Tempor_On.setChecked(false);
                            Tempor_Off.setChecked(true);
                            Tempor_Device.setEnabled(false);
                            Tempor_Device_On.setEnabled(false);
                            Tempor_Device_Off.setEnabled(false);
                            btnChangeTimeTempor.setEnabled(false);
                        }
                    }

                    if(key.equals("TimSingleDate"))
                    {
                        TimSingledate=(String) TimSingleDate.get(key);
                    }

                    if(key.equals("TimRepeatDay"))
                    {
                        TimRepeatday=(String) TimRepeatDay.get(key);
                    }
                    if(key.equals("TimRepeatTime"))
                    {
                        TimeRepeattime=(Long) TimRepeatTime.get(key);

                    }
                    if(key.equals("TemporTime"))
                    {
                        Temportime=(Long) TemporTime.get(key);
                        Timestamp ts = new Timestamp(Temportime*1000);
                        Date Gettime= new Date(ts.getTime());
                        SimpleDateFormat dft = new SimpleDateFormat("HH:mm",Locale.getDefault());
                        dft.setTimeZone(TimeZone.getTimeZone("GMT"));
                        String time = dft.format(Gettime);
                        tvDisplayTemporTime.setText(time);
                    }

                }


                if(onetime.isChecked())
                {
                    long TimeLong=0;
                    try {
                        TimeLong = Long.parseLong(TimSingledate.trim())*1000;
                        System.out.println("long l = " + TimeLong);
                    } catch (NumberFormatException nfe) {

                    }
                    Timestamp ts = new Timestamp(TimeLong);
                    Date GetDate= new Date(ts.getTime());
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String fecha = df.format(GetDate);
                    Date Gettime= new Date(ts.getTime());
                    SimpleDateFormat dft = new SimpleDateFormat("HH:mm",Locale.getDefault());
                    dft.setTimeZone(TimeZone.getTimeZone("GMT"));
                    String time = dft.format(Gettime);
                    tvDisplayDate.setText(fecha);
                    tvDisplayTime.setText(time);
                }
                else
                {
                    Timestamp ts = new Timestamp(TimeRepeattime*1000);
                    Date Gettime= new Date(ts.getTime());
                    SimpleDateFormat dft = new SimpleDateFormat("HH:mm",Locale.getDefault());
                    dft.setTimeZone(TimeZone.getTimeZone("GMT"));
                    String time = dft.format(Gettime);
                    tvDisplayTime.setText(time);
                    tvDisplayDate.setText("mm-dd-yyyy");
                }

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
                                activity_timers.this,
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
                        mDatabaseReference.child("Light_SW/"+Tag+"/TimSingle").setValue(true);
                        mDatabaseReference.child("Light_SW/"+Tag+"/TimRepeat").setValue(false);
                        mDatabaseReference.child("Light_SW/"+Tag+"/TimSingleDate").setValue(timestamp);
                        mDatabaseReference.child("Light_SW/"+Tag+"/TimerChanged").setValue(true);
                        mDatabaseReference.child("Light_SW/"+Tag+"/DataChanged").setValue(true);
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

                if(Repeat.isChecked()&&timerOn.isChecked())
                {

                    if(Time_Timepicker==null||Repeat_Days==null||checkedValues.length==0){
                        Toast.makeText(
                                activity_timers.this,
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
                        mDatabaseReference.child("Light_SW/"+Tag+"/TimSingle").setValue(false);
                        mDatabaseReference.child("Light_SW/"+Tag+"/TimRepeat").setValue(true);
                        mDatabaseReference.child("Light_SW/"+Tag+"/TimRepeatTime").setValue(Timer_timestamp);
                        mDatabaseReference.child("Light_SW/"+Tag+"/TimRepeatDay").setValue(Repeat_Days);
                        mDatabaseReference.child("Light_SW/"+Tag+"/TimerChanged").setValue(true);
                        mDatabaseReference.child("Light_SW/"+Tag+"/DataChanged").setValue(true);
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

                if(Tempor_On.isChecked())
                {
                    String dateTime = "1970-1-1"+" "+Time_TimepickerTempor;
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.getDefault());
                    format.setTimeZone(TimeZone.getTimeZone("GMT"));
                    try {
                        Date date = format.parse(dateTime);
                        Timer_timestamp = date.getTime()/1000;
                        mDatabaseReference.child("Light_SW/"+Tag+"/TemporTime").setValue(Timer_timestamp);
                        mDatabaseReference.child("Light_SW/"+Tag+"/TemporOn").setValue(true);
                        mDatabaseReference.child("Light_SW/"+Tag+"/TimerChanged").setValue(true);
                        mDatabaseReference.child("Light_SW/"+Tag+"/DataChanged").setValue(true);
                        if(Tempor_Device_On.isChecked())
                        {
                            mDatabaseReference.child("Light_SW/"+Tag+"/TemporDevState").setValue(true);

                        }
                        if(Tempor_Device_Off.isChecked())
                        {
                            mDatabaseReference.child("Light_SW/"+Tag+"/TemporDevState").setValue(false);
                        }
                    }
                    catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
                else
                {
                    mDatabaseReference.child("Light_SW/"+Tag+"/TemporOn").setValue(false);
                    mDatabaseReference.child("Light_SW/"+Tag+"/TimerChanged").setValue(true);
                }

                Toast.makeText(
                        activity_timers.this,
                        "Se Guardo la informacion con exito",
                        Toast.LENGTH_SHORT)
                        .show();
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
                //btnTimerSave.setEnabled(false);
                tvDisplayTime.setEnabled(false);
                tvDisplayDate.setEnabled(false);
                mDatabaseReference.child("Light_SW/"+Tag+"/TimerOn").setValue(false);
                break;
        }
    }


    public AlertDialog createMultipleListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder( activity_timers.this);
        Repeat_Days="";
        final ArrayList itemsSeleccionados = new ArrayList();
        checkedValues = new boolean[7];
        CharSequence[] items = new CharSequence[7];
        String[] day = TimRepeatday.split("");
        int indice =0;

        for(int i=1;i<day.length;i++)
        {
            indice=Integer.parseInt(day[i]);
            checkedValues[indice]=true;
        }

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
                                    activity_timers.this,
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
                AlertDialog.Builder builder = new AlertDialog.Builder(activity_timers.this);
                builder.setTitle("Nombre del Dispositivo");

// Set up the input
                final EditText input = new EditText(activity_timers.this);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(activity_timers.this);
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


    View.OnClickListener handleOnClickBtnRefresh(final ImageButton button) {
        return new View.OnClickListener() {
            public void onClick(View v) {

                finish();
                startActivity(getIntent());
                Toast.makeText(
                        activity_timers.this,
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
