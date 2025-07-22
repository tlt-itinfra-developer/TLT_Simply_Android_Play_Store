package tlt.th.co.toyotaleasing.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SetPreparePaymentProcessRequest {

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
    var pkey3: List<Item> = listOf()

    data class Item(
            @SerializedName("PSELECT") var pSELECT: String? = "",
            @SerializedName("PKEY1") var pKEY1: String? = "",
            @SerializedName("PKEY2") var pKEY2: String? = "",
            @SerializedName("PKEY3") var pKEY3: String? = "",
            @SerializedName("PKEY4") var pKEY4: Double? = 0.0,
            @SerializedName("PKEY5") var pKEY5: String? = "",
            @SerializedName("PKEY6") var pKEY6: String? = ""
    )

    companion object {
        fun build(type: String = "",
                  contractNumber: String = "",
                  items: List<Item> = listOf()): SetPreparePaymentProcessRequest {
            return SetPreparePaymentProcessRequest().apply {
                pselect = type
                pkey1 = contractNumber
                pkey3 = items
            }
        }

        fun buildItem(
                bpType: String = "",
                bpCode: String = "",
                bankCode: String = "",
                amount: Double = 0.0,
                detailPay: String = ""
        ): Item {
            return Item().apply {
                pKEY1 = bpType
                pKEY2 = bpCode
                pKEY3 = bankCode
                pKEY4 = amount
                pKEY5 = detailPay
            }
        }
    }
}