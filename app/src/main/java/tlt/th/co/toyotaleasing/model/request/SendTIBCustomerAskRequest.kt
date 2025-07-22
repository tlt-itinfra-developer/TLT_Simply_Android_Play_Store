package tlt.th.co.toyotaleasing.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SendTIBCustomerAskRequest {

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

    companion object {
        fun build(askId: String = "",
                  description: String = "",
                  username: String = "",
                  mobileNumber: String = "",
                  email: String = "",
                  extContract: String = ""): SendTIBCustomerAskRequest {
            return SendTIBCustomerAskRequest().apply {
                pselect = askId
                pkey1 = description
                pkey2 = username
                pkey3 = mobileNumber
                pkey4 = email
                pkey5 = extContract
            }
        }
    }
}