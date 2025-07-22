package tlt.th.co.toyotaleasing.model.request

import com.google.gson.annotations.SerializedName

data class SkippaymentRequest(
        @SerializedName("PAuthorization") var authorization: String? = ""
) {

    companion object {
        fun build(authorization: String = ""
        ): SkippaymentRequest {
            return SkippaymentRequest().apply {
                this.authorization = authorization
            }
        }
    }
}