package tlt.th.co.toyotaleasing.model.response


import com.google.gson.annotations.SerializedName
import tlt.th.co.toyotaleasing.manager.LocalizeManager

open class DecisionEngineResultResponse{
    @SerializedName("flag_offer")
    var flagOffer: String = ""
    @SerializedName("doffer_list")
    var dofferlist: List<DOffer>? = null
    @SerializedName("coffer_list")
    var cofferlist: COffer? = null
    @SerializedName("status")
    var status: String = ""
    @SerializedName("status_detail_th")
    var statusDetailTH: String = ""
    @SerializedName("status_detail_en")
    var statusDetailEN: String = ""
    @SerializedName("status_header_en")
    var statusHeaderEN: String = ""
    @SerializedName("status_header_th")
    var statusHeaderTH: String = ""
    @SerializedName("car_detail")
    var carDetail: CarDetail? = null

    fun statusHeader() = if (LocalizeManager.isThai()) {
        statusHeaderTH
    } else {
        statusHeaderEN
    }

    fun statusDetail() = if (LocalizeManager.isThai()) {
        statusDetailTH
    } else {
        statusDetailEN
    }

        open class DOffer {
            @SerializedName("offer_id")
            var offerId: String = ""
            @SerializedName("offer_value")
            var offerValue: String = ""
            @SerializedName("offer_text_th")
            var offerValueTH: String = ""
            @SerializedName("offer_text_en")
            var offerValueEN: String = ""

            fun offerValue() = if (LocalizeManager.isThai()) {
                offerValueTH
            } else {
                offerValueEN
            }
        }

        data class COffer(
                @SerializedName("ctype")
                var ctype: String,
                @SerializedName("ctype_desc")
                var ctypedesc: String
        )

        open class CarDetail {
            @SerializedName("CAR_IMAGE")
            var cARIMAGE: String = ""
            @SerializedName("CAR_MODEL")
            var cARMODEL: String = ""
            @SerializedName("CAR_PRICE")
            var cARPRICE: String = ""
            @SerializedName("CAR_GRADE")
            var cARGRADE: String = ""
            @SerializedName("EXPIRE_DATE")
            var eXPIREDATE: String = ""
            @SerializedName("REF_ID")
            var rEFID: String = ""
            @SerializedName("REF_STATUS")
            var rEFSTATUS: String = ""
            @SerializedName("STAMP_DATE")
            var sTAMPDATE: String = ""
            @SerializedName("REF_DES_TH")
            var rEFDESTH: String = ""
            @SerializedName("REF_DES_EN")
            var rEFDESEN: String = ""

            fun getResDes() = if (LocalizeManager.isThai()) {
                rEFDESTH
            } else {
                rEFDESEN
            }
        }
}