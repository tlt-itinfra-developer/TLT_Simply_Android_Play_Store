package tlt.th.co.toyotaleasing.manager.notification

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
//import com.google.firebase.messaging.FirebaseMessagingService

import org.greenrobot.eventbus.EventBus
import tlt.th.co.toyotaleasing.common.eventbus.FCMTokenRefreshedEvent

class AppFirebaseInstanceIDService : FirebaseInstanceIdService(){

    override fun onTokenRefresh() {
        val token = FirebaseInstanceId.getInstance().token
        EventBus.getDefault().post(FCMTokenRefreshedEvent(token!!))
    }

//    override fun onNewToken(token: String) {
//        super.onNewToken(token)
//        Log.i(TAG, "receive token:$token")
//        val token = FirebaseInstanceId.getInstance().token
//        EventBus.getDefault().post(FCMTokenRefreshedEvent(token!!))
//    }

    companion object {
        private val TAG = "FCM"
    }
}