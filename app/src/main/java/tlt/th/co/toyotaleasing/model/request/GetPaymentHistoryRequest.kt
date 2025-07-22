package tlt.th.co.toyotaleasing.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetPaymentHistoryRequest {

    @SerializedName("PSELECT")
    @Expose
    var pselect: String = ""
    @SerializedName("PKEY1")
    @Expose
    var pkey1: String = ""
    @SerializedName("PKEY2")
    @Expose
    var pkey2: String = ""

    companion object {
        fun build(contractNumber: String): GetPaymentHistoryRequest {
            return GetPaymentHistoryRequest().apply {
                pkey1 = contractNumber
            }
        }
    }
}