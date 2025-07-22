package tlt.th.co.toyotaleasing.modules.insurance.hotline.service

import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.manager.db.UserManager

class ServiceViewModel : BaseViewModel() {

    val userManager = UserManager.getInstance()

    fun isCustomer() = userManager.isCustomer()
}