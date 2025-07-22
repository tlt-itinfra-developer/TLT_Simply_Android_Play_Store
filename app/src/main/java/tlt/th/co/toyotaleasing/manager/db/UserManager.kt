package tlt.th.co.toyotaleasing.manager.db

import tlt.th.co.toyotaleasing.common.extension.toCurrentByUTC
import tlt.th.co.toyotaleasing.model.entity.UserEntity
import java.util.*

class UserManager private constructor() {

    fun isCustomer(): Boolean {
        return true
    }

    fun getProfile() = DatabaseManager.getInstance().getUserProfile()

    fun saveProfile(user: UserEntity) {
        DatabaseManager.getInstance()
                .saveUserProfile(user)
    }

    fun saveAccessToken(accessToken: String?) {
        if (accessToken == null || accessToken.isEmpty()) {
            return
        }

        val user = getProfile().apply {
            this.token = accessToken
        }

        saveProfile(user)
    }

    fun setZeroUnreadNotification() {
        val user = getProfile().apply {
            this.unreadNotification = 0
        }

        saveProfile(user)
    }

    fun minusUnreadNotification() {
        val user = getProfile().apply {
            this.unreadNotification = this.unreadNotification - 1
        }

        saveProfile(user)
    }

    fun addUnreadNotification() {
        val user = getProfile().apply {
            this.unreadNotification = this.unreadNotification + 1
        }

        saveProfile(user)
    }

    fun getUnreadNotification() = getProfile().unreadNotification.toString()

    fun saveName(name: String) {
        val user = getProfile().apply {
            this.name = name
        }

        saveProfile(user)
    }

    fun saveUuidForNonCustomer(uuid: String) {
        val user = getProfile().apply {
            this.uuidForNonCustomer = uuid
        }

        saveProfile(user)
    }

    fun saveEmail(email: String) {
        val user = getProfile().apply {
            this.email = email
        }

        saveProfile(user)
    }

    fun hashPincodeForAuth(plaintext: String): String {
       return ""
    }

    fun hashPincodeForSetup(plaintext: String): String {
        return ""
    }

    fun isShowAppIntro() = getProfile().isShowAppIntro

    fun isShowAppPAPD() = getProfile().isShowAppPAPD


    fun isShowAppTutorial() = getProfile().isShowTutorial

    fun disableShowAppIntro() {
        val user = getProfile().apply {
            isShowAppIntro = false
        }

        saveProfile(user)
    }

    fun disableShowAppPAPD(){
        val user = getProfile().apply {
            isShowAppPAPD = false
        }

        saveProfile(user)
    }

    fun disableShowAppTutorial() {
        val user = getProfile().apply {
            isShowTutorial = false
        }

        saveProfile(user)
    }

    fun changeToCustomer() {
        val user = getProfile().apply {
            isCustomer = true
        }

        saveProfile(user)
    }

    fun isFingerprintEnabled() = getProfile().isEnableFingerprintAuth

    fun enableFingerprintAuth() {
        val user = getProfile().apply {
            isEnableFingerprintAuth = true
        }

        saveProfile(user)
    }

    fun disableFingerprintAuth() {
        val user = getProfile().apply {
            isEnableFingerprintAuth = false
        }

        saveProfile(user)
    }

    fun increaseLoginAttempt() {
        val databaseManager = DatabaseManager.getInstance()
        val attempt = databaseManager.getLoginAttempt()
        databaseManager.saveLoginAttempt(attempt + 1)
    }

    fun isLoginAttemptOverLimit(): Boolean {
        val limitAttempt = 5
        val attempt = DatabaseManager.getInstance().getLoginAttempt()

        return attempt >= limitAttempt
    }

    fun getLoginAttempt() = DatabaseManager.getInstance().getLoginAttempt()

    fun resetLoginAttempt() {
        DatabaseManager.getInstance()
                .saveLoginAttempt(0)
    }

    fun increaseSecretModeAttempt() {
        val databaseManager = DatabaseManager.getInstance()
        val attempt = databaseManager.getSecretModeAttempt()
        databaseManager.saveSecretModeAttempt(attempt + 1)
    }

    fun isSecretModeAttemptCompleted(): Boolean {
        val secretMode = 7
        val attempt = DatabaseManager.getInstance().getSecretModeAttempt()

        return attempt == secretMode
    }

    fun resetSecretModeAttempt() {
        //DatabaseManager.getInstance().saveSecretModeAttempt(0)
    }

    fun getSecretAttempt() = DatabaseManager.getInstance().getSecretModeAttempt()

    fun isLastActiveOverLimit5mins(): Boolean {
        val current = Date().toCurrentByUTC()
        val lastActive = getProfile().lastActive
        val millseconds = current.time - lastActive.time
        val mins = millseconds / (1000 * 60) % 60

        return mins >= 5
    }

    fun isTimeToSendAnalytics(): Boolean {
        val current = Date().toCurrentByUTC()
        val lastSend = getProfile().lastSendAnalyticTime
        val milliseconds = current.time - lastSend.time
        val mins = milliseconds / (1000 * 60) % 60

        return mins >= 5
    }

    fun updateLastSendAnaluyticTime() {
        val current = Date().toCurrentByUTC()
        val user = getProfile().apply {
            this.lastSendAnalyticTime = current
        }

        //DatabaseManager.getInstance().saveUserProfile(user)
    }

    fun updateLastActive() {
        val current = Date().toCurrentByUTC()
        val user = getProfile().apply {
            this.lastActive = current
        }

        //DatabaseManager.getInstance().saveUserProfile(user)
    }

    companion object {
        private val userManager = UserManager()

        fun getInstance() = userManager
    }
}