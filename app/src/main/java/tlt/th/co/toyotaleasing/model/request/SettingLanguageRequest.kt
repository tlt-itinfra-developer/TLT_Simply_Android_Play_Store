package tlt.th.co.toyotaleasing.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SettingLanguageRequest {

    @SerializedName("PKEY1")
    @Expose
    var pkey1: String = ""
    @SerializedName("PKEY2")
    @Expose
    var pkey2: String = ""
    @SerializedName("PKEY3")
    @Expose
    var pkey3: String = ""

    companion object {
        fun build(lang: String): SettingLanguageRequest {
            return SettingLanguageRequest().apply {
                pkey1 = lang
            }
        }
    }
}