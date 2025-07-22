package tlt.th.co.toyotaleasing.modules.payment.cart.common

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.common.extension.toDatetime
import tlt.th.co.toyotaleasing.common.extension.toNumberFormat
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager
import tlt.th.co.toyotaleasing.manager.db.CacheManager
import tlt.th.co.toyotaleasing.manager.db.PaymentManager
import tlt.th.co.toyotaleasing.util.AppUtils

abstract class CartViewModel : BaseViewModel() {

    val whenDataLoaded = MutableLiveData<Model>()
    val whenFailure = MutableLiveData<String>()
    val whenSubmitButtonStateChanged = MutableLiveData<Boolean>()

    protected val apiManager = TLTApiManager.getInstance()
    protected val currentCar = CacheManager.getCacheCar()

    fun getData(isPayoff : Boolean = false) {
        GlobalScope.launch(Dispatchers.Main) {
            whenLoading.postValue(true)

            try {
                val model = getModel(getDataDetailByApi(isPayoff))

                whenLoading.postValue(false)
                whenDataLoaded.postValue(model)

                checkSubmitButtonStateChanged(model.cartList)
            } catch (e: Exception) {
                if (e.message != "device logon") {
                    whenFailure.postValue(e.message)
                }
                e.printStackTrace()
            }
        }
    }

    fun updateSummaryItem(index: Int, item: CartItem) {
        whenDataLoaded.value?.cartList?.set(index, item)
    }

    fun refreshList() {
        val model = getModel(whenDataLoaded.value?.cartList ?: mutableListOf())
        whenDataLoaded.postValue(model)

        checkSubmitButtonStateChanged(model.cartList)
    }

    fun refreshListByLocalDB() {
    }

    fun saveList() {
        val list = whenDataLoaded.value?.cartList?.toList() ?: listOf()
        PaymentManager.saveSummaryList(list)
    }

    fun submit() {
        val list = whenDataLoaded.value?.cartList?.filter { it.isChecked } ?: listOf()
        PaymentManager.saveSummaryList(list)
    }

    private fun getModel(cartList: MutableList<CartItem>): Model {
        return Model(
                currentCar?.cURRENTDATE?.toDatetime() ?: "",
                "${currentCar?.regNumber} - ${currentCar?.cREGPROVINCE}",
                currentCar?.contractNumber ?: "",
                cartList,
                cartList.filter { it.isChecked }.sumByDouble { it._price.toDouble() }.toString().toNumberFormat()
        )
    }

    private fun checkSubmitButtonStateChanged(cartList: MutableList<CartItem> = mutableListOf()) {
        val isEnable = cartList.any { it.isChecked }

        whenSubmitButtonStateChanged.postValue(isEnable)
    }

    abstract suspend fun getDataDetailByApi(isPayoff: Boolean = false): MutableList<CartItem>

    data class Model(
            var currentDate: String = "",
            var carLicense: String = "",
            var contractNumber: String = "",
            var cartList: MutableList<CartItem> = mutableListOf(),
            var totalPaid: String = "",
            val isStaffApp: Boolean = AppUtils.isStaffApp()
    )
}