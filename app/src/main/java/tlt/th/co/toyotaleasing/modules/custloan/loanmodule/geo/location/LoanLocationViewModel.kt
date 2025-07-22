package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.geo.location

import androidx.lifecycle.MutableLiveData
import android.location.Location
import kotlinx.android.synthetic.main.activity_location_filter.*
import kotlinx.android.synthetic.main.fragment_loan_location_filter.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.common.extension.ifFalse
import tlt.th.co.toyotaleasing.manager.LocationManager
import tlt.th.co.toyotaleasing.manager.db.CacheManager
import tlt.th.co.toyotaleasing.manager.db.MasterDataManager
import tlt.th.co.toyotaleasing.model.entity.LoanDataManager
import tlt.th.co.toyotaleasing.model.entity.location.LoanLocationDetailEntity
import tlt.th.co.toyotaleasing.model.entity.location.LoanLocationFilterEntity
import tlt.th.co.toyotaleasing.model.entity.masterdata.DealerOnline
import tlt.th.co.toyotaleasing.modules.payment.cart.common.CartItem
import tlt.th.co.toyotaleasing.util.PermissionUtils
import kotlin.coroutines.resume

class LoanLocationViewModel : BaseViewModel() {

    val whenDataLoaded = MutableLiveData<Model>()

    private val showroomList by lazy {
        MasterDataManager.getInstance().getDealerOnlineList()
    }

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

    fun getData(selectedType: Type = Type.SHOWROOM  , offer_dealercode : String = "" , offer_showroomcode : String = "" , default : Boolean = false) {
        GlobalScope.launch(Dispatchers.Main) {
            val locationFilter = LocationManager.getLoanLocationFilter()
            val isNearby = false
            val isDealOffer = default && offer_dealercode != "" && offer_showroomcode != ""
            when {
                isDealOffer -> getDealerShowroomOfferCode(offer_dealercode , offer_showroomcode) //getDealerCodeFilterLocations(locationFilter!!)
                isNearby -> getNearbyLocations()
                selectedType == Type.SHOWROOM ->  getShowRoomLocations(locationFilter)
            }

            LocationManager.clearLoanLocationFilter()
        }
    }

    fun getDealerShowroomOfferCode( offer_dealercode : String , offer_showroomcode : String ){
        GlobalScope.launch(Dispatchers.Main) {
            saveDeafaultByDealerCode(dealerCode = offer_dealercode , showroomCode =  offer_showroomcode)
            val locationFilter = LocationManager.getLoanLocationFilter()
            getDealerCodeFilterLocations(locationFilter!!)
        }
    }

    private fun getDealerCodeFilterLocations(locationFilter :LoanLocationFilterEntity ) {
        GlobalScope.launch(Dispatchers.Main) {
        }
    }


    fun saveDeafaultByDealerCode(
            dealerName: String = "",
            dealerCode: String = "",
            showroomCode: String = "",
            provinceIndex: Int = 0,
            amphurIndex: Int = 0,
            isNearly: Boolean = false
    ) {
        LocationManager.saveLoanLocationFilter(
                dealerName,
                showroomCode ,
                dealerCode ,
                provinceIndex,
                amphurIndex,
                isNearly
        )
    }

//    private fun getOfficeLocations() {
//        GlobalScope.launch(Dispatchers.Main) {
//            val locationList = if (PermissionUtils.isGrantedLocation()) {
//                getOfficeLocationsWithDistance()
//            } else {
//                getOfficeLocationsWithoutDistance()
//            }
//
//            whenDataLoaded.postValue(Model(
//                    locationList = locationList ?: listOf(),
//                    locationType = Type.OFFICE
//            ))
//        }
//    }
//
//    private suspend fun getOfficeLocationsWithDistance(): List<Location>? {
//        try {
//            val currentLocation = LocationManager.getLastKnowLocation()
//
//            return officeList?.map {
//                val destLocation = android.location.Location("dest").apply {
//                    latitude = it.lATITUDE?.toDouble() ?: 0.0
//                    longitude = it.lONGITUDE?.toDouble() ?: 0.0
//                }
//                val kilometers = currentLocation.distanceTo(destLocation).div(1000)
//                val distance = String.format("%.1f", kilometers)
//
//                Location(
//                        title = it.getCompanyName() ?: "",
//                        address = it.getAddress() ?: "",
//                        responsibility = it.getResponsibility() ?: "",
//                        tel = it.oFFICEPHONE1?.trim() ?: "",
//                        distance = distance,
//                        lat = it.lATITUDE ?: "",
//                        lng = it.lONGITUDE ?: "",
//                        type = Type.OFFICE
//                )
//            }
//        } catch (error: Exception) {
//            return listOf()
//        }
//    }
//
//    private fun getOfficeLocationsWithoutDistance(): List<Location>? {
//        return officeList?.map {
//            Location(
//                    title = it.getCompanyName() ?: "",
//                    address = it.getAddress() ?: "",
//                    responsibility = it.getResponsibility() ?: "",
//                    tel = it.oFFICEPHONE1?.trim() ?: "",
//                    distance = "-",
//                    lat = it.lATITUDE ?: "",
//                    lng = it.lONGITUDE ?: "",
//                    type = Type.OFFICE
//            )
//        }
//    }

