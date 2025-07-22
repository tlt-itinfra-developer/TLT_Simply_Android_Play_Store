package tlt.th.co.toyotaleasing.manager.api.tlt

import retrofit2.Call
import tlt.th.co.toyotaleasing.model.request.*
import retrofit2.http.*


interface TLTApiService {

    @POST("Application/RegisNonCustomer")
    fun registerNonCustomer(@Body request: RegisterNonCustomerRequest): Call<String>

    @POST("Application/UpdateToken")
    fun updateToken(@Body request: UpdateTokenRequest): Call<String>

    @POST("Application/getBanner")
    fun getBanners(@Body request: BannerRequest): Call<String>

    @POST("Application/CustRegisLoad")
    fun customerRegisLoad(): Call<String>

    @POST("Application/SocialRegister")
    fun socialRegister(@Body request: SocialRegisterRequest): Call<String>

    @POST("Application/SendEmailRegis")
    fun sendEmailRegister(@Body request: SendEmailRegisterRequest): Call<String>

    @POST("Application/CheckVerifyEmail")
    fun checkVerifyEmail(): Call<String>

    @POST("Application/AcceptTermCondition")
    fun acceptTermCondition(@Body request: AcceptTermConditionRequest): Call<String>

    @POST("Application/BeforeRegister")
    fun beforeRegister(@Body request: BeforeRegisterRequest): Call<String>

    @POST("Application/GetPhonenumber")
    fun getPhonenumber(@Body request: GetPhonenumberRequest): Call<String>

    @POST("Application/SendOTPRegis")
    fun sendOTPRegis(@Body request: SendOTPRegisRequest): Call<String>

    @POST("Application/VerifyOTPRegis")
    fun verifyOTPRegis(@Body request: VerifyOTPRegisRequest): Call<String>

    @POST("Application/SetPINRegis")
    fun setPINRegis(@Body request: SetPINRegisRequest): Call<String>

    @POST("Application/SetTouchID")
    fun setTouchID(@Body request: SetTouchIDRequest): Call<String>

    @POST("Application/LoginByCustomer")
    fun loginByCustomer(@Body request: LoginByCustomerRequest): Call<String>

    @POST("Application/GetDataInstallment")
    fun getDataInstallment(@Body request: GetDataInstallmentRequest): Call<String>

    @POST("Application/GetInstallmentDetail")
    fun getInstallmentDetail(@Body request: GetInstallmentDetailRequest): Call<String>

    @POST("Application/CheckMasterLoad")
    fun getMasterData(@Body request: GetMasterDataRequest): Call<String>

    @POST("Application/UpdateMasterLoad")
    fun updateMasterData(@Body request: UpdateMasterDataRequest): Call<String>

    @POST("Application/GetDataRegNumber")
    fun getDataRegNumber(): Call<String>

    @POST("api/application/GetDataRegNumber")
    fun getDataRegNumberMockData(@Body request: GetDataRegNumberMock): Call<String>

    @POST("Application/GetDataTax")
    fun getDataTax(@Body request: GetDataTaxRequest): Call<String>

    @POST("api/tax/getDataTax")
    fun getDataTaxMockData(@Body request: GetDataTaxRequest): Call<String>

    @POST("Application/GetTracking")
    fun getTracking(@Body request: GetTrackingRequest): Call<String>

    @POST("Application/ChangePIN")
    fun changePincode(@Body request: ChangePincodeRequest): Call<String>

    @POST("Application/ForgotPINCode")
    fun forgotPincode(@Body request: ForgotPincodeRequest): Call<String>

    @POST("Application/ChangeProfile")
    fun changeProfile(@Body request: ChangeProfileRequest): Call<String>

    @POST("Application/GetDataProfile")
    fun getDataProfile(): Call<String>

    @POST("Application/BlockNotify")
    fun blockNotify(@Body request: BlockNotifyRequest): Call<String>

    @POST("Insurance/RequestQuotation")
    fun requestQuotation(@Body request: RequestQuotationRequest): Call<String>

    @POST("Insurance/GetDataInsurance")
    fun getDataInsurance(@Body request: GetDataInsuranceRequest): Call<String>

    @POST("api/insurance/getDataInsurance")
    fun getDataInsuranceMock(@Body request: GetDataInsuranceRequest): Call<String>

    @POST("Insurance/GetDataTypeInsurance")
    fun getDataTypeInsurance(@Body request: GetDataTypeInsuranceRequest): Call<String>

