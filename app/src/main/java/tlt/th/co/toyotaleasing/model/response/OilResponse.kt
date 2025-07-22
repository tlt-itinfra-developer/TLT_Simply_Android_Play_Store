package tlt.th.co.toyotaleasing.model.response

import com.google.gson.annotations.SerializedName


data class OilResponse(
        @SerializedName("error_code") var errorCode: Int? = 0,
        @SerializedName("error_msg") var errorMsg: String? = "",
        @SerializedName("results") var results: List<Result?>? = listOf()
) {
    data class Result(
            @SerializedName("applayout_id") var applayoutId: Long? = 0,
            @SerializedName("list") var list: List<X?>? = listOf(),
            @SerializedName("widget_type") var widgetType: String? = "",
            @SerializedName("title_name") var titleName: String? = "",
            @SerializedName("type") var type: String? = "",
            @SerializedName("size") var size: String? = ""
    ) {
        data class X(
                @SerializedName("suffix_name") var suffixName: String? = "",
                @SerializedName("color") var color: String? = "",
                @SerializedName("price") var price: String? = "",
                @SerializedName("prefix_name") var prefixName: String? = "",
                @SerializedName("image") var image: Image? = Image()
        ) {

            data class Image(
                    @SerializedName("status") var status: Boolean? = false,
                    @SerializedName("url") var url: String? = ""
            )
        }
    }
}