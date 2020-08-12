package com.example.eggtimer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Maximum limit of 600s. 600s = 10 minutes.
    public static final int MAX_TIME_SEEKBAR= 600;
    // Default value for countdown.
    public static final int DEFAULT_TIME_SEEKBAR = 30;
    // 1 minute in seconds.
    public static final int MINUTE_TO_SECONDS = 60;

    public static final int DEFAULT_SECONDS = 30;
    public static final int DEFAULT_MINUTES = 0;

    Boolean timerActivated = false;
    SeekBar seekBar;
    TextView display;
    int minutes;
    int seconds;
    int startTime;
    CountDownTimer timer;

    Button button;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // A seekbar to control the time for countdown.
        seekBar = (SeekBar) findViewById(R.id.seekBar);

        seekBar.setMax(MAX_TIME_SEEKBAR);
        seekBar.setProgress(DEFAULT_TIME_SEEKBAR);

        seconds = DEFAULT_SECONDS;
        minutes = DEFAULT_MINUTES;
        setStartTime();

        display = (TextView) findViewById(R.id.display);
        display();

        mediaPlayer = MediaPlayer.create(this, R.raw.alarm);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seconds = progress % MINUTE_TO_SECONDS;
                minutes = progress / MINUTE_TO_SECONDS;
                setStartTime();

                display();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    // Calculates the start time(seconds)
    public void setStartTime(){
        startTime = minutes * MINUTE_TO_SECONDS + seconds;
    }

    // Starts the timer
    public void setTimer(){
        timer = new CountDownTimer(startTime * 1000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                if(seconds == 0){
                    seconds = MINUTE_TO_SECONDS - 1;

                    if(minutes == 0){
                        timerActivated = false;

                    }else {
                        minutes--;

                    }

                }else{
                    seconds--;
                }

                display();
            }

            @Override
            public void onFinish() {

                mediaPlayer.start();
                status_Stop();
            }
        };
        timer.start();

    }

    public void buttonPress(View view){

        button = (Button) findViewById(R.id.enableBtn);

        if(!timerActivated){

            setTimer();
            status_Start();

        }else{

            timer.cancel();
            status_Stop();

        }
    }

    public void status_Start(){

        timerActivated = true;
        button.setText("Stop");
        seekBar.setEnabled(false);
        seekBar.setAlpha(0f);

    }

    public void status_Stop(){

        timerActivated = false;
        button.setText("Start");
        seekBar.setAlpha(1f);
        seekBar.setEnabled(true);

        minutes = DEFAULT_MINUTES;
        seconds = DEFAULT_SECONDS;

        setStartTime();

        seekBar.setProgress(startTime);
    }

    public void display(){

        String secondsString = String.format("%02d", seconds);
        display.setText(minutes + ":" + secondsString);
    }

}