    @POST("Insurance/SetTIBClubDetail")
    fun getTIBClubDetail(@Body request: GetTIBClubDetailRequest): Call<String>

    @POST("Insurance/SendTIBCustomerAsk")
    fun sendTIBCustomerAsk(@Body request: SendTIBCustomerAskRequest): Call<String>

    @POST("Application/UploadImages")
    fun uploadImages(@Body request: UploadImagesRequest): Call<String>

    @POST("Application/GetImageUpload")
    fun getImageUpload(@Body request: GetImageUploadRequest): Call<String>

    @POST("Insurance/GetDataInsuranceDetail")
    fun getDataInsurancePayDetail(@Body request: GetDataPayDetailRequest): Call<String>

    @POST("PaymentProcess/GetDataTaxPayDetail")
    fun getDataTaxPayDetail(@Body request: GetDataPayDetailRequest): Call<String>

    @POST("PaymentProcess/GetDataInstallmentPayDetail")
    fun getDataInstallmentPayDetail(@Body request: GetDataPayDetailRequest): Call<String>

    @POST("PaymentProcess/SetPreparePaymentProcess")
    fun setPreparePaymentProcess(@Body request: SetPreparePaymentProcessRequest): Call<String>

    @POST("PaymentProcess/SetUndoProcess")
    fun setUndoPaymentProcess(@Body request: SetUndoProcessRequest): Call<String>

    @POST("PaymentHis/GetPaymentHis")
    fun getPaymentHistory(@Body request: GetPaymentHistoryRequest): Call<String>

    @POST("PaymentHis/GetReceiptDocument")
    fun getReceiptDocument(@Body request: GetReceiptDocumentRequest): Call<String>

    @POST("Application/GetNews")
    fun getNews(@Body request: GetNewsRequest): Call<String>

    @POST("Application/UpdateBookmark")
    fun updateBookmark(@Body request: UpdateBookmarkRequest): Call<String>

    @POST("Application/SendContactAsk")
    fun sendContactAsk(@Body request: SendContactAskRequest): Call<String>

    @POST("Application/SettingLanguage")
    fun updateLanguage(@Body request: SettingLanguageRequest): Call<String>

    @POST("Application/RequestRefinance")
    fun requestRefinance(@Body request: RequestRefinanceRequest): Call<String>

    @POST("Application/SettingApplication")
    fun settingApplication(@Body request: SettingApplicationRequest): Call<String>

    @POST("Application/GetPrivilege")
    fun getPrivilege(@Body request: GetPrivilegeRequest): Call<String>

    @POST("Application/SetUpdateTaxYear")
    fun setUpdateTaxYear(@Body request: SetUpdateTaxYearRequest): Call<String>

    @POST("Application/logoutByCustomer")
    fun logoutByCustomer(): Call<String>

    @POST("PaymentProcess/CheckStatusFromBank")
    fun checkStatusFromBank(@Body request: CheckStatusFromBankRequest): Call<String>

    @POST("Logmanual/Set2LogManual")
    fun set2LogManual(@Body request: Set2LogManualRequest): Call<String>

    @POST("Application/CheckApplicationStatus")
    fun checkApplicationStatus(@Body request: CheckApplicationStatusRequest): Call<String>

    @POST("Application/CheckedAnnoucement")
    fun checkedAnnoucementRequest(@Body request: CheckedAnnoucementRequest): Call<String>

    @POST("Application/GetAnnoucement")
    fun getAnnoucement(@Body request: GetAnnoucementRequest): Call<String>


    /**
     * APIs End point for TLT Staff application.
     */
    @POST("Application/RegisNonStaff")
    fun registerNonStaff(@Body request: RegisterNonCustomerRequest): Call<String>


    @POST("Transfer/GetTransferOwnershipTracking")
    fun getTransferOwnershipTracking(@Body request: GetTransferOwnershipTrackingRequest): Call<String>

    //    Online Application 07/01/2020 Siri

    @POST("application/CheckIdentity")
    fun checkIdentiyity(@Body request: CheckIdentityRequest): Call<String>

    @POST("application/CheckOnlineRegis")
    fun checkOnlineRegis(@Body request: CheckOnlineRegisRequest): Call<String>

    @POST("application/sendOnlineOTP")
    fun sendOnlineOTP(@Body request: SendOnlineOTPRequest): Call<String>

    @POST("application/VerifyOnlineOTP")
    fun verifyOnlineOTP(@Body request: VerifyOnlineOTPRequest): Call<String>

    @POST("application/MainScreen")
    fun mainScreen(@Body request: GetCarsLoanRequest): Call<String>

