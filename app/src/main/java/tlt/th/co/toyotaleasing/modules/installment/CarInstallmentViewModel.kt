package tlt.th.co.toyotaleasing.modules.installment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import tlt.th.co.toyotaleasing.common.eventbus.DeviceErrorEvent
import tlt.th.co.toyotaleasing.common.extension.getYears
import tlt.th.co.toyotaleasing.common.extension.toDatetimeByPattern
import tlt.th.co.toyotaleasing.manager.BusManager
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager
import tlt.th.co.toyotaleasing.manager.db.CacheManager

import tlt.th.co.toyotaleasing.manager.db.NotifyManager
import tlt.th.co.toyotaleasing.model.request.GetDataRegNumberMock
import tlt.th.co.toyotaleasing.model.request.GetImageUploadRequest
import tlt.th.co.toyotaleasing.model.response.GetCarImageUploadResponse
import tlt.th.co.toyotaleasing.model.response.ItemMyCarResponse
import tlt.th.co.toyotaleasing.modules.notify.Notify
import tlt.th.co.toyotaleasing.util.AppUtils
import kotlin.coroutines.resume

class CarInstallmentViewModel : ViewModel() {

    val whenLoading = MutableLiveData<Boolean>()
    val whenGetCarListLoaded = MutableLiveData<Boolean>()
    val whenCarListISEmpty = MutableLiveData<Boolean>()
    val whenGetNotifyListSize = MutableLiveData<String>()
    val whenDataLoadedFailure = MutableLiveData<String>()

    private val apiManager = TLTApiManager.getInstance()

    fun getCarList() {
        whenLoading.postValue(true)

        if (AppUtils.isLocalhostEnvironment()) {
            val request = GetDataRegNumberMock.build("7388773364147")

            apiManager.getDataRegNumberLocalhost(request) { isError, result ->
                if (!isError) {
                    showLoaded()
                    whenLoading.postValue(false)
                    BusManager.observe(DeviceErrorEvent())
//                it.resumeWithException(ApiException(result))
                    return@getDataRegNumberLocalhost
                }

                val items = JsonMapperManager.getInstance()
                        .gson.fromJson(result, Array<ItemMyCarResponse>::class.java)
                        .toList()

                if (items.isEmpty()) {
                    //DatabaseManager.getInstance().saveMyCarList(items)
                    showLoaded()
                    return@getDataRegNumberLocalhost
                }

                GlobalScope.launch(Dispatchers.Main) {
                    val newItems = items.map {
                        it.apply {
                            cARIMAGE = getCarImageBase64ByApi(it.contractNumber ?: "")
                        }
                    }

                    //DatabaseManager.getInstance().saveMyCarList(newItems)
                    val contractNumber = CacheManager.getCacheCar()?.contractNumber ?: ""

                    if (contractNumber.isEmpty()) {
                        CacheManager.cacheCar(newItems.first())
                    }

                    showLoaded()
//                    getNotifyListSize()
                }
            }

        } else {
            apiManager.getDataRegNumber { isError, result ->
                if (isError) {
//                    showLoaded()
                    if (result != "device logon") {
                        whenDataLoadedFailure.value = result
                        BusManager.observe(DeviceErrorEvent())
//                    it.resumeWithException(ApiException(result))
                        return@getDataRegNumber
                    }
                }

                val items = JsonMapperManager.getInstance()
                        .gson.fromJson(result, Array<ItemMyCarResponse>::class.java)
                        .toList()

                if (items.isEmpty()) {
                    //DatabaseManager.getInstance().saveMyCarList(items)
                    showLoaded()
                    return@getDataRegNumber
                }

                GlobalScope.launch(Dispatchers.Main) {
                    val newItems = items.map {
                        it.apply {
                            cARIMAGE = getCarImageBase64ByApi(it.contractNumber ?: "")
                        }
                    }

                    //DatabaseManager.getInstance().saveMyCarList(newItems)
                    val contractNumber = CacheManager.getCacheCar()?.contractNumber ?: ""

                    if (contractNumber.isEmpty()) {
                        CacheManager.cacheCar(newItems.first())
                    }

                    showLoaded()
//                    getNotifyListSize()
                }
            }
        }
    }

    private fun getNotifyListSize(filterByYear: String = "") {
        whenLoading.postValue(true)

        val items = NotifyManager.getNotifyList()
                .map {
                    val notifyType = if (it.image.isNotEmpty()) {
                        Notify.Type.FOUR
                    } else {
                        Notify.Type.FIRST
                    }
                    Notify(
                            "",
                            it.description,
                            it.image,
                            it.navigation,
                            it.datetime.toDatetimeByPattern(),
                            it.iconColor,
                            notifyType,
                            it.datetime.getYears()
                    )
                }
                .filter {
                    filterByYear.isEmpty() || filterByYear == it.year
                }

        whenGetNotifyListSize.postValue(items.size.toString())
        whenLoading.postValue(false)
    }

    private suspend fun getCarImageBase64ByApi(contractNumber: String) = suspendCancellableCoroutine<String> {
        val request = GetImageUploadRequest.buildForCarImage(contractNumber)

        apiManager.getImageUpload(request) { isError, result ->
            whenLoading.postValue(false)
            if (isError) {
                it.resume("")
                return@getImageUpload
            }

            val items = JsonMapperManager.getInstance()
                    .gson.fromJson(result, Array<GetCarImageUploadResponse>::class.java)

            if (items.isEmpty()) {
                it.resume("")
                return@getImageUpload
            }

            val item = items.first()

            if (item.carImage?.isEmpty() == true) {
                it.resume("")
            } else {
                it.resume(item.carImage ?: "")
            }
        }
    }

    private fun showLoaded() {
        whenLoading.postValue(false)
        whenGetCarListLoaded.postValue(true)
    }
}