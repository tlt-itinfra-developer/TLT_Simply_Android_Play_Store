package tlt.th.co.toyotaleasing.modules.ibanking

import androidx.lifecycle.MutableLiveData
import android.util.Log
import okhttp3.*
import java.io.IOException
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager
import tlt.th.co.toyotaleasing.manager.db.CacheManager

import tlt.th.co.toyotaleasing.manager.db.PaymentManager
import tlt.th.co.toyotaleasing.manager.db.UserManager
import tlt.th.co.toyotaleasing.model.entity.SequenceTransactionEntity
import tlt.th.co.toyotaleasing.model.request.SetPreparePaymentProcessRequest
import tlt.th.co.toyotaleasing.model.request.SetUndoProcessRequest
import tlt.th.co.toyotaleasing.model.request.TLTBankRequest
import tlt.th.co.toyotaleasing.model.response.SetPreparePaymentProcessResponse
import tlt.th.co.toyotaleasing.model.response.SuccessResponse
import tlt.th.co.toyotaleasing.util.AppUtils
import java.lang.Exception

class IBankingViewModel : BaseViewModel() {

    val whenFailure = MutableLiveData<String>()
    val whenDataLoaded = MutableLiveData<Model>()
    val whenMbankDataLoaded = MutableLiveData<String>()

    private val apiManager = TLTApiManager.getInstance()
    private val profile = UserManager.getInstance().getProfile()
    private val jsonMapperManager = JsonMapperManager.getInstance()
    private val selectCar = CacheManager.getCacheCar()
    private val paymentManager = PaymentManager

    private val bankCode by lazy {
        paymentManager.getPaymentMethod()?.paymentMethodId ?: ""
    }

    private val bankURL by lazy {
        paymentManager.getPaymentMethod()?.paymentURL ?: ""
    }

    private val callBankDesc by lazy {
        paymentManager.getPaymentMethod()?.callbankDesc ?: ""
    }

    private val contractNumber by lazy {
        selectCar?.contractNumber ?: ""
    }

    private val amount by lazy {
        paymentManager.getPaymentMethod()?.amount ?: ""
    }

    private val description by lazy {
        paymentManager.getPaymentMethod()?.description ?: ""
    }

    fun getData() {
        apiManager.setPreparePaymentProcess(getPreparePaymentProcessRequest()) { isError, result ->
            if (isError) {
                whenFailure.postValue("SetPreparePaymentProcessResponse Failure.")
                return@setPreparePaymentProcess
            }

            val item = jsonMapperManager
                    .gson.fromJson(result, SetPreparePaymentProcessResponse::class.java)

            saveSequenceIdAfterSetPreparePayment(item.sEQID ?: "")


            val tltBankRequest = TLTBankRequest.build(
                    authorization = "Bearer ${profile.token}",
                    bankCode = bankCode,
                    seqCode =    item.sEQID ?: "",
                    contractNumber = contractNumber,
                    refCode1 = item.rEFCODE1 ?: "",
                    refCode2 = item.rEFCODE2 ?: "",
                    amount = amount,
                    description = description
            )

                val model = Model(
                        bankUrl = bankURL,
                        jsonData = JsonMapperManager.getInstance().gson.toJson(tltBankRequest),
                        token = profile.token,
                        sequenceId = item.sEQID ?: ""
                )

                if(callBankDesc.contains("MBAPI")){
                    callBankAPI(bankURL ,tltBankRequest)
                }else{
                    whenDataLoaded.postValue(model)
                }

        }
    }

    private fun getPreparePaymentProcessRequest(): SetPreparePaymentProcessRequest {
        return SetPreparePaymentProcessRequest.build(
                type = description,
                contractNumber = contractNumber,
                items = listOf()
        )
    }

    fun cancelTransaction() {
        val request = SetUndoProcessRequest.build(whenDataLoaded.value?.sequenceId ?: "")

        apiManager.setUndoPaymentProcess(request) { isError, result ->
            if (isError) {
                return@setUndoPaymentProcess
            }
        }
    }

    private fun saveSequenceIdAfterSetPreparePayment(sequenceId: String) {
        if (sequenceId.isEmpty()) {
            return
        }

        //DatabaseManager.getInstance().deleteSequenceIdList()

        //DatabaseManager.getInstance().save(SequenceTransactionEntity::class.java, sequenceList)
    }


    fun callBankAPI(url : String ,  tltBankRequest  : TLTBankRequest) {
            whenLoading.postValue(true)
            val accessToken = UserManager.getInstance().getProfile().token
            val jsonBank = JsonMapperManager.getInstance().gson.toJson(tltBankRequest)
            val client = OkHttpClient()
            val mediaType = MediaType.parse("application/json")
            val body = RequestBody.create(mediaType, jsonBank)
            val request = Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Content-Type", "text/xml")
                    .addHeader("Authorization", "Bearer $accessToken")
                    .build()


            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    whenLoading.postValue(false)
                    whenMbankDataLoaded.postValue("")

                }

                override fun onResponse(call: Call, response: Response) {
                    try{
                        whenLoading.postValue(false)
                        val res = JsonMapperManager.getInstance().gson.fromJson(response.body()?.string(), SuccessResponse::class.java)
                        Log.d("Call T-wallet", res.toString())
                        whenMbankDataLoaded.postValue(res.wsMsg.result )
                    }catch (e : Exception){
                        whenMbankDataLoaded.postValue("")
                    }
                }
            })


    }






    data class Model(
            val bankUrl: String = "",
            val jsonData: String = "",
            val token: String = "",
            val sequenceId: String = "",
            val isStaffApp: Boolean = AppUtils.isStaffApp()
    )
}