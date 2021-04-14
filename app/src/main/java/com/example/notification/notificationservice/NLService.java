package com.example.notification.notificationservice;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.List;

public class NLService extends NotificationListenerService {
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        Notification notification = sbn.getNotification();
        Bundle extras = notification.extras;
        String title = extras.getString(Notification.EXTRA_TITLE);
        String text = extras.getString(Notification.EXTRA_TEXT);

        Log.d("Satheesh", title);
        Log.d("Satheesh", text);

        Intent intent = new Intent("com.example.notification.NOTIFICATION_LISTENER_SERVICE_EXAMPLE");
        intent.putExtra("MESSAGE", "DATA passed from Service");
        sendBroadcast(intent);
    }
}
