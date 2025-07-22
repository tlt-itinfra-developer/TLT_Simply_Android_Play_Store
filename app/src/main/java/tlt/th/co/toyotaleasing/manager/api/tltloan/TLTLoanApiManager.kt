package tlt.th.co.toyotaleasing.manager.api.tltloan

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import tlt.th.co.toyotaleasing.BuildConfig
import tlt.th.co.toyotaleasing.manager.ContextManager
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiRequestInterceptor
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiService
import tlt.th.co.toyotaleasing.model.request.*
import java.util.concurrent.TimeUnit

class TLTLoanApiManager private constructor() {

    private val service: TLTApiService
    private val serviceNDID: TLTApiService

    init {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
                .addInterceptor(TLTApiRequestInterceptor())
                .addInterceptor(loggingInterceptor)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL_LOAN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

        val retrofitNDID = Retrofit.Builder()
                .baseUrl(BuildConfig.NDID_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

        service = retrofit.create(TLTApiService::class.java)
        serviceNDID = retrofitNDID.create(TLTApiService::class.java)
    }


    //    Online Application 07/01/2020 Siri


    fun getIdentity(request: CheckIdentityRequest,
                    callback: (isError: Boolean, result: String , step : String , msg : String) -> Unit) {
        service.checkIdentiyity(request).enqueue(commonCallback(callback))
    }

    fun checkOnlineRegis(request: CheckOnlineRegisRequest,
                    callback: (isError: Boolean, result: String , step : String, msg : String ) -> Unit) {
        service.checkOnlineRegis(request).enqueue(commonCallback(callback))
    }

    fun sendOnlineOTP(request: SendOnlineOTPRequest, callback: (isError: Boolean, result: String , step : String, msg : String ) -> Unit) {
        service.sendOnlineOTP(request).enqueue(commonCallback(callback))
    }

    fun verifyOnlineOTP(request: VerifyOnlineOTPRequest, callback: (isError: Boolean, result: String , step : String , msg : String) -> Unit) {
        service.verifyOnlineOTP(request).enqueue(commonCallback(callback))
    }

    fun mainScreen(request: GetCarsLoanRequest, callback: (isError: Boolean, result: String , step : String , msg : String) -> Unit) {
        service.mainScreen(request).enqueue(commonCallback(callback))
    }

    fun cancelCarsLoan(request: CancelCarRequest, callback: (isError: Boolean, result: String , step : String , msg : String) -> Unit) {
        service.cancelCarsLoan(request).enqueue(commonCallback(callback))
    }

    fun checkStep(request: CheckStepRequest, callback: (isError: Boolean, result: String , step : String , msg : String) -> Unit) {
        service.checkStep(request).enqueue(commonCallback(callback))
    }

    fun docSumUploaded(request: GetDocSumUploadedRequest, callback: (isError: Boolean, result: String , step : String , msg : String) -> Unit) {
        service.docSumUploaded(request).enqueue(commonCallback(callback))
    }

    fun docUpload(request: GetDocUploadRequest, callback: (isError: Boolean, result: String , step : String , msg : String) -> Unit) {
        service.docUpload(request).enqueue(commonCallback(callback))
    }

    fun syncDocUpload(request: SyncDocUploadRequest, callback: (isError: Boolean, result: String , step : String , msg : String) -> Unit) {
        service.syncDocUpload(request).enqueue(commonCallback(callback))
    }

    fun syncDeleteDocUpload(request: SyncDeleteDocUploadRequest, callback: (isError: Boolean, result: String , step : String , msg : String) -> Unit) {
        service.syncDeleteDocUploadRequest(request).enqueue(commonCallback(callback))
    }

    fun syncPersonal(request: SyncPersonalRequest, callback: (isError: Boolean, result: String, step : String, msg : String) -> Unit) {
        service.syncPersonal(request).enqueue(commonCallback(callback))
    }

    fun personal(request: PersonalRequest, callback: (isError: Boolean, result: String , step : String , msg : String) -> Unit) {
        service.personal(request).enqueue(commonCallback(callback))
    }

    fun verify(request: GetVerifyRequest, callback: (isError: Boolean, result: String , step : String , msg : String) -> Unit) {
        service.veriy(request).enqueue(commonCallback(callback))
    }

    fun syncVerify(request: SyncVerifyRequest, callback: (isError: Boolean, result: String , step : String , msg : String) -> Unit) {
        service.syncVeriy(request).enqueue(commonCallback(callback))
    }

    fun submitDocUpload(request: SubmitDocUploadRequest, callback: (isError: Boolean, result: String , step : String , msg : String) -> Unit) {
        service.submitDocUpload(request).enqueue(commonCallback(callback))
    }

    fun ndidAcceptTermsCondition(request: NDIDAcceptTermsConditionRequest, callback: (isError: Boolean, result: String , step : String , msg : String) -> Unit) {
        service.ndidacceptTermsCondition(request).enqueue(commonCallback(callback))
    }

    fun econsentAcceptTermsCondition(request: EConsenetAcceptTermsConditionRequest, callback: (isError: Boolean, result: String , step : String , msg : String) -> Unit) {
        service.econsentAcceptTermsCondition(request).enqueue(commonCallback(callback))
    }

    fun expenses(request: GetExpenseRequest, callback: (isError: Boolean, result: String , step : String , msg : String) -> Unit) {
        service.expense(request).enqueue(commonCallback(callback))
    }

    fun syncExpenses(request: SyncExpenseRequest, callback: (isError: Boolean, result: String , step : String , msg : String) -> Unit) {
        service.syncExpense(request).enqueue(commonCallback(callback))
    }

    fun waitNDID(request: GetWaitNDIDRequest, callback: (isError: Boolean, result: String , step : String , msg : String) -> Unit) {
        service.waitNDID(request).enqueue(commonCallback(callback))
    }

    fun getListIDP( request: GetListIDPRequest, callback: (isError: Boolean, result: String , step : String , msg : String) -> Unit) {
        serviceNDID.getListIDP( request ).enqueue(commonCallback(callback))
    }

    fun requestNDID( request: NDIDRequest, callback: (isError: Boolean, result: String , step : String , msg : String) -> Unit) {
        serviceNDID.requestNDID( request ).enqueue(commonCallback(callback))
    }

    fun syncExpensesDealer(request: SyncExpenseDealerRequest, callback: (isError: Boolean, result: String , step : String , msg : String) -> Unit) {
        service.syncExpenseDealer(request).enqueue(commonCallback(callback))
    }

    fun decisionEngineResult(request: DecisionEngineResultRequest, callback: (isError: Boolean, result: String , step : String , msg : String) -> Unit) {
        service.decisionEngineResult(request).enqueue(commonCallback(callback))
    }


    fun cancelDecisionEngine(request: CancelDecisionEngineRequest, callback: (isError: Boolean, result: String , step : String , msg : String) -> Unit) {
        service.cancelDecisionEngine(request).enqueue(commonCallback(callback))
    }

    fun syncDecisionEngine(request: SyncDecisionEngineRequest, callback: (isError: Boolean, result: String , step : String , msg : String) -> Unit) {
        service.syncDecisionEngine(request).enqueue(commonCallback(callback))
    }

    fun syncIdCard(request: SyncIdCardRequest, callback: (isError: Boolean, result: String , step : String , msg : String) -> Unit) {
        service.syncIdCard(request).enqueue(commonCallback(callback))
    }

    fun syncLiveness(request: SyncLivenessRequest, callback: (isError: Boolean, result: String, step : String, msg : String) -> Unit) {
        service.syncLiveness(request).enqueue(commonCallback(callback))
    }

    fun fillInfo(request: FillInfoRequest, callback: (isError: Boolean, result: String , step : String , msg : String) -> Unit) {
        service.fillInfo(request).enqueue(commonCallback(callback))
    }

    fun syncInfo(request: SyncInfoRequest, callback: (isError: Boolean, result: String , step : String , msg : String) -> Unit) {
        service.syncInfo(request).enqueue(commonCallback(callback))
    }

    fun syncCancelEContract(request: EContractRequest, callback: (isError: Boolean, result: String , step : String , msg : String) -> Unit) {
        service.syncCancelEContract(request).enqueue(commonCallback(callback))
    }

    fun SyncSubmitEContract(request: EContractRequest, callback: (isError: Boolean, result: String , step : String , msg : String) -> Unit) {
        service.syncSubmitEContract(request).enqueue(commonCallback(callback))
    }

    fun waitNDIDEContract(request: GetWaitNDIDRequest, callback: (isError: Boolean, result: String , step : String , msg : String) -> Unit) {
        service.waitNDIDEContract(request).enqueue(commonCallback(callback))
    }

    fun getListIDPEContract( request: GetListIDPRequest, callback: (isError: Boolean, result: String , step : String , msg : String) -> Unit) {
        serviceNDID.getListIDPEContract( request ).enqueue(commonCallback(callback))
    }

    fun requestNDIDEContract( request: NDIDRequest, callback: (isError: Boolean, result: String , step : String , msg : String) -> Unit) {
        serviceNDID.requestNDIDEContract( request ).enqueue(commonCallback(callback))
    }

    fun getEContract(request: EContractRequest, callback: (isError: Boolean, result: String , step : String , msg : String) -> Unit) {
        service.getEContract(request).enqueue(commonCallback(callback))
    }

    fun decisionEngine(request: DecisionEngineResultRequest, callback: (isError: Boolean, result: String , step : String , msg : String) -> Unit) {
        service.decisionEngine(request).enqueue(commonCallback(callback))
    }


    private fun commonCallback(callback: (isError: Boolean, result: String , step : String , msg : String) -> Unit,
                               isErrorInSomeCase: (result: String) -> Boolean = { false }): TLTLoanApiCallback<String> {
        return object : TLTLoanApiCallback<String>() {
            override fun onSuccess(jsonResult: String , step : String , msg : String) {
                val isError = isErrorInSomeCase(jsonResult)
                callback(isError, jsonResult , step , msg)
            }

            override fun onFailure(message: String,   msg : String , msisWorkable: Boolean) {
                callback(true, message , "" , msg)
            }
        }
    }


    companion object {
        private val httpManagerInstance = TLTLoanApiManager()
        fun getInstance() = httpManagerInstance
    }
}