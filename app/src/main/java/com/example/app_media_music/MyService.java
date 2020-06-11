package com.example.app_media_music;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MyService extends android.app.Service   {
    String title;
    String ACTION_PLAY="PLAY";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
            Intent intent1 = new Intent(this, MainActivity.class);
            intent1.setAction(ACTION_PLAY);
            title = intent1.getStringExtra("title");// bi null tên bài

        PendingIntent pendingIntentPrevious = PendingIntent.getBroadcast(this,0,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntentPlay= PendingIntent.getBroadcast(this,0,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntentNext = PendingIntent.getBroadcast(this,0,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder1 = new NotificationCompat.Builder(this, "123")
                    .setContentTitle("Music Studio")
                    .setContentText(title)
                    .setSmallIcon(R.mipmap.music_2)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.img_loiyeu))
                    .addAction(R.drawable.ic_baseline_skip_previous_24_noti,"previous",pendingIntentPrevious)
                    .addAction(R.drawable.ic_baseline_play_arrow_24_noti,"play",pendingIntentPlay)
                    .addAction(R.drawable.ic_baseline_skip_next_24_noti,"next",pendingIntentNext)

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
            startForeground(1, builder1.build());

        return START_STICKY;
    }


}
