package tlt.th.co.toyotaleasing.model.request


import com.google.gson.annotations.SerializedName

class SyncIdCardRequest{
    @SerializedName("SELECT")
    var select : String = ""
    @SerializedName("PKEY1")
    var pKEY1: String = ""
    @SerializedName("PKEY2")
    var pKEY2: String = ""
    @SerializedName("PKEY3")
    var pKEY3: String = ""
    @SerializedName("PKEY4")
    var pKEY4: String = ""
    @SerializedName("PKEY5")
    var pKEY5: String = ""

    companion object {
    fun build(strImg : String ,  refID: String): SyncIdCardRequest {
        return SyncIdCardRequest().apply {
            pKEY2 = refID
            pKEY3 = strImg
        }
    }
}
}