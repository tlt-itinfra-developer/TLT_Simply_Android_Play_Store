package tlt.th.co.toyotaleasing.modules.policy

import androidx.lifecycle.MutableLiveData
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.db.UserManager
import okhttp3.*
import java.io.IOException
import tlt.th.co.toyotaleasing.manager.db.*
import tlt.th.co.toyotaleasing.model.request.SkippaymentRequest

class PolicyViewModel : BaseViewModel() {

    private val profile = UserManager.getInstance().getProfile()
    val whenDataLoaded = MutableLiveData<String>()


    fun getData() {
        val master = MasterDataManager.getInstance().getPrivacy()
        val url = ""

        whenDataLoaded.postValue(url)
    }


}


