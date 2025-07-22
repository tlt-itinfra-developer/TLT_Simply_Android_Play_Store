package tlt.th.co.toyotaleasing.modules.payment.cart.customize

import androidx.lifecycle.MutableLiveData
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.common.extension.toDatetime
import tlt.th.co.toyotaleasing.common.extension.toNumberFormat
import tlt.th.co.toyotaleasing.manager.db.CacheManager
import tlt.th.co.toyotaleasing.manager.db.MasterDataManager
import tlt.th.co.toyotaleasing.manager.db.PaymentManager
import tlt.th.co.toyotaleasing.modules.payment.cart.common.CartItem
import tlt.th.co.toyotaleasing.util.AppUtils

class CartCustomizeViewModel : BaseViewModel() {

    val whenDataLoaded = MutableLiveData<Model>()
    val whenSummaryListUpdate = MutableLiveData<MutableList<CartItem>>()

    private val currentCar = CacheManager.getCacheCar()

    var clickingPosition = -1
    private val optionItems = ArrayList<OptionItem>()
    private val masterDataManager = MasterDataManager.getInstance()

    fun getData() {
    }

    fun updateSummaryItem(index: Int, item: CartItem) {
        val newList = whenSummaryListUpdate.value?.apply {
            set(index, item)
        }

        whenSummaryListUpdate.postValue(newList)
    }

    fun updateSummaryItemByOptionItem(optionIndex: Int) {
        whenSummaryListUpdate.value?.get(clickingPosition)?.apply {
            optionItems.find { isOptionSelected(it, this) }?.apply {
                isSelected = false
            }
        }

        val optionItem = optionItems[optionIndex].apply {
            isSelected = true
        }

        val newList = whenSummaryListUpdate.value?.apply {
            val item = get(clickingPosition).apply {
                code = optionItem.code
                type = optionItem.type
                title = optionItem.title
            }

            set(clickingPosition, item)
        }

        whenSummaryListUpdate.postValue(newList)
    }

    fun addSummaryItem(oldItems: MutableList<CartItem>) {
        val newList = whenSummaryListUpdate.value?.apply {
            clear()
            addAll(oldItems)
            add(CartItem(_price = "", isRecentlyAdd = true))
        }

        whenSummaryListUpdate.postValue(newList)
    }

    fun removeSummaryItem(index: Int) {
        val newList = whenSummaryListUpdate.value?.apply {
            val item = get(index)

            if (item.isRecentlyAdd) {
                optionItems.find { isOptionSelected(it, item) }?.apply {
                    isSelected = false
                }
            }

            removeAt(index)
        }

        whenSummaryListUpdate.postValue(newList)
    }

    fun getTitleOptionsList() = optionItems.map { it.title }

    fun getIndexsOfDisableOptions(): List<Int> {
        return optionItems.mapIndexed { index, optionItem ->
            if (optionItem.isSelected) {
                index
            } else {
                null
            }
        }.filterNotNull()
    }

    fun isAdd(): Boolean {
        val recentsSize = whenSummaryListUpdate.value?.filter { it.isRecentlyAdd }?.size ?: 0
        val optionsSize = optionItems.size

        return recentsSize != optionsSize
    }

    fun submit(cartItems: List<CartItem>) {
        val newItems = cartItems.filter {
            it.title.isNotEmpty()
        }

        PaymentManager.saveSummaryList(newItems)
    }

    private fun isOptionSelected(optionItem: OptionItem, cartItem: CartItem): Boolean {
        return optionItem.code == cartItem.code && optionItem.type == cartItem.type
    }

    data class Model(
            val currentDate: String = "",
            val carLicense: String = "",
            val contractNumber: String = "",
            val totalPaid: String = "",
            val isStaffApp: Boolean = AppUtils.isStaffApp()
    )

    data class OptionItem(
            val code: String = "",
            val type: String = "",
            val title: String = "",
            var isSelected: Boolean = false
    )
}