package tlt.th.co.toyotaleasing.modules.contactus.question_and_answer

import androidx.lifecycle.MutableLiveData
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager
import tlt.th.co.toyotaleasing.manager.db.CacheManager
import tlt.th.co.toyotaleasing.manager.db.MasterDataManager
import tlt.th.co.toyotaleasing.manager.db.UserManager
import tlt.th.co.toyotaleasing.model.request.SendContactAskRequest
import tlt.th.co.toyotaleasing.util.AppUtils

class QuestionAndAnswerViewModel : BaseViewModel() {

    val whenDataLoaded = MutableLiveData<Model>()
    val whenSendToApiFailure = MutableLiveData<String>()
    val whenSendToApiSuccess = MutableLiveData<Boolean>()

    private val userManager = UserManager.getInstance()
    private val profile = CacheManager.getCacheProfile()
    private val apiManager = TLTApiManager.getInstance()
    private val currentCar = CacheManager.getCacheCar()

    private val topics = null

    fun getData() {
        whenLoading.postValue(true)

        val model = if (userManager.isCustomer()) {
            Model(
                    name = profile?.cUSTNAME ?: "",
                    email = profile?.eMAIL ?: "",
                    phonenumber = profile?.pHONE ?: "",
                    carLicense = "${currentCar?.regNumber}",
                    topics = listOf(),
                    status = Status.CUSTOMER
            )
        } else {
            Model(
                    name = "",
                    email = "",
                    phonenumber = "",
                    carLicense = "",
                    topics = listOf(),
                    status = Status.NON_CUSTOMER
            )
        }
        whenLoading.postValue(false)

        whenDataLoaded.postValue(model)
    }

    fun send(name: String = "",
             email: String = "",
             phone: String = "",
             carLicense: String = "",
             topicIndex: Int = 0,
             detail: String = "") {
        whenLoading.postValue(true)

        val request = SendContactAskRequest.build(
                awCode = "",
                description = detail,
                customerName = name,
                email = email,
                phoneNumber = phone,
                carLicense = carLicense,
                extContract = currentCar?.contractNumber ?: ""
        )

        apiManager.sendContactAsk(request) { isError: Boolean, result: String ->
            whenLoading.postValue(false)

            if (isError) {
                whenSendToApiFailure.postValue(result)
                return@sendContactAsk
            }

            whenSendToApiSuccess.postValue(true)
        }
    }

    data class Model(
            val name: String = "",
            val email: String = "",
            val phonenumber: String = "",
            val carLicense: String = "",
            val topics: List<String> = listOf(),
            val status: Status = Status.NON_CUSTOMER,
            val isStaffApp: Boolean = AppUtils.isStaffApp()
    )

    enum class Status {
        CUSTOMER,
        NON_CUSTOMER
    }
}