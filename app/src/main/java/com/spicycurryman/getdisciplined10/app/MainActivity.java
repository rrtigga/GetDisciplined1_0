package com.spicycurryman.getdisciplined10.app;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.ibc.android.demo.appslist.app.HeartBeat;
import com.triggertrap.seekarc.SeekArc;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class MainActivity extends ActionBarActivity {
    private SeekArc mSeekArc;
    private SeekBar mRotation;
    private SeekBar mStartAngle;
    private SeekBar mSweepAngle;
    private SeekBar mArcWidth;
    private SeekBar mProgressWidth;
    private CheckBox mRoundedEdges;
    private CheckBox mTouchInside;
    private CheckBox mClockwise;
    private TextView mSeekArcProgress;
    private TextView mSeekArcMinuteProgress;



    //will show the time
    private TextView number_text;

    private TextView minute_text;

    private TextView little_hour_text2;
    private TextView little_minute_text2;



    CountDownTimer countDownTimer;          // built in android class CountDownTimer
    long totalTimeCountInMilliseconds;
    long timeBlinkInMilliseconds;           // start time of start blinking
    boolean blink;


    SharedPreferences startimerPreferences;
    SharedPreferences endTimerPreferences;
    SharedPreferences endservice;


    ArrayList<String> packagezList= new ArrayList<String>();
    SharedPreferences sharedPrefsapp;
    Map<String, ?> allEntries;


    long timerstarted; //this is when the user hit start timer.
    long timerends; //this is the time when the time when the timer will end;
    long reopened; //this is when time when the user reopens the application;



    long newtotalTimeCountInMilliseconds;


    // Consider showing drawable scrubber after pressing H M or S




    CircleButton block_button1;
    CircleButton start_timer;


    int hourint, minuteint;


    private static final int REQUEST_CODE = 0;
    private DevicePolicyManager mDPM;
    private ComponentName mAdminName;

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.setting_drop:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Hey you gotta check out this app I found, GetDisciplined! It really helps me stay focused! http://play.google.com/store/apps/details?id=com.spicycurryman.getdisciplined10.app ";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out GetDisciplined");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                return true;
            case R.id.follow_us_drop:
                Intent followintent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.twitter.com/getdisciplined"));
                startActivity(followintent);
                return true;
            case R.id.feedback_drop:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","getdisciplinedapp@gmail.com", null));
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onResume()
    {
        super.onResume();
        ApplicationCheck.activityResumed();



    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Light_appalled);

        setContentView(R.layout.merge);






        // here is where the service is started.
        //startService(new Intent(this, HeartBeat.class));

/*
        Intent iHeartBeatService = new Intent(this, HeartBeat.class);
        PendingIntent piHeartBeatService = PendingIntent.getService(this, 0, iHeartBeatService, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(piHeartBeatService);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 300000, piHeartBeatService);
*/


        Intent ishintent = new Intent(this, HeartBeat.class);
        PendingIntent pintent = PendingIntent.getService(this, 0, ishintent, 0);
        AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pintent);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),150000, pintent);
        getApplicationContext().startService(ishintent);





        //set views

        start_timer = (CircleButton) findViewById(R.id.start_button);
        number_text = (TextView) findViewById(R.id.hour_progress_number);
        minute_text = (TextView) findViewById(R.id.minute_progress_number);


        //getReferenceOfViews ();                         // get all views
        setActionListeners ();

        // This determine what the actual "countdown" time will be.
        //totalTimeCountInMilliseconds = 60 * 1000;      // time count for 3 minutes = 180 seconds
        //timeBlinkInMilliseconds = 30 * 1000;

        //Make sure you find out why it appears after a whole 1 second after the app appears
        SpannableString s = new SpannableString("GetDisciplined");
        s.setSpan(new TypefaceSpan(this, "miso.otf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// Update the action bar title with the TypefaceSpan instance
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(s);
        // set the action bar in this activity as the home
        actionBar.setHomeButtonEnabled(true);

        //Listeners for the buttons
        addListenerOnButton();



        //Editing Button Text



        number_text = (TextView)findViewById(R.id.hour_progress_number);
        number_text.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/roboto-thin.ttf"));

        minute_text = (TextView)findViewById(R.id.minute_progress_number);
        minute_text.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/roboto-thin.ttf"));


        little_hour_text2 = (TextView)findViewById(R.id.little_hour_text2);
        little_hour_text2.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/roboto-thin.ttf"));

        little_minute_text2 = (TextView)findViewById(R.id.little_minute_text2);
        little_minute_text2.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/roboto-thin.ttf"));


        mSeekArc = (SeekArc) findViewById(R.id.seekArc);


//Here is the actual "hour progress number" aka the TextView that changes as the scrubber is dragged around
        mSeekArcProgress = (TextView) findViewById(R.id.hour_progress_number);

        mSeekArcMinuteProgress = (TextView) findViewById(R.id.minute_progress_number);


        startimerPreferences = getPreferences(MODE_APPEND);

        Date startDate = new Date(startimerPreferences.getLong("time", 0));
        timerstarted = startDate.getTime();


        endTimerPreferences = getPreferences(MODE_APPEND);
        Date endDate = new Date(endTimerPreferences.getLong("endtime", 0));
        timerends = endDate.getTime();


        Date openagain = new Date(System.currentTimeMillis());
        reopened = openagain.getTime();


        //make textview selectable

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if((v.getId() == R.id.hour_progress_number)){
                    number_text.setTextColor(getResources().getColor(R.color.black));
                    little_hour_text2.setTextColor(getResources().getColor(R.color.black));





                    minute_text.setTextColor(getResources().getColor(R.color.red_highlight));
                    little_minute_text2.setTextColor(getResources().getColor(R.color.red_highlight));






                    //corresponding button logic should below here
                    mSeekArc.setOnSeekArcChangeListener(new SeekArc.OnSeekArcChangeListener() {

                        @Override
                        public void onStopTrackingTouch(SeekArc seekArc) {
                        }
                        @Override
                        public void onStartTrackingTouch(SeekArc seekArc) {
                            number_text.setTextColor(getResources().getColor(R.color.black));
                        }


                        //This sets the actual string for the hours
                        @Override
                        public void onProgressChanged(SeekArc seekArc, int progress,
                                                      boolean fromUser) {

                            int progress_count = 0;

                            for (int i=0;i<24;i=i+1)
                            {

                                if (progress ==120) {
                                    mSeekArcProgress.setText("24");

                                }
                                else if (progress == progress_count)
                                {
                                    mSeekArcProgress.setText(String.valueOf(String.format("%02d",i)));
                                }

                                progress_count = progress_count + 5;

                            }
                        }
                    });
                } else if((v.getId() == R.id.minute_progress_number)){

                    minute_text.setTextColor(getResources().getColor(R.color.black));
                    little_minute_text2.setTextColor(getResources().getColor(R.color.black));



                    number_text.setTextColor(getResources().getColor(R.color.red_highlight));
                    little_hour_text2.setTextColor(getResources().getColor(R.color.red_highlight));





                    //corresponding button logic should below here

                    mSeekArc.setOnSeekArcChangeListener(new SeekArc.OnSeekArcChangeListener() {


                        @Override
                        public void onStopTrackingTouch(SeekArc seekArc) {
                        }
                        @Override
                        public void onStartTrackingTouch(SeekArc seekArc) {
                            minute_text.setTextColor(getResources().getColor(R.color.black));
                        }


                        //This sets the actual string for the minutes
                        @Override
                        public void onProgressChanged(SeekArc seekArc, int progress,
                                                      boolean fromUser) {
                            int progress_count = 0;

                            for (int i=0;i<120;i++)
                            {

                                if (progress ==120) {
                                    mSeekArcMinuteProgress.setText("00");




                                }
                                else if (progress == progress_count)
                                {
                                    mSeekArcMinuteProgress.setText(String.valueOf(String.format("%02d",i)));
                                }

                                progress_count = progress_count + 2;


                            }

                        }
                    });
                }

            }
        };

        findViewById(R.id.hour_progress_number).setOnClickListener(clickListener);
        findViewById(R.id.minute_progress_number).setOnClickListener(clickListener);


        findViewById(R.id.minute_progress_number).performClick();



        mRotation = (SeekBar) findViewById(R.id.rotation);
        mStartAngle = (SeekBar) findViewById(R.id.startAngle);
        mSweepAngle  = (SeekBar) findViewById(R.id.sweepAngle);
        mArcWidth = (SeekBar) findViewById(R.id.arcWidth);
        mProgressWidth = (SeekBar) findViewById(R.id.progressWidth);
        mRoundedEdges = (CheckBox) findViewById(R.id.roundedEdges);
        mTouchInside = (CheckBox) findViewById(R.id.touchInside);
        mClockwise = (CheckBox) findViewById(R.id.clockwise);

        mRotation.setProgress(mSeekArc.getArcRotation());
        mStartAngle.setProgress(mSeekArc.getStartAngle());
        mSweepAngle.setProgress(mSeekArc.getSweepAngle());
        mArcWidth.setProgress(mSeekArc.getArcWidth());
        mProgressWidth.setProgress(mSeekArc.getProgressWidth());


        mRotation.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar arg0) {

            }
            @Override
            public void onStartTrackingTouch(SeekBar arg0) {
            }

            @Override
            public void onProgressChanged(SeekBar view, int progress, boolean fromUser) {
                mSeekArc.setArcRotation(progress);
                mSeekArc.invalidate();
            }
        });

        mStartAngle.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar arg0) {

            }
            @Override
            public void onStartTrackingTouch(SeekBar arg0) {
            }

            @Override
            public void onProgressChanged(SeekBar view, int progress, boolean fromUser) {
                mSeekArc.setStartAngle(progress);
                mSeekArc.invalidate();
            }
        });

        mSweepAngle.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar arg0) {

            }
            @Override
            public void onStartTrackingTouch(SeekBar arg0) {
            }

            @Override
            public void onProgressChanged(SeekBar view, int progress, boolean fromUser) {
                mSeekArc.setSweepAngle(progress);
                mSeekArc.invalidate();
            }
        });

        mArcWidth.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar arg0) {

            }
            @Override
            public void onStartTrackingTouch(SeekBar arg0) {
            }

            @Override
            public void onProgressChanged(SeekBar view, int progress, boolean fromUser) {
                mSeekArc.setArcWidth(progress);
                mSeekArc.invalidate();
            }
        });

        mProgressWidth.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar arg0) {

            }
            @Override
            public void onStartTrackingTouch(SeekBar arg0) {
            }

            @Override
            public void onProgressChanged(SeekBar view, int progress, boolean fromUser) {
                mSeekArc.setProgressWidth(progress);
                mSeekArc.invalidate();
            }
        });

        mRoundedEdges.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                mSeekArc.setRoundedEdges(isChecked);
                mSeekArc.invalidate();
            }
        });

        mTouchInside.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                mSeekArc.setTouchInSide(false);
            }
        });

        mClockwise.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                mSeekArc.setClockwise(isChecked);
                mSeekArc.invalidate();
            }
        });


        if(timerstarted > 0)
        {
            if(reopened <timerends){
                //start countdown timer with new time.
                //set countdowntime to timerends-reopen.

                newtotalTimeCountInMilliseconds = timerends-reopened;


                number_text.setTextColor(getResources().getColor(R.color.red_highlight));
                little_hour_text2.setTextColor(getResources().getColor(R.color.red_highlight));


                minute_text.setTextColor(getResources().getColor(R.color.red_highlight));
                little_minute_text2.setTextColor(getResources().getColor(R.color.red_highlight));





                // yo

                number_text.setClickable(false);

                minute_text.setClickable(false);






                countDownTimer = new CountDownTimer(newtotalTimeCountInMilliseconds, 500) {
                    // 500 means, onTick function will be called at every 500 milliseconds

                    @Override
                    public void onTick(long leftTimeInMilliseconds) {

                        long seconds = leftTimeInMilliseconds / 1000;
                        mSeekArc.setVisibility(View.INVISIBLE);
                        start_timer.setVisibility(View.INVISIBLE);
                        //block_button1.setVisibility(View.INVISIBLE);


                        if (leftTimeInMilliseconds < timeBlinkInMilliseconds) {
                            // textViewShowTime.setTextAppearance(getApplicationContext(), R.style.blinkText);
                            // change the style of the textview .. giving a red alert style

                            if (blink) {
                                number_text.setVisibility(View.VISIBLE);
                                minute_text.setVisibility(View.VISIBLE);


                                // if blink is true, textview will be visible
                            } else {
                                number_text.setVisibility(View.INVISIBLE);
                                minute_text.setVisibility(View.INVISIBLE);


                            }

                            blink = !blink;         // toggle the value of blink
                        }

                        minute_text.setText(String.format("%02d", (seconds / 60) % 60));
                        number_text.setText(String.format("%02d", seconds / 3600));                     // format the textview to show the easily readable format
                    }


                    @Override
                    public void onFinish() {
                        // this function will be called when the timecount is finished
                        //textViewShowTime.setText("Time up!");
                        number_text.setVisibility(View.VISIBLE);
                        minute_text.setVisibility(View.VISIBLE);
                        mSeekArc.setVisibility(View.VISIBLE);
                        start_timer.setVisibility(View.VISIBLE);

                        number_text.setTextColor(getResources().getColor(R.color.red_highlight));

                        minute_text.setTextColor(getResources().getColor(R.color.red_highlight));

                        little_hour_text2.setTextColor(getResources().getColor(R.color.red_highlight));

                        little_minute_text2.setTextColor(getResources().getColor(R.color.red_highlight));

                        number_text.setClickable(true);

                        minute_text.setClickable(true);





                    }

                }.start();



            }
        }

    }

    private static final long TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    @Override
    public void onBackPressed()
    {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
        {
            super.onBackPressed();
            return;
        }
        else { Toast.makeText(getBaseContext(), "Tap back button again to exit", Toast.LENGTH_SHORT).show(); }

        mBackPressed = System.currentTimeMillis();
    }

    public void addListenerOnButton() {

        final Context context = this;

        block_button1 = (CircleButton) findViewById(R.id.block_button);

        block_button1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(context, InstalledAppActivity.class);
                startActivity(intent);

            }

        });

    }

    private void setActionListeners() {

        number_text = (TextView) findViewById(R.id.hour_progress_number);
        minute_text = (TextView) findViewById(R.id.minute_progress_number);

        start_timer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                AlertDialog.Builder zeroerror = new AlertDialog.Builder(MainActivity.this)
                        .setMessage("Dude, you didn't set a time! :P")
                        .setNegativeButton("Whoops! My bad!", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("AlertDialog", "Negative");
                                dialog.cancel();
                            }
                        });


                AlertDialog alertzero = zeroerror.create();


                AlertDialog.Builder noapp = new AlertDialog.Builder(MainActivity.this)
                        .setMessage("Hey silly! You didn't select any apps to block!")
                        .setNegativeButton("Oh, silly me!", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("AlertDialog", "Negative");
                                dialog.cancel();
                            }
                        });


                AlertDialog zeroapp = noapp.create();



                hourint = Integer.valueOf(number_text.getText().toString());

                minuteint = Integer.valueOf(minute_text.getText().toString());



                AlertDialog.Builder timeset = new AlertDialog.Builder(MainActivity.this)


                        .setMessage("Are you sure you want to block the selected apps for "+ hourint +" hour(s) and "+ minuteint + " minutes?!")
                        .setPositiveButton("Yeah man!", new DialogInterface.OnClickListener() {



                            public void onClick(DialogInterface dialog, int which) {

                                number_text.setTextColor(getResources().getColor(R.color.red_highlight));

                                minute_text.setTextColor(getResources().getColor(R.color.red_highlight));

                                little_hour_text2.setTextColor(getResources().getColor(R.color.red_highlight));

                                little_minute_text2.setTextColor(getResources().getColor(R.color.red_highlight));

                                number_text.setClickable(false);

                                minute_text.setClickable(false);


                                hourint = Integer.valueOf(number_text.getText().toString());

                                minuteint = Integer.valueOf(minute_text.getText().toString());


                                //Make sure it stays alive no matter what until stopservice is called when the timer runs out


                                Date currenttime = new Date(System.currentTimeMillis());

                                timerstarted = currenttime.getTime();
                                startimerPreferences = getPreferences(MODE_APPEND);
                                SharedPreferences.Editor starteditor = startimerPreferences.edit();
                                starteditor.putLong("time", timerstarted);
                                starteditor.apply();


                                Date endtime = new Date(System.currentTimeMillis());


                                if ((((hourint * 60 * 60) + (minuteint * 60)  ) * 1000) > 0) {
                                    timerends = endtime.getTime() + (((hourint * 60 * 60) + (minuteint * 60) ) * 1000);


                                } else {
                                    timerends = 0;
                                }


                                Log.e("This is the end time:  ", timerends + "");
                                endTimerPreferences = getPreferences(MODE_APPEND);
                                SharedPreferences.Editor endeditor = endTimerPreferences.edit();
                                endeditor.putLong("endtime", timerends);
                                endeditor.apply();

                                endservice = getApplicationContext().getSharedPreferences("endservice", Context.MODE_PRIVATE);
                                SharedPreferences.Editor serviceeditor = endservice.edit();
                                serviceeditor.putLong("endservice", timerstarted + (((hourint * 60 * 60) + (minuteint * 60) ) * 1000));
                                Log.e("Check out this time:  ", timerends + "");

                                serviceeditor.apply();


                                totalTimeCountInMilliseconds = (((hourint * 60 * 60) + (minuteint * 60) ) * 1000);      // time count
                                timeBlinkInMilliseconds = 30 * 1000;

                                countDownTimer = new CountDownTimer(totalTimeCountInMilliseconds, 500) {
                                    // 500 means, onTick function will be called at every 500 milliseconds

                                    @Override
                                    public void onTick(long leftTimeInMilliseconds) {

                                        long seconds = leftTimeInMilliseconds / 1000;
                                        mSeekArc.setVisibility(View.INVISIBLE);
                                        start_timer.setVisibility(View.INVISIBLE);
                                        //block_button1.setVisibility(View.INVISIBLE);


                                        if (leftTimeInMilliseconds < timeBlinkInMilliseconds) {
                                            // textViewShowTime.setTextAppearance(getApplicationContext(), R.style.blinkText);
                                            // change the style of the textview .. giving a red alert style

                                            if (blink) {
                                                number_text.setVisibility(View.VISIBLE);
                                                minute_text.setVisibility(View.VISIBLE);


                                                // if blink is true, textview will be visible
                                            } else {
                                                number_text.setVisibility(View.INVISIBLE);
                                                minute_text.setVisibility(View.INVISIBLE);


                                            }

                                            blink = !blink;         // toggle the value of blink
                                        }

                                        minute_text.setText(String.format("%02d", (seconds / 60) % 60));
                                        number_text.setText(String.format("%02d", seconds / 3600));                     // format the textview to show the easily readable format
                                    }


                                    @Override
                                    public void onFinish() {
                                        number_text.setVisibility(View.VISIBLE);
                                        minute_text.setVisibility(View.VISIBLE);
                                        mSeekArc.setVisibility(View.VISIBLE);
                                        start_timer.setVisibility(View.VISIBLE);


                                        number_text.setTextColor(getResources().getColor(R.color.red_highlight));

                                        minute_text.setTextColor(getResources().getColor(R.color.red_highlight));

                                        little_hour_text2.setTextColor(getResources().getColor(R.color.red_highlight));

                                        little_minute_text2.setTextColor(getResources().getColor(R.color.red_highlight));

                                        number_text.setClickable(true);

                                        minute_text.setClickable(true);


                                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                        Notification myNotification = new Notification(R.drawable.ic_launcher, "Time's up!", System.currentTimeMillis());
                                        Context context = getApplicationContext();
                                        String notificationTitle = "Apps are now unlocked!";
                                        String notificationText = "Great job being productive! ;)";
                                        Intent myIntent = new Intent(MainActivity.this, MainActivity.class);
                                        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0,   myIntent, Intent.FILL_IN_ACTION);
                                        myNotification.flags |= Notification.FLAG_AUTO_CANCEL;
                                        myNotification.setLatestEventInfo(context, notificationTitle, notificationText, pendingIntent);
                                        notificationManager.notify(1, myNotification);


                                    }

                                }.start();
                            }
                        });
                timeset.setNegativeButton("Nope!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("AlertDialog", "Negative");
                        dialog.cancel();
                    }
                });


                AlertDialog timerright = timeset.create();

                hourint = Integer.valueOf(number_text.getText().toString());

                minuteint = Integer.valueOf(minute_text.getText().toString());




                sharedPrefsapp = getApplicationContext().getSharedPreferences("appdb", Context.MODE_PRIVATE);
                allEntries= null;
                allEntries = sharedPrefsapp.getAll();


                for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                    packagezList.add(entry.getKey());

                }



                if((((hourint * 60 * 60) + (minuteint * 60)  ) * 1000) == 0)
                {
                    //alertzero.show();
                }

                else if (packagezList.size() ==0){
                    noapp.show();
                }



                else
                {
                    timerright.show();
                }

            }


        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        ApplicationCheck.activityPaused();
    }


}