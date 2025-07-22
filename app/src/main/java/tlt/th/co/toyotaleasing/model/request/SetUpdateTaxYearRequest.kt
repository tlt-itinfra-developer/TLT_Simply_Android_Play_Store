package tlt.th.co.toyotaleasing.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import tlt.th.co.toyotaleasing.manager.LocalizeManager

class SetUpdateTaxYearRequest {

    @SerializedName("PSELECT")
    @Expose
    var pselect: String = ""
    @SerializedName("PKEY1")
    @Expose
    var pkey1: String = ""
    @SerializedName("PKEY2")
    @Expose
    var pkey2: String = ""

    companion object {
        fun build(extContract: String,
                  year: String): SetUpdateTaxYearRequest {
            return SetUpdateTaxYearRequest().apply {
                pkey1 = extContract
                pkey2 = if (LocalizeManager.isThai()) (year.toInt() - 543).toString() else year
            }
        }
    }
}