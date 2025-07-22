package tlt.th.co.toyotaleasing.model.request

import com.google.gson.annotations.SerializedName

data class TLTBankRequest(
        @SerializedName("PAuthorization") var authorization: String? = "",
        @SerializedName("PBANK") var pBANK: String? = "",
        @SerializedName("PSEQ") var pSEQ: String? = "",
        @SerializedName("PExtContract") var pExtContract: String? = "",
        @SerializedName("PRef1") var pRef1: String? = "",
        @SerializedName("PRef2") var pRef2: String? = "",
        @SerializedName("PAmount") var pAmount: String? = "",
        @SerializedName("PDesc") var pDesc: String? = ""
) {

    companion object {
        fun build(authorization: String = "",
                  bankCode: String = "",
                  seqCode: String = "",
                  contractNumber: String = "",
                  refCode1: String = "",
                  refCode2: String = "123456",
                  amount: String = "",
                  description: String = ""
        ): TLTBankRequest {
            return TLTBankRequest().apply {
                this.authorization = authorization
                pBANK = bankCode
                pSEQ = seqCode
                pExtContract = contractNumber
                pRef1 = refCode1
                pRef2 = refCode2
                pAmount = amount
                pDesc = description
            }
        }
    }
}