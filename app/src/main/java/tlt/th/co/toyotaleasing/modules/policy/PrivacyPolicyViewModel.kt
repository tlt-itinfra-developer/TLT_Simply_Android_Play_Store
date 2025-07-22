package tlt.th.co.toyotaleasing.modules.policy

import androidx.lifecycle.MutableLiveData
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.db.UserManager
import okhttp3.*
import java.io.IOException
import tlt.th.co.toyotaleasing.manager.db.*
import tlt.th.co.toyotaleasing.model.request.SkippaymentRequest

class PrivacyPolicyViewModel : BaseViewModel() {


    val whenDataLoaded = MutableLiveData<String>()


    fun getData() {
        
        var url : String = ""
        val master = MasterDataManager.getInstance().getPrivacy()

        whenDataLoaded.postValue(url)
    }
}
