package tlt.th.co.toyotaleasing.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginByCustomerRequest {

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
        fun buildForPincode(pincodeWithHash: String,
                            latitude: String,
                            longitude: String): LoginByCustomerRequest {
            return LoginByCustomerRequest().apply {
                pkey1 = pincodeWithHash
                pkey2 = "N"
                pkey3 = longitude
                pkey4 = latitude
                pkey5 = ""
            }
        }

        fun buildForFingerprint(latitude: String,
                                longitude: String): LoginByCustomerRequest {
            return LoginByCustomerRequest().apply {
                pkey2 = "Y"
                pkey3 = longitude
                pkey4 = latitude
                pkey5 = ""
            }
        }
    }
}