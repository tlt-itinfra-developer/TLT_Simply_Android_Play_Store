package tlt.th.co.toyotaleasing.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import tlt.th.co.toyotaleasing.util.CalendarUtils

class UploadImagesRequest {

    @SerializedName("PSELECT")
    @Expose
    var pselect: String = ""
    @SerializedName("PKEY1")
    @Expose
    var pkey1: String = ""
    @SerializedName("PKEY2")
    @Expose
    var pkey2: String = ""
    @SerializedName("PKEY3")
    @Expose
    var pkey3: String = ""
    @SerializedName("PKEY4")
    @Expose
    var pkey4: String = ""
    @SerializedName("PKEY5")
    @Expose
    var pkey5: String = ""
    @SerializedName("PKEY6")
    @Expose
    var pkey6: String = ""
    @SerializedName("PKEY7")
    @Expose
    var pkey7: String = ""

    companion object {
        fun build(contractNumber: String): UploadImagesRequest {
            return UploadImagesRequest().apply {
                pkey1 = contractNumber
            }
        }

        fun buildForPorlorbor(base64: String = "",
                              contractNumber: String = "",
                              seqId: String = "",
                              amountImages: String = ""): UploadImagesRequest {
            return UploadImagesRequest().apply {
                pselect = "DOCPORLORBOR"
                pkey1 = "INSERT"
                pkey2 = seqId
                pkey3 = contractNumber
                pkey4 = base64
                pkey5 = CalendarUtils.getCurrentDateByPattern("yyyyMMddHHmmss")
                pkey7 = amountImages
            }
        }

        fun buildForTorloraor(base64: String = "",
                              contractNumber: String = "",
                              seqId: String = "",
                              amountImages: String = ""): UploadImagesRequest {
            return UploadImagesRequest().apply {
                pselect = "DOCTORLORAOR"
                pkey1 = "INSERT"
                pkey2 = seqId
                pkey3 = contractNumber
                pkey4 = base64
                pkey5 = CalendarUtils.getCurrentDateByPattern("yyyyMMddHHmmss")
                pkey7 = amountImages
            }
        }

        fun buildForGas(contractNumber: String = "",
                        seqId: String = "",
                        emsNo: String = ""): UploadImagesRequest {
            return UploadImagesRequest().apply {
                pselect = "DOCCARENGINE"
                pkey1 = "INSERT"
                pkey2 = seqId
                pkey3 = contractNumber
                pkey4 = ""
                pkey5 = ""
                pkey6 = emsNo
            }
        }

        fun buildForTIBAsk(base64: String = "",
                           contractNumber: String = "",
                           tcaId: String = "",
                           amountImages: String = ""): UploadImagesRequest {
            return UploadImagesRequest().apply {
                pselect = "TIBASKIMAGE"
                pkey1 = "INSERT"
                pkey2 = tcaId
                pkey3 = contractNumber
                pkey4 = base64
                pkey5 = "${contractNumber}_$tcaId"
                pkey7 = amountImages
            }
        }

        fun buildForImageProfile(base64: String = ""): UploadImagesRequest {
            return UploadImagesRequest().apply {
                pselect = "CUSPRO"
                pkey1 = "UPDATE"
                pkey4 = base64
            }
        }

        fun buildForUpdateCarImage(base64: String = "",
                                   contractNumber: String = ""): UploadImagesRequest {
            return UploadImagesRequest().apply {
                pselect = "CUSCAR"
                pkey1 = "UPDATE"
                pkey3 = contractNumber
                pkey4 = base64
            }
        }

        fun buildForResetCarImage(base64: String = "",
                                  contractNumber: String = ""): UploadImagesRequest {
            return UploadImagesRequest().apply {
                pselect = "CUSCAR"
                pkey1 = "DELETE"
                pkey3 = contractNumber
                pkey4 = base64
            }
        }
    }
}