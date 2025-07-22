package tlt.th.co.toyotaleasing.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ChangePincodeRequest {

    @SerializedName("PSELECT")
    @Expose
    var PSELECT = "CHANGE"
    @SerializedName("PKEY1")
    @Expose
    var PKEY1 = ""
    @SerializedName("PKEY2")
    @Expose
    var PKEY2 = ""
    @SerializedName("PKEY3")
    @Expose
    var PKEY3 = ""
    @SerializedName("PUSER")
    @Expose
    var PUSER = ""

    companion object {
        fun build(oldPincodeHash: String = "",
                  newPincodeHash: String = ""): ChangePincodeRequest {
            return ChangePincodeRequest().apply {
                PKEY1 = oldPincodeHash
                PKEY2 = newPincodeHash
            }
        }
    }
}