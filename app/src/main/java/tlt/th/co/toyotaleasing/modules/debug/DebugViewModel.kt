package tlt.th.co.toyotaleasing.modules.debug

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tlt.th.co.toyotaleasing.manager.ContextManager
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.db.UserManager
import tlt.th.co.toyotaleasing.model.response.PassportResponse
import java.io.IOException
import java.nio.charset.Charset

class DebugViewModel : ViewModel() {

    val whenTokenChanged = MutableLiveData<Boolean>()
    val whenDataLoaded = MutableLiveData<Model>()
    val whenProfileChanged = MutableLiveData<Boolean>()
    private val userManager = UserManager.getInstance()

    fun changeToken(token: String) {
        if (token.isEmpty()) {
            return
        }

        UserManager.getInstance().saveAccessToken(token)
        whenTokenChanged.value = true
    }

    fun getData() {
        val result = readPassportFromJsonAsset()
        val items = JsonMapperManager.getInstance()
                .gson.fromJson(result, Array<PassportResponse>::class.java)

        whenDataLoaded.postValue(Model(
                passportList = items.toList()
        ))
    }

    fun passportChange(position: Int) {
        val item = whenDataLoaded.value?.passportList?.getOrNull(position)

        val entity = userManager.getProfile().apply {
            id = ""
            name = item?.name ?: ""
            token = item?.token ?: ""
            email = ""
            idCard = item?.passportId ?: ""
            birthdate = ""
            salt = item?.salt ?: ""
            isEnableFingerprintAuth = false
            isShowAppIntro = false
            isCustomer = true
            isShowAppPAPD = false
        }

        userManager.saveProfile(entity)
        whenProfileChanged.postValue(true)
    }

    private fun readPassportFromJsonAsset(): String? {
        var json: String? = null
        try {
            val context = ContextManager.getInstance().getApplicationContext()
            val `is` = context.assets.open("stub/bypass_passport.json")
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            json = String(buffer, Charset.forName("UTF-8"))
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }

        return json
    }

    data class Model(
            val passportList: List<PassportResponse> = listOf()
    )
}