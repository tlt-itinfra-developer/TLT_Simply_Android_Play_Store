package tlt.th.co.toyotaleasing.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ItemTrackingResponse(
        @SerializedName("EXT_CONTRACT")
        @Expose
        val eXTCONTRACT: String = "",
        @SerializedName("TRACKING_NO")
        @Expose
        val tRACKINGNO: String = "",
        @SerializedName("TAX_YEAR")
        @Expose
        val tAXYEAR: String = "",
        @SerializedName("MAILING_CODE")
        @Expose
        val mAILINGCODE: String = "",
        @SerializedName("MAILING_ADR1")
        @Expose
        val mAILINGADR1: String = "",
        @SerializedName("MAILING_ADR2")
        @Expose
        val mAILINGADR2: String = "",
        @SerializedName("MAILING_ADR3")
        @Expose
        val mAILINGADR3: String = "",
        @SerializedName("MAILING_ADR4")
        @Expose
        val mAILINGADR4: String = "",
        @SerializedName("MAILING_POST")
        @Expose
        val mAILINGPOST: String = "",
        @SerializedName("RECIEVE_DATE")
        @Expose
        val rECIEVEDATE: String = "",
        @SerializedName("PROCESS_DATE")
        @Expose
        val pROCESSDATE: String = "",
        @SerializedName("FINISH_DATE")
        @Expose
        val fINISHDATE: String = "",
        @SerializedName("RETURN_CAUSE")
        @Expose
        val rETURNCAUSE: String = "",
        @SerializedName("CURRENT_DATE")
        @Expose
        val currentDate: String = "" ,
        @SerializedName("FLAG_HOLD")
        @Expose
        var flagHold: String? = "" ,
        @SerializedName("HOLD_MSG")
        @Expose
        var holdMsg: String? = "" ,
        @SerializedName("HOLD_DATE")
        @Expose
        var holdDate: String? = "" ,
        @SerializedName("HOLD_DESCRIPTION")
        @Expose
        var holdDesc: String? = ""
)