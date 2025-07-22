package tlt.th.co.toyotaleasing.modules.customer.mycar

import androidx.lifecycle.MutableLiveData
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.common.extension.toDatetime
import tlt.th.co.toyotaleasing.common.extension.toNumberFormatWithoutComma
import tlt.th.co.toyotaleasing.manager.db.CacheManager


class MyCarViewModel : BaseViewModel() {

    val whenGetCarList = MutableLiveData<List<MyCar>>()

    fun getCarList() {

    }

    fun getCarByPosition(position: Int): MyCar? {
        return whenGetCarList.value?.getOrNull(position)
    }

    fun selectCar(car: MyCar) {

    }

    fun getPositionDefaultCar(contractNumber: String = ""): Int {
        if (contractNumber.isEmpty()) {
            val mycar = CacheManager.getCacheCar()
            return whenGetCarList.value?.indexOfFirst { it.contractNumber == mycar?.contractNumber }!!
        }

        return whenGetCarList.value?.indexOfFirst { it.contractNumber == contractNumber }!!
    }
}