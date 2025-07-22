package tlt.th.co.toyotaleasing.modules.selectphone

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tlt.th.co.toyotaleasing.manager.FlowManager

import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager
import tlt.th.co.toyotaleasing.model.request.GetPhonenumberRequest
import tlt.th.co.toyotaleasing.model.response.ItemPhoneNumberResponse

class SelectPhoneViewModel : ViewModel() {

    val whenLoading = MutableLiveData<Boolean>()
    val whenPhoneNumberListLoaded = MutableLiveData<ArrayList<PhoneNumber>>()

    fun getPhonenumberList() {
        val request = if (FlowManager.isRegisterFlow()) {
            GetPhonenumberRequest.build("", "")
        } else {
            GetPhonenumberRequest.buildForForgotPincode("", "")
        }

        whenLoading.value = true

        TLTApiManager.getInstance()
                .getPhonenumber(request) { isError, result ->
                    whenLoading.value = false

                    if (isError) {
                        whenPhoneNumberListLoaded.value = ArrayList()
                        return@getPhonenumber
                    }

                    val items = JsonMapperManager.getInstance()
                            .gson.fromJson(result, Array<ItemPhoneNumberResponse>::class.java)
                            .toList()
                            .map { PhoneNumber(it.keyPhone, it.phonenumber) }

                    whenPhoneNumberListLoaded.value = ArrayList(items)
                }
    }

    fun savePhonenumber(selectPhone: PhoneNumber) {
//        val registerState = DatabaseManager.getInstance().getRegisterData().apply {
//            phoneKey = selectPhone.id
//        }

       // //DatabaseManager.getInstance().saveRegisterData(registerState)
    }
}