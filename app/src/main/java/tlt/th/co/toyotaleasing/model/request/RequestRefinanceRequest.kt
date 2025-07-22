package tlt.th.co.toyotaleasing.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestRefinanceRequest {

    @SerializedName("PKEY1")
    @Expose
    var PKEY1 = ""
    @SerializedName("PKEY2")
    @Expose
    var PKEY2 = ""

    companion object {
        fun build(contractNumber: String): RequestRefinanceRequest {
            return RequestRefinanceRequest().apply {
                PKEY1 = contractNumber
            }
        }
    }
}