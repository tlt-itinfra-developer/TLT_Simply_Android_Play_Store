package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.carloan

import android.util.Log
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.common.extension.ifTrue
import tlt.th.co.toyotaleasing.common.livedata.SingleLiveData
import tlt.th.co.toyotaleasing.manager.api.tltloan.TLTLoanApiManager
import tlt.th.co.toyotaleasing.manager.db.MasterDataManager
import tlt.th.co.toyotaleasing.model.entity.masterdata.SyncImage
import tlt.th.co.toyotaleasing.model.request.SyncDeleteDocUploadRequest
import tlt.th.co.toyotaleasing.model.request.SyncDocUploadRequest
import tlt.th.co.toyotaleasing.model.response.ItemDocsUpload
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.carloan.common.ItemFilesData
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.center.common.MenuStepData
import tlt.th.co.toyotaleasing.view.imagehistory.FileHistoriesWidget
import tlt.th.co.toyotaleasing.view.imagehistory.ImageHistoriesWidget
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SyncDocumentViewModel : BaseViewModel() {

    private val apiLoanManager = TLTLoanApiManager.getInstance()
    private val masterDataManager = MasterDataManager.getInstance()
    val whenDataLoadedMessage = MutableLiveData<String>()
    val whenDataLoadedNoData = MutableLiveData<Boolean>()
    val whenImageUploading = SingleLiveData<Boolean>()
    val whenImageUploadFail = SingleLiveData<Boolean>()
    val whenImageUploadSuccess = SingleLiveData<Boolean>()
    val whenDataLoadedFailure = MutableLiveData<String>()
    val whenSyncSuccessData = MutableLiveData<MenuStepData>()
    val whenSyncSuccess = MutableLiveData<Boolean>()
    val whenDataLoadedDoc = MutableLiveData<Model>()
    val whenDataLoadedPdf = MutableLiveData<Model>()
    val whenDataLoadedDocMaster = MutableLiveData<List<DocItem>>()
    val whenDataLoadedHistory = MutableLiveData<List<ImageHistoriesWidget.History>>()
    val whenDataLoadedPdfHistory = MutableLiveData<List<FileHistoriesWidget.History>>()

    fun getMasterDocType() {
        whenLoading.postValue(true)
        val docUpload = masterDataManager.getSyncDocTypeListV2()?.map {
            DocItem(
                dOCTYPE = it.dOCTYPE ?: "",
                dOCDESC = it.getDocDes() ?: "",
                dOCEXTENS = it.dOCREMARK ?: "",
                max = it.mAXUPLOAD ?: "",
                min = it.mINUPLOAD ?: "",
                dOCREMARK = it.dOCREMARK ?: ""
            )
        }
        whenLoading.postValue(false)
        whenDataLoadedDocMaster.postValue(docUpload)
    }


    fun getDataDocUpload(refNo: String, type: String) {

        whenLoading.value = true

        try {

            val items = ItemDocsUpload.mockList().first()
            whenLoading.value = false

            val itemsDocList = items.dOCUPLOAD.map {
                DocUploadItem(
                    dOCTYPE = it.dOCTYPE ?: "",
                    dOCNAME = it.dOCNAME ?: "",
                    dOCIMAGE = it.dOCIMG ?: "",
                    dOCFILENAME = it.dOCNAME_2 ?: ""
                )
            }

            val itemsHistList = items.dOCHISTORY.filter { it.dOCIMG != "" }.map {
                DocUploadItem(
                    dOCTYPE = it.dOCTYPE ?: "",
                    dOCNAME = it.dOCNAME ?: "",
                    dOCIMAGE = it.dOCIMG ?: "",
                    dOCFILENAME = it.dOCNAME_2 ?: ""
                )

            }

            val itemsPdfList = items.pDFUPLOAD.map {
                DocUploadItem(
                    dOCTYPE = it.dOCTYPE ?: "",
                    dOCNAME = it.dOCNAME ?: "",
                    dOCIMAGE = it.dOCIMG ?: "",
                    dOCFILENAME = it.dOCNAME_2 ?: ""
                )
            }

            val itemsPdfHistList = items.pDFHISTORY.filter { it.dOCIMG != "" }.map {
                DocUploadItem(
                    dOCTYPE = it.dOCTYPE ?: "",
                    dOCNAME = it.dOCNAME ?: "",
                    dOCIMAGE = it.dOCIMG ?: "",
                    dOCFILENAME = it.dOCNAME_2 ?: ""
                )

            }

            var sDoc = getDocType(type, "IMG")

            // Doc for upload
            var listDoc = Model(
                docUploadItem = itemsDocList,
                m_max = itemsDocList.size ?: (sDoc!!.get(0).mAXUPLOAD)!!.toInt(),
                m_min = sDoc!!.get(0).mINUPLOAD!!.toInt() ?: 1
            )

            if (itemsDocList.size == 0) {
                whenDataLoadedNoData.postValue(true)
            } else {
                whenDataLoadedDoc.postValue(listDoc)
            }

            // History for upload
            val listBase64 = itemsHistList.map { it.dOCIMAGE }
            val histories = ImageHistoriesWidget.History(title = "", images = listBase64)
            whenDataLoadedHistory.postValue(listOf(histories))


            ///////// PDF  MODE ////////

            var sPdf = getDocType(type, "PDF")

            // PDF for upload
            var listPdf = Model(
                docUploadItem = itemsPdfList,
                m_max = itemsPdfList.size ?: (sPdf!!.get(0).mAXUPLOAD)!!.toInt(),
                m_min = sPdf!!.get(0).mINUPLOAD!!.toInt() ?: 1
            )

            if (itemsPdfList.size == 0) {
                whenDataLoadedNoData.postValue(true)
            } else {
                whenDataLoadedPdf.postValue(listPdf)
            }

            // PDF for upload
            val pdfListBase64 = itemsPdfHistList.map {
                ItemFilesData(
                    fName = it.dOCFILENAME,
                    fBase64 = it.dOCIMAGE,
                    fDocName = it.dOCNAME
                )
            }
            val pdfhistories = FileHistoriesWidget.History(
                title = "",
                images = pdfListBase64
            )
            whenDataLoadedPdfHistory.postValue(listOf(pdfhistories))
            whenLoading.postValue(false)

        } catch (e: Exception) {
            whenLoading.postValue(false)
            Log.d("Error Connect API : ", e.stackTrace.toString())
        }

    }


    fun uploadSyncImage(item: List<SyncDocumentActivity.SyncDocModel>) {
        GlobalScope.launch(Dispatchers.Main) {
            if (item?.isEmpty() != false) {
                return@launch
            }
            val requestList = withContext(Dispatchers.Default) {
                item.map {
                    SyncDocUploadRequest.build(
                        doctype = it.doctype,
                        ref_no = it.refNo,
                        docname = it.docname,
                        imagebase64 = it.imagebase64,
                        docname2 = it.docname2,
                        extendsion = it.extension
                    )
                }
            }
//            syncDocuploadPDF(requestList)
            withContext(Dispatchers.Default) { upload(requestList) }
        }
    }


    private suspend fun upload(requests: List<SyncDocUploadRequest>) =
        suspendCoroutine<Boolean> { suspend ->
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


    private suspend fun uploadToApi(request: SyncDocUploadRequest) = suspendCoroutine<Boolean> {

                whenDataLoadedMessage.postValue("success")

    }

    fun getDocType(type: String, typeExtend: String): List<SyncImage>? {
        val syncDoc = masterDataManager.getSyncDocTypeListV2()?.filter {
            it.dOCTYPE == type && it.dOCREMARK == typeExtend
        }
        return syncDoc

    }


    data class DocUploadItem(
        var dOCTYPE: String = "",
        var dOCNAME: String = "",
        var dOCIMAGE: String = "",
        var dOCFILENAME: String = ""
    )

    data class Model(
        var docUploadItem: List<DocUploadItem> = listOf(),
        var m_max: Int,
        var m_min: Int
    )

//    Delete

    fun deleteDocUpload(item: List<SyncDocumentActivity.SyncDocModel>) {
        GlobalScope.launch(Dispatchers.Main) {
            if (item?.isEmpty() != false) {
                return@launch
            }
            val requestList = withContext(Dispatchers.Default) {
                item.map {
                    SyncDeleteDocUploadRequest.build(
                        doctype = it.doctype,
                        ref_no = it.refNo,
                        docname = it.docname
                    )
                }
            }

            withContext(Dispatchers.Default) { delete(requestList) }
        }
    }


    private suspend fun delete(requests: List<SyncDeleteDocUploadRequest>) =
        suspendCoroutine<Boolean> { suspend ->
            GlobalScope.launch(Dispatchers.Main) {
                whenImageUploading.postValue(true)

                requests.map {
                    GlobalScope.async { deleteToApi(it) }
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

    private suspend fun deleteToApi(request: SyncDeleteDocUploadRequest) =
        suspendCoroutine<Boolean> {
            apiLoanManager.syncDeleteDocUpload(request) { isError, result, step, msg ->
                if (isError) {
                    whenLoading.value = false
                    if (result != "device logon") {
                        whenDataLoadedFailure.value = result
                    }
                    if (msg.length > 0) {
                        whenDataLoadedMessage.postValue(msg)
                    }
                    return@syncDeleteDocUpload
                }

                if (msg.length > 0) {
                    whenDataLoadedMessage.postValue(msg)
                }

                it.resume(isError)
            }
        }

    data class DocItem(
        val dOCTYPE: String = "",
        val dOCDESC: String = "",
        val dOCEXTENS: String = "",
        val max: String = "",
        val min: String = "",
        val dOCREMARK: String = ""

    )


}