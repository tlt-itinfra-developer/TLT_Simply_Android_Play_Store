package tlt.th.co.toyotaleasing.model.entity



open class MyCarEntity(
        // JSON String of ItemMyCarResponse.kt
        var json: String = ""
)  {

    fun transformToRealm(state: MyCarEntity) {
        this@MyCarEntity.json = state.json
    }

    fun transform(): MyCarEntity {
        return MyCarEntity().apply {
            this.json = this@MyCarEntity.json
        }
    }
}