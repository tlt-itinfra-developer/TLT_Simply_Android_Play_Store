package tlt.th.co.toyotaleasing.model.request

import com.google.gson.annotations.SerializedName

data class GetDataTypeInsuranceRequest(
        @SerializedName("PSELECT") var pSELECT: String? = "",
        @SerializedName("PKEY1") var pKEY1: String? = "",
        @SerializedName("PKEY2") var pKEY2: String? = "",
        @SerializedName("PKEY3") var pKEY3: String? = ""
) {
    companion object {
        fun buildForHistory(contractNumber: String = ""): GetDataTypeInsuranceRequest {
            return GetDataTypeInsuranceRequest().apply {
                pSELECT = "HISTORY"
                pKEY1 = contractNumber
            }
        }

        fun buildForStatus(contractNumber: String = ""): GetDataTypeInsuranceRequest {
            return GetDataTypeInsuranceRequest().apply {
                pSELECT = "STATUS"
                pKEY1 = contractNumber
            }
        }
    }
}