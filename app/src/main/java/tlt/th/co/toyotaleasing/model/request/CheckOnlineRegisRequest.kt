package tlt.th.co.toyotaleasing.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import tlt.th.co.toyotaleasing.manager.LocalizeManager

class CheckOnlineRegisRequest {

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


    companion object {
        fun build(idcard: String,
                  refID: String): CheckOnlineRegisRequest {
            return CheckOnlineRegisRequest().apply {
                pselect = ""
                pkey1 = idcard
                pkey2 = refID
                pkey3 = ""
            }
        }
    }
}