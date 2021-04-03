# NotificationService

This repository is an example of creating a Status bar notification and accessing the notification in android.
I hope this repo will be useful for those who are interested to learn android.
Before we get into the implementation details we need to understand a bit about some android components. 
I am listing them below.

- [Intent](https://developer.android.com/reference/android/content/Intent)
- [NotificationCompat.Builder](https://developer.android.com/reference/androidx/core/app/NotificationCompat.Builder)
- [PendingIntent](https://developer.android.com/reference/android/app/PendingIntent)
- [NotificationChannel](https://developer.android.com/reference/android/app/NotificationChannel)
- [NotificationManager](https://developer.android.com/reference/android/app/NotificationManager)
- [NotificationListenerService](https://developer.android.com/reference/android/service/notification/NotificationListenerService)
- [BroadcastReceiver](https://developer.android.com/reference/android/content/BroadcastReceiver)

#### Intent

- An intent is an abstract description of an operation to be performed.
- It can be used with startActivity to launch an Activity
- An Intent provides a facility for performing late runtime binding between the code in different applications.
- Its most significant use is in the launching of activities, where it can be thought of as the glue between activities

#### NotificationCompat.Builder

- Builder class for NotificationCompat objects. Allows easier control over all the flags, as well as help constructing the typical notification layouts.

#### PendingIntent

- A description of an Intent and target action to perform with it
- By giving a PendingIntent to another application, you are granting it the right to perform the operation you have specified as if the other application was yourself (with the same permissions and identity)

#### NotificationChannel

- A representation of settings that apply to a collection of similarly themed notifications.

#### NotificationManager

- Class to notify the user of events that happen. This is how you tell the user that something has happened in the background.

#### NotificationListenerService

- A service that receives calls from the system when new notifications are posted or removed, or their ranking changed.

#### BroadcastReceiver

- Base class for code that receives and handles broadcast intents sent by Context.sendBroadcast(Intent).

## Creation of Notification

The creation of a status bar notification is very simple.

```kotlin
NotificationCompat.Builder(this, channelId)
.setSmallIcon(R.drawable.ic_launcher_background)
.setContentTitle("My notification")
.setContentText("Hello boss...")
.setPriority(NotificationCompat.PRIORITY_DEFAULT)
.setWhen(System.currentTimeMillis())
.setContentIntent(this.createPendingIntent())
.setAutoCancel(true)
```

Call the `Builder` constructor with the context and a specific and unique channelId. The channelId is useful for the system to identify the notification
with similarities. There are several method in the `Notification.Builder` to customize and add features upon that. In which `setSmallIcon`, `setContentTitle`,
 `setContentText` and `setContentIntent` are very important to specify. The `setContentIntent` method receives a PendingIntent which is a future intent
and this argument specifies the action that need to be performed upon opening the notification.

## Create PendingIntent

```kotlin
private fun createPendingIntent(): PendingIntent {
    val intent = Intent(this, AlertActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
}
```

## Notify the NotificationManager

If we are ready with the notification compat builder, we are good to launch the notification.
For that get the `NotificationManager` from the system services and call the `notify` method upon that instance.

```kotlin
val builder = this.createNotificationBuilder()
val notificationManager: NotificationManager =
    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
notificationManager.notify(notificationId++, builder.build())
```

The notification id needs to be unique all the time.

We are done with the implementation of the notification except one thing that creating a notification channel.

## Create NotificationChannel

Creation of the notification service is necessary for only **API level** greater than **26** which means greater than *Android 8*
This creation should be done earlier as possible. **onCreate** would be the right place to do it.

```kotlin
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    val importance = NotificationManager.IMPORTANCE_DEFAULT
    val channel =
        NotificationChannel(channelId, getString(R.string.notification_channel), importance)
    val notificationManager = getSystemService(NotificationManager::class.java)
    channel.description = getString(R.string.channel_description)
    notificationManager.createNotificationChannel(channel)
}
```

**The app is ready to launch new Notification**

## Listening to the Notification events

This is the key part of this project that `How do we listen to the system notification (StatusBarNotification)`.
That is where the *NotificationListenerService* class is useful. Extend the class in your code base. There are few methods in this class 
we can override this class according to our convenience an usage. Which are `onNotificationPosted`, `onNotificationRemoved` etc.

In order to use this service the app need to get some permission. which we can specify in `AndroidManifest.xml` under `application` tag.

```xml
<service
    android:name="com.example.notification.notificationservice.NLService"
    android:enabled="true"
    android:permission="android.permission.BIND_NOTIFICATION_LISTENER_SERVICE">
    <intent-filter>
        <action android:name="android.service.notification.NotificationListenerService"/>
    </intent-filter>
</service>
```

Here I specified the name of the service as `com.example.notification.notificationservice.NLService` in which `com.example.notification.notificationservice` 
is the package name where I placed my service class in the code base and `NLService` is the class that extends to the `NotificationListenerService` class.
