package tlt.th.co.toyotaleasing.model.response


import com.google.gson.annotations.SerializedName

data class ItemSyncDocsUpload(
    @SerializedName("DOC_HISTORY")
    var dOCHISTORY: List<DOCHISTORY>,
    @SerializedName("DOC_UPLOAD")
    var dOCUPLOAD: List<DOCUPLOAD>
) {
    data class DOCHISTORY(
        @SerializedName("DOC_IMG")
        var dOCIMG: String,
        @SerializedName("DOC_NAME")
        var dOCNAME: String,
        @SerializedName("DOC_TYPE")
        var dOCTYPE: String
    )

    data class DOCUPLOAD(
        @SerializedName("DOC_IMG")
        var dOCIMG: String,
        @SerializedName("DOC_NAME")
        var dOCNAME: String,
        @SerializedName("DOC_TYPE")
        var dOCTYPE: String
    )
}