package tlt.th.co.toyotaleasing.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetDataPayDetailRequest {

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
        fun build(contractNumber: String , type_pay  : Boolean = false): GetDataPayDetailRequest {
            return GetDataPayDetailRequest().apply {
                pkey1 = contractNumber
                pselect = if (type_pay) { "PAYOFF" } else { "" }
            }
        }

        fun buildForPorlorborBuyNow(contractNumber: String): GetDataPayDetailRequest {
            return GetDataPayDetailRequest().apply {
                pselect = "ACT"
                pkey1 = contractNumber
            }
        }
    }
}