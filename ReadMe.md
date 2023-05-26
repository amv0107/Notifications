## Notification

[Android fundamentals 08.1:Notifications](https://developer.android.com/codelabs/android-training-notifications?index=..%2F..%2Fandroid-training#0)

## Create a basic notification

### 1. Set the notification content

  ```kotlin
var builder = NotificationCompat.Builder(this, CHANNEL_ID)
        .setSmallIcon(R.drawable.notification_icon)
        .setContentTitle(textTitle)
        .setContentText(textContent)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT))
```
### 2. Create a channel and set the importance
```kotlin
private fun createNotificationChannel() {
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is new and not in the support library
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = getString(R.string.channel_name)
        val descriptionText = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
```
**Set the importance level**
| User-visible importance level | Importance (Android 8.0 and higher) |
| --- | :---: |
| **Urgent**  Makes a sound and appears as a heads-up notification. | IMPORTANCE_HIGH |
| **High** Makes a sound. | IMPORTANCE_DEFAULT |
| **Medium** Makes no sound. | IMPORTANCE_LOW |
| **Low** Makes no sound and doesn't appear in the status bar. | IMPORTANCE_MIN |
| **None** Makes no sound and doesn't appear in the status bar or shade. | IMPORTANCE_NONE |

### 3. Set the notification's tap action
Every notification should respond to a tap, usually to open an activity in your app that corresponds to the notification. To do so, you must specify a content intent defined with a PendingIntent object and pass it to setContentIntent().
 ```kotlin
 // Create an explicit intent for an Activity in your app
val intent = Intent(this, AlertDetails::class.java).apply {
    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
}
val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

val builder = NotificationCompat.Builder(this, CHANNEL_ID)
        .setSmallIcon(R.drawable.notification_icon)
        .setContentTitle("My notification")
        .setContentText("Hello World!")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        // Set the intent that will fire when the user taps the notification
        .setContentIntent(pendingIntent)
        .setAutoCancel(true) // automatically removes the notification when the user taps it
```

### 4. Show the notification
```kotlin
with(NotificationManagerCompat.from(this)) {
    // notificationId is a unique int for each notification that you must define
    notify(notificationId, builder.build())
}
```

<details>

<summary>Add action buttons</summary>

```kotlin
val snoozeIntent = Intent(this, MyBroadcastReceiver::class.java).apply {

action = ACTION_SNOOZE
    putExtra(EXTRA_NOTIFICATION_ID, 0)
}
val snoozePendingIntent: PendingIntent =
    PendingIntent.getBroadcast(this, 0, snoozeIntent, 0)
val builder = NotificationCompat.Builder(this, CHANNEL_ID)
        .setSmallIcon(R.drawable.notification_icon)
        .setContentTitle("My notification")
        .setContentText("Hello World!")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(pendingIntent)
        .addAction(R.drawable.ic_snooze, getString(R.string.snooze),
                snoozePendingIntent)
```
</details>

<details>

  <summary> Add a progress bar</summary>

```kotlin
val builder = NotificationCompat.Builder(this, CHANNEL_ID).apply {
    setContentTitle("Picture Download")
    setContentText("Download in progress")
    setSmallIcon(R.drawable.ic_notification)
    setPriority(NotificationCompat.PRIORITY_LOW)
}
val PROGRESS_MAX = 100
val PROGRESS_CURRENT = 0
NotificationManagerCompat.from(this).apply {
    // Issue the initial notification with zero progress
    builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false)
    notify(notificationId, builder.build())

    // Do the job here that tracks the progress.
    // Usually, this should be in a
    // worker thread
    // To show progress, update PROGRESS_CURRENT and update the notification with:
    // builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false);
    // notificationManager.notify(notificationId, builder.build());

    // When done, update the notification one more time to remove the progress bar
    builder.setContentText("Download complete")
            .setProgress(0, 0, false)
    notify(notificationId, builder.build())
}
```
</details>

<details>

<summary>Set a system-wide category</summary>

If your notification falls into one of the pre-defined notification categories defined in NotificationCompat—such as CATEGORY_ALARM, CATEGORY_REMINDER, CATEGORY_EVENT, or CATEGORY_CALL—you should declare it as such by passing the appropriate category to setCategory().

  ```kotlin
var builder = NotificationCompat.Builder(this, CHANNEL_ID)
        .setSmallIcon(R.drawable.notification_icon)
        .setContentTitle("My notification")
        .setContentText("Hello World!")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
```
</details>

<details>

<summary>Show an urgent message</summary>

 </details>

 <details>

<summary></summary>

 </details>