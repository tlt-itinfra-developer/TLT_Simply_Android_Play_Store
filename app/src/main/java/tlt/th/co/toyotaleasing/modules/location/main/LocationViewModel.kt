package tlt.th.co.toyotaleasing.modules.location.main

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.common.extension.ifFalse
import tlt.th.co.toyotaleasing.manager.LocationManager
import tlt.th.co.toyotaleasing.manager.db.CacheManager
import tlt.th.co.toyotaleasing.manager.db.MasterDataManager
import tlt.th.co.toyotaleasing.model.entity.location.LocationDetailEntity
import tlt.th.co.toyotaleasing.model.entity.location.LocationFilterEntity
import tlt.th.co.toyotaleasing.model.entity.masterdata.DealerInfo
import tlt.th.co.toyotaleasing.util.PermissionUtils

class LocationViewModel : BaseViewModel() {

    val whenDataLoaded = MutableLiveData<Model>()

    private val officeList = null

    private val showroomList = null

    private val NEARBY_LIMIT_ITEM = 10

    fun getDataFromContractDetail() {
        GlobalScope.launch(Dispatchers.Main) {
            val selectedCar = CacheManager.getCacheInstallment()!!

            val locationList = getShowroomLocationByCodeAndName(
                    code = selectedCar.dealerNostraId,
                    nameTh = selectedCar.dEALERNAMETH,
                    nameEn = selectedCar.dEALERNAMEEN
            )

            whenDataLoaded.postValue(Model(
                    locationList = locationList ?: listOf(),
                    locationType = Type.SHOWROOM
            ))
        }

    }

    fun getData(selectedType: Type = Type.OFFICE) {
        GlobalScope.launch(Dispatchers.Main) {
            val locationFilter = LocationManager.getLocationFilter()
            val isNearby = false

            when {
                isNearby -> getNearbyLocations()
                selectedType == Type.OFFICE -> getOfficeLocations()
                else -> getShowRoomLocations(locationFilter)
            }

            LocationManager.clearLocationFilter()
        }
    }

    private fun getOfficeLocations() {
        GlobalScope.launch(Dispatchers.Main) {
            val locationList = if (PermissionUtils.isGrantedLocation()) {
                getOfficeLocationsWithDistance()
            } else {
                getOfficeLocationsWithoutDistance()
            }

            whenDataLoaded.postValue(Model(
                    locationList = locationList ?: listOf(),
                    locationType = Type.OFFICE
            ))
        }
    }

    private suspend fun getOfficeLocationsWithDistance(): List<Location>? {
        try {
            val currentLocation = LocationManager.getLastKnowLocation()

            return listOf()
        } catch (error: Exception) {
            return listOf()
        }
    }

    private fun getOfficeLocationsWithoutDistance(): List<Location>? {
        return listOf()
    }

    private fun getShowRoomLocations(locationFilter: LocationFilterEntity?) {
        GlobalScope.launch(Dispatchers.Main) {
            val locationList = getShowroomLocationsByFilter(locationFilter)

            whenDataLoaded.postValue(Model(
                    locationList = locationList ?: listOf(),
                    locationType = Type.SHOWROOM
            ))
        }
    }

    private fun getNearbyLocations() {
        GlobalScope.launch(Dispatchers.Main) {
            val locationList = getShowroomLocationsByNearly()?.take(NEARBY_LIMIT_ITEM)

            whenDataLoaded.postValue(Model(
                    locationList = locationList ?: listOf(),
                    locationType = Type.SHOWROOM
            ))
        }
    }

    private suspend fun getShowroomLocationByCodeAndName(code: String, nameTh: String, nameEn: String): List<Location>? {
        var locations: List<DealerInfo>? = showroomList
        val locationMerge = mutableListOf<DealerInfo>()


        if (locationMerge.isNotEmpty()) {
            locations = locationMerge.distinctBy { it.dealerCode }
        }

        if (PermissionUtils.isGrantedLocation()) {
            val currentLocation = LocationManager.getLastKnowLocation()

            return getShowroomLocationsWithDistance(
                    currentLocation,
                    locations
            )
        }

        return locations?.map {
            Location(
                    code = it.dealerCode ?: "",
                    title = it.getNameByLocalize() ?: "",
                    address = it.getAddressByLocalize() ?: "",
                    branch = it.getBranchByLocalize() ?: "",
                    tel = it.telSs?.trim() ?: "",
                    openTime = it.getOpenHrShowRoomByLocalize() ?: "",
                    distance = "-",
                    website = it.url1 ?: "",
                    lat = it.lat ?: "",
                    lng = it.lon ?: "",
                    type = Type.SHOWROOM
            )
        }
    }

