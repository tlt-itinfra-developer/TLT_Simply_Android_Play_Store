package tlt.th.co.toyotaleasing.modules.livechat

import androidx.lifecycle.MutableLiveData
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager
import tlt.th.co.toyotaleasing.manager.db.CacheManager
import tlt.th.co.toyotaleasing.model.request.SendContactAskRequest

class LiveChatViewModel : BaseViewModel() {

    val whenDataLoaded = MutableLiveData<Model>()
    val whenDataLoadedChatBotSuccess = MutableLiveData<String>()
    val whenDataLoadedChatBotFail = MutableLiveData<String>()
    private val apiManager = TLTApiManager.getInstance()


//    fun getData() {
//        val profile = CacheManager.getCacheProfile()
//        val model = Model(
//                name = profile?.cUSTNAME ?: "Anonymous",
//                email = profile?.eMAIL ?: "",
//                phone = profile?.pHONE ?: ""
//        )
//
//        whenDataLoaded.postValue(model)
//    }


    fun getData() {
        whenLoading.postValue(true)

        apiManager.getChatBot(){ isError: Boolean, result: String ->
            whenLoading.postValue(false)

            if (isError) {
                whenDataLoadedChatBotFail.postValue(result)
                return@getChatBot
            }

            if(result != "") {
                whenDataLoadedChatBotSuccess.postValue(result)
            }
            else{
                whenDataLoadedChatBotFail.postValue(result)
            }
        }
    }

    data class Model(
            val name: String = "",
            val email: String = "",
            val phone: String = ""
    )
}
