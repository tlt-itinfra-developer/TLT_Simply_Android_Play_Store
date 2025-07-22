package tlt.th.co.toyotaleasing.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetTIBClubDetailRequest {

    @SerializedName("PSELECT")
    @Expose
    var pselect: String = ""
    @SerializedName("PUSER")
    @Expose
    var puser: String = ""

    companion object {
        fun build(): GetTIBClubDetailRequest {
            return GetTIBClubDetailRequest()
        }
    }
}