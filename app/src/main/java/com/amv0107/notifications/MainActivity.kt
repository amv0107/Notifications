package com.amv0107.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build.VERSION_CODES.N
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.update
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import kotlinx.coroutines.NonCancellable.cancel

class MainActivity : AppCompatActivity() {

    companion object {
        private const val PRIMARY_CHANNEL_ID = "primary_notification_channel"
        private const val NOTIFICATION_ID = 0
        private const val ACTION_UPDATE_NOTIFICATION = "com.amv0107.notifications.ACTION_UPDATE_NOTIFICATION"
    }

    private lateinit var button_notify: Button
    private lateinit var button_cancel: Button
    private lateinit var button_update: Button
    private lateinit var mNotifyManager: NotificationManager
    private var mReceiver = NotificationReceiver()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()
        registerReceiver(mReceiver, IntentFilter(ACTION_UPDATE_NOTIFICATION))

        button_notify = findViewById(R.id.notify)
        button_cancel = findViewById(R.id.cancel)
        button_update = findViewById(R.id.update)

        button_cancel.setOnClickListener { cancelNotification() }

        button_update.setOnClickListener { updateNotification() }

        button_notify.setOnClickListener { sendNotification() }

        setNotificationButtonState()

    }

    override fun onDestroy() {
        unregisterReceiver(mReceiver)
        super.onDestroy()
    }

    fun updateNotification() {
        val androidImage = BitmapFactory.decodeResource(resources, R.drawable.mascot_1)
        val nBuilder = getNotificationBuilder()
        nBuilder.setStyle(
            NotificationCompat.BigPictureStyle()
                .bigPicture(androidImage)
                .setBigContentTitle("Notification Updated!")
        )
        mNotifyManager.notify(NOTIFICATION_ID, nBuilder.build())
        setNotificationButtonState(isNotifyEnabled = false, isCancelEnabled = true)
    }

    private fun cancelNotification() {
        mNotifyManager.cancel(NOTIFICATION_ID)
        setNotificationButtonState(isNotifyEnabled = true)
    }

    private fun createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            // Create a NotificationChannel
            val notificationChannel =
                NotificationChannel(PRIMARY_CHANNEL_ID, "Mascot Notification", NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.BLUE
            notificationChannel.setShowBadge(true)
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Notification from Mascot"
            mNotifyManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            mNotifyManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun getNotificationBuilder(): NotificationCompat.Builder {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        return NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
            .setContentTitle("You've been notified!")
            .setContentText("This is your notification text.")
            .setSmallIcon(R.drawable.ic_androiod)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)
    }

    private fun sendNotification() {
        val updateIntent = Intent(ACTION_UPDATE_NOTIFICATION)
        val updatePendingIntent =
            PendingIntent.getBroadcast(this, NOTIFICATION_ID, updateIntent, PendingIntent.FLAG_ONE_SHOT)

        val nBuilder = getNotificationBuilder()
        nBuilder.addAction(R.drawable.ic_update, "Update Notification", updatePendingIntent)
        mNotifyManager.notify(NOTIFICATION_ID, nBuilder.build())
        setNotificationButtonState(isNotifyEnabled = false, isUpdateEnabled = true, isCancelEnabled = true)
    }

    /**
     * Helper method to enable/disable the buttons.
     *
     * @param isNotifyEnabled, boolean: true if notify button enabled
     * @param isUpdateEnabled, boolean: true if update button enabled
     * @param isCancelEnabled, boolean: true if cancel button enabled
     */
    private fun setNotificationButtonState(
        isNotifyEnabled: Boolean = true,
        isUpdateEnabled: Boolean = false,
        isCancelEnabled: Boolean = false
    ) {
        button_notify.isEnabled = isNotifyEnabled
        button_update.isEnabled = isUpdateEnabled
        button_cancel.isEnabled = isCancelEnabled
    }

    inner class NotificationReceiver() : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            updateNotification()
        }
    }
}