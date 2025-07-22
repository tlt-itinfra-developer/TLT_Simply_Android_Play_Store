package tlt.th.co.toyotaleasing.model.response


import com.google.gson.annotations.SerializedName
import tlt.th.co.toyotaleasing.manager.LocalizeManager

data class TransferOwnershipTrackingResponse(
    @SerializedName("EMS_NO")
    var eMSNO: String,
    @SerializedName("MAILING_ADDRESS")
    var mAILINGADDRESS: String,
    @SerializedName("STEP_INFO")
    var sTEPINFO: List<STEPINFO>,
    @SerializedName("TRACKING_TYPE")
    var tRACKINGTYPE: String,
    @SerializedName("URL_EMS")
    var uRLEMS: String
) {
    open class STEPINFO {
        @SerializedName("car_img")
        var carImg: String = ""

        @SerializedName("date_time")
        var dateTime: String = ""

        @SerializedName("document_download")
        var documentDownload: String = ""

        @SerializedName("document_name")
        var documentName: String = ""

        @SerializedName("img_color")
        var imgColor: String = ""

        @SerializedName("img_type")
        var imgType: String = ""

        @SerializedName("issue_desc_th")
        var issueDescTH: String = ""

        @SerializedName("issue_desc_en")
        var issueDescEN: String = ""

        @SerializedName("step_no")
        var step: String = ""

        @SerializedName("text_color")
        var textColor: String = ""

        @SerializedName("text_desc_en")
        var textDescEn: String = ""

        @SerializedName("text_desc_th")
        var textDescTh: String = ""

        @SerializedName("des_load_th")
        var desLoadTH: String = ""

        @SerializedName("des_load_en")
        var desLoadEN: String = ""

        fun desLoad() = if (LocalizeManager.isThai()) {
            desLoadTH!!
        } else {
            desLoadEN!!
        }

        fun getIssueDesc() = if (LocalizeManager.isThai()) {
            issueDescTH!!
        } else {
            issueDescEN!!
        }
        fun gettextDesc() = if (LocalizeManager.isThai()) {
            textDescTh!!
        } else {
            textDescEn!!
        }
    }
}