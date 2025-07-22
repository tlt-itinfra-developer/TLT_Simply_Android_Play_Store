package tlt.th.co.toyotaleasing.modules.location.filter

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.manager.LocationManager
import tlt.th.co.toyotaleasing.manager.db.MasterDataManager
import tlt.th.co.toyotaleasing.util.AppUtils
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LocationFilterViewModel : BaseViewModel() {

    val whenDataLoaded = MutableLiveData<Model>()
    val whenProvinceChanged = MutableLiveData<List<String>>()
    val whenAmphurChanged = MutableLiveData<List<String>>()

    private val masterDataManager = MasterDataManager.getInstance()

    fun getData() {
        val model = Model(
                isSearchNearlyEnable = true
        )

        whenDataLoaded.postValue(model)
        initProvinceAndAmphur()
    }

    fun search(
            dealerName: String = "",
            provinceIndex: Int = 0,
            amphurIndex: Int = 0,
            isOfficeChecked: Boolean = true,
            isShowRoomChecked: Boolean = true,
            isServiceCenterChecked: Boolean = true,
            isRepairServiceChecked: Boolean = true,
            isNearly: Boolean = false
    ) {

        LocationManager.saveLocationFilter(
                dealerName,
                provinceIndex,
                amphurIndex,
                isOfficeChecked,
                isShowRoomChecked,
                isServiceCenterChecked,
                isRepairServiceChecked,
                isNearly
        )
    }

    fun amphurChanged(provinceIndex: Int = -1) {
        GlobalScope.launch(Dispatchers.Main) {
            val provinces = getProvinces(provinceIndex)
            val amphurs = getAmphurs(provinces?.firstOrNull())

            whenAmphurChanged.postValue(amphurs?.map { it.name })
        }
    }

    private fun initProvinceAndAmphur(provinceIndex: Int = -1) {
        GlobalScope.launch(Dispatchers.Main) {
            val provinces = getProvinces(provinceIndex)
            val amphurs = getAmphurs(provinces?.firstOrNull())

            whenProvinceChanged.postValue(provinces?.map { it.name })
            whenAmphurChanged.postValue(amphurs?.map { it.name })
        }
    }

    private suspend fun getProvinces(index: Int = -1) = suspendCoroutine<List<Province>?> {
        val provinces = masterDataManager.getProvinceList()

        val items = if (index == -1) {
            provinces?.map {
                Province(it.pROVINCECODE ?: "", it.getProvinceName() ?: "")
            }
        } else {
            val province = provinces?.getOrNull(index)
            listOf(province).map {
                Province(it?.pROVINCECODE ?: "", it?.getProvinceName() ?: "")
            }
        }

        it.resume(items)
    }

    private suspend fun getAmphurs(province: Province?) = suspendCoroutine<List<Amphur>?> {
        val items = masterDataManager
                .getAmphurByProvinceCode(province?.id ?: "")?.map {
                    Amphur(
                            it.aMPHURCODE ?: "",
                            it.getAmphurName() ?: "",
                            it.pOSTCODE ?: ""
                    )
                }

        it.resume(items)
    }

    data class Model(
            val dealerName: String = "",
            val isSelectOffice: Boolean = false,
            val isSelectShowRoom: Boolean = false,
            val isSelectServiceCenter: Boolean = true,
            val isSelectRepairService: Boolean = true,
            val isSearchNearlyEnable: Boolean = false,
            val isSearchEnable: Boolean = true,
            val isStaffApp: Boolean = AppUtils.isStaffApp()
    )

    data class Province(
            val id: String = "",
            val name: String = ""
    )

    data class Amphur(
            val id: String = "",
            val name: String = "",
            val postcode: String
    )
}