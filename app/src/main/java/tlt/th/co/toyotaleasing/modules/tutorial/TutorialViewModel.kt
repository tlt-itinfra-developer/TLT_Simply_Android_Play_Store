package tlt.th.co.toyotaleasing.modules.tutorial

import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.manager.db.UserManager

class TutorialViewModel : BaseViewModel() {

    fun disableShowTutorialForOpenNextTime() {
        UserManager.getInstance().disableShowAppTutorial()
    }

}