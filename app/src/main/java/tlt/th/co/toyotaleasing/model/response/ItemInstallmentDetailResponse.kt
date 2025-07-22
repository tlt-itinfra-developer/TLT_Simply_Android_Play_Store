package tlt.th.co.toyotaleasing.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ItemInstallmentDetailResponse(
        @SerializedName("DATA_TYPE")
        @Expose
        val dATATYPE: String = "",
        @SerializedName("DATA_DESC")
        @Expose
        val dATADESC: String = "",
        @SerializedName("DATA_DESC_EN")
        @Expose
        val dATADESCEN: String = "",
        @SerializedName("DATA_NO")
        @Expose
        val dATANO: String = "",
        @SerializedName("DATA_DUE_DATE")
        @Expose
        val dATADUEDATE: String = "",
        @SerializedName("DATA_AMOUNT")
        @Expose
        val dATAAMOUNT: String = "",
        @SerializedName("DATA_VAT")
        @Expose
        val dATAVAT: String = "",
        @SerializedName("DATA_SUM_TOTAL")
        @Expose
        val dATASUMTOTAL: String = ""
)