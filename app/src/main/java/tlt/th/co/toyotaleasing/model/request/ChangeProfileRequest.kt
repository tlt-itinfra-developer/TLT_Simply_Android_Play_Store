package tlt.th.co.toyotaleasing.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import tlt.th.co.toyotaleasing.common.extension.ifNotEmpty

class ChangeProfileRequest {

    @SerializedName("PKEY1")
    @Expose
    var PKEY1 = ""
    @SerializedName("PKEY2")
    @Expose
    var PKEY2 = ""
    @SerializedName("PKEY3")
    @Expose
    var PKEY3 = ""
    @SerializedName("PKEY4")
    @Expose
    var PKEY4 = ""
    @SerializedName("PKEY5")
    @Expose
    var PKEY5 = ""
    @SerializedName("PKEY6")
    @Expose
    var PKEY6 = ""
    @SerializedName("PKEY7")
    @Expose
    var PKEY7 = ""
    @SerializedName("PKEY8")
    @Expose
    var PKEY8 = ""
    @SerializedName("PKEY9")
    @Expose
    var PKEY9 = ""
    @SerializedName("PKEY10")
    @Expose
    var PKEY10 = ""
    @SerializedName("PKEY11")
    @Expose
    var PKEY11 = ""
    @SerializedName("PKEY12")
    @Expose
    var PKEY12 = ""
    @SerializedName("PKEY13")
    @Expose
    var PKEY13 = ""

    companion object {
        fun buildForContractAddress(isUseInEveryContract: Boolean,
                                    contractNumber: String,
                                    addressLine1: String,
                                    addressLine2: String,
                                    addressLine3: String,
                                    addressLine4: String,
                                    postcode: String,
                                    provinceCode: String,
                                    amphurCode: String,
                                    imageBase64: String): ChangeProfileRequest {
            return ChangeProfileRequest().apply {
                PKEY1 = contractNumber
                PKEY2 = "MAILLING"
                PKEY3 = if (isUseInEveryContract) "ALL" else "CONTRACT"
                PKEY4 = addressLine1
                PKEY5 = addressLine2
                PKEY6 = addressLine3
                PKEY7 = addressLine4
                PKEY8 = provinceCode
                PKEY9 = amphurCode
                PKEY10 = postcode
                PKEY13 = imageBase64
            }
        }

        fun buildForChangeEmail(contractNumber: String,
                                email: String): ChangeProfileRequest {
            return ChangeProfileRequest().apply {
                PKEY1 = contractNumber
                PKEY2 = "PMCHANGE"
                PKEY3 = "CONTRACT"

                email.ifNotEmpty {
                    PKEY4 = "EMAIL"
                    PKEY5 = email
                }
            }
        }

        fun buildForContractEmailAndPhone(contractNumber: String,
                                          email: String = "",
                                          phonenumber: String = ""): ChangeProfileRequest {
            return ChangeProfileRequest().apply {
                PKEY1 = contractNumber
                PKEY2 = "PMCHANGE"
                PKEY3 = "CONTRACT"

                email.ifNotEmpty {
                    PKEY4 = "EMAIL"
                    PKEY5 = email
                }

                phonenumber.ifNotEmpty {
                    PKEY6 = "PHONE"
                    PKEY7 = phonenumber
                }
            }
        }

        fun buildForTaxDelivery(contractNumber: String,
                                addressLine1: String,
                                addressLine2: String,
                                addressLine3: String,
                                addressLine4: String,
                                postcode: String,
                                provinceCode: String,
                                amphurCode: String): ChangeProfileRequest {
            return ChangeProfileRequest().apply {
                PKEY1 = contractNumber
                PKEY2 = "MAILLINGTAX"
                PKEY3 = "CONTRACT"
                PKEY4 = addressLine1
                PKEY5 = addressLine2
                PKEY6 = addressLine3
                PKEY7 = addressLine4
                PKEY8 = provinceCode
                PKEY9 = amphurCode
                PKEY10 = postcode
            }
        }


    }
}