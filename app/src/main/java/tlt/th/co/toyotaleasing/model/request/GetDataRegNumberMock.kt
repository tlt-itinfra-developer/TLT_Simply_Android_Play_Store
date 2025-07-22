package tlt.th.co.toyotaleasing.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetDataRegNumberMock {

    @SerializedName("PKEY1")
    @Expose
    var pkey1: String = ""

    companion object {
        fun build(number: String): GetDataRegNumberMock {
            return GetDataRegNumberMock().apply {
                pkey1 = number
            }
        }
    }

}