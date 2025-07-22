package tlt.th.co.toyotaleasing.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BlockNotifyRequest {

    @SerializedName("PKEY1")
    @Expose
    var PKEY1 = ""
    @SerializedName("PKEY2")
    @Expose
    var PKEY2 = ""
    @SerializedName("PKEY3")
    @Expose
    var PKEY3 = ""

    companion object {
        fun build(type: String, sequenceId: String): BlockNotifyRequest {
            return BlockNotifyRequest().apply {
                PKEY1 = type
                PKEY2 = sequenceId
            }
        }

        fun buildForDontShowAgain(type: String): BlockNotifyRequest {
            return BlockNotifyRequest().apply {
                PKEY1 = type
                PKEY2 = "Y"
            }
        }

        fun buildForShowAgain(type: String): BlockNotifyRequest {
            return BlockNotifyRequest().apply {
                PKEY1 = type
                PKEY2 = "N"
            }
        }
    }
}