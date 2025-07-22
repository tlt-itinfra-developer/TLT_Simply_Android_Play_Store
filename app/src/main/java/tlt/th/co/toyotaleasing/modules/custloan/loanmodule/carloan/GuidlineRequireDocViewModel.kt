package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.carloan

import androidx.lifecycle.MutableLiveData
import tlt.th.co.toyotaleasing.common.base.BaseViewModel

class GuidlineRequireDocViewModel  : BaseViewModel() {

    val whenDataLoaded = MutableLiveData<Model>()

    fun getData(uniqueID: String) {
//        val items = MasterDataManager.getInstance()
//                .getFAQListByType(topicId)?.map {
//                    Item(
//                            id = it.aWCODE ?: "",
//                            title = it.wording() ?: "",
//                            detail = it.awAccept() ?: ""
//                    )
//                } ?: listOf()


        val data =  arrayListOf(Item(
                id = uniqueID ?: "",
                detail = "uniqueID : " + uniqueID ?: ""
        )
        )

        val items = data?.map {
            Item(
                    id = it.id,
                    detail = it.detail
            )
                } ?: listOf()



        whenDataLoaded.postValue(Model(items))
    }

    data class Model(
            val items: List<Item> = listOf()
    )

    data class Item(
            val id: String = "",
            val detail: String = ""
    )
}
