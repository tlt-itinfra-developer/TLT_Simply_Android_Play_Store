package tlt.th.co.toyotaleasing.model.response

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
        @SerializedName("WSStatus")
        val status: String = "",
        @SerializedName("WSMsg")
        val message: String = ""
)