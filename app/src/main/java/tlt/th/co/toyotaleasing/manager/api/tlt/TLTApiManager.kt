package tlt.th.co.toyotaleasing.manager.api.tlt

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import tlt.th.co.toyotaleasing.BuildConfig
import tlt.th.co.toyotaleasing.manager.ContextManager
import tlt.th.co.toyotaleasing.model.request.*
import java.util.concurrent.TimeUnit

class TLTApiManager private constructor() {

    private val service: TLTApiService
    private val serviceLocalhost: TLTApiService

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
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

        /**
         * Localhost service
         */
        val localhostClient = OkHttpClient.Builder()
                .addInterceptor(TLTApiRequestInterceptor())
                .addInterceptor(loggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()

        val localhostRetrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(localhostClient)
                .build()

        serviceLocalhost = localhostRetrofit.create(TLTApiService::class.java)
        service = retrofit.create(TLTApiService::class.java)
    }

    fun registerNonCustomer(request: RegisterNonCustomerRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.registerNonCustomer(request).enqueue(commonCallback(callback))
    }

    fun getBanners(request: BannerRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.getBanners(request).enqueue(commonCallback(callback))
    }

    fun custRegisLoad(callback: (isError: Boolean, result: String) -> Unit) {
        service.customerRegisLoad().enqueue(commonCallback(callback))
    }

    fun sendEmailRegister(request: SendEmailRegisterRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.sendEmailRegister(request).enqueue(commonCallback(callback))
    }

    fun checkVerifyEmail(callback: (isError: Boolean, result: String) -> Unit) {
        val additional = commonCallback(
                callback = callback,
                isErrorInSomeCase = { result -> result != "verify success" }
        )

        service.checkVerifyEmail().enqueue(additional)
    }

    fun acceptTermCondition(request: AcceptTermConditionRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.acceptTermCondition(request).enqueue(commonCallback(callback))
    }

    fun beforeRegister(request: BeforeRegisterRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.beforeRegister(request).enqueue(commonCallback(callback))
    }

    fun getPhonenumber(request: GetPhonenumberRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.getPhonenumber(request).enqueue(commonCallback(callback))
    }

    fun sendOTPRegister(request: SendOTPRegisRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.sendOTPRegis(request).enqueue(commonCallback(callback))
    }

    fun verifyOTPRegister(request: VerifyOTPRegisRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.verifyOTPRegis(request).enqueue(commonCallback(callback))
    }

    fun setPinRegister(request: SetPINRegisRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.setPINRegis(request).enqueue(commonCallback(callback))
    }

    fun setTouchID(request: SetTouchIDRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.setTouchID(request).enqueue(commonCallback(callback))
    }

    fun socialRegister(registerCustomerRequest: SocialRegisterRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.socialRegister(registerCustomerRequest).enqueue(commonCallback(callback))
    }

    fun getDataInstallment(request: GetDataInstallmentRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.getDataInstallment(request).enqueue(commonCallback(callback))
    }

    fun getInstallmentDetail(request: GetInstallmentDetailRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.getInstallmentDetail(request).enqueue(commonCallback(callback))
    }

    fun loginByCustomer(request: LoginByCustomerRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.loginByCustomer(request).enqueue(commonCallback(callback))
    }

    fun getMasterData(request: GetMasterDataRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.getMasterData(request).enqueue(commonCallback(callback))
    }

    fun updateMasterData(request: UpdateMasterDataRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.updateMasterData(request).enqueue(commonCallback(callback))
    }

    fun getDataRegNumber(callback: (isError: Boolean, result: String) -> Unit) {
        service.getDataRegNumber().enqueue(commonCallback(callback))
    }

    fun getDataRegNumberLocalhost(request: GetDataRegNumberMock, callback: (isError: Boolean, result: String) -> Unit) {
        serviceLocalhost.getDataRegNumberMockData(request).enqueue(commonCallback(callback))
    }

