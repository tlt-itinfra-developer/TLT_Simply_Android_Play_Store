package tlt.th.co.toyotaleasing.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SetTouchIDRequest {

    @SerializedName("PSELECT")
    @Expose
    var pselect: String = ""
    @SerializedName("PUSER")
    @Expose
    var puser: String = ""

    companion object {
        fun buildForActivate(): SetTouchIDRequest {
            return SetTouchIDRequest().apply {
                pselect = "Y"
            }
        }

        fun buildForDeactivate(): SetTouchIDRequest {
            return SetTouchIDRequest().apply {
                pselect = "N"
            }
        }
    }
}