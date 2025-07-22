package tlt.th.co.toyotaleasing.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SettingApplicationRequest {

    @SerializedName("PKEY1")
    @Expose
    var pkey1: String = ""
    @SerializedName("PKEY2")
    @Expose
    var pkey2: String = ""
    @SerializedName("PKEY3")
    @Expose
    var pkey3: String = ""
    @SerializedName("PKEY4")
    @Expose
    var pkey4: String = ""
    @SerializedName("PKEY5")
    @Expose
    var pkey5: String = ""
    @SerializedName("PKEY6")
    @Expose
    var pkey6: String = ""
    @SerializedName("PKEY7")
    @Expose
    var pkey7: String = ""
    @SerializedName("PKEY8")
    @Expose
    var pkey8: String = ""

    companion object {
        fun build(lang: String = "TH",
                  installment : String = "5",
                  tax : String = "15",
                  insurance : String = "15",
                  flagNews : String = "Y",
                  flagTIB : String = "Y"): SettingApplicationRequest {
            return SettingApplicationRequest().apply {
                pkey1 = lang
                pkey2 = installment
                pkey3 = tax
                pkey4 = insurance
                pkey5 = flagNews
                pkey6 = flagTIB
            }
        }
    }
}