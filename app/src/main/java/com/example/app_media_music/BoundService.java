package com.example.app_media_music;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class BoundService extends Service {
    Binder binder = new LocalBinder();
    String a;
    public static String ACTION_PLAY="PLAY";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        a= intent.getStringExtra("title");
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
    public void CreateNotification(){

        Intent playIntent = new Intent(this, MainActivity.class);
        /* title = intent1.getStringExtra("title");// bi null tên bài*/
        playIntent.setAction(ACTION_PLAY);


        PendingIntent pendingIntentPrevious = PendingIntent.getBroadcast(this,0,playIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntentPlay= PendingIntent.getBroadcast(this,0,playIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntentNext = PendingIntent.getBroadcast(this,0,playIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder1 = new NotificationCompat.Builder(this, "123")
                .setContentTitle("Music Studio")
                .setContentText(a)
                .setSmallIcon(R.mipmap.music_2)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.img_loiyeu))
                .addAction(R.drawable.ic_baseline_skip_previous_24_noti,"previous",null)
                .addAction(R.drawable.ic_baseline_play_arrow_24_noti,"play",pendingIntentPlay)
                .addAction(R.drawable.ic_baseline_skip_next_24_noti,"next",null)

                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle());
                  /*  .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(BitmapFactory
                                .decodeResource(Resources.getSystem(),R.drawable.img_loiyeu)));*/

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("123", "Option", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(1,builder1.build());
    }
}
