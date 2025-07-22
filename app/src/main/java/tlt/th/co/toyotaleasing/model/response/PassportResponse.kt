package tlt.th.co.toyotaleasing.model.response

import com.google.gson.annotations.SerializedName

data class PassportResponse(
        @SerializedName("passportId") var passportId: String? = "",
        @SerializedName("name") var name: String? = "",
        @SerializedName("token") var token: String? = "",
        @SerializedName("salt") var salt: String? = ""
)