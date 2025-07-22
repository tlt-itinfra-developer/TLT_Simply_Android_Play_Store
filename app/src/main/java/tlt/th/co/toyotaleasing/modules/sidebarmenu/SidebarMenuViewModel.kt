package tlt.th.co.toyotaleasing.modules.sidebarmenu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager
import tlt.th.co.toyotaleasing.manager.api.tltloan.TLTLoanApiManager
import tlt.th.co.toyotaleasing.manager.db.CacheManager
import tlt.th.co.toyotaleasing.manager.db.UserManager
import tlt.th.co.toyotaleasing.model.request.CheckIdentityRequest
import tlt.th.co.toyotaleasing.model.response.GetDataProfileResponse

class SidebarMenuViewModel : ViewModel() {
    private val apiLoanManager = TLTLoanApiManager.getInstance()
    val whenDataLoaded = MutableLiveData<Model>()
    val whenLogoutSuccess = MutableLiveData<Boolean>()
    val whenCheckShowTutorial = MutableLiveData<Boolean>()
    val whenCheckIdentity  = MutableLiveData<Boolean>()

    fun getUserProfile() {
        val isCustomer = UserManager.getInstance().isCustomer()

        whenDataLoaded.value = Model(isCustomer)

        if (!isCustomer) {
            return
        }

        TLTApiManager.getInstance()
                .getDataProfile { isError, result ->
                    if (isError) {
                        return@getDataProfile
                    }

                    val items = JsonMapperManager.getInstance()
                            .gson.fromJson(result, Array<GetDataProfileResponse>::class.java)

                    if (items.isEmpty()) {
                        return@getDataProfile
                    }

                    val item = items.first()

                    CacheManager.cacheProfile(item)

                    UserManager.getInstance().saveName(item.cUSTNAME ?: "")

                    whenDataLoaded.value = Model(
                            isCustomer = UserManager.getInstance().isCustomer(),
                            fullname = item.cUSTNAME ?: "",
                            profileImage = item.pROIMAGE ?: ""
                    )
                }
    }

    fun checkShowTutorial() {
        val user = UserManager.getInstance()

        whenCheckShowTutorial.postValue(user.isShowAppTutorial())
    }

    fun logoutByCustomer() {
        val isCustomer = UserManager.getInstance().isCustomer()

        if (!isCustomer) {
            return
        }

        TLTApiManager.getInstance()
                .logoutByCustomer { isError, _ ->
                    whenLogoutSuccess.postValue(!isError)
                }
    }

    fun isCustomer() = UserManager.getInstance().isCustomer()

    fun getIdentity() {
        GlobalScope.launch(Dispatchers.Main) {
            apiLoanManager.getIdentity(CheckIdentityRequest.build(idcard = "",
                    refID = "")) { isError, result, step, msg ->
                if (isError) {
                    return@getIdentity
                }

                if (result == "Y") {
                    whenCheckIdentity.value = true
                } else {
                    whenCheckIdentity.value = false
                }
                return@getIdentity
            }
        }
    }


    data class Model(
            val isCustomer: Boolean = false,
            val fullname: String = "",
            val profileImage: String = ""
    )
}