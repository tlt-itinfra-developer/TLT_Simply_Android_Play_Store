package tlt.th.co.toyotaleasing.model.request


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SyncDocUploadRequest {

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
    @SerializedName("PSELECT")
    @Expose
    var pselect: String = ""

    companion object {
        fun build(doctype: String ,
                  ref_no : String ,
                  docname : String ,
                  imagebase64 : String,
                  extendsion : String,
                  docname2 : String ): SyncDocUploadRequest {
            return SyncDocUploadRequest().apply {
                pkey1 = doctype
                pkey2 = ref_no
                pkey3 = docname
                pkey4 = extendsion
                pkey5 = imagebase64
                pkey6 = docname2
                pselect = "SYNC_DOC"
            }
        }
    }
}