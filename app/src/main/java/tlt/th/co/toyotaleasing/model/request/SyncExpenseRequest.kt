package tlt.th.co.toyotaleasing.model.request


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SyncExpenseRequest {

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
        fun build(refId: String , qrStr: String): SyncExpenseRequest {
            return SyncExpenseRequest().apply {
                pKEY2 = refId
                pKEY3 = qrStr
            }
        }

    }
}
