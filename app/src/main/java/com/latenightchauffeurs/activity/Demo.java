package com.latenightchauffeurs.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TimePicker;
import android.widget.Toast;

import com.latenightchauffeurs.R;
import com.latenightchauffeurs.fragment.BookReservation_new;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Demo extends AppCompatActivity {

    private static final String TAG = "Demo";

    private int mHour, mMinute;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.demo);


//        DialogFragment newFragment = new TimePickerFragment();
//        newFragment.show(getFragmentManager(), "timePicker");


        Calendar calendar = Calendar.getInstance();

        Log.e(TAG, "calendarcalendar "+calendar.getTime());


//            TimePickerDialog mTimePicker;
//            mTimePicker = new TimePickerDialog(Demo.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK,new TimePickerDialog.OnTimeSetListener() {
//                @Override
//                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                    //eReminderTime.setText( selectedHour + ":" + selectedMinute);
//                }
//            }, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), false);//Yes 24 hour time
//            mTimePicker.setTitle("Select Time");
//            mTimePicker.show();



//        TimePickerDialog timePickerDialog = new TimePickerDialog(Demo.this, new TimePickerDialog.OnTimeSetListener() {
//            @Override
//            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
//
//            }
//        }, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), false);
//        timePickerDialog.show();


        setDateTimeFieldOne();



    }



    private void setDateTimeFieldOne() {
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(Demo.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        //buttonStartTime.setText(updateTime(hourOfDay , minute));
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }




    @SuppressLint("ValidFragment")
    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            Calendar calendar = Calendar.getInstance();
//            hour = calendar.get(Calendar.HOUR);
//            minute = calendar.get(Calendar.MINUTE);
//            second = calendar.get(Calendar.SECOND);


//            TimePickerDialog mTimePicker;
//            mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
//                @Override
//                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                    //eReminderTime.setText( selectedHour + ":" + selectedMinute);
//                }
//            }, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), false);//Yes 24 hour time
//            mTimePicker.setTitle("Select Time");
//            mTimePicker.show();

             TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), this, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), false);

            // Create a new instance of TimePickerDialog and return it
            return timePickerDialog;
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int selectedMinute) {
            String hh = "";
            String mm = "";
            Calendar calendar = Calendar.getInstance();
            Calendar datetime = Calendar.getInstance();
            Log.e("GetDate", "MinDate1 " + calendar.getTime().getDate() + "  " + calendar.getTimeInMillis());


        }

    }


}
