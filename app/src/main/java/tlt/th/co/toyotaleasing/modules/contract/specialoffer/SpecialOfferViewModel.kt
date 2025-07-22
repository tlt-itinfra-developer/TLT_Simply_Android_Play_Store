package tlt.th.co.toyotaleasing.modules.contract.specialoffer

import androidx.lifecycle.MutableLiveData
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.common.extension.toDatetime
import tlt.th.co.toyotaleasing.common.extension.toNumberFormat
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.LocalizeManager
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager
import tlt.th.co.toyotaleasing.manager.db.CacheManager
import tlt.th.co.toyotaleasing.model.request.GetPrivilegeRequest
import tlt.th.co.toyotaleasing.model.response.GetPrivilegeResponse
import tlt.th.co.toyotaleasing.modules.contract.contract.ContractViewModel

class SpecialOfferViewModel : BaseViewModel() {

    private val apiManager = TLTApiManager.getInstance()

    val whenPrivilegeLoaded = MutableLiveData<List<GetPrivilegeResponse.Privilege>>()
    val whenPrivilegeIsEmpty = MutableLiveData<String>()
    val whenDataLoaded = MutableLiveData<ContractViewModel.Model>()

    fun getPrivilege() {
        whenLoading.postValue(true)
        val getPrivilegeRequest = GetPrivilegeRequest.build(CacheManager.getCacheCar()?.contractNumber
                ?: "")

        apiManager.getPrivilege(getPrivilegeRequest) { isError: Boolean, result: String ->
            whenLoading.postValue(false)
            if (isError) {
                return@getPrivilege
            }

            getDataByDB()
            val items = JsonMapperManager.getInstance()
                    .gson.fromJson(result, Array<GetPrivilegeResponse>::class.java)

            whenPrivilegeLoaded.postValue(items[0].privileges)
        }

    }


    private fun getDataByDB() {
        val selectedCar = CacheManager.getCacheInstallment()!!
        val dealerName = if (LocalizeManager.isThai()) {
            selectedCar.dEALERNAMETH
        } else {
            selectedCar.dEALERNAMEEN
        }

        val data = ContractViewModel.Model(
                selectedCar.cURRENTDATE.toDatetime(),
                "${selectedCar.cREGNO} - ${selectedCar.cREGPROVINCE}",
                selectedCar.cUSTNAME,
                selectedCar.eXTCONTRACT,
                selectedCar.cVEHICLEMODEL,
                selectedCar.cCONTRACTDATE.toDatetime(),
                selectedCar.cINSTALLDUEDATE,
                selectedCar.cINSTALLAMT.toNumberFormat(),
                "${selectedCar.cPAIDTERM.toFloat().toInt()}/${selectedCar.cTOTALTERM.toFloat().toInt()}",
                selectedCar.cUNPAIDINSTALL.toFloat().toInt().toString(),
                selectedCar.cUNPAIDINSTALLAMT.toNumberFormat(),
                selectedCar.cTOTALPAIDAMT.toNumberFormat(),
                selectedCar.cOUTSTANDINGAMT.toNumberFormat(),
                selectedCar.cPENALTY.toNumberFormat(),
                selectedCar.mOBILENO,
                selectedCar.eMAIL,
                "${selectedCar.aDDRESSLINE1} ${selectedCar.aDDRESSLINE2}\n${selectedCar.aDDRESSLINE3}\n${selectedCar.aDDRESSLINE4} ${selectedCar.pOSTCODE}",
                selectedCar.cTUCPCARPRICE.toNumberFormat(),
                dealerName,
                selectedCar.dEALERIMAGE1,
                selectedCar.cCONTRACTSTATUS,
                dealerCode = selectedCar.dEALERCODE
        )

        whenDataLoaded.value = data
    }
}