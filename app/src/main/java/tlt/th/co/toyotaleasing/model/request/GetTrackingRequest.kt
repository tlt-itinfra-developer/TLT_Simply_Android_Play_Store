package tlt.th.co.toyotaleasing.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetTrackingRequest {

    @SerializedName("PKEY1")
    @Expose
    var PKEY1 = ""
    @SerializedName("PKEY2")
    @Expose
    var PKEY2 = ""
    @SerializedName("PKEY3")
    @Expose
    var PKEY3 = ""

    companion object {
        fun build(contractNumber: String): GetTrackingRequest {
            return GetTrackingRequest().apply {
                PKEY1 = contractNumber
            }
        }
    }
}