package tlt.th.co.toyotaleasing.model.entity.masterdata

import com.google.gson.annotations.SerializedName

import tlt.th.co.toyotaleasing.manager.LocalizeManager

open class DealerOnline  {
    @SerializedName("TYPE_SYNC")
    var tYPESYNC: String? = ""
    @SerializedName("dealer_nostra_id")
    var dealerNostraId: String? = ""
    @SerializedName("DEALER_CODE")
    var dealerCode: String? = ""
    @SerializedName("SHOWROOM_NAME_TH")
    var name: String? = ""
    @SerializedName("SHOWROOM_NAME_EN")
    var nameEn: String? = ""
    @SerializedName("branch_code")
    var branchCode: String? = ""
    @SerializedName("branch")
    var branch: String? = ""
    @SerializedName("branch_en")
    var branchEn: String? = ""
    @SerializedName("ADDRESS_TH")
    var aDDRESSTH: String? = ""
    @SerializedName("ADDRESS_EN")
    var aDDRESSEN: String? = ""
    @SerializedName("tel_ss_call")
    var telSsCall: String? = ""
    @SerializedName("tel_ss")
    var telSs: String? = ""
    @SerializedName("tel_cc")
    var telCc: String? = ""
    @SerializedName("tel_fax")
    var telFax: String? = ""
    @SerializedName("email")
    var email: String? = ""
    @SerializedName("url1")
    var url1: String? = ""
    @SerializedName("url2")
    var url2: String? = ""
    @SerializedName("type_of")
    var typeOf: String? = ""
    @SerializedName("open_hr_type_of")
    var openHrTypeOf: String? = ""
    @SerializedName("open_hr_type_of_en")
    var openHrTypeOfEn: String? = ""
    @SerializedName("type_sr")
    var typeSr: String? = ""
    @SerializedName("open_hr_type_sr")
    var openHrTypeSr: String? = ""
    @SerializedName("open_hr_type_sr_en")
    var openHrTypeSrEn: String? = ""
    @SerializedName("type_sc")
    var typeSc: String? = ""
    @SerializedName("open_hr_type_sc")
    var openHrTypeSc: String? = ""
    @SerializedName("open_hr_type_sc_en")
    var openHrTypeScEn: String? = ""
    @SerializedName("type_bp")
    var typeBp: String? = ""
    @SerializedName("open_hr_type_bp")
    var openHrTypeBp: String? = ""
    @SerializedName("open_hr_type_bp_en")
    var openHrTypeBpEn: String? = ""
    @SerializedName("pic_url1")
    var picUrl1: String? = ""
    @SerializedName("pic_url2")
    var picUrl2: String? = ""
    @SerializedName("pic_url3")
    var picUrl3: String? = ""
    @SerializedName("lat")
    var lat: String? = ""
    @SerializedName("lon")
    var lon: String? = ""
    @SerializedName("REC_ACTIVE")
    var rECACTIVE: String? = ""
    @SerializedName("PROVINCE_CODE")
    var provinceCode: String? = ""
    @SerializedName("AMPHUR_CODE")
    var amphurCode: String? = ""
    @SerializedName("DEALER_CODE_AMBIT")
    var dealerCodeAmbit: String? = ""
    @SerializedName("SHOWROOM_CODE_AMBIT")
    var showroomCodeAmbit: String? = ""

    fun getNameByLocalize(): String? {
        return if (LocalizeManager.isThai()) {
            name
        } else {
            nameEn
        }
    }

    fun getAddressByLocalize(): String? {
        return if (LocalizeManager.isThai()) {
            aDDRESSTH
        } else {
            aDDRESSEN
        }
    }

    fun getBranchByLocalize(): String? {
        return if (LocalizeManager.isThai()) {
            branch
        } else {
            branchEn
        }
    }

    fun getOpenHrShowRoomByLocalize(): String? {
        return if (LocalizeManager.isThai()) {
            openHrTypeSr
        } else {
            openHrTypeSrEn
        }
    }

}