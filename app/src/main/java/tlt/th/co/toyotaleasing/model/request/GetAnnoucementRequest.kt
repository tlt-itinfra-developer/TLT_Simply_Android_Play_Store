package tlt.th.co.toyotaleasing.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class  GetAnnoucementRequest{
        @SerializedName("PSELECT")
        @Expose
        var pselect: String = ""
        @SerializedName("PSUCCESS")
        @Expose
        var psuccess: String = ""

        companion object {
                fun build(): GetAnnoucementRequest {
                        return GetAnnoucementRequest()
                }
        }
}