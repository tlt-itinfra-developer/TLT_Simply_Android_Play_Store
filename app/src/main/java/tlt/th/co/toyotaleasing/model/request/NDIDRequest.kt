package tlt.th.co.toyotaleasing.model.request


import com.google.gson.annotations.SerializedName

class NDIDRequest {
    @SerializedName("comp_code")
    var compCode: String = ""
    @SerializedName("ind_code")
    var indCode: String= ""
    @SerializedName("node_id")
    var nodeId: String= ""
    @SerializedName("tltreference_id")
    var tltreferenceId: String= ""
    @SerializedName("tltrequest_msg")
    var tltrequestMsg: String= ""
    @SerializedName("ndid_id")
    var ndidid: String= ""

    companion object {
        fun build( ref_id : String ,
                   node_id : String  ,
                   ind_code : String  ,
                   comp_code : String ,
                   ndid_id : String,
                   tltMsg : String
        ): NDIDRequest {
            return NDIDRequest().apply {
                tltreferenceId = ref_id
                compCode  =  comp_code
                indCode = ind_code
                nodeId  = node_id
                tltrequestMsg  = tltMsg
                ndidid  = ndid_id
            }
        }
    }
}