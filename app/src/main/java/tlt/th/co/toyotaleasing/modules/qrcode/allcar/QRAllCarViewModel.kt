package tlt.th.co.toyotaleasing.modules.qrcode.allcar

import androidx.lifecycle.MutableLiveData
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.manager.db.CacheManager

import tlt.th.co.toyotaleasing.util.AppUtils

class QRAllCarViewModel : BaseViewModel() {

    val whenDataLoaded = MutableLiveData<Model>()


    fun getData() {
    }

    fun saveCar(position: Int) {
    }

    data class Model(
            val list: MutableList<Car> = mutableListOf(),
            val isStaffApp: Boolean = AppUtils.isStaffApp()
    )

    data class Car(
            val id: String = "",
            val carLicense: String = "",
            val isInstallmentEnable: Boolean = false,
            val isTaxEnable: Boolean = false,
            val isInsuranceEnable: Boolean = false,
            val isOtherEnable: Boolean = false
    )
}