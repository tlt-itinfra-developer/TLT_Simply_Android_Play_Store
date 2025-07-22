package tlt.th.co.toyotaleasing.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ItemPhoneNumberResponse(
        @SerializedName("PHONE")
        @Expose
        var phonenumber: String = "",
        @SerializedName("KEY_PHONE")
        @Expose
        var keyPhone: String = ""
)