package tlt.th.co.toyotaleasing.model.request


import com.google.gson.annotations.SerializedName

class SubmitDocUploadRequest {
    @SerializedName("PKEY1")
    var pKEY1: String = ""
    @SerializedName("PKEY2")
    var pKEY2: String = ""
    @SerializedName("PKEY3")
    var pKEY3: String = ""
    @SerializedName("PKEY4")
    var pKEY4: String = ""
    @SerializedName("PKEY5")
    var pKEY5: String = ""
    @SerializedName("PSELECT")
    var pSELECT:  String = ""

    companion object {
        fun build(ref_no : String ): SubmitDocUploadRequest {
            return SubmitDocUploadRequest().apply {
                pKEY2 = ref_no
                pSELECT = "FINISH_DOC"
            }
        }
    }
}