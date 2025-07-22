package tlt.th.co.toyotaleasing.model.entity



open class TaxEntity(
        // JSON String of ItemInstallmentResponse.kt
        var json: String = ""
)  {

    fun transformToRealm(state: TaxEntity) {
        this@TaxEntity.json = state.json
    }

    fun transform(): TaxEntity {
        return TaxEntity().apply {
            this.json = this@TaxEntity.json
        }
    }
}