package tlt.th.co.toyotaleasing.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FacebookPictureData {
    @SerializedName("data")
    @Expose
    var facebookPicture: FacebookPicture = FacebookPicture()
}
