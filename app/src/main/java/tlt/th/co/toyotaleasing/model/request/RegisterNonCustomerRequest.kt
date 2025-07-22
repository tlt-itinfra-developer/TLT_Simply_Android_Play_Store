package tlt.th.co.toyotaleasing.model.request

import android.os.Build
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.jaredrummler.android.device.DeviceName
import tlt.th.co.toyotaleasing.BuildConfig
import tlt.th.co.toyotaleasing.manager.LocalizeManager

class RegisterNonCustomerRequest {

    @SerializedName("PSELECT")
    @Expose
    var pselect: String? = null
    @SerializedName("PKEY1")
    @Expose
    var platform: String? = null
    @SerializedName("PKEY2")
    @Expose
    var brand: String? = null
    @SerializedName("PKEY3")
    @Expose
    var fcmToken: String? = null
    @SerializedName("PKEY4")
    @Expose
    var osVersion: String? = null
    @SerializedName("PKEY5")
    @Expose
    var appName: String? = null
    @SerializedName("PKEY6")
    @Expose
    var appVersion: String? = null
    @SerializedName("PKEY7")
    @Expose
    var currentLatitude: String? = null
    @SerializedName("PKEY8")
    @Expose
    var currentLongitude: String? = null
    @SerializedName("PUSER")
    @Expose
    var puser: String? = null

    companion object {
        fun build(): RegisterNonCustomerRequest {
            return RegisterNonCustomerRequest().apply {
                pselect = LocalizeManager.getLanguage()
                platform = "CUST_ANDROID"
                brand = "${Build.MANUFACTURER} ${DeviceName.getDeviceName()}"
                fcmToken = FirebaseInstanceId.getInstance().token
                osVersion = "Android ${Build.VERSION.RELEASE}"
                appName = BuildConfig.APP_NAME
                appVersion = BuildConfig.VERSION_NAME
                currentLatitude = ""
                currentLongitude = ""
                puser = ""
            }
        }

        fun buildForStaff(): RegisterNonCustomerRequest {
            return RegisterNonCustomerRequest().apply {
                pselect = LocalizeManager.getLanguage()
                platform = "STAFF_ANDROID"
                brand = "${Build.MANUFACTURER} ${DeviceName.getDeviceName()}"
                fcmToken = FirebaseInstanceId.getInstance().token
                osVersion = "Android ${Build.VERSION.RELEASE}"
                appName = BuildConfig.APP_NAME
                appVersion = BuildConfig.VERSION_NAME
                currentLatitude = ""
                currentLongitude = ""
                puser = ""
            }
        }
    }
}