package tlt.th.co.toyotaleasing.model.request


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NDIDAcceptTermsConditionRequest {

    @SerializedName("PKEY1")
    @Expose
    var pKEY1: String= ""
    @SerializedName("PKEY2")
    @Expose
    var pKEY2: String= ""
    @SerializedName("PKEY3")
    @Expose
    var pKEY3: String= ""
    @SerializedName("PKEY4")
    @Expose
    var pKEY4: String= ""
    @SerializedName("PKEY5")
    @Expose
    var pKEY5: String= ""
    @SerializedName("PSELECT")
    @Expose
    var pSELECT: String = ""


    companion object {
        fun buildForAccept(refID: String): NDIDAcceptTermsConditionRequest {
            return NDIDAcceptTermsConditionRequest().apply {
                pSELECT = "NDIDTERM"
                pKEY2 = refID
                pKEY3 = "Y"
            }
        }

        fun buildForDenied(refID: String): NDIDAcceptTermsConditionRequest {
            return NDIDAcceptTermsConditionRequest().apply {
                pSELECT = "NDIDTERM"
                pKEY2 = refID
                pKEY3 = "N"
            }
        }
    }
}
