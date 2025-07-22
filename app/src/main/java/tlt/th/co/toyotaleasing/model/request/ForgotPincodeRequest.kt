package tlt.th.co.toyotaleasing.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ForgotPincodeRequest {

    @SerializedName("PKEY1")
    @Expose
    var PKEY1 = ""

    companion object {
        fun build(idCard: String): ForgotPincodeRequest {
            return ForgotPincodeRequest().apply {
                PKEY1 = idCard
            }
        }
    }
}