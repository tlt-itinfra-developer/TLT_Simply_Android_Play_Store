package tlt.th.co.toyotaleasing.analytics

import android.os.Bundle
import java.util.*

object AnalyticsManager {
    var startTime: Date = Calendar.getInstance().time
    var endTime: Date = Calendar.getInstance().time
    var dialogStartTime: Date = Calendar.getInstance().time
    var dialogEndTime: Date = Calendar.getInstance().time
    var sideBarStartTime: Date = Calendar.getInstance().time

    fun trackScreen(screenName: String) {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, screenName)
            putBaseViewEventParam(startTime, endTime)
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun trackScreenDialog(screenName: String) {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, screenName)
            putBaseViewEventParam(dialogStartTime, dialogEndTime)
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun trackScreenSideBar(screenName: String) {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, screenName)
            putBaseViewEventParam(sideBarStartTime, Calendar.getInstance().time)
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun trackScreenError(screenName: String) {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, screenName)
            putBaseViewEventParam(Calendar.getInstance().time, Calendar.getInstance().time)
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //Home ( Register ) Fill E-mail
    fun homeBannerClicked(bannerName: String) {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.REGISTER_MAIN)
            putString(AnalyticsConstants.VALUE, bannerName)
            putString(AnalyticsConstants.LABEL, "banner")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun homeEmailClicked(email: String) {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.REGISTER_MAIN)
            putString(AnalyticsConstants.VALUE, email)
            putString(AnalyticsConstants.LABEL, "email")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun homeRegisterEmailClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.REGISTER_MAIN)
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.LABEL, "register_email")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun homeFacebookClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.REGISTER_MAIN)
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.LABEL, "facebook")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun homeGooglePlusClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.REGISTER_MAIN)
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.LABEL, "googleplus")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun homeLineClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.REGISTER_MAIN)
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.LABEL, "line")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun homeBannerSlide() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.REGISTER_MAIN)
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.LABEL, "banner")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam(action = "slide")
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun homeMenuHamburgerClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.REGISTER_MAIN)
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.LABEL, "menu_home")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //Verify E-mail success
    fun verifyEmailSuccessNext() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.VERIFY_EMAIL_SUCCESS)
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.LABEL, "next")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //Verify E-mail fail
    fun verifyEmailFailConfirm() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.VERIFY_EMAIL_FAIL)
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.LABEL, "confirm_email")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun verifyEmailFailResend() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.VERIFY_EMAIL_FAIL)
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.LABEL, "resend")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //Register term&condition
    fun termAndConditionCheckBox() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.REGISTER_TERMS_CONDITIONS)
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.LABEL, "agree")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun termAndConditionNextClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.REGISTER_TERMS_CONDITIONS)
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.LABEL, "next")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //Register privacy policy
    fun policyAgreeClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.REGISTER_PRIVACY_POLICY)
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.LABEL, "agree")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun policyDisAgreeClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.REGISTER_PRIVACY_POLICY)
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.LABEL, "not_agree")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun policyNextClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.REGISTER_PRIVACY_POLICY)
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.LABEL, "next")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //Register form activity
    fun formCustomerInformationEmail(email: String) {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.REGISTER_CUSTOMER_INFO)
            putString(AnalyticsConstants.VALUE, email)
            putString(AnalyticsConstants.LABEL, "email")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun formCustomerInformationIdPassport(idPassport: String) {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.REGISTER_CUSTOMER_INFO)
            putString(AnalyticsConstants.VALUE, idPassport)
            putString(AnalyticsConstants.LABEL, "id_passport")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun formCustomerInformationBirthDay(birthDate: String) {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.REGISTER_CUSTOMER_INFO)
            putString(AnalyticsConstants.VALUE, birthDate)
            putString(AnalyticsConstants.LABEL, "birth_date")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun formCustomerInformationNext() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.REGISTER_CUSTOMER_INFO)
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.LABEL, "next")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //Register select phone
    fun selectPhoneClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.REGISTER_SELECT_PHONE)
            putString(AnalyticsConstants.LABEL, "phone_number")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun selectPhoneContactStaffClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.REGISTER_SELECT_PHONE)
            putString(AnalyticsConstants.LABEL, "contact_staff")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //Register select phone call
    fun selectPhoneCallClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.REGISTER_SELECT_PHONE_CALL)
            putString(AnalyticsConstants.LABEL, "call")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun selectPhoneCancelClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.REGISTER_SELECT_PHONE_CALL)
            putString(AnalyticsConstants.LABEL, "cancel")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //Fill otp
    fun otpCode() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.REGISTER_FILL_OTP)
            putString(AnalyticsConstants.LABEL, "opt_code")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun otpResendCode() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.REGISTER_FILL_OTP)
            putString(AnalyticsConstants.LABEL, "resend")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //Register success
    fun registerSuccessClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.REGISTER_SUCCESS)
            putString(AnalyticsConstants.LABEL, "setpin")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //Register setup pin
    fun setPinCodeClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.REGISTER_SET_PIN)
            putString(AnalyticsConstants.LABEL, "pincode")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun setConfirmPinCodeClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.REGISTER_SET_PIN)
            putString(AnalyticsConstants.LABEL, "confirm_pincode")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun setViewPinCodeClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.REGISTER_SET_PIN)
            putString(AnalyticsConstants.LABEL, "view")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun setPinCodeConfirmButtonClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.REGISTER_SET_PIN)
            putString(AnalyticsConstants.LABEL, "confirm")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //Register set pin success
    fun pinSuccessSetTouchIdClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.REGISTER_SET_PIN_SUCCESS)
            putString(AnalyticsConstants.LABEL, "set_touchid")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun pinSuccessGoMainClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.REGISTER_SET_PIN_SUCCESS)
            putString(AnalyticsConstants.LABEL, "mainpage")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //register set touch id
    fun touchIdMainPageClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.REGISTER_SET_TOUCH_ID)
            putString(AnalyticsConstants.LABEL, "mainpage")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun touchIdScan() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.REGISTER_SET_TOUCH_ID)
            putString(AnalyticsConstants.LABEL, "finger")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam("scan")
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //Login touch id
    fun loginTouchIdCancelClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.LOGIN_TOUCH_ID)
            putString(AnalyticsConstants.LABEL, "cancel")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun loginTouchIdScan() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.LOGIN_TOUCH_ID)
            putString(AnalyticsConstants.LABEL, "finger")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam("scan")
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //Login touch id fail
    fun loginTouchIdFailCancelClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.LOGIN_TOUCH_ID_FAIL)
            putString(AnalyticsConstants.LABEL, "cancel")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //User Guide
    fun userGuideScreen1() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.GUIDE)
            putString(AnalyticsConstants.LABEL, "guide1")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun userGuideScreen2() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.GUIDE)
            putString(AnalyticsConstants.LABEL, "guide2")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun userGuideScreen3() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.GUIDE)
            putString(AnalyticsConstants.LABEL, "guide3")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //Tutorial Screen
    fun tutorialScreen1() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.TUTORIAL_PAGE_1)
            putString(AnalyticsConstants.LABEL, "skip")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun tutorialScreen2() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.TUTORIAL_PAGE_2)
            putString(AnalyticsConstants.LABEL, "skip")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun tutorialScreen3() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.TUTORIAL_PAGE_3)
            putString(AnalyticsConstants.LABEL, "next")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //Login pincode
    fun loginPincodeClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.LOGIN_PINCODE)
            putString(AnalyticsConstants.LABEL, "pincode")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun loginPincodeForgotClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.LOGIN_PINCODE)
            putString(AnalyticsConstants.LABEL, "forgot_pin")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //Main Menu
    fun mainMenuInstallmentClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAIN_MENU)
            putString(AnalyticsConstants.LABEL, "installment_mycar")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun mainMenuTaxClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAIN_MENU)
            putString(AnalyticsConstants.LABEL, "tax")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun mainMenuInsuranceClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAIN_MENU)
            putString(AnalyticsConstants.LABEL, "insurance")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun mainMenuQrClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAIN_MENU)
            putString(AnalyticsConstants.LABEL, "pay_qr_barcode")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun mainMenuLoanCalculationClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAIN_MENU)
            putString(AnalyticsConstants.LABEL, "loan_calculation")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun mainMenuNewsClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAIN_MENU)
            putString(AnalyticsConstants.LABEL, "news_promotion")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun mainMenuContactUsClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAIN_MENU)
            putString(AnalyticsConstants.LABEL, "contact_us")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun mainMenuLocationClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAIN_MENU)
            putString(AnalyticsConstants.LABEL, "location")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun mainMenuChatClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAIN_MENU)
            putString(AnalyticsConstants.LABEL, "chat")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun mainMenuDownloadDocClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAIN_MENU)
            putString(AnalyticsConstants.LABEL, "download_documents")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun mainMenuFaqClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAIN_MENU)
            putString(AnalyticsConstants.LABEL, "faq")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun mainMenuTermConditionClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAIN_MENU)
            putString(AnalyticsConstants.LABEL, "terms&con")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun mainMenuSettingClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAIN_MENU)
            putString(AnalyticsConstants.LABEL, "setting")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun mainMenuSignoutClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAIN_MENU)
            putString(AnalyticsConstants.LABEL, "sign_out")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun mainMenuCloseClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAIN_MENU)
            putString(AnalyticsConstants.LABEL, "close")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //MainMenu noncus
    fun mainMenuNonInstallmentClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAIN_MENU_NON_CUS)
            putString(AnalyticsConstants.LABEL, "installment_mycar")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun mainMenuNonTaxClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAIN_MENU_NON_CUS)
            putString(AnalyticsConstants.LABEL, "tax")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun mainMenuNonInsuranceClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAIN_MENU_NON_CUS)
            putString(AnalyticsConstants.LABEL, "insurance")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun mainMenuNonQrClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAIN_MENU_NON_CUS)
            putString(AnalyticsConstants.LABEL, "pay_qr_barcode")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun mainMenuNonLoanCalculationClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAIN_MENU_NON_CUS)
            putString(AnalyticsConstants.LABEL, "loan_calculation")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun mainMenuNonNewsClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAIN_MENU_NON_CUS)
            putString(AnalyticsConstants.LABEL, "news_promotion")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun mainMenuNonContactUsClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAIN_MENU_NON_CUS)
            putString(AnalyticsConstants.LABEL, "contact_us")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun mainMenuNonLocationClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAIN_MENU_NON_CUS)
            putString(AnalyticsConstants.LABEL, "location")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun mainMenuNonChatClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAIN_MENU_NON_CUS)
            putString(AnalyticsConstants.LABEL, "chat")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun mainMenuNonDownloadDocClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAIN_MENU_NON_CUS)
            putString(AnalyticsConstants.LABEL, "download_documents")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun mainMenuNonFaqClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAIN_MENU_NON_CUS)
            putString(AnalyticsConstants.LABEL, "faq")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun mainMenuNonTermConditionClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAIN_MENU_NON_CUS)
            putString(AnalyticsConstants.LABEL, "terms&con")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun mainMenuNonSettingClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAIN_MENU_NON_CUS)
            putString(AnalyticsConstants.LABEL, "setting")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun mainMenuNonCloseClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAIN_MENU_NON_CUS)
            putString(AnalyticsConstants.LABEL, "close")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun mainMenuNonRegisterClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAIN_MENU_NON_CUS)
            putString(AnalyticsConstants.LABEL, "register")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun mainMenuNonNotificatoinClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAIN_MENU_NON_CUS)
            putString(AnalyticsConstants.LABEL, "notification")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //popup notification
    fun popupNotifyDetail(adName: String = "") {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.PUSH_POPUP)
            putString(AnalyticsConstants.LABEL, "detail")
            putString(AnalyticsConstants.VALUE, adName)
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun popupNotifyClose() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.PUSH_POPUP)
            putString(AnalyticsConstants.LABEL, "close")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun popupNotifyCheckbox() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.PUSH_POPUP)
            putString(AnalyticsConstants.LABEL, "donnot_showagain")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //installment my car
    fun installmentMyCarMenuHomeClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_MY_CAR)
            putString(AnalyticsConstants.LABEL, "menuhome")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun installmentMyCarSelectCarClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_MY_CAR)
            putString(AnalyticsConstants.LABEL, "selectcar")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun installmentMyCarMyContractClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_MY_CAR)
            putString(AnalyticsConstants.LABEL, "mycontract")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun installmentMyCarInstallmentDetailClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_MY_CAR)
            putString(AnalyticsConstants.LABEL, "installment_detail")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun installmentMyCarCheckingPaymentClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_MY_CAR)
            putString(AnalyticsConstants.LABEL, "checkingpayment")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun installmentMyCarPaymentHistoryClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_MY_CAR)
            putString(AnalyticsConstants.LABEL, "payment_history")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun installmentMyCarNotificationClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_MY_CAR)
            putString(AnalyticsConstants.LABEL, "notification")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun installmentMyCarCallClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_MY_CAR)
            putString(AnalyticsConstants.LABEL, "call")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //installment my car select car
    fun selectCarRegistrationClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_MY_CAR_SELECT_CAR)
            putString(AnalyticsConstants.LABEL, "car_registration")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun selectCarCloseClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_MY_CAR_SELECT_CAR)
            putString(AnalyticsConstants.LABEL, "close")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //installment my contract
    fun myContractPayoffClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_MY_CONTRACT)
            putString(AnalyticsConstants.LABEL, "payoff")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun myContractRefinanceClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_MY_CONTRACT)
            putString(AnalyticsConstants.LABEL, "refinance")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun myContractCustomerAddressClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_MY_CONTRACT)
            putString(AnalyticsConstants.LABEL, "customer_address")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun myContractRecommendationClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_MY_CONTRACT)
            putString(AnalyticsConstants.LABEL, "recommendation")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun myContractDealerContractClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_MY_CONTRACT)
            putString(AnalyticsConstants.LABEL, "dealer_contact")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //installment my contract popup
    fun myContractPopupEnterClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_MY_CONTRACT_POPUP)
            putString(AnalyticsConstants.LABEL, "enter")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //installment payoff
    fun payoffInterestFinanceClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_PAYOFF)
            putString(AnalyticsConstants.LABEL, "interest_refinance")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun payoffCallClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_PAYOFF)
            putString(AnalyticsConstants.LABEL, "call")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun payoffMyContractClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_PAYOFF)
            putString(AnalyticsConstants.LABEL, "mycontract")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun payoffRefinanceClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_PAYOFF)
            putString(AnalyticsConstants.LABEL, "refinance")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //installment payoff interest
    fun payoffInterestEnterClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_PAYOFF_INTEREST)
            putString(AnalyticsConstants.LABEL, "enter")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //installment refinance
    fun refinanceMyContractClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_REFINANCE)
            putString(AnalyticsConstants.LABEL, "mycontact")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun refinancePayoffClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_REFINANCE)
            putString(AnalyticsConstants.LABEL, "payoff")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun refinanceInterestRefinanceClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_REFINANCE)
            putString(AnalyticsConstants.LABEL, "interest_refinance")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun refinanceCallClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_REFINANCE)
            putString(AnalyticsConstants.LABEL, "call")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //installment refinance interest
    fun refinanceInterestEnterClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_REFINANCE_INTEREST)
            putString(AnalyticsConstants.LABEL, "enter")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //installment my contract cahnge address
    fun addressChangeEmailTabClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_MY_CONTRACT_CHANGE_ADDRESS)
            putString(AnalyticsConstants.LABEL, "changemail")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun addressHousingNoClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_MY_CONTRACT_CHANGE_ADDRESS)
            putString(AnalyticsConstants.LABEL, "housing_no")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun addressSoiClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_MY_CONTRACT_CHANGE_ADDRESS)
            putString(AnalyticsConstants.LABEL, "soi")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun addressStreetClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_MY_CONTRACT_CHANGE_ADDRESS)
            putString(AnalyticsConstants.LABEL, "street")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun addressDistrictClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_MY_CONTRACT_CHANGE_ADDRESS)
            putString(AnalyticsConstants.LABEL, "district")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun addressCityClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_MY_CONTRACT_CHANGE_ADDRESS)
            putString(AnalyticsConstants.LABEL, "city")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun addressAmphoeClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_MY_CONTRACT_CHANGE_ADDRESS)
            putString(AnalyticsConstants.LABEL, "amphoe")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun addressPostalCodeClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_MY_CONTRACT_CHANGE_ADDRESS)
            putString(AnalyticsConstants.LABEL, "postal_code")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun addressUseAllAddressClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_MY_CONTRACT_CHANGE_ADDRESS)
            putString(AnalyticsConstants.LABEL, "use_alladdress")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam("select")
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun addressUploadClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_MY_CONTRACT_CHANGE_ADDRESS)
            putString(AnalyticsConstants.LABEL, "upload")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun addressConfirmClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_MY_CONTRACT_CHANGE_ADDRESS)
            putString(AnalyticsConstants.LABEL, "confirm")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //installment my contract change address confirm
    fun addressConfirmEnterClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_MY_CONTRACT_CHANGE_ADDRESS_CONFIRM)
            putString(AnalyticsConstants.LABEL, "enter")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //installment my contract change email
    fun emailChangeAddressTabClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_MY_CONTRACT_CHANGE_EMAIL)
            putString(AnalyticsConstants.LABEL, "changeaddress")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun emailEmailClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_MY_CONTRACT_CHANGE_EMAIL)
            putString(AnalyticsConstants.LABEL, "email")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun emailTelClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_MY_CONTRACT_CHANGE_EMAIL)
            putString(AnalyticsConstants.LABEL, "tel")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun emailConfirmClicked() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_MY_CONTRACT_CHANGE_EMAIL)
            putString(AnalyticsConstants.LABEL, "confirm")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //installment detail
    fun installmentDetailCheckingPayment() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_DETAIL)
            putString(AnalyticsConstants.LABEL, "checkingpayment")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun installmentDetailCall() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_DETAIL)
            putString(AnalyticsConstants.LABEL, "call")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //instalment payment list
    fun paymentListInstallment() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_PAYMENT_LIST)
            putString(AnalyticsConstants.LABEL, "installment")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam(action = "select")
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun paymentListLatePayment() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_PAYMENT_LIST)
            putString(AnalyticsConstants.LABEL, "late_payment")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam(action = "select")
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun paymentListAdjustPayment() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_PAYMENT_LIST)
            putString(AnalyticsConstants.LABEL, "adjust_payment")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun paymentListPayment() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_PAYMENT_LIST)
            putString(AnalyticsConstants.LABEL, "payment")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //installment adjust payment
    fun adjustPaymentInstallment() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_ADJUST_PAYMENT)
            putString(AnalyticsConstants.LABEL, "installment")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam(action = "select")
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun adjustPaymentLatePayment() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_ADJUST_PAYMENT)
            putString(AnalyticsConstants.LABEL, "late_payment")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam(action = "select")
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun adjustPaymentAddOther() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_ADJUST_PAYMENT)
            putString(AnalyticsConstants.LABEL, "add_other")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun adjustPaymentEdit() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_ADJUST_PAYMENT)
            putString(AnalyticsConstants.LABEL, "installment")
            putString(AnalyticsConstants.VALUE, "number")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam(action = "edit")
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun adjustPaymentEditLatePayment() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_ADJUST_PAYMENT)
            putString(AnalyticsConstants.LABEL, "late_payment")
            putString(AnalyticsConstants.VALUE, "number")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam(action = "edit")
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun adjustPaymentSelectOther() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_ADJUST_PAYMENT)
            putString(AnalyticsConstants.LABEL, "select_other")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun adjustPaymentOther() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_ADJUST_PAYMENT)
            putString(AnalyticsConstants.LABEL, "other")
            putString(AnalyticsConstants.VALUE, "number")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam(action = "edit")
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun adjustPaymentDelete() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_ADJUST_PAYMENT)
            putString(AnalyticsConstants.LABEL, "delete")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun adjustPaymentConfirm() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_ADJUST_PAYMENT)
            putString(AnalyticsConstants.LABEL, "confirm")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //add other list
    fun addOtherPayment() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSTALLMENT_ADD_OTHER )
            putString(AnalyticsConstants.LABEL, "payment")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //payment channel
    fun paymentChannelBank(bankName: String) {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.PAYMENT_CHANNEL)
            putString(AnalyticsConstants.LABEL, "channal_bank")
            putString(AnalyticsConstants.VALUE, bankName)
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun paymentChannelQr() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.PAYMENT_CHANNEL)
            putString(AnalyticsConstants.LABEL, "qr_barcode")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun paymentChannelPayment() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.PAYMENT_CHANNEL)
            putString(AnalyticsConstants.LABEL, "payment")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //installment payment success
    fun receiptBackMainPage() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.PAYMENT_SUCCESS)
            putString(AnalyticsConstants.LABEL, "back_mainpage")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun receiptCall() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.PAYMENT_SUCCESS)
            putString(AnalyticsConstants.LABEL, "call")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun receiptSendEmail() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.PAYMENT_SUCCESS)
            putString(AnalyticsConstants.LABEL, "send_email")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun receiptSave() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.PAYMENT_SUCCESS)
            putString(AnalyticsConstants.LABEL, "save")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //installment payment fail
    fun paymentFailMainPage() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.PAYMENT_FAIL)
            putString(AnalyticsConstants.LABEL, "back_mainpage")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //payment history
    fun historyFilter() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.PAYMENT_HISTORY)
            putString(AnalyticsConstants.LABEL, "filter")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun historyPaymentAmount() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.PAYMENT_HISTORY)
            putString(AnalyticsConstants.LABEL, "payment_amount")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun historyDownloadReceipt() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.PAYMENT_HISTORY)
            putString(AnalyticsConstants.LABEL, "download_receipt")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun historyMyCar() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.PAYMENT_HISTORY)
            putString(AnalyticsConstants.LABEL, "mycar")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun historyNotification() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.PAYMENT_HISTORY)
            putString(AnalyticsConstants.LABEL, "notification")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun historyMenuhome() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.PAYMENT_HISTORY)
            putString(AnalyticsConstants.LABEL, "menuhome")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun historySelectCar() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.PAYMENT_HISTORY)
            putString(AnalyticsConstants.LABEL, "selectcar")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun historyCall() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.PAYMENT_HISTORY)
            putString(AnalyticsConstants.LABEL, "call")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //payment history select car
    fun historySelectCarRegistration() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.PAYMENT_HISTORY_SELECT_CAR)
            putString(AnalyticsConstants.LABEL, "car_registration")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun historySelectCarClose() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.PAYMENT_HISTORY_SELECT_CAR)
            putString(AnalyticsConstants.LABEL, "close")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //payment history filter
    fun historyFilterYear() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.PAYMENT_HISTORY_FILTER)
            putString(AnalyticsConstants.LABEL, "year")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam(action = "select")
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun historyFilterDone() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.PAYMENT_HISTORY_FILTER)
            putString(AnalyticsConstants.LABEL, "done")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //Notificatioon
    fun notificationDetial() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.NOTIFICATION)
            putString(AnalyticsConstants.LABEL, "detail")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun notificationFilter() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.NOTIFICATION)
            putString(AnalyticsConstants.LABEL, "filter")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun notificationMyCar() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.NOTIFICATION)
            putString(AnalyticsConstants.LABEL, "mycar")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun notificationPaymentHistory() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.NOTIFICATION)
            putString(AnalyticsConstants.LABEL, "payment_history")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun notificationSelectCar() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.NOTIFICATION)
            putString(AnalyticsConstants.LABEL, "selectcar")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun notificationMenuHome() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.NOTIFICATION)
            putString(AnalyticsConstants.LABEL, "menuhome")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //notification filter
    fun notificationFilterFilter() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.NOTIFICATION_FILTER)
            putString(AnalyticsConstants.LABEL, "filter")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun notificationFilterEnter() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.NOTIFICATION_FILTER)
            putString(AnalyticsConstants.LABEL, "enter")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //notification select car
    fun notificationSelectCarRegistration() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.NOTIFICATION_SELECT_CAR)
            putString(AnalyticsConstants.LABEL, "car_registration")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun notificatitonSelectCarClose() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.NOTIFICATION_SELECT_CAR)
            putString(AnalyticsConstants.LABEL, "close")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //News and promotion List
    fun newsAndPromotionListNewsPromotionDetail() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.NEWS_AND_PROMOTION_LIST)
            putString(AnalyticsConstants.LABEL, "news&promotion_detail")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun newsAndPromotionListBookmark() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.NEWS_AND_PROMOTION_LIST)
            putString(AnalyticsConstants.LABEL, "bookmark")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun newsAndPromotionListMenuHome() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.NEWS_AND_PROMOTION_LIST)
            putString(AnalyticsConstants.LABEL, "menuhome")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun newsAndPromotionListNewsAndPromotionList() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.NEWS_AND_PROMOTION_LIST)
            putString(AnalyticsConstants.LABEL, "news&promotion_list")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun newsAndPromotionListNewsAndPromotionOil() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.NEWS_AND_PROMOTION_LIST)
            putString(AnalyticsConstants.LABEL, "news&promotion_oil")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun newsAndPromotionListNewsAndPromotionTraffic() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.NEWS_AND_PROMOTION_LIST)
            putString(AnalyticsConstants.LABEL, "news&promotion_traffic")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //News and promotion detail
    fun newsDetailBookmark() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.NEWS_AND_PROMOTION_DETAIL)
            putString(AnalyticsConstants.LABEL, "bookmark")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun newsDetailShare() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.NEWS_AND_PROMOTION_DETAIL)
            putString(AnalyticsConstants.LABEL, "share")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //Contact main
    fun contactMainCallCenter() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.CONTACT_MAIN)
            putString(AnalyticsConstants.LABEL, "share")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun contactMainContactQuestion() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.CONTACT_MAIN)
            putString(AnalyticsConstants.LABEL, "contact_question")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun contactMainLine() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.CONTACT_MAIN)
            putString(AnalyticsConstants.LABEL, "lineofficial")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun contactMainFacebook() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.CONTACT_MAIN)
            putString(AnalyticsConstants.LABEL, "facebookpage")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun contactMainInsurance() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.CONTACT_MAIN)
            putString(AnalyticsConstants.LABEL, "insurance_main")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun contactMainCallCenterDialogCall() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.CONTACT_MAIN)
            putString(AnalyticsConstants.LABEL, "callcenter_call")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun contactMainCallCenterDialogCancel() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.CONTACT_MAIN)
            putString(AnalyticsConstants.LABEL, "callcenter_cancel")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //Contact question
    fun contactQuestionSubmit() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.CONTACT_QUESTION)
            putString(AnalyticsConstants.LABEL, "submit")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //Map office
    fun mapOfficeSelectType() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAP_OFFICE)
            putString(AnalyticsConstants.LABEL, "select_type")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun mapOfficeMapDealer() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAP_OFFICE)
            putString(AnalyticsConstants.LABEL, "map_dealer")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun mapOfficeAllList() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAP_OFFICE)
            putString(AnalyticsConstants.LABEL, "all_list")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun mapOfficeHideList() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAP_OFFICE)
            putString(AnalyticsConstants.LABEL, "hide_list")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun mapOfficeRoute() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAP_OFFICE)
            putString(AnalyticsConstants.LABEL, "route")
            putString(AnalyticsConstants.VALUE, "google_map")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun mapOfficeMapPin(dealerName: String = "") {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAP_OFFICE)
            putString(AnalyticsConstants.LABEL, "map_pin")
            putString(AnalyticsConstants.VALUE, dealerName)
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun mapOfficeMenuhome() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAP_OFFICE)
            putString(AnalyticsConstants.LABEL, "menuhome")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //Map location setting
    fun mapLocationSettingOk() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAP_LOCATIONSETTING)
            putString(AnalyticsConstants.LABEL, "setting")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun mapLocationSettingCancel() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAP_LOCATIONSETTING)
            putString(AnalyticsConstants.LABEL, "cancel")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //Map dealer
    fun mapDealerSearch() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAP_DEALER)
            putString(AnalyticsConstants.LABEL, "search")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun mapDealerDetail() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAP_DEALER)
            putString(AnalyticsConstants.LABEL, "detail")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun mapDealerCall() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAP_DEALER)
            putString(AnalyticsConstants.LABEL, "call")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun mapDealerPin(dealerName: String = "") {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAP_DEALER)
            putString(AnalyticsConstants.LABEL, "route")
            putString(AnalyticsConstants.VALUE, dealerName)
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //map dealer detail
    fun mapDealerDetailCall() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAP_DEALER_DETAIL)
            putString(AnalyticsConstants.LABEL, "call")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun mapDealerDetailRoute() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAP_DEALER_DETAIL)
            putString(AnalyticsConstants.LABEL, "route")
            putString(AnalyticsConstants.VALUE, "google_map")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun mapDealerDetailCancel() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAP_DEALER_DETAIL)
            putString(AnalyticsConstants.LABEL, "cancel")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //map dealer search
    fun mapSearchOffice() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAP_DEALER_SEARCH)
            putString(AnalyticsConstants.LABEL, "office")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun mapSearchShowroom() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAP_DEALER_SEARCH)
            putString(AnalyticsConstants.LABEL, "showroom")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun mapSearchServiceCenter() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAP_DEALER_SEARCH)
            putString(AnalyticsConstants.LABEL, "servicecenter")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun mapSearchBodyPaintCenter() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAP_DEALER_SEARCH)
            putString(AnalyticsConstants.LABEL, "bodypaintcenter")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun mapSearchNearby() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAP_DEALER_SEARCH)
            putString(AnalyticsConstants.LABEL, "nearby")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun mapSearchSearch() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAP_DEALER_SEARCH)
            putString(AnalyticsConstants.LABEL, "search")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //map dealer no search
    fun mapSearchBackToSearch() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAP_DEALER_NO_SEARCH)
            putString(AnalyticsConstants.LABEL, "back_to_search")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //download document
    fun downloadDocumentName() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.DOWNLOAD_DOC)
            putString(AnalyticsConstants.LABEL, "doc_name")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun downloadDocumentMenuHome() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.DOWNLOAD_DOC)
            putString(AnalyticsConstants.LABEL, "manuhome")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //FAQ
    fun faqTopic() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.FAQ_TOPIC)
            putString(AnalyticsConstants.LABEL, "manuhome")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun faqMenuHome() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.FAQ_TOPIC)
            putString(AnalyticsConstants.LABEL, "manuhome")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //FAQ Detail
    fun faqDetailQuestion() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.FAQ_QUESTION)
            putString(AnalyticsConstants.LABEL, "question")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //Setting
    fun settingInstallmentDue() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.SETTING_MAIN)
            putString(AnalyticsConstants.LABEL, "installment_due")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun settingTaxPaymentDue() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.SETTING_MAIN)
            putString(AnalyticsConstants.LABEL, "taxpayment_due")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun settingInsurancePaymentDue() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.SETTING_MAIN)
            putString(AnalyticsConstants.LABEL, "insurancepayment_due")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun settingChangeEmail() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.SETTING_MAIN)
            putString(AnalyticsConstants.LABEL, "change_email")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun settingCarProfile() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.SETTING_MAIN)
            putString(AnalyticsConstants.LABEL, "car_profile")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun settingNewsAndPromotion() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.SETTING_MAIN)
            putString(AnalyticsConstants.LABEL, "news&promotion")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun settingTibClub() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.SETTING_MAIN)
            putString(AnalyticsConstants.LABEL, "tibclub")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun settingChangeLanguage() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.SETTING_MAIN)
            putString(AnalyticsConstants.LABEL, "change_language")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun settingEnabledTouchId() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.SETTING_MAIN)
            putString(AnalyticsConstants.LABEL, "enabled_touch_id")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun settingChangePin() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.SETTING_MAIN)
            putString(AnalyticsConstants.LABEL, "change_pin")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun settingUserProfile() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.SETTING_MAIN)
            putString(AnalyticsConstants.LABEL, "user_profile")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun settingMenuHome() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.SETTING_MAIN)
            putString(AnalyticsConstants.LABEL, "manuhome")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //setting change Email
    fun settingChangeEmailConfirm() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.SETTING_CHANGE_EMAIL)
            putString(AnalyticsConstants.LABEL, "confirm")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //setting change email success
    fun settingChangeEmailSuccessButton() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.SETTING_CHANGE_EMAIL_SUCCESS)
            putString(AnalyticsConstants.LABEL, "back_to_setting_main")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //setting car profile
    fun carProfileCamera() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.SETTING_CAR_PROFILE)
            putString(AnalyticsConstants.LABEL, "camera")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun carProfileDefaultPicture() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.SETTING_CAR_PROFILE)
            putString(AnalyticsConstants.LABEL, "default_picture")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun carProfileSelectCar() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.SETTING_CAR_PROFILE)
            putString(AnalyticsConstants.LABEL, "select_car")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun carProfileConfirm() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.SETTING_CAR_PROFILE)
            putString(AnalyticsConstants.LABEL, "confirm")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun carProfileCancel() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.SETTING_CAR_PROFILE)
            putString(AnalyticsConstants.LABEL, "cancel")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //Tax main
    fun taxMainUpdateYear() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.TAX_MAIN)
            putString(AnalyticsConstants.LABEL, "update_year")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun taxMainPayment() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.TAX_MAIN)
            putString(AnalyticsConstants.LABEL, "payment")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun taxMainTaxLabelStatus() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.TAX_MAIN)
            putString(AnalyticsConstants.LABEL, "taxlabel_delivery_status")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun taxMainTaxLabelReceiveAddress() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.TAX_MAIN)
            putString(AnalyticsConstants.LABEL, "taxlabel_receive_address")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun taxMainCall() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.TAX_MAIN)
            putString(AnalyticsConstants.LABEL, "call")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun taxMainInsuranceChecking() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.TAX_MAIN)
            putString(AnalyticsConstants.LABEL, "com_insurance_checking")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun taxMainInspectCerChecking() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.TAX_MAIN)
            putString(AnalyticsConstants.LABEL, "inspect_cer_checking")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun taxMainNgvCerChecking() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.TAX_MAIN)
            putString(AnalyticsConstants.LABEL, "ngv_cer_checking")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun taxMainMainMenu() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.TAX_MAIN)
            putString(AnalyticsConstants.LABEL, "main_menu")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //tax update year
    fun updateTaxYearEnter() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.TAX_UPDATE_YEAR)
            putString(AnalyticsConstants.LABEL, "enter")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //Tax label delivery status
    fun taxLabelPostlink() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.TAX_LABEL_DELIVERY_STATUS)
            putString(AnalyticsConstants.LABEL, "post_link")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //tax label receive address
    fun taxReceiveAddressConfirm() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.TAX_LABEL_RECEIVE_ADDRESS)
            putString(AnalyticsConstants.LABEL, "confirm")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun taxReceiveAddressEmail() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.TAX_LABEL_RECEIVE_ADDRESS)
            putString(AnalyticsConstants.LABEL, "email")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //tax label receive email
    fun taxReceiveEmailConfirm() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.TAX_LABEL_RECEIVE_EMAIL)
            putString(AnalyticsConstants.LABEL, "confirm")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun taxReceiveEmailAddress() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.TAX_LABEL_RECEIVE_EMAIL)
            putString(AnalyticsConstants.LABEL, "address")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //tax payment list
    fun taxPaymentListPayment() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.TAX_PAYMENT_LIST)
            putString(AnalyticsConstants.LABEL, "payment")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun taxPaymentListUploadDoc() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.TAX_PAYMENT_LIST)
            putString(AnalyticsConstants.LABEL, "upload_doc")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //tax cominsurance upload
    fun attachComInsuranceUpload() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.TAX_COM_INSURANCE_UPLOAD)
            putString(AnalyticsConstants.LABEL, "upload")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun attachComInsuranceConfirm() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.TAX_COM_INSURANCE_UPLOAD)
            putString(AnalyticsConstants.LABEL, "confirm")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun attachComInsuranceSkip() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.TAX_COM_INSURANCE_UPLOAD)
            putString(AnalyticsConstants.LABEL, "skip")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //tax inspectioncer upload
    fun attachInspectCerUpload() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.TAX_INSPECTIONCER_UPLOAD)
            putString(AnalyticsConstants.LABEL, "upload")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun attachInspectCerConfirm() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.TAX_INSPECTIONCER_UPLOAD)
            putString(AnalyticsConstants.LABEL, "confirm")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun attachInspectCerSkip() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.TAX_INSPECTIONCER_UPLOAD)
            putString(AnalyticsConstants.LABEL, "skip")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun attachInspectCerLine() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.TAX_INSPECTIONCER_UPLOAD)
            putString(AnalyticsConstants.LABEL, "line_account")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun attachInspectCerEmail() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.TAX_INSPECTIONCER_UPLOAD)
            putString(AnalyticsConstants.LABEL, "send_email")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //tax cominsurance selectpopup
    fun attachInspectCerPopupBuy() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.TAX_COMINSURANCE_SELECTPOPUP)
            putString(AnalyticsConstants.LABEL, "buy_com_insurance")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun attachInspectCerPopupUpload() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.TAX_COMINSURANCE_SELECTPOPUP)
            putString(AnalyticsConstants.LABEL, "upload_doc")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //tax ngvcer sent
    fun attachNgvConfirm() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.TAX_NGVCER_SENT)
            putString(AnalyticsConstants.LABEL, "upload_doc")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //insurance main
    fun insuranceMainInfor() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSURANCE_MAIN)
            putString(AnalyticsConstants.LABEL, "tib_main")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun insuranceMainPolicyStatus() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSURANCE_MAIN)
            putString(AnalyticsConstants.LABEL, "tib_insurance_policy")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun insuranceMainQuotationForm() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSURANCE_MAIN)
            putString(AnalyticsConstants.LABEL, "Quotationform")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun insuranceMainCheckingPayment() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSURANCE_MAIN)
            putString(AnalyticsConstants.LABEL, "checkingpayment")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun insuranceMainClaimCenter() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSURANCE_MAIN)
            putString(AnalyticsConstants.LABEL, "Claim_Center")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun insuranceMainEmerCall() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSURANCE_MAIN)
            putString(AnalyticsConstants.LABEL, "Emer_Call")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun insuranceMainDealerLocation() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSURANCE_MAIN)
            putString(AnalyticsConstants.LABEL, "Dealer_location")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun insuranceMainContactQuestion() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSURANCE_MAIN)
            putString(AnalyticsConstants.LABEL, "Contact_question")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun insuranceMainTibClub() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSURANCE_MAIN)
            putString(AnalyticsConstants.LABEL, "tibclub")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun insuranceMainSelector() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSURANCE_MAIN)
            putString(AnalyticsConstants.LABEL, "selector")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun insuranceMainMenuHome() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSURANCE_MAIN)
            putString(AnalyticsConstants.LABEL, "menuhome")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //insurance infor
    fun insuranceInfoRecommended() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSURANCE_INFOR)
            putString(AnalyticsConstants.LABEL, "Recommended")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun insuranceInfoContactQuestion() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSURANCE_INFOR)
            putString(AnalyticsConstants.LABEL, "Contact_question")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //insurance policy status
    fun insurancePolicyStatusEmsLink() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSURANCE_POLICY_STATUS)
            putString(AnalyticsConstants.LABEL, "ems_link")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //insurance recommended
    fun insuranceRecommendedPayment() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSURANCE_RECOMMENDED)
            putString(AnalyticsConstants.LABEL, "payment")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun insuranceRecommendedQuotationForm() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSURANCE_RECOMMENDED)
            putString(AnalyticsConstants.LABEL, "Quotationform")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //insurance payment detail
    fun insurancePaymentDetailPayment() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSURANCE_PAYMENT_DETAIL)
            putString(AnalyticsConstants.LABEL, "payment")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun insurancePaymentDetailCall() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSURANCE_PAYMENT_DETAIL)
            putString(AnalyticsConstants.LABEL, "call")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //insurance contact
    fun insuranceContactClaim() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSURANCE_CONTACT)
            putString(AnalyticsConstants.LABEL, "contact_claim")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
    }

    fun insuranceContactDealerLocation() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSURANCE_CONTACT)
            putString(AnalyticsConstants.LABEL, "dealer_location")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
    }

    fun insuranceContactQuestion() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSURANCE_CONTACT)
            putString(AnalyticsConstants.LABEL, "contact_question")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
    }

    fun insuranceContactEmerCall() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSURANCE_CONTACT)
            putString(AnalyticsConstants.LABEL, "contact_emercall")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
    }

    //insurance contact emercall
    fun insuranceContactEmerCallContactCall() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSURANCE_CONTACT_EMERGENCY_CALL)
            putString(AnalyticsConstants.LABEL, "call")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //insurance contact claim
    fun insuranceContactClaimCall() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSURANCE_CONTACT_CLAIM)
            putString(AnalyticsConstants.LABEL, "call")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //insurance contact question
    fun insuranceContactQuestionSendQuestion() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSURANCE_CONTACT_QUESTION)
            putString(AnalyticsConstants.LABEL, "send_question")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    //insurance tib club
    fun insuranceTibClubLine() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSURANCE_TIB_CLUB)
            putString(AnalyticsConstants.LABEL, "line")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun insuranceTibClubActivity() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.INSURANCE_TIB_CLUB)
            putString(AnalyticsConstants.LABEL, "activity")
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }


