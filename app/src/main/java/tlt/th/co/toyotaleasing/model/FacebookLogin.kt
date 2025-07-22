package tlt.th.co.toyotaleasing.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FacebookLogin {
    @SerializedName("id")
    @Expose
    val id: String = ""
    @SerializedName("email")
    @Expose
    val email: String = ""
    @SerializedName("first_name")
    @Expose
    val first_name: String = ""
    @SerializedName("last_name")
    @Expose
    val last_name: String = ""
    @SerializedName("link")
    @Expose
    val link: String = ""
    @SerializedName("birthday")
    @Expose
    val birthday: String = ""
    @SerializedName("gender")
    @Expose
    val gender: String = ""
    @SerializedName("locale")
    @Expose
    val locale: String = ""
    @SerializedName("name")
    @Expose
    val name: String = ""
    @SerializedName("timezone")
    @Expose
    val timezone: String = ""
    @SerializedName("updated_time")
    @Expose
    val updated_time: String = ""
    @SerializedName("verified")
    @Expose
    val verified: String = ""
    @SerializedName("picture")
    @Expose
    val picture: FacebookPictureData = FacebookPictureData()
}
