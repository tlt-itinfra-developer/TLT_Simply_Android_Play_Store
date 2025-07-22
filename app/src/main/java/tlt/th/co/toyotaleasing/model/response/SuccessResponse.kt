package tlt.th.co.toyotaleasing.model.response

import com.google.gson.annotations.SerializedName

data class SuccessResponse(
        @SerializedName("WSStatus")
        val status: String = "",
        @SerializedName("WSMsg")
        val wsMsg: Result
) {
    data class Result(
            @SerializedName("MsgStatus")
            val msgStatus: String = "",
            @SerializedName(value = "result", alternate = ["Result"])
            val result: String = "",
            @SerializedName("rslc")
            val token: String = ""
    )
}