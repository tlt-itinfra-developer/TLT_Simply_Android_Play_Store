package tlt.th.co.toyotaleasing.model.entity.masterdata

import com.google.gson.annotations.SerializedName

import tlt.th.co.toyotaleasing.manager.LocalizeManager

open class MailingA  {
    @SerializedName("TYPE_SYNC")
    var tYPESYNC: String? = ""
    @SerializedName("PROVINCE_NAME_TH")
    var pROVINCENAMETH: String? = ""
    @SerializedName("AMPHUR_NAME_TH")
    var aMPHURNAMETH: String? = ""
    @SerializedName("PROVINCE_NAME_EN")
    var pROVINCENAMEEN: String? = ""
    @SerializedName("AMPHUR_NAME_EN")
    var aMPHURNAMEEN: String? = ""
    @SerializedName("PROVINCE_CODE")
    var pROVINCECODE: String? = ""
    @SerializedName("POST_CODE")
    var pOSTCODE: String? = ""
    @SerializedName("AMPHUR_CODE")
    var aMPHURCODE: String? = ""
    @SerializedName("REC_ACTIVE")
    var rECACTIVE: String? = ""

    companion object {
        const val PROVINCE_CODE = "pROVINCECODE"
        const val POST_CODE = "pOSTCODE"
    }

    fun getProvinceName(): String? {
        return if (LocalizeManager.isThai()) {
            pROVINCENAMETH
        } else {
            pROVINCENAMEEN
        }
    }

    fun getAmphurName(): String? {
        return if (LocalizeManager.isThai()) {
            aMPHURNAMETH
        } else {
            aMPHURNAMEEN
        }
    }
}