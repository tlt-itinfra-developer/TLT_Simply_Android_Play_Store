package tlt.th.co.toyotaleasing.model.request


import com.google.gson.annotations.SerializedName

 class GetListIDPRequest{
    @SerializedName("ndid_id")
    var ndidId: String = ""
    @SerializedName("request_namespace")
    var requestNamespace: String= ""
    @SerializedName("tltreference_id")
    var tltreferenceId: String = ""

    companion object {
        fun build( ref_id : String , ndid_id : String ): GetListIDPRequest {
            return GetListIDPRequest().apply {
                tltreferenceId  = ref_id
                requestNamespace = "citizen_id"
                ndidId  = ndid_id
            }
        }
    }
}