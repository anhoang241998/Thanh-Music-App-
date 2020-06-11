package com.example.app_media_music;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.txtTenbaihat)
    TextView txtTenbaihat;
    @BindView(R.id.ImgeView)
    ImageView ImgeView;
    @BindView(R.id.ImgCardview)
    CardView ImgCardview;
    @BindView(R.id.txtTimeBegin)
    TextView txtTimeBegin;
    @BindView(R.id.txtTimeEnd)
    TextView txtTimeEnd;
    @BindView(R.id.seekbar)
    SeekBar seekbar;
    @BindView(R.id.ImgPre)
    ImageView ImgPre;
    @BindView(R.id.ImgPlay)
    ImageView ImgPlay;
    @BindView(R.id.ImgPause)
    ImageView ImgPause;
    @BindView(R.id.ImgNext)
    ImageView ImgNext;

    ArrayList<Song> arraySong;
    int positon = 0;
    int n = 1;
    MediaPlayer mediaPlayer;
    Animation animationRotate;
    BoundService boundService;
   // BroadcastReceiver mBroadcastReceiver;
    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BoundService.LocalBinder localBinder = (BoundService.LocalBinder) service;
            boundService= localBinder.getService();
            boundService.CreateNotification();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Log.d("BBB",getIntent().toString());
        addSong();
        KhoitaoMedia();
        Click();
    }
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            mediaPlayer.pause();
        }
    };

    @Override
    protected void onStart() {
        Broadcast();
        super.onStart();
    }
    public void Broadcast(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BoundService.ACTION_PLAY);
        registerReceiver(broadcastReceiver,intentFilter);
    }

    public void CreateAnimation(Context context) {
        animationRotate = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate_anim);
        ImgeView.startAnimation(animationRotate);
    }

    public void Click() {

        ImgPlay.setOnClickListener(v -> {

            CreateAnimation(MainActivity.this);
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
                ImgPlay.setBackgroundResource(R.drawable.ic_baseline_pause_24);
            } else {
                mediaPlayer.pause();
                ImgPlay.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
            }
            UpdateTime();
            setTxtTimeEnd();
            Intent intent = new Intent(this,BoundService.class);
            intent.putExtra("title","Lời yêu ngây dại");
            startService(intent);
            bindService(intent,mServiceConnection,BIND_AUTO_CREATE);
        });

        ImgPause.setOnClickListener(v -> {

            mediaPlayer.stop();
            mediaPlayer.release();
            ImgPlay.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
        });

        ImgNext.setOnClickListener(v -> {
            positon++;

            if (positon == 1)
                ImgeView.setBackgroundResource(R.drawable.img_vannho);
            if (positon == 2)
                ImgeView.setBackgroundResource(R.drawable.img_nuocmat);
            if (positon == arraySong.size()) {
                positon = 0;
            }
            if (positon == 0) ImgeView.setBackgroundResource(R.drawable.img_loiyeu);
            mediaPlayer.stop();
            mediaPlayer.release();
            KhoitaoMedia();
            mediaPlayer.start();
            ImgPlay.setBackgroundResource(R.drawable.ic_baseline_pause_24);
            setTxtTimeEnd();


        });

        ImgPre.setOnClickListener(v -> {
            positon--;
            if (positon == 0)
                ImgeView.setBackgroundResource(R.drawable.img_loiyeu);
            if (positon == 1)
                ImgeView.setBackgroundResource(R.drawable.img_vannho);
            if (positon == 2)
                ImgeView.setBackgroundResource(R.drawable.img_nuocmat);
            if (positon < 0) {
                positon = 0;
            }

            mediaPlayer.stop();
            mediaPlayer.release();
            KhoitaoMedia();
            mediaPlayer.start();
            setTxtTimeEnd();
        });
    }

    public void KhoitaoMedia() {
        mediaPlayer = MediaPlayer.create(MainActivity.this
                , arraySong.get(positon).getFile());
        txtTenbaihat.setText(arraySong.get(positon).getTitle());
    }

    public void addSong() {
        arraySong = new ArrayList<>();
        arraySong.add(new Song("Lời yêu ngây dại", R.raw.loiyeungaydai));
        arraySong.add(new Song("Vẫn nhớ", R.raw.vannho));
        arraySong.add(new Song("nước mắt anh lau bằng tình yêu mới", R.raw.nuocmatemlaubangtinhyeu));

    }

    public void setTxtTimeEnd() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        txtTimeEnd.setText(simpleDateFormat.format(mediaPlayer.getDuration()));
        seekbar.setMax(mediaPlayer.getDuration());
    }

    public void UpdateTime() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                txtTimeBegin.setText(simpleDateFormat.format(mediaPlayer.getCurrentPosition()));
                seekbar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this, 500);
            }
        }, 100);
    }


}