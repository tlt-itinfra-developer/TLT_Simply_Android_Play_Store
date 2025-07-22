package tlt.th.co.toyotaleasing.model.response

import com.google.gson.annotations.SerializedName

data class GetCarImageUploadResponse(
        @SerializedName("EXT_CONTRACT") var extContract: String? = "",
        @SerializedName("CAR_NAME") var carName: String? = "",
        @SerializedName("CAR_IMAGE") var carImage: String? = "",
        @SerializedName("UPDATE_DATE") var uPDATEDATE: String? = ""
)