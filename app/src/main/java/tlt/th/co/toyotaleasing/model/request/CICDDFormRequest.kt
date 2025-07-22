package tlt.th.co.toyotaleasing.model.request


import com.google.gson.annotations.SerializedName
import tlt.th.co.toyotaleasing.manager.LocalizeManager

class CICDDFormRequest{
    @SerializedName("PKEY1")
         var pKEY1: String =""
    @SerializedName("PKEY2")
         var pKEY2: String=""
    @SerializedName("PKEY3")
        var pKEY3: String=""
    @SerializedName("PKEY4")
        var pKEY4: String=""
    @SerializedName("PKEY5")
        var pKEY5: String=""
    @SerializedName("PSELECT")
        var pSELECT: String=""

    companion object {
        fun build(refID: String , token : String): CICDDFormRequest {
            return CICDDFormRequest().apply {
                pSELECT = token
                pKEY2 = refID
                pKEY3 = if (LocalizeManager.isThai()) {
                    "TH"
                } else {
                    "EN"
                }
            }
        }
    }
}