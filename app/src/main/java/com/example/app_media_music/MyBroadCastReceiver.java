package com.example.app_media_music;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MyBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
            Intent intent1 = new Intent("NEXTBR");
            intent1.putExtra("actionName",intent.getAction());
            context.sendBroadcast(intent1);
    }
}
