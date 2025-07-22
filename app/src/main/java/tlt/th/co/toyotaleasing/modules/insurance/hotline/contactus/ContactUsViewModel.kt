package tlt.th.co.toyotaleasing.modules.insurance.hotline.contactus

import tlt.th.co.toyotaleasing.common.livedata.SingleLiveData
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager
import tlt.th.co.toyotaleasing.manager.db.CacheManager
import tlt.th.co.toyotaleasing.manager.db.MasterDataManager
import tlt.th.co.toyotaleasing.manager.db.UserManager
import tlt.th.co.toyotaleasing.model.request.SendTIBCustomerAskRequest

class ContactUsViewModel : BaseViewModel() {

    val whenDataLoaded = SingleLiveData<Model>()
    val whenSendTIBQuestionFail = SingleLiveData<Boolean>()
    val whenSendTIBQuestionSuccess = SingleLiveData<String>()

    private val apiManager = TLTApiManager.getInstance()
    private val myProfile = CacheManager.getCacheProfile()
    private val currentCar = CacheManager.getCacheCar()

    private val tibQuestionList by lazy {
        MasterDataManager.getInstance().getTIBQuestionList()
    }

    fun getData() {
        val model = Model(
                name = myProfile?.cUSTNAME ?: "",
                email = myProfile?.eMAIL ?: "",
                tel = myProfile?.pHONE ?: "",
                contactTitleList = listOf()
        )

        whenDataLoaded.postValue(model)
    }

    fun sendContactUsData(fullname: String,
                          tel: String,
                          email: String,
                          contactTitleItemPosition: Int,
                          detail: String) {
        whenLoading.postValue(true)

        val request = SendTIBCustomerAskRequest.build(
                askId = getAskId(contactTitleItemPosition),
                description = detail,
                username = fullname,
                mobileNumber = tel,
                email = email,
                extContract = currentCar?.contractNumber ?: ""
        )

        apiManager.sendTIBCustomerAsk(request) { isError, result ->
            whenLoading.postValue(false)

            if (isError) {
                whenSendTIBQuestionFail.postValue(true)
                return@sendTIBCustomerAsk
            }

            whenSendTIBQuestionSuccess.postValue(result)
        }
    }

    private fun getAskId(position: Int) = ""

    data class Model(
            val name: String = "",
            val tel: String = "",
            val email: String = "",
            val contactTitleList: List<String> = listOf()
    ) {
        val status: Status
            get() {
                val userManager = UserManager.getInstance()

                return if (userManager.isCustomer()) {
                    Status.CUSTOMER
                } else {
                    Status.NON_CUSTOMER
                }
            }
    }

    enum class Status {
        CUSTOMER,
        NON_CUSTOMER
    }
}