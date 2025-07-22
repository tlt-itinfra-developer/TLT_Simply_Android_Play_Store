package tlt.th.co.toyotaleasing.model.request

import com.google.gson.annotations.SerializedName

data class GetDataInsuranceRequest(
        @SerializedName("PKEY1") var pKEY1: String? = "",
        @SerializedName("PKEY2") var pKEY2: String? = "",
        @SerializedName("PKEY3") var pKEY3: String? = ""
) {
    companion object {
        fun build(contractNumber: String = ""): GetDataInsuranceRequest {
            return GetDataInsuranceRequest().apply {
                pKEY1 = contractNumber
            }
        }
    }
}