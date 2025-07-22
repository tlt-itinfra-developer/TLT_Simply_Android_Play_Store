package tlt.th.co.toyotaleasing.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CheckStatusFromBankRequest {

    @SerializedName("PSEQ")
    @Expose
    var pseq: List<SequenceIdRequest> = listOf()

    companion object {
        fun build(sequenceIds: List<SequenceIdRequest>): CheckStatusFromBankRequest {
            return CheckStatusFromBankRequest().apply {
                pseq = sequenceIds
            }
        }
    }
}