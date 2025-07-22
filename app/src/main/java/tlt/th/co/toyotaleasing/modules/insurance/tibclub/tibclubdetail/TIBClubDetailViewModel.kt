package tlt.th.co.toyotaleasing.modules.insurance.tibclub.tibclubdetail

import androidx.lifecycle.MutableLiveData
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.manager.db.CacheManager
import tlt.th.co.toyotaleasing.util.AppUtils

class TIBClubDetailViewModel : BaseViewModel() {

    val whenDataLoaded = MutableLiveData<Model>()

    fun getData() {
        CacheManager.getCacheTIB()?.let {
            val model = Model(
                    "",
                    "",
                    ""
            )

            whenDataLoaded.postValue(model)
        }
    }

    data class Model(
            val title: String = "",
            val detail: String = "",
            val imageUrl: String = "",
            val isStaffApp: Boolean = AppUtils.isStaffApp()
    )
}