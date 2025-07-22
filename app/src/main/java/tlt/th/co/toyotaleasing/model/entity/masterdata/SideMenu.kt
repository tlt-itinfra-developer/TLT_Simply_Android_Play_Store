package tlt.th.co.toyotaleasing.model.entity.masterdata


import com.google.gson.annotations.SerializedName

import tlt.th.co.toyotaleasing.manager.LocalizeManager

open class SideMenu  {
    @SerializedName("MENU_EN")
    var mENUEN: String = ""
    @SerializedName("MENU_ICON")
    var mENUICON: String= ""
    @SerializedName("MENU_ID")
    var mENUID: String= ""
    @SerializedName("MENU_LINK")
    var mENULINK: String= ""
    @SerializedName("MENU_TH")
    var mENUTH: String = ""

    fun mENUNAME(): String? {
    return if (LocalizeManager.isThai()) {
        mENUTH
    } else {
        mENUEN
    }
}
}