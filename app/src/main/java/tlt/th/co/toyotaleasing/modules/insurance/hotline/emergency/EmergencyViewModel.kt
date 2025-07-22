package tlt.th.co.toyotaleasing.modules.insurance.hotline.emergency

import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.common.livedata.SingleLiveData
import tlt.th.co.toyotaleasing.manager.db.MasterDataManager
import tlt.th.co.toyotaleasing.util.AppUtils

class EmergencyViewModel : BaseViewModel() {

    val data = SingleLiveData<Model>()
    private val masterDataManager = MasterDataManager.getInstance()

    fun getData() {
    }

    data class Model(
            var emergencyList: List<EmergencyModel>,
            val isStaffApp: Boolean = AppUtils.isStaffApp()
    ) {
        data class EmergencyModel(var type: EmergencyViewType = EmergencyViewType.EMERGENCY_VIEW,
                                  var name: String = "",
                                  var telephone: String = "")
    }
}