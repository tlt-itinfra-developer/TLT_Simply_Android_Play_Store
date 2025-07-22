package tlt.th.co.toyotaleasing.modules.filtercar

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tlt.th.co.toyotaleasing.manager.db.CacheManager


class FilterCarViewModel : ViewModel() {

    val whenLoading = MutableLiveData<Boolean>()
    val whenGetFilterCarList = MutableLiveData<List<FilterCar>>()

    fun getFilterCarList() {
        whenGetFilterCarList.value = listOf()
    }

    fun selectedCar(contractNumber: String) {

    }
}