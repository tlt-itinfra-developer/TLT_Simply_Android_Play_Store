package tlt.th.co.toyotaleasing.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AcceptTermConditionRequest {

    @SerializedName("PSELECT")
    @Expose
    var pselect: String = "INSCONFIRM"
    @SerializedName("PKEY1")
    @Expose
    var pkey1: String = "TCC-10110"
    @SerializedName("PKEY2")
    @Expose
    var pkey2: String = ""
    @SerializedName("PKEY3")
    @Expose
    var pkey3: String = ""

    companion object {
        fun buildForAccept(termConditionCode: String): AcceptTermConditionRequest {
            return AcceptTermConditionRequest().apply {
                pkey1 = termConditionCode
                pkey2 = "Y"
            }
        }

        fun buildForDenied(termConditionCode: String): AcceptTermConditionRequest {
            return AcceptTermConditionRequest().apply {
                pkey1 = termConditionCode
                pkey2 = "N"
            }
        }
    }
}