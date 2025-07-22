package tlt.th.co.toyotaleasing.model.response

import com.google.gson.annotations.SerializedName

data class GetImageUploadResponse(
        @SerializedName("FILE_NAME") var fILENAME: String? = "",
        @SerializedName("FILE_ATTACH") var fILEATTACH: String? = "",
        @SerializedName("UPDATE_DATE") var uPDATEDATE: String? = ""
)