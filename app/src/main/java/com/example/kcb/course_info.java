package com.example.kcb;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class course_info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_info);
        Button homework = (Button)findViewById(R.id.button2);
        homework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    private int id = 1;

    public void notification(android.view.View view) {
        Drawable drawable = ContextCompat.getDrawable(this, R.mipmap.ic_launcher_foreground);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        //设置小图标
        mBuilder.setSmallIcon(R.mipmap.ic_launcher_foreground);
        //设置大图标
        mBuilder.setLargeIcon(bitmap);
        //设置标题
        mBuilder.setContentTitle("这是标题");
        //设置通知正文
        mBuilder.setContentText("这是正文，当前ID是：" + id);
        //设置摘要
        mBuilder.setSubText("这是摘要");
        //设置是否点击消息后自动clean
        mBuilder.setAutoCancel(true);
        //显示指定文本
        mBuilder.setContentInfo("Info");
        //与setContentInfo类似，但如果设置了setContentInfo则无效果
        //用于当显示了多个相同ID的Notification时，显示消息总数
        mBuilder.setNumber(2);
        //通知在状态栏显示时的文本
        mBuilder.setTicker("在状态栏上显示的文本");
        //设置优先级
        mBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
        //自定义消息时间，以毫秒为单位，当前设置为比系统时间少一小时
        mBuilder.setWhen(System.currentTimeMillis() - 3600000);
        //设置为一个正在进行的通知，此时用户无法清除通知
        mBuilder.setOngoing(true);
        //设置消息的提醒方式，震动提醒：DEFAULT_VIBRATE     声音提醒：NotificationCompat.DEFAULT_SOUND
        //三色灯提醒NotificationCompat.DEFAULT_LIGHTS     以上三种方式一起：DEFAULT_ALL
        mBuilder.setDefaults(NotificationCompat.DEFAULT_SOUND);
        //设置震动方式，延迟零秒，震动一秒，延迟一秒、震动一秒
        mBuilder.setVibrate(new long[]{0, 1000, 1000, 1000});

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
        mBuilder.setContentIntent(pIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(id++, mBuilder.build());
    }


    public void cleanNotification(View view) {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancelAll();
        mNotificationManager.cancel(1);
    }

}
