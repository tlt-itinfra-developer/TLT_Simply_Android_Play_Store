package tlt.th.co.toyotaleasing.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import tlt.th.co.toyotaleasing.BuildConfig

class CheckApplicationStatusRequest {

    @SerializedName("PSELECT")
    @Expose
    var PSELECT = ""

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

        fun build(): CheckApplicationStatusRequest {
            return CheckApplicationStatusRequest().apply {
                PSELECT = "ANDROID"
                PKEY1 = BuildConfig.VERSION_NAME
                PKEY2 = BuildConfig.VERSION_CODE.toString()
                PKEY3 = ""
            }
        }
    }
}