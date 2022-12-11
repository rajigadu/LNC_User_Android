package com.latenightchauffeurs;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.latenightchauffeurs.Utils.MyVectorClock;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Demo5 extends AppCompatActivity {


    private static final String TAG = "Demo5";
    Button button, buttonDate, buttonTime;


    int yearC, monthC , dayC , hhC, mmC, ssC;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.demo4);


        button = (Button) findViewById(R.id.button4);
        buttonDate = (Button) findViewById(R.id.button5);
        buttonTime = (Button) findViewById(R.id.button6);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build();
        StrictMode.setThreadPolicy(policy);

        Calendar calendar = Calendar.getInstance();
       // calendar.add(Calendar.HOUR,-2);

        MyVectorClock vectorAnalogClock = findViewById(R.id.clock);

        //customization
        vectorAnalogClock.setCalendar(calendar)
                .setDiameterInDp(400.0f)
                .setOpacity(1.0f)
                .setShowSeconds(true)
                .setColor(Color.BLACK);

        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
//                getCarList();

              //  Calendar calendar = Calendar.getInstance();

//                Calendar c1 = Calendar.getInstance();
//
//                c1.set(Calendar.YEAR, yearC);
//                c1.set(Calendar.MONTH, monthC);
//                c1.set(Calendar.DATE, dayC);
//
//                c1.set(Calendar.HOUR, hhC);
//                c1.set(Calendar.MINUTE, mmC);
//                c1.set(Calendar.SECOND, 0);
//
//                //c1.set(Calendar.AM_PM, Calendar.PM);
//
//
//                //calendar.set(2019, 4, 31, 5, 15, 0);
//
//               // calendar.set(Calendar.AM_PM, Calendar.AM);
//                Log.e(TAG , "yearC "+yearC);
//                Log.e(TAG , "monthC "+monthC);
//                Log.e(TAG , "dayC "+dayC);
//                Log.e(TAG , "hhC "+hhC);
//                Log.e(TAG , "mmC "+mmC);
//
//
//                Log.e(TAG , "calendarasaa "+c1.getTime());
//
//                Date date = c1.getTime();
//
//                SimpleDateFormat dfDate = new SimpleDateFormat("dd MMM yyyy hh.mm a");
//
//                String CurrentDate = dfDate.format(date);
//                Log.e(TAG ,"Start Date " + CurrentDate);


                try{
//                    Calendar cal = Calendar.getInstance();
//                    TimeZone tz = cal.getTimeZone();
//
//                    /* debug: is it local time? */
//                    Log.d("Time zone: ", tz.getDisplayName());
//
//                    /* date formatter in local timezone */
//                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//                    sdf.setTimeZone(tz);
//
//                    /* print your timestamp and double check it's the date you expect */
//                  //  long timestamp = cursor.getLong(columnIndex);
//                    String localTime = sdf.format(new Date(timestamp * 1000)); // I assume your timestamp is in seconds and you're converting to milliseconds?
//                    Log.d("Time: ", localTime);


//                    long UTC_TIMEZONE=1470960000;
//                    String OUTPUT_DATE_FORMATE="dd-MM-yyyy - hh:mm a";
//
//                    getDateFromUTCTimestamp(UTC_TIMEZONE,OUTPUT_DATE_FORMATE);

//                    String dateInString = "22-1-2015 10:15:55 AM";
//
//                  //  SimpleDateFormat sdf = new SimpleDateFormat(dateInString);
//
//
//                    @SuppressLint({"NewApi", "LocalSuppress"})
//                    LocalDateTime ldt = LocalDateTime.parse(dateInString, DateTimeFormatter.ofPattern(DATE_FORMAT));
//
//                    @SuppressLint({"NewApi", "LocalSuppress"})
//                    ZoneId singaporeZoneId = ZoneId.of("Asia/Singapore");
//                    System.out.println("TimeZone : " + singaporeZoneId);
//
//                    @SuppressLint({"NewApi", "LocalSuppress"})
//                    ZonedDateTime asiaZonedDateTime = ldt.atZone(singaporeZoneId);
//                    System.out.println("Date (Singapore) : " + asiaZonedDateTime);



//                    String DATE_FORMAT = "dd-M-yyyy hh:mm:ss";
//
//                        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
//
//                        String dateInString = "31-10-2019 07:02:00";
//                        Date date = formatter.parse(dateInString);
//                        TimeZone tz = TimeZone.getDefault();
//
////                        // From TimeZone Asia/Singapore
////                        System.out.println("TimeZone : " + tz.getID() + " - " + tz.getDisplayName());
////                        System.out.println("TimeZone : " + tz);
////                        System.out.println("Date (Singapore) : " + formatter.format(date));
//
//                        // To TimeZone America/New_York
//                        SimpleDateFormat sdfAmerica = new SimpleDateFormat(DATE_FORMAT);
//                        TimeZone tzInAmerica = TimeZone.getTimeZone("GMT+5:30");
//                        sdfAmerica.setTimeZone(tzInAmerica);
//
//                        String sDateInAmerica = sdfAmerica.format(date); // Convert to String first
//                        Date dateInAmerica = formatter.parse(sDateInAmerica); // Create a new Date object
//
//                        System.out.println("\nTimeZone : " + tzInAmerica.getID() + " - " + tzInAmerica.getDisplayName());
//                        System.out.println("TimeZone : " + tzInAmerica);
//                        System.out.println("Date (New York) (String) : " + sDateInAmerica);
//                        System.out.println("Date (New York) (Object) : " + formatter.format(dateInAmerica));
//
//
//                        save(sDateInAmerica);
//                    GMT+5:30

//                    Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
//
//                    SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy HH:mm:ss z");
//                    formatter.setTimeZone(TimeZone.getTimeZone("GMT+13"));
//
//                    String newZealandTime = formatter.format(calendar.getTime());



//                    String format = "yyyy-MM-dd HH:mm:ss";
//                    SimpleDateFormat estFormatter = new SimpleDateFormat(format);
//                    estFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
//                    Date date = estFormatter.parse("2019-10-31 07:02:00");
//
//                    SimpleDateFormat utcFormatter = new SimpleDateFormat(format);
//                    utcFormatter.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
//
//                    System.out.println(utcFormatter.format(date));


//                    String serverTime = "2017/12/06 21:04:07.406"; // GMT
//                    ZonedDateTime gmtTime = LocalDateTime.parse(serverTime, DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS")).atZone(ZoneId.of("GMT"));
//                    LocalDateTime localTime = gmtTime.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
//
//
//                    System.out.println(localTime.toString());



//                    final SimpleDateFormat formatter = (SimpleDateFormat) DateFormat.getDateTimeInstance();
//                    formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
//                    final Date timezone = formatter.parse("2012-04-14 14:23:34");
//                    formatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
//                    System.out.println(formatter.format(timezone));




                }catch (Exception e){

                }




            }
        });


        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(Demo5.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                yearC = year;
                                monthC = monthOfYear;
                                dayC = dayOfMonth;

                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
                datePickerDialog.show();
            }
        });


        buttonTime.setOnClickListener(new View.OnClickListener() {
            Calendar calendar = Calendar.getInstance();
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(Demo5.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                hhC = hourOfDay;
                                mmC = minute;
                            }
                        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
                timePickerDialog.show();
            }
        });


    }

    private void save(String ti) {

        try{
            String DATE_FORMAT = "dd-M-yyyy hh:mm:ss";
            SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
            //String dateInString = "31-10-2019 07:02:00";
            Date date = formatter.parse(ti);
            SimpleDateFormat sdfAmerica = new SimpleDateFormat(DATE_FORMAT);
            TimeZone tzInAmerica = TimeZone.getTimeZone("GMT+5:30");
            sdfAmerica.setTimeZone(tzInAmerica);

            String sDateInAmerica = sdfAmerica.format(date); // Convert to String first
            Date dateInAmerica = formatter.parse(sDateInAmerica); // Create a new Date object

            System.out.println("\nTimeZone : " + tzInAmerica.getID() + " - " + tzInAmerica.getDisplayName());
            System.out.println("TimeZone : " + tzInAmerica);
            System.out.println("Date1 (New York) (String) : " + sDateInAmerica);
            System.out.println("Date (New York) (Object) : " + formatter.format(dateInAmerica));

        }catch (Exception e){

        }

    }

//    public Date getDateWithServerTimeStamp() {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
//
//        TimeZone tz = dateFormat.getTimeZone("GMT");
//
//
//        //dateFormat.timeZone = TimeZone.getTimeZone("GMT") ;
//        try {
//            return dateFormat.parse(String.valueOf(tz));
//        } catch (Exception e){
//            return null;
//        }
//    }






}
