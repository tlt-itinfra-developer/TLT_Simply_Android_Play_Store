package tlt.th.co.toyotaleasing.model.entity.masterdata

import com.google.gson.annotations.SerializedName


open class RefinanceImage  {
    @SerializedName("AD_ID")
    var adId: String? = ""
    @SerializedName("TYPE")
    var type: String? = ""
    @SerializedName("IMAGE_NAME")
    var imageName: String? = ""
    @SerializedName("IMAGE_URL")
    var imageUrl: String? = ""
    @SerializedName("DETAIL_DES1")
    var detailDes1: String? = ""
    @SerializedName("LINK_REF")
    var detailDes2: String? = ""
    @SerializedName("REC_ACTIVE")
    var recActive: String? = ""
}