//    fun carLoanClicked() {
//        val bundle = Bundle().apply {
//            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.MAIN_MENU)
//            putString(AnalyticsConstants.LABEL, "car_loan")
//            putString(AnalyticsConstants.VALUE, "")
//            putString(AnalyticsConstants.INFO, "")
//            putBaseEventParam()
//        }
//        ManualAnalyticManager.saveAnalytics(bundle = bundle)


        /////     TIB  INSURANCE        //////


        fun tibInsuranceMain() {
            val bundle = Bundle().apply {
                putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.TIB_MAIN)
                putString(AnalyticsConstants.LABEL, "tib_main")
                putString(AnalyticsConstants.VALUE, "")
                putString(AnalyticsConstants.INFO, "")
                putBaseEventParam()
            }
            ManualAnalyticManager.saveAnalytics(bundle = bundle)
        }

        fun tibInsuranceOther() {
            val bundle = Bundle().apply {
                putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.TIB_OTHER)
                putString(AnalyticsConstants.LABEL, "tib_other")
                putString(AnalyticsConstants.VALUE, "")
                putString(AnalyticsConstants.INFO, "")
                putBaseEventParam()
            }
            ManualAnalyticManager.saveAnalytics(bundle = bundle)
        }


        fun tibInsuranceInfor() {
            val bundle = Bundle().apply {
                putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.TIB_INSURANCE_INFOR)
                putString(AnalyticsConstants.LABEL, "tib_insurance_infor")
                putString(AnalyticsConstants.VALUE, "")
                putString(AnalyticsConstants.INFO, "")
                putBaseEventParam()
            }
            ManualAnalyticManager.saveAnalytics(bundle = bundle)
        }

        fun tibContactUs() {
            val bundle = Bundle().apply {
                putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.TIB_CONTACT_US)
                putString(AnalyticsConstants.LABEL, "tib_contact_us")
                putString(AnalyticsConstants.VALUE, "")
                putString(AnalyticsConstants.INFO, "")
                putBaseEventParam()
            }
            ManualAnalyticManager.saveAnalytics(bundle = bundle)
        }

        fun tibEmergencyCall() {
            val bundle = Bundle().apply {
                putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.TIB_EMERGENCY_CALL)
                putString(AnalyticsConstants.LABEL, "tib_emergency_call")
                putString(AnalyticsConstants.VALUE, "")
                putString(AnalyticsConstants.INFO, "")
                putBaseEventParam()
            }
            ManualAnalyticManager.saveAnalytics(bundle = bundle)
        }

        fun tibInsuranceHistory() {
            val bundle = Bundle().apply {
                putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.TIB_INSURANCE_HISTORY)
                putString(AnalyticsConstants.LABEL, "tib_insurance_history")
                putString(AnalyticsConstants.VALUE, "")
                putString(AnalyticsConstants.INFO, "")
                putBaseEventParam()
            }
            ManualAnalyticManager.saveAnalytics(bundle = bundle)
        }

        fun tibClaimCenter() {
            val bundle = Bundle().apply {
                putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.TIB_CLAIM_CENTER)
                putString(AnalyticsConstants.LABEL, "tib_claim_center")
                putString(AnalyticsConstants.VALUE, "")
                putString(AnalyticsConstants.INFO, "")
                putBaseEventParam()
            }
            ManualAnalyticManager.saveAnalytics(bundle = bundle)
        }

        fun tibCall() {
            val bundle = Bundle().apply {
                putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.TIB_CALL)
                putString(AnalyticsConstants.LABEL, "tib_call")
                putString(AnalyticsConstants.VALUE, "")
                putString(AnalyticsConstants.INFO, "")
                putBaseEventParam()
            }
            ManualAnalyticManager.saveAnalytics(bundle = bundle)
        }

        fun tibInsuracePolicy() {
            val bundle = Bundle().apply {
                putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.TIB_INSURACE_POLICY)
                putString(AnalyticsConstants.LABEL, "tib_insurace_policy")
                putString(AnalyticsConstants.VALUE, "")
                putString(AnalyticsConstants.INFO, "")
                putBaseEventParam()
            }
            ManualAnalyticManager.saveAnalytics(bundle = bundle)

        }


    fun onlineCarAdd() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.ONLINE_MAIN_CAR)
            putString(AnalyticsConstants.LABEL, AnalyticsScreenName.ONLINE_CAR_ADD)
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun onlineCarDelete() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.ONLINE_MAIN_CAR)
            putString(AnalyticsConstants.LABEL, AnalyticsScreenName.ONLINE_CAR_DELETE)
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun onlineCarClick() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.ONLINE_MAIN_CAR)
            putString(AnalyticsConstants.LABEL, AnalyticsScreenName.ONLINE_CAR_CLICK)
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }


    fun onlineIDCardAdd() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.ONLINE_IDCARD)
            putString(AnalyticsConstants.LABEL, AnalyticsScreenName.ONLINE_IDCARD_ADD)
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun onlineLivenessAdd() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.ONLINE_LIVENESS)
            putString(AnalyticsConstants.LABEL, AnalyticsScreenName.ONLINE_LIVENESS_ADD)
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }


    fun onlineDopaSuccessAdd() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.ONLINE_DOPA)
            putString(AnalyticsConstants.LABEL, AnalyticsScreenName.ONLINE_DOPA_SUCCESS)
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun onlineDopaUnsuccessAdd() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.ONLINE_DOPA)
            putString(AnalyticsConstants.LABEL, AnalyticsScreenName.ONLINE_DOPA_UNSUCCESS)
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun onlineAdddoc() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.ONLINE_ADDDOC)
            putString(AnalyticsConstants.LABEL, AnalyticsScreenName.ONLINE_ADDDOC_SUBMIT)
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun onlinePersonalMapClick() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.ONLINE_PERSONAL)
            putString(AnalyticsConstants.LABEL, AnalyticsScreenName.ONLINE_PERSONAL_MAP_CLICK)
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun onlineTermconAccept() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.ONLINE_NDID_TERMCON)
            putString(AnalyticsConstants.LABEL, AnalyticsScreenName.ONLINE_NDID_TERMCON_ACCEPT)
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun onlineTermconUnAccept() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.ONLINE_NDID_TERMCON)
            putString(AnalyticsConstants.LABEL, AnalyticsScreenName.ONLINE_NDID_TERMCON_UNACCEPT)
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }


    fun onlineEConsentAccept() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.ONLINE_E_CONSENT)
            putString(AnalyticsConstants.LABEL, AnalyticsScreenName.ONLINE_E_CONSENT_ACCEPT)
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }


    fun onlineEConsentUnAccept() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.ONLINE_E_CONSENT)
            putString(AnalyticsConstants.LABEL, AnalyticsScreenName.ONLINE_E_CONSENT_UNACCEPT)
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun onlineBankClick(label : String) {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.ONLINE_BANK_NDID)
            putString(AnalyticsConstants.LABEL, AnalyticsScreenName.ONLINE_BANK_NDID_CLICK + label)
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun onlineCreditAccept() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.ONLINE_CREDIT_RESULT)
            putString(AnalyticsConstants.LABEL, AnalyticsScreenName.ONLINE_CREDIT_RESULT_ACCEPT )
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun onlineCreditUnAccept() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.ONLINE_CREDIT_RESULT)
            putString(AnalyticsConstants.LABEL, AnalyticsScreenName.ONLINE_CREDIT_RESULT_UNACCEPT )
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun onlineCreditDOfferAccept() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.ONLINE_CREDIT_RESULT)
            putString(AnalyticsConstants.LABEL, AnalyticsScreenName.ONLINE_CREDIT_DOFFER_ACCEPT )
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun onlineCreditAddDoc() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.ONLINE_CREDIT_RESULT)
            putString(AnalyticsConstants.LABEL, AnalyticsScreenName.ONLINE_CREDIT_ADDDOC_SUBMIT )
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }


    fun onlineVerifyConfirmMapClick() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.ONLINE_VERIFY_CONFIRM)
            putString(AnalyticsConstants.LABEL, AnalyticsScreenName.ONLINE_VERIFY_CONFIRM_MAP_CLICK )
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }


    fun onlineDealerAdd() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.ONLINE_DEALER)
            putString(AnalyticsConstants.LABEL, AnalyticsScreenName.ONLINE_DEALER_MAP_ADD )
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun onlineSummaryDealerAdd() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.ONLINE_SUMMMARY)
            putString(AnalyticsConstants.LABEL, AnalyticsScreenName.ONLINE_SUMMARY_DEALER_CHANGE)
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }


    fun onlineSummaryScanQR() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.ONLINE_SUMMMARY)
            putString(AnalyticsConstants.LABEL, AnalyticsScreenName.ONLINE_SUMMARY_SCANQR)
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun onlineSummaryOtherExpense() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.ONLINE_SUMMMARY)
            putString(AnalyticsConstants.LABEL, AnalyticsScreenName.ONLINE_SUMMARY_VIEW_OTHER_EXPENSE)
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }


    fun onlineEndViewEcontract() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.ONLINE_ENDGAME)
            putString(AnalyticsConstants.LABEL, AnalyticsScreenName.ONLINE_ENDGAME_VIEW_CONTRACT)
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun onlineEndViewInstalment() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.ONLINE_ENDGAME)
            putString(AnalyticsConstants.LABEL, AnalyticsScreenName.ONLINE_ENDGAME_VIEW_INSTALMENT)
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun onlineMapADD() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.ONLINE_MAP)
            putString(AnalyticsConstants.LABEL, AnalyticsScreenName.ONLINE_MAP_ADD)
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }

    fun onlineMapStreetViewADD() {
        val bundle = Bundle().apply {
            putString(AnalyticsConstants.SCREEN, AnalyticsScreenName.ONLINE_STREET_VIEW)
            putString(AnalyticsConstants.LABEL, AnalyticsScreenName.ONLINE_STREET_VIEW_ADD)
            putString(AnalyticsConstants.VALUE, "")
            putString(AnalyticsConstants.INFO, "")
            putBaseEventParam()
        }
        ManualAnalyticManager.saveAnalytics(bundle = bundle)
    }


 }
