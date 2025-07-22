package tlt.th.co.toyotaleasing.model.request

import com.google.gson.annotations.SerializedName

data class RequestQuotationRequest(
        @SerializedName("PKEY1") var pKEY1: String? = "",
        @SerializedName("PKEY2") var pKEY2: String? = "",
        @SerializedName("PKEY3") var pKEY3: String? = "",
        @SerializedName("PKEY4") var pKEY4: String? = "",
        @SerializedName("PKEY5") var pKEY5: String? = "",
        @SerializedName("PKEY6") var pKEY6: String? = "",
        @SerializedName("PKEY7") var pKEY7: String? = "",
        @SerializedName("PKEY8") var pKEY8: String? = "",
        @SerializedName("PKEY9") var pKEY9: String? = "",
        @SerializedName("PKEY10") var pKEY10: String? = "",
        @SerializedName("PKEY11") var pKEY11: String? = "",
        @SerializedName("PKEY12") var pKEY12: List<PKEY12?>? = listOf(),
        @SerializedName("PKEY13") var pKEY13: String? = "",
        @SerializedName("PKEY14") var pKEY14: String? = ""
) {
    data class PKEY12(
            @SerializedName("PKEY1") var pKEY1: String? = "",
            @SerializedName("PKEY2") var pKEY2: String? = "",
            @SerializedName("PKEY3") var pKEY3: String? = "",
            @SerializedName("PKEY4") var pKEY4: String? = "",
            @SerializedName("PKEY5") var pKEY5: String? = "",
            @SerializedName("PKEY6") var pKEY6: String? = "",
            @SerializedName("PKEY7") var pKEY7: String? = "",
            @SerializedName("PKEY8") var pKEY8: String? = "",
            @SerializedName("PKEY9") var pKEY9: String? = ""
    ) {
        companion object {
            fun build(lineItem: String = "",
                      insCompany: String = "",
                      insCompanyCode: String = "",
                      policyType: String = "",
                      policyTypeCode: String = "",
                      payType: String = "",
                      payTypeCode: String = ""): PKEY12 {
                return PKEY12().apply {
                    pKEY1 = lineItem
                    pKEY2 = insCompany
                    pKEY3 = insCompanyCode
                    pKEY4 = policyType
                    pKEY5 = policyTypeCode
                    pKEY6 = payType
                    pKEY7 = payTypeCode
                }
            }
        }
    }

    companion object {
        fun build(extContractNumber: String = "",
                  customerName: String = "",
                  regisNo: String = "",
                  regisProvince: String = "",
                  provinceCode: String = "",
                  vehicleModel: String = "",
                  customerPhone: String = "",
                  customerEmail: String = "",
                  customerRemark: String = "",
                  isCall: Boolean = false,
                  quotationList: List<PKEY12> = listOf()): RequestQuotationRequest {
            return RequestQuotationRequest().apply {
                pKEY1 = extContractNumber
                pKEY2 = customerName
                pKEY3 = regisNo
                pKEY4 = regisProvince
                pKEY5 = provinceCode
                pKEY6 = vehicleModel
                pKEY7 = customerPhone
                pKEY8 = customerEmail
                pKEY9 = customerRemark
                pKEY10 = if (isCall) "Y" else "N"
                pKEY11 = if (!isCall) "Y" else "N"
                pKEY12 = quotationList
            }
        }
    }
}