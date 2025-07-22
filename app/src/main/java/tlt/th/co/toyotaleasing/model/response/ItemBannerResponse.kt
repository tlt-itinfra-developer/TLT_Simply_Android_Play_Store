package tlt.th.co.toyotaleasing.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class ItemBannerResponse(
        @SerializedName("BANNER_NAME")
        @Expose
        var bannername: String = "",
        @SerializedName("BANNER_IMG")
        @Expose
        var bannerimg: String = "",
        @SerializedName("BANNER_TYPE")
        @Expose
        var bannertype: String = "",
        @SerializedName("BANNER_INDEX")
        @Expose
        var bannerindex: Int = 0,
        @SerializedName("BANNER_DESC_1")
        @Expose
        var bannerdesC1: String = "",
        @SerializedName("BANNER_DESC_2")
        @Expose
        var bannerdesC2: String = "",
        @SerializedName("REC_ACTIVE")
        @Expose
        var recactive: String = ""
)