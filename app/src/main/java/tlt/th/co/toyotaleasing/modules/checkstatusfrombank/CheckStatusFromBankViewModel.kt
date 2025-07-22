package tlt.th.co.toyotaleasing.modules.checkstatusfrombank

import androidx.lifecycle.MutableLiveData
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager

import tlt.th.co.toyotaleasing.model.entity.SequenceTransactionEntity
import tlt.th.co.toyotaleasing.model.request.CheckStatusFromBankRequest
import tlt.th.co.toyotaleasing.model.request.SequenceIdRequest
import tlt.th.co.toyotaleasing.model.response.CheckStatusFromBankResponse

class CheckStatusFromBankViewModel : BaseViewModel() {

    val whenPushReceiptPage = MutableLiveData<List<CheckStatusFromBankResponse>>()
    val whenNeedFinish = MutableLiveData<Boolean>()
    private val apiTLTApiManager = TLTApiManager.getInstance()

    fun getReceiptData() {
        whenLoading.postValue(true)

        val sequenceIDList = null

        val sequenceIdsListRequest: MutableList<SequenceIdRequest> = mutableListOf()

        val checkStatusFromBankRequest = CheckStatusFromBankRequest.build(sequenceIds = sequenceIdsListRequest)
        apiTLTApiManager.checkStatusFromBank(checkStatusFromBankRequest) { isError: Boolean, result: String ->
            whenLoading.postValue(false)

            if (isError) {
                if (result == "noInternet") {
                    whenNoInternetConnection.postValue(true)
                }
                return@checkStatusFromBank
            }

            val results = JsonMapperManager.getInstance()
                    .gson.fromJson(result, Array<CheckStatusFromBankResponse>::class.java)

            val resultForKeep = results.filter {
                it.flagPpStatus!!.toLowerCase() == "w" ||
                        it.flagPpStatus!!.toLowerCase() == "g"
            }

            val resultUpdated: MutableList<SequenceTransactionEntity> = mutableListOf()

            resultForKeep.forEach {
                resultUpdated.add(SequenceTransactionEntity(sequenceId = it.ppId.toString()))
            }

            //DatabaseManager.getInstance().deleteSequenceIdList()
            //DatabaseManager.getInstance().save(SequenceTransactionEntity::class.java, resultUpdated)

            val receiptData = results.filter {
                it.flagPpStatus!!.toLowerCase() == "y"
            }

//            val receiptTestData = results.filter {
//                it.flagPpStatus!!.isNotEmpty()
//            }

            if (receiptData.isNotEmpty()) {
                whenPushReceiptPage.postValue(receiptData)
            } else {
                whenNeedFinish.postValue(true)
            }
        }
    }
}