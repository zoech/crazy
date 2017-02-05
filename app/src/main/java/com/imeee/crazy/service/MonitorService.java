package com.imeee.crazy.service;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.imeee.crazy.BaseApp;
import com.imeee.crazy.BroadcastReceiver.MonitorReceiver;
import com.imeee.crazy.R;
import com.imeee.crazy.activity.HisDiffActivity;
import com.imeee.crazy.activity.HomeActivity;
import com.imeee.crazy.api.ApiInterface;
import com.imeee.crazy.api.SecInfo;
import com.imeee.crazy.dao.Differ;
import com.imeee.crazy.enity.Rank;
import com.imeee.crazy.enity.RankDiffer;
import com.imeee.crazy.misc.RankDiff;
import com.imeee.crazy.misc.ResHandle;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zoey on 2/4/17.
 */

public class MonitorService extends Service {

    private static int notifyId = 0;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @TargetApi(19)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("--------------", "executed at " + new Date().
                        toString());

                getRank();
            }
        }).start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long interval = ((BaseApp)getApplication()).getMonitorInterval();
        long triggerAtTime = 0;

        Calendar currentTime = Calendar.getInstance();
        int currentHour = currentTime.get(Calendar.HOUR_OF_DAY);

        if(currentHour > 1 && currentHour < 5){
            long intervalNight = ((BaseApp)getApplication()).getMonitorIntervalNight();

            Calendar fiveOclock = Calendar.getInstance();
            Calendar nextTime = Calendar.getInstance();

            fiveOclock.set(Calendar.HOUR_OF_DAY, 5);
            fiveOclock.set(Calendar.MINUTE,0);
            fiveOclock.set(Calendar.MILLISECOND,0);

            nextTime.add(Calendar.MILLISECOND, (int)intervalNight);

            if(nextTime.after(fiveOclock)){
                // next time is late than 5 o'clock
                long intervalTmp = fiveOclock.getTimeInMillis() - currentTime.getTimeInMillis();
                triggerAtTime = SystemClock.elapsedRealtime() + intervalTmp;
            } else {
                triggerAtTime = SystemClock.elapsedRealtime() + intervalNight;
            }
        } else {
            triggerAtTime = SystemClock.elapsedRealtime() + interval;
        }


        //triggerAtTime = SystemClock.elapsedRealtime() + interval;
        Intent i = new Intent(this, MonitorReceiver.class);
        i.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);

        if(Build.VERSION.SDK_INT >= 19){
            manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        } else {
            manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        }

        //manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }


    private void getRank(){

        final BaseApp app = (BaseApp)getApplication();
        if(app == null){
            return;
        }
        ApiInterface i = app.getHttpClient().getApiInterface();

        Call<String> getRecipe = i.getRank("", SecInfo.PARAMS,SecInfo.ENCSECKEY);

        getRecipe.enqueue(new Callback<String>(){
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if(response.body() == null || response.body().equals("")){
                    // no response body
                } else {

                    String jsonStr = response.body();
                    Log.i("+++", jsonStr);

                    JSONObject jobj = JSON.parseObject(jsonStr);

                    if(ResHandle.getResponseCode(jobj) == 200) {
                        // response ok

                        Rank newRank = ResHandle.parseRank(jobj);

                        if(app.getNewRank() != null) {
                            app.setOldRank(app.getNewRank());
                        }

                        RankDiffer rankDiffer = null;
                        if(app.getOldRank() == null){
                            rankDiffer = RankDiff.rankDiff(null,newRank);
                        } else {
                            rankDiffer = RankDiff.rankDiff(app.getOldRank(),newRank);
                        }

                        app.setNewRank(newRank);
                        app.setRankDiffer(rankDiffer);


                        if(rankDiffer.getIsChg()){
                            Differ differ = app.saveDiffToDb(rankDiffer);
                            // save the new rank to preference cache
                            app.getRankPref().setOldRank(JSON.toJSONString(newRank));


                            // show a notifycation
                            if(differ != null) {
                                CreateInform(differ);
                            } else {
                                Toast.makeText(getApplicationContext(), "in monitor service, on network response, differ return by saveDiffToDb on BaseApp is null",
                                        Toast.LENGTH_LONG);
                            }

                        }

                        HomeActivity instance = HomeActivity.getInstance();
                        if(instance != null) {
                            instance.notifyAllListUpdate();
                        }

                    } // end of code 200
                    else { // code in response json is not 200
                        //alertDilog("network info", "the code of return json is not 200");
                    }
                }
            } // end of onResponse

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("+++", t.toString());
                t.printStackTrace();

                //alertDilog("network info", "network failed, check your network");

            }
        });

    }

    public void CreateInform(Differ differ) {
        int notifyId = ((BaseApp)getApplication()).getNotifyId();

        //定义一个PendingIntent，当用户点击通知时，跳转到某个Activity(也可以发送广播等)
        Intent intent = new Intent(getApplicationContext(),HisDiffActivity.class);
        intent.putExtra(HisDiffActivity.INTENT_KEY_DIFFERID, differ.getId());
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), notifyId,
                                                        intent, PendingIntent.FLAG_UPDATE_CURRENT);


        String contentText = differ.getTotalIsChg() ?
                "(^_^) Total rank updated!" : "(._.) Total rank not changed~";

        //创建一个通知
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

        mBuilder.setContentTitle("She's listening music~~")//设置通知栏标题
                .setContentText(contentText)
                .setContentIntent(pi) //设置通知栏点击意图
                //.setNumber(10) //设置通知集合的数量
                .setTicker("Aside") //通知首次出现在通知栏，带上升动画效果的
                //.setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setWhen(differ.getUpdateDate().getTime())
                .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_ALL)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                .setSmallIcon(R.drawable.ic_launcher);//设置通知小ICON


        mNotificationManager.notify(notifyId, mBuilder.build());

        //Log.i("---- notify, differ id", String.valueOf(differ.getId()));
        //Log.i("---- notify, notify id", String.valueOf(notifyId));

        //notifyId += 1;
    }

}