    private suspend fun getShowroomLocationsByFilter(locationFilter: LocationFilterEntity?): List<Location>? {
        val provinces = MasterDataManager.getInstance().getProvinceList()
        val provideCode = provinces?.getOrNull(locationFilter?.provinceIndex ?: -1)?.pROVINCECODE
                ?: ""
        val amphurs = MasterDataManager.getInstance().getAmphurByProvinceCode(provideCode)
        val amphurCode = amphurs?.getOrNull(locationFilter?.amphurIndex ?: -1)?.aMPHURCODE ?: ""

        val enableFlag = "Y"
        val locationMerge = mutableListOf<DealerInfo>()


        if (PermissionUtils.isGrantedLocation()) {
            val currentLocation = LocationManager.getLastKnowLocation()

            return getShowroomLocationsWithDistance(
                    currentLocation,
                    listOf()
            )?.sortedBy {
                it.distance.toDouble()
            }
        }

        return listOf()
    }

    private suspend fun getShowroomLocationsByNearly(): List<Location>? {
        if (!PermissionUtils.isGrantedLocation()) {
            return emptyList()
        }

        val currentLocation = LocationManager.getLastKnowLocation()

        return getShowroomLocationsWithDistance(currentLocation, showroomList)
                ?.sortedBy {
                    it.distance.toDouble()
                }
    }

    private fun getShowroomLocationsWithDistance(
            currentLocation: android.location.Location,
            showroomList: List<DealerInfo>?
    ): List<Location>? {
        return showroomList?.map {
            val destLocation = android.location.Location("dest").apply {
                latitude = it.lat?.toDouble() ?: 0.0
                longitude = it.lon?.toDouble() ?: 0.0
            }
            val kilometers = currentLocation.distanceTo(destLocation).div(1000)
            val distance = String.format("%.1f", kilometers)

            Location(
                    code = it.dealerCode ?: "",
                    title = it.getNameByLocalize() ?: "",
                    address = it.getAddressByLocalize() ?: "",
                    branch = it.getBranchByLocalize() ?: "",
                    tel = it.telSs?.trim() ?: "",
                    telForCall = it.telSsCall?.trim() ?: "",
                    openTime = it.getOpenHrShowRoomByLocalize() ?: "",
                    distance = distance,
                    website = it.url1 ?: "",
                    lat = it.lat ?: "",
                    lng = it.lon ?: "",
                    type = Type.SHOWROOM
            )
        }?.sortedBy {
            it.distance.toDouble()
        }
    }

    fun onLocationClick(index: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            val model = whenDataLoaded.value?.copy()

            val locations = model?.locationList?.toMutableList()

            val firstItem = locations?.getOrNull(0)?.copy()
            val selectedItem = locations?.getOrNull(index)?.copy()

            locations?.set(0, selectedItem ?: Location())
            locations?.set(index, firstItem ?: Location())

            model?.locationList = locations?.toList() ?: listOf()

            whenDataLoaded.postValue(model)
        }
    }

    fun saveLocationDetail(item: Location) {
        val locationDetail = LocationDetailEntity(
                id = item.code,
                company = item.title,
                branch = item.branch,
                distance = item.distance,
                address = item.address,
                tel = item.tel,
                telForCall = item.telForCall,
                time = item.openTime,
                officeType = item.showroomType,
                website = item.website,
                lat = item.lat,
                lng = item.lng
        )

        LocationManager.saveLocationDetail(locationDetail)
    }

    data class Model(
            var locationList: List<Location> = listOf(),
            val locationType: Type = Type.OFFICE
    )

    data class Location(
            val code: String = "",
            val title: String = "",
            val address: String = "",
            val detail: String = "",
            val branch: String = "",
            val showroomType: String = "",
            val openTime: String = "",
            val website: String = "",
            val responsibility: String = "",
            val tel: String = "",
            val telForCall: String = "",
            val distance: String = "",
            val lat: String = "",
            val lng: String = "",
            val type: Type = Type.OFFICE
    )

    enum class Type {
        OFFICE,
        SHOWROOM,
        NEARBY
    }
}