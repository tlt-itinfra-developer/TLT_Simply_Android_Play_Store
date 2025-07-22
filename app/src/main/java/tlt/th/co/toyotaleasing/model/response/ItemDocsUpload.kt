package tlt.th.co.toyotaleasing.model.response


import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import tlt.th.co.toyotaleasing.manager.JsonMapperManager

data class ItemDocsUpload(
    @SerializedName("DOC_HISTORY")
    var dOCHISTORY: List<DOCHISTORY>,
    @SerializedName("DOC_UPLOAD")
    var dOCUPLOAD: List<DOCUPLOAD>,
    @SerializedName("PDF_HISTORY")
    var pDFHISTORY: List<PDFHISTORY>,
    @SerializedName("PDF_UPLOAD")
    var pDFUPLOAD: List<PDFUPLOAD>
) {
    data class DOCHISTORY(
        @SerializedName("DOC_IMG")
        var dOCIMG: String,
        @SerializedName("DOC_NAME")
        var dOCNAME: String,
        @SerializedName("DOC_TYPE")
        var dOCTYPE: String,
        @SerializedName("DOC_NAME_2")
        var dOCNAME_2: String
    )

    data class DOCUPLOAD(
        @SerializedName("DOC_IMG")
        var dOCIMG: String,
        @SerializedName("DOC_NAME")
        var dOCNAME: String,
        @SerializedName("DOC_TYPE")
        var dOCTYPE: String,
        @SerializedName("DOC_NAME_2")
        var dOCNAME_2: String
    )

    data class PDFHISTORY(
        @SerializedName("DOC_IMG")
        var dOCIMG: String,
        @SerializedName("DOC_NAME")
        var dOCNAME: String,
        @SerializedName("DOC_TYPE")
        var dOCTYPE: String,
        @SerializedName("DOC_NAME_2")
        var dOCNAME_2: String
    )

    data class PDFUPLOAD(
        @SerializedName("DOC_IMG")
        var dOCIMG: String,
        @SerializedName("DOC_NAME")
        var dOCNAME: String,
        @SerializedName("DOC_TYPE")
        var dOCTYPE: String,
        @SerializedName("DOC_NAME_2")
        var dOCNAME_2: String

    )

    companion object {
        fun mockList(): List<ItemDocsUpload> {
            val mockJson = """[{"DOC_HISTORY":[],"DOC_UPLOAD":[{"DOC_TYPE":"BANK","DOC_NAME":"EC201","DOC_IMG":"","DOC_NAME_2":""},{"DOC_TYPE":"BANK","DOC_NAME":"EC202","DOC_IMG":"","DOC_NAME_2":""},{"DOC_TYPE":"BANK","DOC_NAME":"EC203","DOC_IMG":"","DOC_NAME_2":""},{"DOC_TYPE":"BANK","DOC_NAME":"EC204","DOC_IMG":"","DOC_NAME_2":""},{"DOC_TYPE":"BANK","DOC_NAME":"EC205","DOC_IMG":"","DOC_NAME_2":""},{"DOC_TYPE":"BANK","DOC_NAME":"EC206","DOC_IMG":"","DOC_NAME_2":""},{"DOC_TYPE":"BANK","DOC_NAME":"EC207","DOC_IMG":"","DOC_NAME_2":""},{"DOC_TYPE":"BANK","DOC_NAME":"EC208","DOC_IMG":"","DOC_NAME_2":""},{"DOC_TYPE":"BANK","DOC_NAME":"EC209","DOC_IMG":"","DOC_NAME_2":""}],"PDF_HISTORY":[],"PDF_UPLOAD":[{"DOC_TYPE":"BANK","DOC_NAME":"EP201","DOC_IMG":"","DOC_NAME_2":""},{"DOC_TYPE":"BANK","DOC_NAME":"EP202","DOC_IMG":"","DOC_NAME_2":""},{"DOC_TYPE":"BANK","DOC_NAME":"EP203","DOC_IMG":"","DOC_NAME_2":""}]}]""".trimIndent()

            return JsonMapperManager.getInstance()
                .gson.fromJson(mockJson, Array<ItemDocsUpload>::class.java)
                .toList()
        }
    }
}


