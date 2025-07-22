package tlt.th.co.toyotaleasing.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Set2LogManualRequest {

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

    companion object {
        fun build(base64: String): Set2LogManualRequest {
            return Set2LogManualRequest().apply {
                pkey1 = base64
            }
        }
    }
}