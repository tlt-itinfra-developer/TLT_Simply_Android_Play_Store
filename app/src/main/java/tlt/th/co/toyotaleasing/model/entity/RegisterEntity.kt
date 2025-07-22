package tlt.th.co.toyotaleasing.model.entity



open class RegisterEntity(var email: String = "",
                          var simplyId: String = "",
                          var birthdate: String = "",
                          var phoneKey: String = "")  {

    fun transformToRealm(state: RegisterEntity) {
        this@RegisterEntity.email = state.email
        this@RegisterEntity.simplyId = state.simplyId
        this@RegisterEntity.birthdate = state.birthdate
        this@RegisterEntity.phoneKey = state.phoneKey
    }

    fun transform(): RegisterEntity {
        return RegisterEntity().apply {
            this.email = this@RegisterEntity.email
            this.simplyId = this@RegisterEntity.simplyId
            this.birthdate = this@RegisterEntity.birthdate
            this.phoneKey = this@RegisterEntity.phoneKey
        }
    }
}