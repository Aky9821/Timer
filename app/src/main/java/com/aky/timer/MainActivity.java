package com.aky.timer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    int state;
    CountDownTimer countDownTimer;
    MediaPlayer mediaPlayer;
    AudioManager audioManager;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        state = 0;
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setMin(1);
        seekBar.setMax(120);

        TextView stateText=(TextView) findViewById(R.id.stateTextView);
        stateText.setText("Timer Ready");

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (state == 0) {
                    EditText minute = (EditText) findViewById(R.id.minute);
                    EditText hour = (EditText) findViewById(R.id.hour);

                    if (progress < 9) {
                        minute.setText("0" + progress);
                        hour.setText("00");
                    } else if (progress < 59) {
                        minute.setText("" + progress);
                        hour.setText("00");
                    } else {

                        int h = progress / 60;
                        int m = progress % 60;
                        minute.setText("" + m);
                        hour.setText("" + h);

                    }
                }
                fixFxn();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    public void upFxn(View v) {

        Button buttonPressed = (Button) v;
        EditText number;
        String tag = buttonPressed.getTag().toString();
        int Id = getResources().getIdentifier(tag, "id", getPackageName());
        number = (EditText) findViewById(Id);
        String text = number.getText().toString();
        if (text.equals(""))
            number.setText("01");
        else {
            int n = Integer.parseInt(text);
            n++;
            if (n > 99)
                number.setText("00");
            else if (n < 10)
                number.setText("0" + n);
            else
                number.setText("" + n);
        }
        fixFxn();
        if (state == 1) {
            countDownTimer.cancel();
            run();
            countDownTimer.start();
        }
    }

    public void downFxn(View v) {

            Button buttonPressed = (Button) v;
            EditText number;
            String tag = buttonPressed.getTag().toString();
            int Id = getResources().getIdentifier(tag, "id", getPackageName());
            number = (EditText) findViewById(Id);
            String text = number.getText().toString();
            if (text.equals(""))
                number.setText("00");
            else {
                int n = Integer.parseInt(text);
                n--;
                if (n < 0)
                    number.setText("00");
                else if (n < 10)
                    number.setText("0" + n);
                else
                    number.setText("" + n);
            }

        fixFxn();
        if (state == 1) {
            countDownTimer.cancel();
            run();
            countDownTimer.start();
        }
    }

    public void fixFxn() {
        EditText hour = (EditText) findViewById(R.id.hour);
        EditText minute = (EditText) findViewById(R.id.minute);
        EditText second = (EditText) findViewById(R.id.second);
        String hourText = hour.getText().toString();
        String minuteText = minute.getText().toString();
        String secondText = second.getText().toString();
        int s, m, h;
        if (!secondText.equals(""))
            s = Integer.parseInt(secondText);
        else
            s = 0;
        if (!minuteText.equals(""))
            m = Integer.parseInt(minuteText);
        else
            m = 0;
        if (!hourText.equals(""))
            h = Integer.parseInt(hourText);
        else
            h = 0;

        if (s > 59) {
            m = m + 1;
            s = s - 60;
        }
        if (m > 59) {
            h = h + 1;
            m = m - 60;
        }
        if (s < 10)
            second.setText("0" + s);
        else
            second.setText("" + s);

        if (m < 10)
            minute.setText("0" + m);
        else
            minute.setText("" + m);

        if (h < 10)
            hour.setText("0" + h);
        else
            hour.setText("" + h);

    }

    public void run() {

        EditText hour = (EditText) findViewById(R.id.hour);
        EditText minute = (EditText) findViewById(R.id.minute);
        EditText second = (EditText) findViewById(R.id.second);
        String hourText = hour.getText().toString();
        String minuteText = minute.getText().toString();
        String secondText = second.getText().toString();
        final int[] s = new int[1];
        final int[] m = new int[1];
        final int[] h = new int[1];
        mediaPlayer = MediaPlayer.create(this,R.raw.audio1);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        //audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,maxVolume,0);
        s[0] = Integer.parseInt(secondText);
        m[0] = Integer.parseInt(minuteText);
        h[0] = Integer.parseInt(hourText);
        int total = (s[0] + m[0] * 60 + h[0] * 60 * 60) * 1000;
        if(total==0){
            total=60*1000;
            minute.setText("01");
            m[0]=1;
        }
        countDownTimer = new CountDownTimer(total, 1000) {

            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            int s2 = s[0];
            int m2 = m[0];
            int h2 = h[0];

            @Override
            public void onTick(long millisUntilFinished) {
                imageView.animate().rotationBy(6);
                Log.i("Second", millisUntilFinished + "");
                if (s2 == 0) {
                    if (m2 > 0) {
                        m2--;
                        s2 = 60;
                    } else {
                        if (h2 > 0) {
                            h2--;
                            m2 = 59;
                            s2 = 60;
                        }

                    }
                }
                s2--;
                second.setText("" + s2);
                minute.setText("" + m2);
                hour.setText("" + h2);
                fixFxn();
            }

            @Override
            public void onFinish() {
                mediaPlayer.start();
                TextView stateText=(TextView) findViewById(R.id.stateTextView);
                stateText.setText("Timer Completed");
                state=2;
            }
        };
    }

    public void clickFxn(View v) {
        fixFxn();
        TextView stateText=(TextView) findViewById(R.id.stateTextView);

        if (state == 0) {
            state = 1;
            run();
            countDownTimer.start();
            stateText.setText("Timer Running");
        } else if (state == 1) {
            state = 0;
            countDownTimer.cancel();
            stateText.setText("Timer Stopped");
        }
        else  if(state==2){
            mediaPlayer.stop();
           //audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,50,0);
            state=0;
            stateText.setText("Timer Ready");
        }
    }
}