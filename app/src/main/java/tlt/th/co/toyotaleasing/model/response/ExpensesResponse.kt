package tlt.th.co.toyotaleasing.model.response


import com.google.gson.annotations.SerializedName
import tlt.th.co.toyotaleasing.manager.LocalizeManager

data class ExpensesResponse(
        @SerializedName("ADDRESS")
    var aDDRESS: ADDRESS ,
        @SerializedName("DELIVERCAR")
    var dELIVERCAR: DELIVERCAR ,
        @SerializedName("MAILING")
    var mAILING: MAILING ,
        @SerializedName("MAIN")
    var mAIN: MAIN ,
        @SerializedName("MCI")
    var mCI: MCI ,
        @SerializedName("QRCODE")
    var qRCODE: QRCODE ,
        @SerializedName("REGIS")
    var rEGIS: REGIS

) {
    data class ADDRESS(
        @SerializedName("PLAT")
        var pLAT: String= "" ,
        @SerializedName("PLONG")
        var pLONG: String= "" ,
        @SerializedName("REAL_ADDRESS")
        var rEALADDRESS: String
    )

    open class DELIVERCAR {
        @SerializedName("APPOINTMENT")
        var aPPOINTMENT: String = ""
        @SerializedName("DUE_DATE")
        var dUEDATE: String = ""
        @SerializedName("SHOWROOM_CODE")
        var sHOWROOMCODE: String = ""
        @SerializedName("SHOWROOM_NAME")
        var sHOWROOMNAME: String = ""
        @SerializedName("CAN_CHANGE")
        var cANCHANGE: String= ""
        @SerializedName("CHANGE_DESC_TH")
        var cHANGEDESCTH: String= ""
        @SerializedName("CHANGE_DESC_EN")
        var cHANGEDESCEN: String= ""
        @SerializedName("CHANGE_STATUS_BTN_TH")
        var cHANGESTATUSBTNTH: String= ""
        @SerializedName("CHANGE_STATUS_BTN_EN")
        var cHANGESTATUSBTNEN: String= ""

        fun cHANGESTATUSBTN() = if (LocalizeManager.isThai()) {
            cHANGESTATUSBTNTH
        } else {
            cHANGESTATUSBTNEN
        }

        fun cHANGEDESC() = if (LocalizeManager.isThai()) {
            cHANGEDESCTH
        } else {
            cHANGEDESCEN
        }
    }

    data class MAILING(
        @SerializedName("PLAT")
        var pLAT: String= "" ,
        @SerializedName("PLON")
        var pLON: String= "" ,
        @SerializedName("REAL_ADDRESS")
        var rEALADDRESS: String
    )

    data class MAIN(
        @SerializedName("PROPOSAL_NAME")
        var pROPOSALNAME: String= "" ,
        @SerializedName("PROPOSAL_NO")
        var pROPOSALNO: String= "" ,
        @SerializedName("STATUS_APP")
        var sTATUSAPP: String
    )

    data class MCI(
        @SerializedName("CAR_PRICE")
        var cARPRICE: String= "" ,
        @SerializedName("DOWN_PAYMENT")
        var dOWNPAYMENT: String= "" ,
        @SerializedName("GRADE")
        var gRADE: String= "" ,
        @SerializedName("INSTALLMENT")
        var iNSTALLMENT: String= "" ,
        @SerializedName("INTERESTS")
        var iNTERESTS: String= "" ,
        @SerializedName("MODEL")
        var mODEL: String= "" ,
        @SerializedName("PAYMENT_TERM")
        var pAYMENTTERM: String ,
        @SerializedName("CONTRACT_HANDLING_FEE")
        var contractHandlingFee: String,
        @SerializedName("OTHER_EXPENSE")
        var otherexpense: String ,
        @SerializedName("OTHER_DETAIL")
        var oTHERDETAIL : List<OTHER_DETAIL>? = null

    )

    data class QRCODE(
        @SerializedName("SCANQR")
        var sCANQR: String
    )

    data class REGIS(
            @SerializedName("PLAT")
            var pLAT: String= "" ,
            @SerializedName("PLONG")
            var pLONG: String= "" ,
            @SerializedName("REAL_ADDRESS")
            var rEALADDRESS: String = ""
    )

    data class OTHER_DETAIL(
            @SerializedName("item_name")
            var itemname: String= "" ,
            @SerializedName("item_amount")
            var itemamt: String= ""
    )

}