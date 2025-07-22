package tlt.th.co.toyotaleasing.modules.insurance.hotline.accident

import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.common.livedata.SingleLiveData
import tlt.th.co.toyotaleasing.manager.db.MasterDataManager
import tlt.th.co.toyotaleasing.util.AppUtils

class AccidentViewModel : BaseViewModel() {

    val data = SingleLiveData<Model>()
    private val masterDataManager = MasterDataManager.getInstance()

    fun getData() {

    }

    data class Model(
            var accidentList: List<AccidentModel>,
            val isStaffApp: Boolean = AppUtils.isStaffApp()
    ) {
        data class AccidentModel(var type: AccidentViewType = AccidentViewType.ACCIDENT_VIEW,
                                 var imageUrl: String = "",
                                 var name: String = "",
                                 var telephone: String = "",
                                 var telephoneDisplay: String = "")
    }
}