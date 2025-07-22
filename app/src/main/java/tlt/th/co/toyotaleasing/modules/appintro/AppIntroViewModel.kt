package tlt.th.co.toyotaleasing.modules.appintro

import androidx.lifecycle.ViewModel
import tlt.th.co.toyotaleasing.manager.db.UserManager

class AppIntroViewModel : ViewModel() {

    fun disableShowForNextOpenApp() {
        UserManager.getInstance().disableShowAppIntro()
    }
}