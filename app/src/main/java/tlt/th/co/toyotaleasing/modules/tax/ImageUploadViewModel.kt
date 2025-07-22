package tlt.th.co.toyotaleasing.modules.tax

import kotlinx.coroutines.*
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.common.extension.ifTrue
import tlt.th.co.toyotaleasing.common.livedata.SingleLiveData
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager
import tlt.th.co.toyotaleasing.manager.db.CacheManager
import tlt.th.co.toyotaleasing.manager.db.PaymentManager
import tlt.th.co.toyotaleasing.model.request.UploadImagesRequest
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ImageUploadViewModel : BaseViewModel() {

    private val apiManager = TLTApiManager.getInstance()
    private val mycar = CacheManager.getCacheCar()

    val whenImageUploading = SingleLiveData<Boolean>()
    val whenImageUploadFail = SingleLiveData<Boolean>()
    val whenImageUploadSuccess = SingleLiveData<Boolean>()

    fun uploadByPorlorbor() {
        GlobalScope.launch(Dispatchers.Main) {
            val uri = PaymentManager.getPorlorborDocuments()
            val seqId = ""

            PaymentManager.savePorlorborDocs(listOf())
        }
    }

    fun uploadByTorloraor() {
        GlobalScope.launch(Dispatchers.Main) {
            val uri = PaymentManager.getTorloraorDocuments()
            val seqId = ""


            PaymentManager.saveTorloraorDocs(listOf())
        }
    }

    fun uploadByTIBAsk(base64List: List<String> = listOf(), tcaId: String = "") {
        GlobalScope.launch(Dispatchers.Main) {
            val requestList = withContext(Dispatchers.Default) {
                base64List.map {
                    UploadImagesRequest.buildForTIBAsk(
                            contractNumber = mycar?.contractNumber ?: "",
                            base64 = it,
                            tcaId = tcaId,
                            amountImages = base64List.size.toString()
                    )
                }
            }

            upload(requestList)
        }
    }

    private suspend fun upload(requests: List<UploadImagesRequest>) = suspendCoroutine<Boolean> { suspend ->
        GlobalScope.launch(Dispatchers.Main) {
            whenImageUploading.postValue(true)

            requests.map {
                GlobalScope.async { uploadToApi(it) }
            }.forEach {
                val isError = it.await()

                isError.ifTrue {
                    whenImageUploading.postValue(false)
                    whenImageUploadFail.postValue(true)
                    suspend.resume(false)
                    return@launch
                }
            }

            whenImageUploading.postValue(false)
            whenImageUploadSuccess.postValue(true)
            suspend.resume(true)
        }
    }

    private suspend fun uploadToApi(request: UploadImagesRequest) = suspendCoroutine<Boolean> {
        apiManager.uploadImages(request) { isError: Boolean, result: String ->
            it.resume(isError)
        }
    }
}