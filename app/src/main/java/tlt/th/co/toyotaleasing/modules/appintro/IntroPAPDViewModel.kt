package tlt.th.co.toyotaleasing.modules.appintro

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager
import tlt.th.co.toyotaleasing.manager.db.MasterDataManager
import tlt.th.co.toyotaleasing.manager.db.UserManager
import tlt.th.co.toyotaleasing.model.request.AcceptTermConditionRequest

class IntroPAPDViewModel : ViewModel() {

    val whenLoading = MutableLiveData<Boolean>()
    val whenURLDataLoaded = MutableLiveData<String>()
    val whenUserDenied = MutableLiveData<String>()

    fun disableShowForNextOpenApp() {
        UserManager.getInstance().disableShowAppPAPD()
    }

    val whenDataLoaded = MutableLiveData<String>()


    fun getData() {
        val master = MasterDataManager.getInstance().getPrivacy()
        val url = ""

        whenURLDataLoaded.postValue(url)
    }
}