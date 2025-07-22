package tlt.th.co.toyotaleasing.modules.insurance.quotation

import androidx.lifecycle.MutableLiveData
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.manager.db.MasterDataManager
import tlt.th.co.toyotaleasing.model.entity.masterdata.InsPolicy
import tlt.th.co.toyotaleasing.util.AppUtils

class QuotationDialogViewModel : BaseViewModel() {

    val whenDataLoaded = MutableLiveData<Model>()
    val whenQuotationPoliciesLoaded = MutableLiveData<List<InsPolicy>>()

    fun getDefaultData() {

    }

    data class Model(
            val title: String = "",
            val isStaffApp: Boolean = AppUtils.isStaffApp()
    )

    fun getQuotationPolicies() {
        val items = MasterDataManager.getInstance()
                .getInsurancePolicies()

        whenQuotationPoliciesLoaded.postValue(items)
    }
}