package tlt.th.co.toyotaleasing.common.lifecycleobserver

import tlt.th.co.toyotaleasing.modules.fingerprint.FingerprintSettingActivity
import tlt.th.co.toyotaleasing.modules.home.noncustomer.MainNonCustomerActivity
import tlt.th.co.toyotaleasing.modules.main.MainActivity
import tlt.th.co.toyotaleasing.modules.otp.OTPActivity
import tlt.th.co.toyotaleasing.modules.pincode.AuthPincodeActivity
import tlt.th.co.toyotaleasing.modules.pincode.SetupPincodeActivity
import tlt.th.co.toyotaleasing.modules.register.RegisterFormActivity
import tlt.th.co.toyotaleasing.modules.register.RegisterSuccessActivity
import tlt.th.co.toyotaleasing.modules.selectphone.SelectPhoneActivity
import tlt.th.co.toyotaleasing.modules.termcondition.TermAndConditionActivity
import tlt.th.co.toyotaleasing.modules.verifyemail.VerifyEmailActivity
import tlt.th.co.toyotaleasing.modules.verifyemail.VerifyEmailFailActivity
import tlt.th.co.toyotaleasing.modules.verifyemail.VerifyEmailSuccessActivity

object


Screens {
    val nonCustomer = setOf(
            MainActivity::class.java.simpleName,
            MainNonCustomerActivity::class.java.simpleName,
            VerifyEmailActivity::class.java.simpleName,
            AuthPincodeActivity::class.java.simpleName,
            VerifyEmailSuccessActivity::class.java.simpleName,
            VerifyEmailFailActivity::class.java.simpleName,
            TermAndConditionActivity::class.java.simpleName,
            RegisterFormActivity::class.java.simpleName,
            SelectPhoneActivity::class.java.simpleName,
            OTPActivity::class.java.simpleName,
            RegisterSuccessActivity::class.java.simpleName,
            SetupPincodeActivity::class.java.simpleName,
            FingerprintSettingActivity::class.java.simpleName
    )

    val mainActivity = setOf(
            MainActivity::class.java.simpleName,
            MainNonCustomerActivity::class.java.simpleName
    )
}