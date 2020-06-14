package com.example.app_media_music;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class BoundService extends Service {
    Binder binder = new LocalBinder();
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private ArrayList<Song> arraySong = new ArrayList<>();
    String Title;
    String TimeEnd;
    final static String  ACTION_NEXT="NEXT",ACTION_PLAY="PLAY",ACTION_PREVIOUS="PREVIOUS";
    int mPosition =0;
    int mStateImg;
    int Img;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class LocalBinder extends Binder {
        BoundService getService(){
            return BoundService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void addSong(){
        arraySong.add(new Song("Lời yêu ngây dại",R.raw.loiyeungaydai,R.drawable.img_loiyeu));
        arraySong.add(new Song("Đừng chờ anh nữa",R.raw.dungchoanhnua,R.drawable.dungchoanhnua));
        arraySong.add(new Song("Nước mắt em lau bằng tình yêu mới",R.raw.nuocmatemlaubangtinhyeu,R.drawable.img_nuocmat));
    }
    public void CreateMedia(){
        mediaPlayer = MediaPlayer.create(this,arraySong.get(mPosition).getFile());
        Title = arraySong.get(mPosition).getTitle();
        Img  = arraySong.get(mPosition).getImg();
        Log.d("bbb",mPosition+"'");
    }

    public void Play(){
        if(mediaPlayer.isPlaying()) {
            mStateImg = 0;
            mediaPlayer.pause();
        }
        else {
            mStateImg = 1;
            mediaPlayer.start();
        }
    }

    public void Next(){

        mediaPlayer.stop();
        mediaPlayer.release();
        mPosition++;
        if(mPosition >= arraySong.size())
            mPosition=0;
        CreateMedia();
        mediaPlayer.start();
        Log.d("bbb",mPosition+"");
    }
    public void Stop(){
        mediaPlayer.stop();
        mediaPlayer.release();
    }
    public void Previous(){
        mediaPlayer.stop();
        mediaPlayer.release();
        mPosition--;
        if(mPosition < 0)
            mPosition =0;
        CreateMedia();
        mediaPlayer.start();

    }
    public void TimeEnd(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        TimeEnd =simpleDateFormat.format(mediaPlayer.getDuration());
    }
    public String getTimeEnd() {
        return TimeEnd;
    }

    public String getTitle() {
        return Title;
    }

    public void CreateNotification(int id){


        Intent intent1 = new Intent(this,MyBroadCastReceiver.class);
        intent1.setAction(ACTION_NEXT);
            PendingIntent pendingIntentPrevious = PendingIntent.getBroadcast(this,0,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntentPlay= PendingIntent.getBroadcast(this,0,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntentNext = PendingIntent.getBroadcast(getApplicationContext(),0,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder1 = new NotificationCompat.Builder(this, "123")
                .setContentTitle("Music Studio")
                .setContentText(Title)
                .setSmallIcon(R.mipmap.music_2)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),Img))
                .addAction(R.drawable.ic_baseline_skip_previous_24_noti,"previous",pendingIntentPrevious)
                .addAction(R.drawable.ic_baseline_play_arrow_24_noti,"play",pendingIntentPlay)
                .addAction(R.drawable.ic_baseline_skip_next_24_noti,"next",pendingIntentNext)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(Title))
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle());


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("123", "Option", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.enableVibration(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(id,builder1.build());

    }


}
