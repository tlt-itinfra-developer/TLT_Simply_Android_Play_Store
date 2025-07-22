package tlt.th.co.toyotaleasing.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetNewsRequest {

    @SerializedName("PSELECT")
    @Expose
    var pselect: String = ""
    @SerializedName("PSUCCESS")
    @Expose
    var psuccess: String = ""

    companion object {
        fun build(): GetNewsRequest {
            return GetNewsRequest()
        }
    }
}