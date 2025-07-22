package tlt.th.co.toyotaleasing.model.entity.masterdata


import com.google.gson.annotations.SerializedName

import tlt.th.co.toyotaleasing.manager.LocalizeManager

open class Marital  {
    @SerializedName("MARITAL_EN")
    var mARITALEN: String? = ""
    @SerializedName("MARITAL_ID")
    var mARITALID: String? = ""
    @SerializedName("MARITAL_TH")
    var mARITALTH: String? = ""

    fun getMarital(): String? {
        return if (LocalizeManager.isThai()) {
            mARITALTH
        } else {
            mARITALEN
        }
    }
}