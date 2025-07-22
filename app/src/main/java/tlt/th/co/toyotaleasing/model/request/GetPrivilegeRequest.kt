package tlt.th.co.toyotaleasing.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetPrivilegeRequest {

    @SerializedName("PKEY1")
    @Expose
    var pkey1: String = ""
    @SerializedName("PKEY2")
    @Expose
    var pkey2: String = ""

    companion object {
        fun build(extContract: String): GetPrivilegeRequest {
            return GetPrivilegeRequest().apply {
                pkey1 = extContract
            }
        }
    }
}