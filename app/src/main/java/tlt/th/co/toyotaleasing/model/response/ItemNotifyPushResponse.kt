package tlt.th.co.toyotaleasing.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ItemNotifyPushResponse(
        @SerializedName("title")
        @Expose
        var title: String? = "Title is empty",
        @SerializedName("seq_id")
        @Expose
        var seqId: String? = "",
        @SerializedName("option_name")
        @Expose
        var optionName: String? = "",
        @SerializedName("flag_chkbox")
        @Expose
        var flagChkbox: String? = "N",
        @SerializedName("exp_date")
        @Expose
        var expDate: String? = "",
        @SerializedName("msg_type")
        @Expose
        var msgType: String? = "",
        @SerializedName("reg_no")
        @Expose
        var regNo: String? = "",
        @SerializedName("dateTime")
        @Expose
        var dateTime: String? = "",
        @SerializedName("msg")
        @Expose
        var msg: String? = "Message is empty",
        @SerializedName("image")
        @Expose
        var image: String? = "",
        @SerializedName("media")
        @Expose
        var media: String? = "",
        @SerializedName("install_amt")
        @Expose
        var installAmt: String? = "",
        @SerializedName("ic_color")
        @Expose
        var iconColor: String? = "R",
        @SerializedName("navigation")
        @Expose
        var navigation: String? = ""
)