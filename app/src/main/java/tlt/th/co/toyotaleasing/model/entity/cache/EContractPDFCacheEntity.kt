package tlt.th.co.toyotaleasing.model.entity.cache



open class EContractPDFCacheEntity(
        var json: String? = ""
) : Jsonable {
    override fun getJsonString(): String {
        return json ?: ""
    }

    override fun setJsonString(json: String) {
        this.json = json
    }
}