    @POST("application/CancelCar")
    fun cancelCarsLoan(@Body request: CancelCarRequest): Call<String>

    @POST("application/CheckStep")
    fun checkStep(@Body request: CheckStepRequest): Call<String>

    @POST("Information/DocSumUploaded")
    fun docSumUploaded(@Body request: GetDocSumUploadedRequest): Call<String>

    @POST("Information/DocUpload")
    fun docUpload(@Body request: GetDocUploadRequest): Call<String>

    @POST("Information/SyncDocUpload")
    fun syncDocUpload(@Body request: SyncDocUploadRequest): Call<String>

    @POST("Information/IDocUpload")
    fun syncDeleteDocUploadRequest(@Body request: SyncDeleteDocUploadRequest): Call<String>

    @POST("Information/SyncPersonal")
    fun syncPersonal(@Body request: SyncPersonalRequest): Call<String>

    @POST("Information/Personal")
    fun personal(@Body request: PersonalRequest): Call<String>

    @POST("Information/Verify")
    fun veriy(@Body request: GetVerifyRequest): Call<String>

    @POST("Information/DecisionEngineResult")
    fun decisionEngineResult(@Body request: DecisionEngineResultRequest): Call<String>

    @POST("Information/DecisionEngine")
    fun decisionEngine(@Body request: DecisionEngineResultRequest): Call<String>

    @POST("Information/SyncVerify")
    fun syncVeriy(@Body request: SyncVerifyRequest): Call<String>

    @POST("Information/SubmitDocUpload")
    fun submitDocUpload(@Body request: SubmitDocUploadRequest): Call<String>

    @POST("Information/AcceptTermsCondition")
    fun ndidacceptTermsCondition(@Body request: NDIDAcceptTermsConditionRequest): Call<String>

    @POST("Information/AcceptTermsCondition")
    fun econsentAcceptTermsCondition(@Body request: EConsenetAcceptTermsConditionRequest): Call<String>

    @POST("Information/Expenses")
    fun expense(@Body request: GetExpenseRequest): Call<String>

    @POST("Information/SyncExpenses")
    fun syncExpense(@Body request: SyncExpenseRequest): Call<String>

    @POST("Information/WaitNDID")
    fun waitNDID(@Body request: GetWaitNDIDRequest): Call<String>


    @POST("Information/CancelDecisionEngine")
    fun cancelDecisionEngine(@Body request: CancelDecisionEngineRequest): Call<String>

    @POST("Information/SyncDecisionEngine")
    fun syncDecisionEngine(@Body request: SyncDecisionEngineRequest): Call<String>


//    @GET("utility/GetListIDP/{id}/{token}")
//    fun getListIDP(@Path("id") id: String,
//                   @Path("token") token : String ): Call<String>

     @POST("utility/GetListIDP")
    fun getListIDP(@Body request: GetListIDPRequest): Call<String>


    @POST("rp/RequestsNDID")
    fun requestNDID(@Body request: NDIDRequest): Call<String>


    @POST("Information/SyncExpensesDealer")
    fun syncExpenseDealer(@Body request: SyncExpenseDealerRequest): Call<String>


    //     Waiting API
    @POST("Information/SyncIdCard")
    fun syncIdCard(@Body request: SyncIdCardRequest): Call<String>

    @POST("Information/SyncSelfie")
    fun syncLiveness(@Body request: SyncLivenessRequest): Call<String>

    @POST("Information/FillInfo")
    fun fillInfo(@Body request: FillInfoRequest): Call<String>

    @POST("Information/SyncInfo")
    fun syncInfo(@Body request: SyncInfoRequest): Call<String>

    @POST("Information/SyncSubmitEContract")
    fun syncSubmitEContract(@Body request: EContractRequest): Call<String>

    @POST("Information/SyncCancelEContract")
    fun syncCancelEContract(@Body request: EContractRequest): Call<String>

    @POST("Information/WaitNDIDEContract")
    fun waitNDIDEContract(@Body request: GetWaitNDIDRequest): Call<String>

    @POST("utility/GetListIDP")
    fun getListIDPEContract(@Body request: GetListIDPRequest): Call<String>


    @POST("rp/RequestsEcontract")
    fun requestNDIDEContract(@Body request: NDIDRequest): Call<String>


    @POST("Information/GetEContract")
    fun getEContract(@Body request: EContractRequest): Call<String>


    @POST("Application/Chatbot")
    fun getChatBot(): Call<String>
}