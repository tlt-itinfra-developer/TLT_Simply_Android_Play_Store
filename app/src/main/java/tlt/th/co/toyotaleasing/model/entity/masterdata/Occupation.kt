package tlt.th.co.toyotaleasing.model.entity.masterdata


import com.google.gson.annotations.SerializedName

import tlt.th.co.toyotaleasing.manager.LocalizeManager

open class Occupation  {
    @SerializedName("EmployeeTypeID")
    var employeeTypeID : String? = ""
    @SerializedName("OC_ID")
    var oCID: String? = ""
    @SerializedName("OC_NAME_EN")
    var oCNAMEEN: String? = ""
    @SerializedName("OC_NAME_TH")
    var oCNAMETH: String? = ""

    fun getOCname(): String? {
        return if (LocalizeManager.isThai()) {
            oCNAMETH
        } else {
            oCNAMEEN
        }
    }
}