package tlt.th.co.toyotaleasing.model.request


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FillInfoRequest{
    @SerializedName("PKEY1")
    @Expose
    var pKEY1: String  = ""
    @SerializedName("PKEY2")
    @Expose
    var pKEY2: String  = ""
    @SerializedName("PKEY3")
    @Expose
    var pKEY3: String  = ""
    @SerializedName("PKEY4")
    @Expose
    var pKEY4: String  = ""
    @SerializedName("PKEY5")
    @Expose
    var pKEY5: String  = ""
    @SerializedName("PSELECT")
    @Expose
    var pSELECT: String  = ""

    companion object {
        fun build(ref_no: String
        ): FillInfoRequest {
            return FillInfoRequest().apply {
                pKEY2 = ref_no
            }
        }
    }
}