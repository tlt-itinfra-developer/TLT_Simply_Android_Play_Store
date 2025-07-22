package tlt.th.co.toyotaleasing.model.entity.masterdata


import com.google.gson.annotations.SerializedName

import tlt.th.co.toyotaleasing.manager.LocalizeManager

open class Property  {

    @SerializedName("PROPERTY_EN")
    var pROPERTYEN: String? = ""
    @SerializedName("PROPERTY_ID")
    var pROPERTYID: String? = ""
    @SerializedName("PROPERTY_TH")
    var pROPERTYTH: String? = ""

    fun getProperty(): String? {
        return if (LocalizeManager.isThai()) {
            pROPERTYTH
        } else {
            pROPERTYEN
        }
    }
}