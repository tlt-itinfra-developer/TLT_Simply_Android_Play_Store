package tlt.th.co.toyotaleasing.model.request


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SyncDeleteDocUploadRequest {

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
    @SerializedName("PSELECT")
    @Expose
    var pselect: String = ""

    companion object {
        fun build(doctype: String ,
                  ref_no : String ,
                  docname : String ): SyncDeleteDocUploadRequest {
            return SyncDeleteDocUploadRequest().apply {
                pkey1 = doctype
                pkey2 = ref_no
                pkey3 = docname
                pselect = "SYNC_DOC"
            }
        }
    }
}