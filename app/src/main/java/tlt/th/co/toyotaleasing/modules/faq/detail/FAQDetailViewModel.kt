package tlt.th.co.toyotaleasing.modules.faq.detail

import androidx.lifecycle.MutableLiveData
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.manager.db.MasterDataManager
import tlt.th.co.toyotaleasing.util.AppUtils

class FAQDetailViewModel : BaseViewModel() {

    val whenDataLoaded = MutableLiveData<Model>()

    fun getData(topicId: String) {
        whenDataLoaded.postValue(Model(listOf()))
    }

    data class Model(
            val items: List<Item> = listOf(),
            val isStaffApp: Boolean = AppUtils.isStaffApp()
    )

    data class Item(
            val id: String = "",
            val title: String = "",
            val detail: String = "",
            var isExpand: Boolean = false
    )
}