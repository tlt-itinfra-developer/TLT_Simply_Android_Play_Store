package tlt.th.co.toyotaleasing.model.response

import com.google.gson.annotations.SerializedName

data class OilPriceSoapResponse(
        @SerializedName("PTT_DS") var pttDs: DataAccess? = null
) {
    data class DataAccess(
            @SerializedName("DataAccess") var results: List<Oil?>? = listOf()
    ) {
        data class Oil(
                @SerializedName("PRODUCT") var productName: String? = "",
                @SerializedName("PRICE") var price: String? = "",
                @SerializedName("PRICE_DATE") var priceDate: String? = "",
                @SerializedName("IMAGE") var productThumbnail: Int? = 0
        )
    }
}