package tlt.th.co.toyotaleasing.modules.pincode

import androidx.lifecycle.ViewModel
import tlt.th.co.toyotaleasing.manager.FlowManager
import tlt.th.co.toyotaleasing.manager.db.UserManager

class ChangePincodeViewModel : ViewModel() {

    private val userManager = UserManager.getInstance()

    fun resetLoginAttempt() {
        userManager.resetLoginAttempt()
    }

    fun isResetPincodeFlow() = FlowManager.isResetPinFlow()
}