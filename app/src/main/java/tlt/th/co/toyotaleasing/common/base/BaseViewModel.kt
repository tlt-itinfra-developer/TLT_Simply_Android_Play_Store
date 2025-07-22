package tlt.th.co.toyotaleasing.common.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tlt.th.co.toyotaleasing.analytics.ManualAnalyticManager
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager

import tlt.th.co.toyotaleasing.manager.db.UserManager
import tlt.th.co.toyotaleasing.model.request.Set2LogManualRequest

open class BaseViewModel : ViewModel() {

    val whenLoading = MutableLiveData<Boolean>()
    val whenDataNotFound = MutableLiveData<Boolean>()
    val whenNoInternetConnection = MutableLiveData<Boolean>()
    val whenDataFailure = MutableLiveData<String>()

    fun LogPrintViewModel() {
//        val request = Set2LogManualRequest.build(
//                base64 = ManualAnalyticManager.getAnalyticBase64()
//        )
//
//        TLTApiManager.getInstance().set2LogManual(request) { isError, result ->
//
//            if (isError) {
//                return@set2LogManual
//            }
//
//            UserManager.getInstance().updateLastSendAnaluyticTime()
//            //DatabaseManager.getInstance().deleteAnalyticsList()
//        }
        //DatabaseManager.getInstance().deleteAnalyticsList()
    }

}