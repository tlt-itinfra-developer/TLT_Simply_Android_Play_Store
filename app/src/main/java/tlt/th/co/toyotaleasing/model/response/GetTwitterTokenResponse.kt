package tlt.th.co.toyotaleasing.model.response

import com.google.gson.annotations.SerializedName

data class GetTwitterTokenResponse(
        @SerializedName("token_type") var tokenType: String? = "",
        @SerializedName("access_token") var accessToken: String? = ""
)