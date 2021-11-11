package com.ss.ftpserver.ftpService;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.ss.ftpserver.R;
import com.ss.ftpserver.gui.HomeFragment;
import com.ss.ftpserver.gui.MainActivity;

public class FtpService extends Service {
    public FtpService() {
    }

    @Override
    public void onCreate() {
        Log.d("FTPService","create");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("FTPService","startCommand");
        //发送前台服务的通知
        //设置通知渠道
        NotificationChannel chan = new NotificationChannel(
                "MyChannelId",
                "My Foreground Service",
                NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(chan);

        //点击通知后启动main activity
        Intent notifyIntent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this,0,notifyIntent,PendingIntent.FLAG_IMMUTABLE);
        Notification notification = new NotificationCompat.Builder(this,"MyChannelId")
                .setContentTitle("ftp server")
                .setContentText("server is running")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();
        startForeground(1,notification);//开启前台任务，优先级更高，不会被系统自动回收
        return super.onStartCommand(notifyIntent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}