    private fun getShowRoomLocations(locationFilter: LoanLocationFilterEntity?) {

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

    private fun getNearbyDealerLocations(dealer : List<DealerOnline>) {
        GlobalScope.launch(Dispatchers.Main) {

            val locationList : List<Location>  = getShowroomLocationsByNearlyDealer(dealer.get(0))?.take(NEARBY_LIMIT_ITEM)!!
            val nearlocationList : MutableList<Location>  = locationList.toMutableList()
            val showroomLocation : MutableList<Location> = mutableListOf<Location>()
            val isHave = locationList!!.filter {
                ( it.code!! ==  dealer.get(0).showroomCodeAmbit ) ?: false
            }

            if (isHave.size == 0) {
                showroomLocation!!.add(Location(
                        code = dealer.get(0).showroomCodeAmbit ?: "",
                        title = dealer.get(0).getNameByLocalize() ?: "",
                        address = dealer.get(0).getAddressByLocalize() ?: "",
                        branch = dealer.get(0).getBranchByLocalize() ?: "",
                        tel = dealer.get(0).telSs?.trim() ?: "",
                        telForCall = dealer.get(0).telSsCall?.trim() ?: "",
                        openTime = dealer.get(0).getOpenHrShowRoomByLocalize() ?: "",
                        distance = "0.0",
                        website = dealer.get(0).url1 ?: "",
                        lat = dealer.get(0).lat ?: "",
                        lng = dealer.get(0).lon ?: "",
                        type = Type.SHOWROOM
                ))
            }
            showroomLocation?.addAll(nearlocationList)
//                locationList?.map {
//                    Location(
//                            code = dealer.get(0).showroomCodeAmbit ?: "",
//                            title = dealer.get(0).getNameByLocalize() ?: "",
//                            address = dealer.get(0).getAddressByLocalize() ?: "",
//                            branch = dealer.get(0).getBranchByLocalize() ?: "",
//                            tel = dealer.get(0).telSs?.trim() ?: "",
//                            telForCall = dealer.get(0).telSsCall?.trim() ?: "",
//                            openTime = dealer.get(0).getOpenHrShowRoomByLocalize() ?: "",
//                            distance = "0.0",
//                            website = dealer.get(0).url1 ?: "",
//                            lat = dealer.get(0).lat ?: "",
//                            lng = dealer.get(0).lon ?: "",
//                            type = Type.SHOWROOM
//                    )
//                }      ?.sortedBy {
//                    it.distance.toDouble()
//                }

            whenDataLoaded.postValue(Model(
                    locationList = showroomLocation ?: listOf(),
                    locationType = Type.SHOWROOM
            ))
        }
    }


    private suspend fun getShowroomLocationByCodeAndName(code: String, nameTh: String, nameEn: String): List<Location>? {
        var locations: List<DealerOnline>? = showroomList
        val locationMerge = mutableListOf<DealerOnline>()

        if (code.isNotEmpty()) {
        }

        if (locationMerge.isNullOrEmpty() && nameTh.isNotEmpty()) {
        }

        if (locationMerge.isNullOrEmpty() && nameEn.isNotEmpty()) {
        }

        if (locationMerge.isNotEmpty()) {
            locations = locationMerge.distinctBy { it.showroomCodeAmbit }
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
                    code = it.showroomCodeAmbit ?: "",
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

    private suspend fun getShowroomLocationsByFilter(locationFilter: LoanLocationFilterEntity?): List<Location>? {

        val provinces = MasterDataManager.getInstance().getProvinceList()
        val provideCode = provinces?.getOrNull(locationFilter?.provinceIndex ?: -1)?.pROVINCECODE
                ?: ""
        val amphurs = MasterDataManager.getInstance().getAmphurByProvinceCode(provideCode)
        val amphurCode = amphurs?.getOrNull(locationFilter?.amphurIndex ?: -1)?.aMPHURCODE ?: ""

        val enableFlag = "Y"
        val locationMerge = mutableListOf<DealerOnline>()

        return listOf()
    }

    private suspend fun getShowroomLocationsDealerByFilter(locationFilter: LoanLocationFilterEntity? ): List<Location>? {
        val provinces = MasterDataManager.getInstance().getProvinceList()
        val provideCode = provinces?.getOrNull(locationFilter?.provinceIndex ?: -1)?.pROVINCECODE
                ?: ""
        val amphurs = MasterDataManager.getInstance().getAmphurByProvinceCode(provideCode)
        val amphurCode = amphurs?.getOrNull(locationFilter?.amphurIndex ?: -1)?.aMPHURCODE ?: ""


        if (PermissionUtils.isGrantedLocation()) {
            val currentLocation = LocationManager.getLastKnowLocation()

            return listOf()
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


    private  fun getShowroomLocationsByNearlyDealer(dealer : DealerOnline ): List<Location>? {
        val locationDefault = android.location.Location("GPS_Provider")
        locationDefault.latitude = dealer.lat?.toDouble() ?: 0.0
        locationDefault.longitude = dealer.lon?.toDouble() ?: 0.0
        val currentLocation = locationDefault

        val dealerList = getShowroomLocationsDealerOnlineWithDistance(currentLocation, showroomList)
        return dealerList
    }

    private fun getShowroomLocationsDealerOnlineWithDistance(
            currentLocation: android.location.Location,
            showroomList: List<DealerOnline>?
    ): List<Location>? {

        return showroomList?.map {
            val destLocation = android.location.Location("dest").apply {
                latitude = currentLocation.latitude?: 0.0 //it.lat?.toDouble() ?: 0.0
                longitude = currentLocation.longitude?: 0.0 //it.lon?.toDouble() ?: 0.0
            }

            val kilometers = currentLocation.distanceTo(destLocation).div(1000)
            val distance = String.format("%.1f", kilometers)

            Location(
                    code = it.showroomCodeAmbit ?: "",
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


    private fun getShowroomLocationsWithDistance(
            currentLocation: android.location.Location,
            showroomList: List<DealerOnline>?
    ): List<Location>? {
        return showroomList?.map {
            val destLocation = android.location.Location("dest").apply {
                latitude = it.lat?.toDouble() ?: 0.0
                longitude = it.lon?.toDouble() ?: 0.0
            }
            val kilometers = currentLocation.distanceTo(destLocation).div(1000)
            val distance = String.format("%.1f", kilometers)

            Location(
                    code = it.showroomCodeAmbit ?: "",
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
//
//    fun saveLocationDetail(item: Location) {
//        val locationDetail = LoanLocationDetailEntity(
//                id = item.code,
//                company = item.title,
//                branch = item.branch,
//                distance = item.distance,
//                address = item.address,
//                tel = item.tel,
//                telForCall = item.telForCall,
//                time = item.openTime,
//                website = item.website,
//                lat = item.lat,
//                lng = item.lng
//        )
//
//        LocationManager.saveLoanLocationDetail(locationDetail)
//    }

    fun updateShowroomLocationDetail(item: Location ) {
        val showroomList = ShowroomModel(
                showroom = item.title,
                showroomCode = item.code
        )
       // LoanDataManager.saveVerifyDataNewShowroomLocation(showroomList.showroomCode , showroomList.showroom)
    }

    data class Model(
            var locationList: List<Location> = listOf(),
            val locationType: Type = Type.SHOWROOM
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
            val type: Type = Type.SHOWROOM
    )

    enum class Type {
        SHOWROOM,
        NEARBY
    }

    data class ShowroomModel(
            var showroom: String = "",
            val showroomCode: String = ""
    )

    data class OfferDealLocate(
            var lat: String = "",
            val lon: String = ""
    )
}