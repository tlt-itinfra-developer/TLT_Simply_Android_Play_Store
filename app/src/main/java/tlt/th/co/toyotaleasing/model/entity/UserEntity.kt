package tlt.th.co.toyotaleasing.model.entity


import tlt.th.co.toyotaleasing.common.extension.toCurrentByUTC
import java.util.*

open class UserEntity(var id: String = "",
                      var name: String = "",
                      var token: String = "",
                      var email: String = "",
                      var idCard: String = "",
                      var birthdate: String = "",
                      var salt: String = "",
                      var isEnableFingerprintAuth: Boolean = false,
                      var isShowAppIntro: Boolean = true,
                      var isCustomer: Boolean = false,
                      var lastActive: Date = Date().toCurrentByUTC(),
                      var unreadNotification: Int = 0,
                      var uuidForNonCustomer: String = "",
                      var isShowTutorial: Boolean = true,
                      var lastSendAnalyticTime: Date = Date().toCurrentByUTC() ,
                      var isShowAppPAPD: Boolean = true)  {

    fun transformToRealm(user: UserEntity) {
        this@UserEntity.id = user.id
        this@UserEntity.name = user.name
        this@UserEntity.token = user.token
        this@UserEntity.email = user.email
        this@UserEntity.idCard = user.idCard
        this@UserEntity.birthdate = user.birthdate
        this@UserEntity.salt = user.salt
        this@UserEntity.isEnableFingerprintAuth = user.isEnableFingerprintAuth
        this@UserEntity.isShowAppIntro = user.isShowAppIntro
        this@UserEntity.isCustomer = user.isCustomer
        this@UserEntity.lastActive = user.lastActive
        this@UserEntity.unreadNotification = user.unreadNotification
        this@UserEntity.uuidForNonCustomer = user.uuidForNonCustomer
        this@UserEntity.isShowTutorial = user.isShowTutorial
        this@UserEntity.lastSendAnalyticTime = user.lastSendAnalyticTime
        this@UserEntity.isShowAppPAPD = user.isShowAppPAPD
    }

    fun transform(): UserEntity {
        return UserEntity().apply {
            this.id = this@UserEntity.id
            this.name = this@UserEntity.name
            this.token = this@UserEntity.token
            this.email = this@UserEntity.email
            this.idCard = this@UserEntity.idCard
            this.birthdate = this@UserEntity.birthdate
            this.salt = this@UserEntity.salt
            this.isEnableFingerprintAuth = this@UserEntity.isEnableFingerprintAuth
            this.isShowAppIntro = this@UserEntity.isShowAppIntro
            this.isCustomer = this@UserEntity.isCustomer
            this.lastActive = this@UserEntity.lastActive
            this.unreadNotification = this@UserEntity.unreadNotification
            this.uuidForNonCustomer = this@UserEntity.uuidForNonCustomer
            this.isShowTutorial = this@UserEntity.isShowTutorial
            this.lastSendAnalyticTime = this@UserEntity.lastSendAnalyticTime
            this.isShowAppPAPD = this@UserEntity.isShowAppPAPD

        }
    }
}