    fun getDataTax(request: GetDataTaxRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.getDataTax(request).enqueue(commonCallback(callback))
    }

    fun getDataTaxLocalhost(request: GetDataTaxRequest, callback: (isError: Boolean, result: String) -> Unit) {
        serviceLocalhost.getDataTaxMockData(request).enqueue(commonCallback(callback))
    }

    fun getTracking(request: GetTrackingRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.getTracking(request).enqueue(commonCallback(callback))
    }

    fun forgotPincode(request: ForgotPincodeRequest, callback: (isError: Boolean, result: String) -> Unit) {
        val additional = commonCallback(
                callback = callback,
                isErrorInSomeCase = { result -> result == "Data not found." }
        )

        service.forgotPincode(request).enqueue(additional)
    }

    fun changeProfile(request: ChangeProfileRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.changeProfile(request).enqueue(commonCallback(callback))
    }

    fun getDataProfile(callback: (isError: Boolean, result: String) -> Unit) {
        service.getDataProfile().enqueue(commonCallback(callback))
    }

    fun blockNotify(request: BlockNotifyRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.blockNotify(request).enqueue(commonCallback(callback))
    }

    fun requestQuotation(request: RequestQuotationRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.requestQuotation(request).enqueue(commonCallback(callback))
    }

    fun getDataInsurance(request: GetDataInsuranceRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.getDataInsurance(request).enqueue(commonCallback(callback))
    }

    fun getDataInsuranceLocalhost(request: GetDataInsuranceRequest, callback: (isError: Boolean, result: String) -> Unit) {
        serviceLocalhost.getDataInsuranceMock(request).enqueue(commonCallback(callback))
    }

    fun getDataTypeInsurance(request: GetDataTypeInsuranceRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.getDataTypeInsurance(request).enqueue(commonCallback(callback))
    }

    fun getTIBClubDetail(request: GetTIBClubDetailRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.getTIBClubDetail(request).enqueue(commonCallback(callback))
    }

    fun sendTIBCustomerAsk(request: SendTIBCustomerAskRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.sendTIBCustomerAsk(request).enqueue(commonCallback(callback))
    }

    fun uploadImages(request: UploadImagesRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.uploadImages(request).enqueue(commonCallback(callback))
    }

    fun getImageUpload(request: GetImageUploadRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.getImageUpload(request).enqueue(commonCallback(callback))
    }

    fun getDataInsurancePayDetail(request: GetDataPayDetailRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.getDataInsurancePayDetail(request).enqueue(commonCallback(callback))
    }

    fun getDataInstallmentPayDetail(request: GetDataPayDetailRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.getDataInstallmentPayDetail(request).enqueue(commonCallback(callback))
    }

    fun getDataTaxPayDetail(request: GetDataPayDetailRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.getDataTaxPayDetail(request).enqueue(commonCallback(callback))
    }

    fun setPreparePaymentProcess(request: SetPreparePaymentProcessRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.setPreparePaymentProcess(request).enqueue(commonCallback(callback))
    }

    fun setUndoPaymentProcess(request: SetUndoProcessRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.setUndoPaymentProcess(request).enqueue(commonCallback(callback))
    }

    fun getPaymentHistory(request: GetPaymentHistoryRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.getPaymentHistory(request).enqueue(commonCallback(callback))
    }

    fun getReceiptDocument(request: GetReceiptDocumentRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.getReceiptDocument(request).enqueue(commonCallback(callback))
    }

    fun getNews(request: GetNewsRequest,
                callback: (isError: Boolean, result: String) -> Unit) {
        service.getNews(request).enqueue(commonCallback(callback))
    }

    fun updateBookmark(request: UpdateBookmarkRequest,
                       callback: (isError: Boolean, result: String) -> Unit) {
        service.updateBookmark(request).enqueue(commonCallback(callback))
    }

    fun sendContactAsk(request: SendContactAskRequest,
                       callback: (isError: Boolean, result: String) -> Unit) {
        service.sendContactAsk(request).enqueue(commonCallback(callback))
    }

