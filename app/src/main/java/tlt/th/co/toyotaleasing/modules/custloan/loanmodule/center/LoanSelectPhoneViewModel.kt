package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.center

import androidx.lifecycle.MutableLiveData
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.api.tltloan.TLTLoanApiManager

import tlt.th.co.toyotaleasing.model.request.CheckOnlineRegisRequest
import tlt.th.co.toyotaleasing.model.response.ItemPhoneNumberResponse
import tlt.th.co.toyotaleasing.modules.selectphone.PhoneNumber

class LoanSelectPhoneViewModel  : BaseViewModel() {
    private val apiLoanManager = TLTLoanApiManager.getInstance()
    val whenPhoneNumberListLoaded = MutableLiveData<ArrayList<PhoneNumber>>()
    val whenDataLoadedMessage = MutableLiveData<String>()

    fun getPhonenumberList() {
//    whenPhoneNumberListLoaded.value = ArrayList(items)
    }

    fun getPhonenumberList(idcard: String , refID : String ) {
        whenLoading.value = true

        apiLoanManager
                .checkOnlineRegis(CheckOnlineRegisRequest.build(idcard =  idcard,
                        refID =  refID )) { isError, result , step , msg ->
                    whenLoading.value = false

                    if (isError) {
                        whenPhoneNumberListLoaded.value = ArrayList()
                        if(msg.length > 0 ) {
                            whenDataLoadedMessage.postValue(msg)
                        }
                        return@checkOnlineRegis
                    }

                    if(msg.length > 0 ) {
                        whenDataLoadedMessage.postValue(msg)
                    }

                    val items = JsonMapperManager.getInstance()
                            .gson.fromJson(result, Array<ItemPhoneNumberResponse>::class.java)
                            .toList()
                            .map { PhoneNumber(it.keyPhone, it.phonenumber) }

//                    whenPhoneNumberListLoaded.value = ArrayList(items)
                    whenPhoneNumberListLoaded.postValue(ArrayList(items))
                }


    }

    fun savePhonenumber(selectPhone: PhoneNumber) {

        //DatabaseManager.getInstance().saveRegisterData(registerState)
    }
}
