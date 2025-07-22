package tlt.th.co.toyotaleasing.modules.contract.refinance

import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager
import tlt.th.co.toyotaleasing.manager.db.CacheManager
import tlt.th.co.toyotaleasing.model.request.RequestRefinanceRequest

class RefinanceDialogViewModel : BaseViewModel() {

    val installment = CacheManager.getCacheInstallment()
    val apiManager = TLTApiManager.getInstance()

    fun requestRefinance() {
        val contractNumber = installment?.eXTCONTRACT ?: ""
        val request = RequestRefinanceRequest.build(contractNumber)
        apiManager.requestRefinance(request) { isError, result -> }
    }
}