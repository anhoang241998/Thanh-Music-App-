package com.example.app_media_music;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static com.example.app_media_music.MainActivity.PLAYER_SERVICE_NOTIFICATION_ID;

public class BoundService extends Service {
    Binder binder = new LocalBinder();
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private ArrayList<Song> arraySong = new ArrayList<>();
    String Title;
    String TimeEnd;
    public final static String ACTION_NEXT = "NEXT", ACTION_PLAY = "PLAY", ACTION_PREVIOUS = "PREVIOUS";
    int mPosition = 0;
    int mStateImg;
    int Img;
    private MediaSessionCompat mMediaSessionCompat;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getStringExtra("actionName");
            switch (action) {
                case ACTION_PLAY:
                    if (mediaPlayer.isPlaying()) {
                        updateNotification();
                        mediaPlayer.pause();
                    } else {
                        updateNotification();
                        mediaPlayer.start();
                    }
                    break;
                   /*
                    Anh tự thêm vô các trường hợp nha (nhớ là nó còn ở bên BoundService nữa á)
                case ACTION_NEXT:
                    break;
                case ACTION_PREVIOUS:
                    break;*/
            }
        }
    };


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class LocalBinder extends Binder {
        BoundService getService() {
            return BoundService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMediaSessionCompat = new MediaSessionCompat(this, "tag");
        registerReceiver(mBroadcastReceiver, new IntentFilter("NEXTBR"));
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

    public void Next() {
        mediaPlayer.stop();
        mediaPlayer.release();
        mPosition++;
        if (mPosition >= arraySong.size())
            mPosition = 0;
        CreateMedia();
        mediaPlayer.start();
    }

    public void Stop() {
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public void Previous() {
        mediaPlayer.stop();
        mediaPlayer.release();
        mPosition--;
        if (mPosition < 0)
            mPosition = 0;
        CreateMedia();
        mediaPlayer.start();
    }

    public void TimeEnd() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        TimeEnd = simpleDateFormat.format(mediaPlayer.getDuration());
    }

    public String getTimeEnd() {
        return TimeEnd;
    }

    public String getTitle() {
        return Title;
    }

    public NotificationCompat.Builder CreateNotification(int id) {
        Intent intentPlay = new Intent(this, MyBroadCastReceiver.class).setAction(ACTION_PLAY);
        PendingIntent pendingIntentPlay = PendingIntent.getBroadcast(this, 0, intentPlay, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentPrevious = new Intent(this, MyBroadCastReceiver.class).setAction(ACTION_PREVIOUS);
        PendingIntent pendingIntentPrevious = PendingIntent.getBroadcast(this, 0, intentPrevious, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentNext = new Intent(this, MyBroadCastReceiver.class).setAction(ACTION_NEXT);
        PendingIntent pendingIntentNext = PendingIntent.getBroadcast(getApplicationContext(), 0, intentNext, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder1 = new NotificationCompat.Builder(this, "123")
                .setContentTitle("Music Studio")
                .setContentText(Title)
                .setOngoing(false)
                .setSmallIcon(R.drawable.ic_music)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), Img))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(Title))
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mMediaSessionCompat.getSessionToken()));

        if (mediaPlayer.isPlaying()) {
            builder1.addAction(R.drawable.ic_baseline_skip_previous_24_noti, "previous", pendingIntentPrevious)
                    .addAction(R.drawable.ic_baseline_play_arrow_24_noti, "play", pendingIntentPlay)
                    .addAction(R.drawable.ic_baseline_skip_next_24_noti, "next", pendingIntentNext);
        } else {
            builder1.addAction(R.drawable.ic_baseline_skip_previous_24_noti, "previous", pendingIntentPrevious)
                    .addAction(R.drawable.ic_baseline_pause_24, "pause", pendingIntentPlay)
                    .addAction(R.drawable.ic_baseline_skip_next_24_noti, "next", pendingIntentNext);
        }
        return builder1;
    }

    private void updateNotification() {
        Notification Notification = CreateNotification(10).build();
        // display updated notification
        NotificationManager notificationManager = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(PLAYER_SERVICE_NOTIFICATION_ID, Notification);
    }
}
