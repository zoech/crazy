package com.imeee.crazy.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.imeee.crazy.service.MonitorService;

/**
 * Created by zoey on 2/4/17.
 */

public class MonitorReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, MonitorService.class);
        //context.startService(i);
        startWakefulService(context, i);
    }
}
