package com.lethithanhngan_16110396.mpver1;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import com.lethithanhngan_16110396.mpver1.ListBaiHat;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class PlayerActivity extends AppCompatActivity {
    ImageButton imgbtnPlay, imgbtnNext, imgbtnPrevious, imgbtnPause;
    TextView tvSongName, txtStartTime, txtfinalTime;
    private Handler myHandler = new Handler();;
    SeekBar seekbar;
    String snameSong;

    static MediaPlayer myMediaPlayer;
    int position;
    ArrayList<File> mySongs;
    Thread updateSeekBar;
    private double startTime = 0;
    private double finalTime = 0;
    public static int oneTimeOnly = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        imgbtnPlay = (ImageButton) findViewById(R.id.imgbtnPlay);
        imgbtnNext = (ImageButton) findViewById(R.id.imgbtnNext);
        imgbtnPrevious = (ImageButton) findViewById(R.id.imgbtnPrevious);
        tvSongName = (TextView) findViewById(R.id.tvSongName);
        txtStartTime = (TextView) findViewById(R.id.startTime);
        txtfinalTime = (TextView) findViewById(R.id.finalTime);
        seekbar = (SeekBar) findViewById(R.id.seekBarSong);
        seekbar.setClickable(false);
        updateSeekBar = new Thread(){

            @Override
            public void run() {

                int totalDuraltion = myMediaPlayer.getDuration();
                int currentPosition = 0;
                while (currentPosition<totalDuraltion){
                    try{
                        sleep(500);
                        currentPosition = myMediaPlayer.getCurrentPosition();
                        seekbar.setProgress(currentPosition);
                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        };

        if (myMediaPlayer != null){
            myMediaPlayer.stop();
            myMediaPlayer.release();
        }

        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        mySongs = (ArrayList) bundle.getParcelableArrayList("songs");

        snameSong = mySongs.get(position).getName().toString();
        String songName = i.getStringExtra("songname");
        tvSongName.setText(songName);
        tvSongName.setSelected(true);

        position = bundle.getInt("pos", 0);

        Uri u = Uri.parse(mySongs.get(position).toString());
        myMediaPlayer = MediaPlayer.create(getApplicationContext(), u);
        myMediaPlayer.start();
        finalTime = myMediaPlayer.getDuration();
        startTime = myMediaPlayer.getCurrentPosition();
        seekbar.setMax(myMediaPlayer.getDuration());
        if (oneTimeOnly == 0) {
            seekbar.setMax((int) finalTime);
            oneTimeOnly = 1;
        }
        txtfinalTime.setText(String.format("%d:%d",
                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                finalTime)))
        );

        txtStartTime.setText(String.format("%d:%d",
                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                startTime)))
        );

        seekbar.setProgress((int)startTime);
        myHandler.postDelayed(UpdateSongTime,100);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                myMediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        imgbtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekbar.setMax(myMediaPlayer.getDuration());

                if (myMediaPlayer.isPlaying()){
                    imgbtnPlay.setImageResource(R.drawable.play);
                    myMediaPlayer.pause();

                }
                else {
                    imgbtnPlay.setImageResource(R.drawable.pause);
                    myMediaPlayer.start();
                }
            }
        });
        imgbtnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // check if next song is there or not
                if(position < (mySongs.size() - 1)){
                    myMediaPlayer.stop();
                    Intent i = getIntent();
                    Bundle bundle = i.getExtras();
                    mySongs = (ArrayList) bundle.getParcelableArrayList("songs");
                    snameSong = mySongs.get(position+1).getName().toString();
                    tvSongName.setText(snameSong);
                    tvSongName.setSelected(true);
                    Uri u = Uri.parse(mySongs.get(position + 1).toString());
                    myMediaPlayer = MediaPlayer.create(getApplicationContext(), u);
                    myMediaPlayer.start();
                    finalTime = myMediaPlayer.getDuration();
                    startTime = myMediaPlayer.getCurrentPosition();
                    txtfinalTime.setText(String.format("%d:%d",
                            TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                            finalTime)))
                    );
                    position = position + 1;
                }else{
                    // play first song
                    Intent i = getIntent();
                    Bundle bundle = i.getExtras();
                    mySongs = (ArrayList) bundle.getParcelableArrayList("songs");
                    snameSong = mySongs.get(0).getName().toString();
                    tvSongName.setText(snameSong);
                    tvSongName.setSelected(true);
                    Uri u = Uri.parse(mySongs.get(0).toString());
                    myMediaPlayer = MediaPlayer.create(getApplicationContext(), u);
                    myMediaPlayer.start();
                    finalTime = myMediaPlayer.getDuration();
                    startTime = myMediaPlayer.getCurrentPosition();
                    txtfinalTime.setText(String.format("%d:%d",
                            TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                            finalTime)))
                    );
                    position = 0;

                }
            }
        });
        imgbtnPrevious.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // check if previous song is there or not
                if(position > 0){
                    myMediaPlayer.stop();
                    Intent i = getIntent();
                    Bundle bundle = i.getExtras();
                    mySongs = (ArrayList) bundle.getParcelableArrayList("songs");
                    snameSong = mySongs.get(position-1).getName().toString();
                    tvSongName.setText(snameSong);
                    tvSongName.setSelected(true);
                    Uri u = Uri.parse(mySongs.get(position - 1).toString());
                    myMediaPlayer = MediaPlayer.create(getApplicationContext(), u);
                    myMediaPlayer.start();
                    finalTime = myMediaPlayer.getDuration();
                    startTime = myMediaPlayer.getCurrentPosition();
                    txtfinalTime.setText(String.format("%d:%d",
                            TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                            finalTime)))
                    );
                    position = position - 1;
                }else{
                    // play first song
                    Intent i = getIntent();
                    Bundle bundle = i.getExtras();
                    mySongs = (ArrayList) bundle.getParcelableArrayList("songs");
                    snameSong = mySongs.get(mySongs.size() - 1).getName().toString();
                    tvSongName.setText(snameSong);
                    tvSongName.setSelected(true);
                    Uri u = Uri.parse(mySongs.get(mySongs.size() - 1).toString());
                    myMediaPlayer = MediaPlayer.create(getApplicationContext(), u);
                    myMediaPlayer.start();
                    finalTime = myMediaPlayer.getDuration();
                    startTime = myMediaPlayer.getCurrentPosition();
                    txtfinalTime.setText(String.format("%d:%d",
                            TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                            finalTime)))
                    );
                    position = 0;

                }
            }
        });
    }
    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = myMediaPlayer.getCurrentPosition();
            txtStartTime.setText(String.format("%d:%d",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)))
            );
            seekbar.setProgress((int)startTime);
            myHandler.postDelayed(this, 100);
        }
    };

}
