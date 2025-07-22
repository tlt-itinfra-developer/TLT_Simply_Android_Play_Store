package tlt.th.co.toyotaleasing.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BannerRequest {

    @SerializedName("PSELECT")
    @Expose
    var pselect: String = ""
    @SerializedName("PUSER")
    @Expose
    var puser: String = ""

    companion object {
        fun build(): BannerRequest {
            return BannerRequest().apply {
                puser = "Y"
            }
        }
    }
}