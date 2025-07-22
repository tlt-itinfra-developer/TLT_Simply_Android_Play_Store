package tlt.th.co.toyotaleasing.model.request


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetCarsLoanRequest {

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
    var pkey4: String = ""


    companion object {
        fun build(idCard: String): GetCarsLoanRequest {
            return GetCarsLoanRequest().apply {
                pkey1 = idCard
            }
        }
    }
}