package tlt.th.co.toyotaleasing.modules.ownership

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tlt.th.co.toyotaleasing.common.livedata.SingleLiveData
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager
import tlt.th.co.toyotaleasing.manager.db.CacheManager
import tlt.th.co.toyotaleasing.model.request.GetTransferOwnershipTrackingRequest
import tlt.th.co.toyotaleasing.model.response.TransferOwnershipTrackingResponse
import tlt.th.co.toyotaleasing.util.PdfUtils

class OwnershipTransferTrackingViewModel : ViewModel() {
    val whenDataLoaded = MutableLiveData<Model>()
    val whenLoading = MutableLiveData<Boolean>()
    val whenDataListLoaded = SingleLiveData<ArrayList<StepInfoModel>>()
    val whenDataLoadedFailure = MutableLiveData<String>()
    val whenOpenPdf = MutableLiveData<String>()

    fun getOwnershipData() {
        whenLoading.value = true
        val currentCar = CacheManager.getCacheCar()
        var extContract =  currentCar!!.contractNumber.toString()
//        val car = CacheManager.getCacheTax()
//        if(car == null){
//            extContract =  car!!.cREGNO.toString()
//        }

        TLTApiManager.getInstance()
                .getTransferOwnershipTracking(GetTransferOwnershipTrackingRequest.build(extContract
                        ?: "")) { isError, result ->
                    whenLoading.value = false

                    if (isError) {
                        return@getTransferOwnershipTracking
                    }

                    val items = JsonMapperManager.getInstance()
                            .gson.fromJson(result, Array<TransferOwnershipTrackingResponse>::class.java)

                    var data = items.map {
                        Model(
                                regNo = currentCar.regNumber.toString(),
                                date = currentCar!!.cURRENTDATE.toString(),
                                eMSNO = it.eMSNO,
                                mAILINGADDRESS = it.mAILINGADDRESS,
                                tRACKINGTYPE = it.tRACKINGTYPE,
                                uRLEMS = it.uRLEMS ,
                                thaipostTrackWebsite = "https://track.thailandpost.co.th/?lang=th"
                        )
                    }.get(0)
                    whenDataLoaded.postValue(data)

                    var dataList = items[0].sTEPINFO.toList().map {
                        StepInfoModel(

                                carImg = it.carImg,
                                dateTime = it.dateTime,
                                documentDownload = it.documentDownload,
                                documentName = it.documentName + ".pdf",
                                imgColor = it.imgColor,
                                imgType = it.imgType,
                                issueDesc = it.getIssueDesc(),
                                step = it.step,
                                textColor = it.textColor,
                                textDesc = it.gettextDesc() ,
                                desLoad =  it.desLoad()
                        )
                    }
                    whenDataListLoaded.value = ArrayList(dataList)

                }

    }



    fun showPDF( docBase64: String , showName : String)  {
        var filename = showName

        if(docBase64 == "") {
            whenLoading.value = false
            whenDataLoadedFailure.value = ""
        }else {
            whenLoading.value = false
            PdfUtils.savePdf(filename, docBase64 ?: "")
            whenOpenPdf.postValue(filename)
        }
    }


     data class StepInfoModel(
            var carImg: String = "",
            var dateTime : String = "",
            var documentDownload: String = "",
            var documentName: String = "",
            var imgColor: String = "",
            var imgType: String = "",
            var issueDesc: String = "",
            var step: String = "",
            var textColor: String = "",
            var textDesc: String = "" ,
            var desLoad: String = ""
    )

    data class Model(
            var regNo: String = "",
            var date : String = "",
            var eMSNO: String = "",
            var mAILINGADDRESS : String = "",
            var tRACKINGTYPE: String = "",
            var uRLEMS: String = "" ,
            var thaipostTrackWebsite : String =  ""
    )
}
