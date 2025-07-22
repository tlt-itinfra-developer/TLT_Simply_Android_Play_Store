package tlt.th.co.toyotaleasing.model.response


import com.google.gson.annotations.SerializedName
import tlt.th.co.toyotaleasing.manager.JsonMapperManager

data class DocSumUploadedResponse(
    @SerializedName("DOC_TYPE")
    var dOCTYPE: String,
    @SerializedName("SUMMARY")
    var sUMMARY: String
) {
    companion object {
        fun mockList(): List<DocSumUploadedResponse> {
            val mockJson = """
                [
                    {"DOC_TYPE":"BANK","SUMMARY":"0/12"},
                    {"DOC_TYPE":"HOME","SUMMARY":"0/12"},
                    {"DOC_TYPE":"INCOME","SUMMARY":"0/12"},
                    {"DOC_TYPE":"OTHER","SUMMARY":"0/14"},
                    {"DOC_TYPE":"REGISTRATION","SUMMARY":"0/9"}
                ]
            """.trimIndent()

            return JsonMapperManager.getInstance()
                .gson.fromJson(mockJson, Array<DocSumUploadedResponse>::class.java)
                .toList()
        }
    }
}
