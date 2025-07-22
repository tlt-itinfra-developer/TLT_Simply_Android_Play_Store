package tlt.th.co.toyotaleasing.model.response


import com.google.gson.annotations.SerializedName
import tlt.th.co.toyotaleasing.manager.LocalizeManager

open class IDPListResponse {
    @SerializedName("node_id")
    var nodeId: String = ""
    @SerializedName("node_img_url")
    var nodeImgUrl: String= ""
    @SerializedName("node_name_en")
    var nodeNameEn: String= ""
    @SerializedName("node_name_th")
    var nodeNameTh: String= ""
    @SerializedName("ind_code")
    var indCode: String= ""
    @SerializedName("comp_code")
    var compCode: String= ""

    fun NodeName(): String? {
        return if (LocalizeManager.isThai()) {
            nodeNameEn
        } else {
            nodeNameTh
        }
    }

}