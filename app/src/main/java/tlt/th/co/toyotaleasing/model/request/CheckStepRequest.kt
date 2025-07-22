package tlt.th.co.toyotaleasing.model.request


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CheckStepRequest {

    @SerializedName("PKEY1")
    @Expose
    var pkey1: String = ""
    @SerializedName("PKEY2")
    @Expose
    var pkey2: String = ""
    @SerializedName("PKEY3")
    @Expose
    var pkey3: String = ""
    @SerializedName("PSELECT")
    @Expose
    var pselect : String = ""


    companion object {
        fun build(refID: String , screen_name : String = "" ): CheckStepRequest {
            return CheckStepRequest().apply {
                pkey2 = refID
                pselect = screen_name
            }
        }
    }
}