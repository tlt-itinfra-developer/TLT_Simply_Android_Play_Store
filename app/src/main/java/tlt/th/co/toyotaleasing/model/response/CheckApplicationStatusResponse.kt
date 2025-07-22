package tlt.th.co.toyotaleasing.model.response

import com.google.gson.annotations.SerializedName

data class CheckApplicationStatusResponse(
        @SerializedName("FLAG_APP_STATUS") var flagAppStatus: String = "",
        @SerializedName("STATUS_DES") var statusDes: String = "",
        @SerializedName("NOTICE_STATUS") var noticeStatus: String = "",
        @SerializedName("NOTICE_DES") var noticeDes: String = "",
        @SerializedName("DETAIL") var detail: String = ""
)