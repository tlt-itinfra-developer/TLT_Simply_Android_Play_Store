package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.center

import androidx.lifecycle.MutableLiveData
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.common.extension.isDoesNotContainCharacter
import tlt.th.co.toyotaleasing.manager.ContextManager
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.api.tltloan.TLTLoanApiManager
import tlt.th.co.toyotaleasing.model.request.CheckOnlineRegisRequest
import tlt.th.co.toyotaleasing.model.response.ItemPhoneNumberResponse
import tlt.th.co.toyotaleasing.modules.selectphone.PhoneNumber

class LoanAuthenViewModel  : BaseViewModel() {
    private val apiLoanManager = TLTLoanApiManager.getInstance()
    val whenIdCardInvalid = MutableLiveData<Boolean>()
    val whenPhoneNumberListLoaded = MutableLiveData<ArrayList<PhoneNumber>>()
    val whenPhoneNumberListNoLoaded = MutableLiveData<Boolean>()
    val whenDataLoadedMessage = MutableLiveData<String>()

    private fun formValidate( idcard: String): Boolean {
        var valid = true

        if (!idcard.isDoesNotContainCharacter()
                && idcard.isNullOrEmpty()){
            whenIdCardInvalid.postValue(true)
            valid = false
        }

        return valid
    }

    fun getPhonenumberList(idcard: String , refID : String ) {
        if (!formValidate(idcard )) {
            return
        }

        whenLoading.value = true

        apiLoanManager
                .checkOnlineRegis(CheckOnlineRegisRequest.build(idcard =  idcard,
                                                           refID =  refID )) { isError, result , step, msg ->
                    whenLoading.value = false

                    if (isError) {
                        if(msg.length > 0 ) {
                            whenDataLoadedMessage.postValue(msg)
                        }
                        whenPhoneNumberListNoLoaded.value = false
                        return@checkOnlineRegis
                    }

                    if(msg.length > 0 ) {
                        whenDataLoadedMessage.postValue(msg)
                    }

                    val items = JsonMapperManager.getInstance()
                            .gson.fromJson(result, Array<ItemPhoneNumberResponse>::class.java)
                            .toList()
                            .map { PhoneNumber(it.keyPhone, it.phonenumber) }

                    whenPhoneNumberListLoaded.value = ArrayList(items)
                    whenPhoneNumberListLoaded.postValue(ArrayList(items))
                }


    }


}