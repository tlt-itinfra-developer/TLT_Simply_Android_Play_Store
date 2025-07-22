package tlt.th.co.toyotaleasing.manager.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.greenrobot.eventbus.EventBus
import org.threeten.bp.Instant
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.eventbus.FCMTokenRefreshedEvent
import tlt.th.co.toyotaleasing.common.eventbus.UpdateBadgeNotificationEvent
import tlt.th.co.toyotaleasing.manager.BusManager
import tlt.th.co.toyotaleasing.manager.db.NotifyManager
import tlt.th.co.toyotaleasing.manager.db.UserManager
import java.net.HttpURLConnection
import java.net.URL


class AppFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.d("Token", "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        EventBus.getDefault().post(FCMTokenRefreshedEvent(token!!))
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if ((remoteMessage == null || remoteMessage.data == null)
                || remoteMessage.data.isEmpty()) {
            return
        }
        Log.e("TEST", remoteMessage.data.toString())
        val notify = NotifyManager.saveNotify(remoteMessage.data, remoteMessage.messageId!!)
        BusManager.observe(UpdateBadgeNotificationEvent(UserManager.getInstance().getUnreadNotification()))

        handlePushPopupNotify()
        handleOtherNotify()
        sendNotification(
                notify.title ?: "",
                notify.msg ?: "",
                notify.image ?: "",
                notify.navigation ?: "")

    }

    private fun handlePushPopupNotify() {
        if (!NotifyManager.isShowPushPopup()) {
            return
        }

        NotifyManager.showPushPopupImmediately()
    }

    private fun handleOtherNotify() {
        if (NotifyManager.isShowPushPopup()) {
            return
        }

        NotifyManager.triggerNotifyImmediately()
    }

    private fun sendNotification(title: String = "",
                                 messageBody: String = "",
                                 imagePath: String = "",
                                 navigation: String = "") {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(navigation))
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(NotificationCompat.BigTextStyle().bigText(messageBody))

        if (imagePath.isNotEmpty()) {
            val bitmap = getBitmapFromUrl(imagePath)
            notificationBuilder.setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(channelId, title, NotificationManager.IMPORTANCE_DEFAULT)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }

        with(NotificationManagerCompat.from(this)) {
            notify(Instant.now().nano, notificationBuilder.build())
        }
    }

    private fun getBitmapFromUrl(imageUrl: String): Bitmap? {
        return try {
            val url = URL(imageUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    companion object {
        private val TAG = "AppFirebaseMessagingService"
    }
}