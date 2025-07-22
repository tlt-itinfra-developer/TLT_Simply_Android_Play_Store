package tlt.th.co.toyotaleasing.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SequenceIdRequest {

    @SerializedName("PSEQ_ID")
    @Expose
    var pseqId: String = ""

    companion object {
        fun build(sequenceId: String): SequenceIdRequest {
            return SequenceIdRequest().apply {
                pseqId = sequenceId
            }
        }
    }
}