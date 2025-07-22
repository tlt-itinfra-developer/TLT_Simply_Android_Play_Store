package tlt.th.co.toyotaleasing.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SetPINRegisRequest {

    @SerializedName("PSELECT")
    @Expose
    var pselect: String = "INSERT"
    @SerializedName("PKEY1")
    @Expose
    var pkey1: String = ""
    @SerializedName("PKEY2")
    @Expose
    var pkey2: String = ""
    @SerializedName("PKEY3")
    @Expose
    var pkey3: String = ""

    companion object {
        fun buildForRegister(password: String): SetPINRegisRequest {
            return SetPINRegisRequest().apply {
                pkey1 = password
            }
        }

        fun buildForForgotPincode(password: String): SetPINRegisRequest {
            return SetPINRegisRequest().apply {
                pselect = "FORGOT"
                pkey1 = password
            }
        }
    }
}