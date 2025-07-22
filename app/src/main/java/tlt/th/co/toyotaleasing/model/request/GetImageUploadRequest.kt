package tlt.th.co.toyotaleasing.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetImageUploadRequest {

    @SerializedName("PSELECT")
    @Expose
    var pselect: String = ""
    @SerializedName("PKEY1")
    @Expose
    var pkey1: String = ""
    @SerializedName("PKEY2")
    @Expose
    var pkey2: String = ""

    companion object {
        fun buildForPorlorbor(contractNumber: String): GetImageUploadRequest {
            return GetImageUploadRequest().apply {
                pselect = "PORLORBOR"
                pkey1 = contractNumber
            }
        }

        fun buildForTorloraor(contractNumber: String): GetImageUploadRequest {
            return GetImageUploadRequest().apply {
                pselect = "TORLORAOR"
                pkey1 = contractNumber
            }
        }

        fun buildForCarImage(contractNumber: String): GetImageUploadRequest {
            return GetImageUploadRequest().apply {
                pselect = "CUSCAR"
                pkey1 = contractNumber
            }
        }
    }
}