package tlt.th.co.toyotaleasing.model.request

import android.os.Build
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import tlt.th.co.toyotaleasing.BuildConfig

class UpdateTokenRequest {

    @SerializedName("PSELECT")
    @Expose
    var pselect = ""
    @SerializedName("PKEY1")
    @Expose
    var brand = ""
    @SerializedName("PKEY2")
    @Expose
    var fcmToken = ""
    @SerializedName("PKEY3")
    @Expose
    var osVersion = ""
    @SerializedName("PKEY4")
    @Expose
    var appName = ""
    @SerializedName("PKEY5")
    @Expose
    var appVersion = ""
    @SerializedName("PKEY6")
    @Expose
    var currentLatitude = ""
    @SerializedName("PKEY7")
    @Expose
    var currentLongitude = ""
    @SerializedName("PUSER")
    @Expose
    var puser = ""

    companion object {
        fun build(): UpdateTokenRequest {
            return UpdateTokenRequest().apply {
                pselect = "UTOKEN"
                brand = "${Build.MANUFACTURER} ${Build.MODEL}"
                fcmToken = FirebaseInstanceId.getInstance().token!!
                osVersion = Build.VERSION.RELEASE
                appName = BuildConfig.APP_NAME
                appVersion = BuildConfig.VERSION_NAME
                currentLatitude = ""
                currentLongitude = ""
                puser = ""
            }
        }
    }
}