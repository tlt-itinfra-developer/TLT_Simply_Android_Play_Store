package tlt.th.co.toyotaleasing.modules.faq.main

import androidx.lifecycle.MutableLiveData
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.manager.db.MasterDataManager
import tlt.th.co.toyotaleasing.util.AppUtils

class FAQViewModel : BaseViewModel() {

    val whenDataLoaded = MutableLiveData<Model>()

    fun getData() {
        whenDataLoaded.postValue(Model(listOf()))
    }

    data class Model(
            val items: List<Item> = listOf(),
            val isStaffApp: Boolean = AppUtils.isStaffApp()
    )

    data class Item(
            val id: String = "",
            val title: String = ""
    )
}