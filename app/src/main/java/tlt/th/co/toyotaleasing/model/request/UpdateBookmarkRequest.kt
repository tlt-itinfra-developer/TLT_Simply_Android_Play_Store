package tlt.th.co.toyotaleasing.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UpdateBookmarkRequest {

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
        fun build(adId: String): UpdateBookmarkRequest {
            return UpdateBookmarkRequest().apply {
                pselect = adId
            }
        }
    }
}