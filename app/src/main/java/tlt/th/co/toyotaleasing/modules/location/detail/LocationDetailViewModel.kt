package tlt.th.co.toyotaleasing.modules.location.detail

import androidx.lifecycle.MutableLiveData
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.manager.LocationManager
import tlt.th.co.toyotaleasing.util.AppUtils

class LocationDetailViewModel : BaseViewModel() {

    val whenDataLoaded = MutableLiveData<Model>()

    fun getData() {
        val locationDetail = LocationManager.getLocationDetail()

        val model = Model(
                id = "",
                branch = "",
                company = "",
                distance = "",
                address = "",
                tel = "",
                telForCall = "",
                time = "",
                officeType = "",
                website = "",
                lat =  "",
                lng = ""
        )

        whenDataLoaded.postValue(model)
    }

    data class Model(
            val id: String = "",
            val company: String = "",
            val branch: String = "",
            val distance: String = "",
            val officeType: String = "",
            val address: String = "",
            val tel: String = "",
            val telForCall: String = "",
            val time: String = "",
            val website: String = "",
            val isStaffApp: Boolean = AppUtils.isStaffApp(),
            val lat: String = "",
            val lng: String = ""
    )
}