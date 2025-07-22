package tlt.th.co.toyotaleasing.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FacebookPicture {
    @SerializedName("is_silhouette")
    @Expose
    var isSilhouette: Boolean = false
    @SerializedName("url")
    @Expose
    var url: String = ""
}
