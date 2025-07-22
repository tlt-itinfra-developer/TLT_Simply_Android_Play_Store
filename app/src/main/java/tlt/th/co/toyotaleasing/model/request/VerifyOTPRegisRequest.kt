package tlt.th.co.toyotaleasing.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class VerifyOTPRegisRequest {

    @SerializedName("PKEY1")
    @Expose
    var PKEY1 = "REGISTER"
    @SerializedName("PKEY2")
    @Expose
    var PKEY2 = ""
    @SerializedName("PKEY3")
    @Expose
    var PKEY3 = ""
    @SerializedName("PKEY4")
    @Expose
    var PKEY4 = ""
    @SerializedName("PKEY5")
    @Expose
    var PKEY5 = ""
    @SerializedName("PKEY6")
    @Expose
    var PKEY6 = ""
    @SerializedName("PKEY7")
    @Expose
    var PKEY7 = ""

    companion object {
        fun build(otp: String): VerifyOTPRegisRequest {
            return VerifyOTPRegisRequest().apply {
                PKEY4 = otp
            }
        }
    }
}