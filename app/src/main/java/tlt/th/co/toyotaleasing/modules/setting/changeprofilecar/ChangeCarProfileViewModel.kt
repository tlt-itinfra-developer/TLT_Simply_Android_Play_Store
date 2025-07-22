package tlt.th.co.toyotaleasing.modules.setting.changeprofilecar

import androidx.lifecycle.MutableLiveData
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.common.eventbus.DeviceErrorEvent
import tlt.th.co.toyotaleasing.common.exception.ApiException
import tlt.th.co.toyotaleasing.manager.BusManager
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager
import tlt.th.co.toyotaleasing.manager.db.CacheManager

import tlt.th.co.toyotaleasing.model.request.GetImageUploadRequest
import tlt.th.co.toyotaleasing.model.request.UploadImagesRequest
import tlt.th.co.toyotaleasing.model.response.GetCarImageUploadResponse
import tlt.th.co.toyotaleasing.model.response.ItemMyCarResponse
import tlt.th.co.toyotaleasing.util.AppUtils
import tlt.th.co.toyotaleasing.util.ImageUtils
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ChangeCarProfileViewModel : BaseViewModel() {

    val whenDataLoaded = MutableLiveData<Model>()
    val whenCarProfileUploadFailure = MutableLiveData<Boolean>()
    val whenCarProfileUploadSuccess = MutableLiveData<Boolean>()

    private val apiManager = TLTApiManager.getInstance()

    fun getData() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
               // whenLoading.postValue(true)
                val carList = getCarListByApi()
                val model = convertToModel(carList)
                whenDataLoaded.postValue(model)
            } catch (e: ApiException) {
                whenDataFailure.postValue(e.message)
            } finally {
                whenLoading.postValue(false)
            }
        }
    }

    private fun convertToModel(carList: List<ItemMyCarResponse>): Model {
        val items = carList.asSequence()
                .map {
                    Item(
                            id = it.contractNumber ?: "",
                            imageUrl = it.cARDEFAULT ?: "",
                            imageBase64 = it.cARIMAGE ?: "",
                            carLicense = "${it.regNumber} - ${it.cREGPROVINCE}",
                            carModel = it.cVEHICLEMODEL ?: "",
                            contractNumber = it.contractNumber ?: ""
                    )
                }.toMutableList()

        return Model(items)
    }

    fun resetCarImage(contractNumber: String) {
        GlobalScope.launch(Dispatchers.Main) {
            val request = UploadImagesRequest.buildForResetCarImage(
                    contractNumber = contractNumber
            )

            apiManager.uploadImages(request) { _, _ -> }
        }
    }

    fun updateCarImage(contractNumber: String, uri: Uri?) {
        if (uri == null || uri == Uri.EMPTY) {
            return
        }

        GlobalScope.launch(Dispatchers.Main) {
            val request = UploadImagesRequest.buildForUpdateCarImage(
                    base64 = ImageUtils.encodeToBase64(uri),
                    contractNumber = contractNumber
            )

            apiManager.uploadImages(request) { _, _ -> }
        }
    }

    private suspend fun getCarListByApi() = suspendCoroutine<List<ItemMyCarResponse>> {
        apiManager.getDataRegNumber { isError, result ->
            if (isError) {
                whenLoading.postValue(false)
                BusManager.observe(DeviceErrorEvent())
//                it.resumeWithException(ApiException(result))
                return@getDataRegNumber
            }

            val items = JsonMapperManager.getInstance()
                    .gson.fromJson(result, Array<ItemMyCarResponse>::class.java)
                    .toList()

            if (items.isEmpty()) {
                whenLoading.postValue(false)
                ////DatabaseManager.getInstance().saveMyCarList(items)
                it.resumeWithException(ApiException(result))
                return@getDataRegNumber
            }

            GlobalScope.launch(Dispatchers.Main) {
                val newItems = items.map {
                    it.apply {
                        cARIMAGE = getCarImageBase64ByApi(it.contractNumber ?: "")
                    }
                }

                ////DatabaseManager.getInstance().saveMyCarList(newItems)
                val contractNumber = CacheManager.getCacheCar()?.contractNumber ?: ""

                if (contractNumber.isEmpty()) {
                    CacheManager.cacheCar(newItems.first())
                }

                whenLoading.postValue(false)
                it.resume(newItems)
            }
        }
    }

    private suspend fun getCarImageBase64ByApi(contractNumber: String) = suspendCancellableCoroutine<String> {
        whenLoading.postValue(true)
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

    data class Model(
            val carList: MutableList<Item> = mutableListOf(),
            val isStaffApp: Boolean = AppUtils.isStaffApp()
    )

    data class Item(
            val id: String = "",
            var imageUri: Uri? = Uri.EMPTY,
            val imageUrl: String = "",
            var imageBase64: String = "",
            val carLicense: String = "",
            val carModel: String = "",
            val contractNumber: String = ""
    )
}