package tlt.th.co.toyotaleasing.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SendContactAskRequest {

    @SerializedName("PSELECT")
    @Expose
    var pselect: String = ""
    @SerializedName("PKEY1")
    @Expose
    var pkey1: String = ""
    @SerializedName("PKEY2")
    @Expose
    var pkey2: String = ""
    @SerializedName("PKEY3")
    @Expose
    var pkey3: String = ""
    @SerializedName("PKEY4")
    @Expose
    var pkey4: String = ""
    @SerializedName("PKEY5")
    @Expose
    var pkey5: String = ""
    @SerializedName("PKEY6")
    @Expose
    var pkey6: String = ""
    @SerializedName("PKEY7")
    @Expose
    var pkey7: String = ""

    companion object {
        fun build(awCode: String = "",
                  description: String = "",
                  customerName: String = "",
                  phoneNumber: String = "",
                  email: String = "",
                  carLicense: String = "",
                  extContract : String = ""): SendContactAskRequest {
            return SendContactAskRequest().apply {
                pselect = awCode
                pkey1 = description
                pkey2 = customerName
                pkey3 = phoneNumber
                pkey4 = email
                pkey5 = carLicense
                pkey6 = extContract
            }
        }
    }
}