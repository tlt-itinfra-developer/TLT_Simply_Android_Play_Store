package tlt.th.co.toyotaleasing.modules.contract

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager
import tlt.th.co.toyotaleasing.manager.db.CacheManager
import tlt.th.co.toyotaleasing.model.request.GetDataInstallmentRequest
import tlt.th.co.toyotaleasing.model.response.ItemInstallmentResponse

class ContractDetailViewModel : ViewModel() {

    val whenLoading = MutableLiveData<Boolean>()
    val whenDataLoadedSuccess = MutableLiveData<Boolean>()
    val whenDataLoadedFailure = MutableLiveData<String>()

    fun getInstallment(extContract: String = "") {
        val contractNumber = if (extContract.isEmpty()) {
            CacheManager.getCacheCar()?.contractNumber ?: ""
        } else {
            extContract
        }

        val request = GetDataInstallmentRequest.build(contractNumber)

        whenLoading.value = true

        TLTApiManager.getInstance()
                .getDataInstallment(request) { isError, result ->
                    whenLoading.value = false

                    if (isError) {
                        if (result != "device logon") {
                            whenDataLoadedFailure.value = result
                        }
                        return@getDataInstallment
                    }

                    val items = JsonMapperManager.getInstance()
                            .gson.fromJson(result, Array<ItemInstallmentResponse>::class.java)

                    if (items.isEmpty()) {
                        whenDataLoadedFailure.value = ""
                        return@getDataInstallment
                    }

                    val carContract = items.first()

                    CacheManager.cacheInstallment(carContract)

                    whenDataLoadedSuccess.value = true
                }
    }
}