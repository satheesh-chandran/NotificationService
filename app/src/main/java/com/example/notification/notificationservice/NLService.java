package com.example.notification.notificationservice;

import android.content.Intent;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

public class NLService extends NotificationListenerService {
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        Intent intent = new Intent("com.example.notification.NOTIFICATION_LISTENER_SERVICE_EXAMPLE");
        intent.putExtra("MESSAGE", "DATA passed from Service");
        sendBroadcast(intent);
    }
}
