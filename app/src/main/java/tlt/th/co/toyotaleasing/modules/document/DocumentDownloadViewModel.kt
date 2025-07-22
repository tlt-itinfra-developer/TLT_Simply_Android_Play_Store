package tlt.th.co.toyotaleasing.modules.document

import androidx.lifecycle.MutableLiveData
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.manager.db.MasterDataManager
import tlt.th.co.toyotaleasing.util.AppUtils

class DocumentDownloadViewModel : BaseViewModel() {

    val whenDataLoaded = MutableLiveData<Model>()

    fun getData() {
        whenDataLoaded.postValue(Model(listOf()))
    }

    data class Model(
            val documents: List<Document> = listOf(),
            val isStaffApp: Boolean = AppUtils.isStaffApp()
    )

    data class Document(
            val id: String = "",
            val title: String = "",
            val fileUrl: String = ""
    )
}