    fun updateLanguage(request: SettingLanguageRequest,
                       callback: (isError: Boolean, result: String) -> Unit) {
        service.updateLanguage(request).enqueue(commonCallback(callback))
    }

    fun requestRefinance(request: RequestRefinanceRequest,
                         callback: (isError: Boolean, result: String) -> Unit) {
        service.requestRefinance(request).enqueue(commonCallback(callback))
    }

    fun settingApplication(request: SettingApplicationRequest,
                           callback: (isError: Boolean, result: String) -> Unit) {
        service.settingApplication(request).enqueue(commonCallback(callback))
    }

    fun changePincode(request: ChangePincodeRequest,
                      callback: (isError: Boolean, result: String) -> Unit) {
        service.changePincode(request).enqueue(commonCallback(callback))
    }

    fun getPrivilege(request: GetPrivilegeRequest,
                     callback: (isError: Boolean, result: String) -> Unit) {
        service.getPrivilege(request).enqueue(commonCallback(callback))
    }

    fun updateTaxYear(request: SetUpdateTaxYearRequest,
                      callback: (isError: Boolean, result: String) -> Unit) {
        service.setUpdateTaxYear(request).enqueue(commonCallback(callback))
    }

    fun logoutByCustomer(callback: (isError: Boolean, result: String) -> Unit) {
        service.logoutByCustomer().enqueue(commonCallback(callback))
    }

    fun checkStatusFromBank(request: CheckStatusFromBankRequest,
                            callback: (isError: Boolean, result: String) -> Unit) {
        service.checkStatusFromBank(request).enqueue(commonCallback(callback))
    }

    fun set2LogManual(request: Set2LogManualRequest,
                      callback: (isError: Boolean, result: String) -> Unit) {
        service.set2LogManual(request).enqueue(commonCallback(callback))
    }

    fun checkApplicationStatus(request: CheckApplicationStatusRequest,
                               callback: (isError: Boolean, result: String) -> Unit) {
        service.checkApplicationStatus(request).enqueue(commonCallback(callback))
    }

    fun getAnnoucement(request: GetAnnoucementRequest,
                       callback: (isError: Boolean, result: String) -> Unit) {
        service.getAnnoucement(request).enqueue(commonCallback(callback))
    }


    fun checkedAnnoucement(request: CheckedAnnoucementRequest,
                           callback: (isError: Boolean, result: String) -> Unit) {
        service.checkedAnnoucementRequest(request).enqueue(commonCallback(callback))
    }

    fun updateToken(request: UpdateTokenRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.updateToken(request).enqueue(commonCallback(callback))
    }


    fun getTransferOwnershipTracking(request: GetTransferOwnershipTrackingRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.getTransferOwnershipTracking(request).enqueue(commonCallback(callback))
    }

    fun getChatBot(callback: (isError: Boolean, result: String) -> Unit) {
        service.getChatBot().enqueue(commonCallback(callback))
    }

    //    Online Application 07/01/2020 Siri

    private fun commonCallback(callback: (isError: Boolean, result: String) -> Unit,
                               isErrorInSomeCase: (result: String) -> Boolean = { false }): TLTApiCallback<String> {
        return object : TLTApiCallback<String>() {
            override fun onSuccess(jsonResult: String) {
                val isError = isErrorInSomeCase(jsonResult)
                callback(isError, jsonResult)
            }

            override fun onFailure(message: String, isWorkable: Boolean) {
                callback(true, message)
            }
        }
    }

    /**
     * APIs for TLT Staff App
     */
    fun registerNonStaff(request: RegisterNonCustomerRequest, callback: (isError: Boolean, result: String) -> Unit) {
        service.registerNonStaff(request).enqueue(commonCallback(callback))
    }

    companion object {
        private val httpManagerInstance = TLTApiManager()
        fun getInstance() = httpManagerInstance
    }
}