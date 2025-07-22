package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.geo.location.filter

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.manager.LocationManager
import tlt.th.co.toyotaleasing.manager.db.MasterDataManager
import tlt.th.co.toyotaleasing.model.entity.masterdata.DealerOnline
import tlt.th.co.toyotaleasing.util.AppUtils
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LoanLocationFilterViewModel : BaseViewModel() {

    val whenDataLoaded = MutableLiveData<Model>()
    val whenProvinceChanged = MutableLiveData<List<String>>()
    val whenAmphurChanged = MutableLiveData<List<String>>()
    val whenLoadShowroom = MutableLiveData<List<String>>()

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
            showroomCode: String = "",
            dealerCode: String = "",
            provinceIndex: Int = 0,
            amphurIndex: Int = 0,
            isNearly: Boolean = false
    ) {
        LocationManager.saveLoanLocationFilter(
                dealerName,
                showroomCode  ,
                dealerCode ,
                provinceIndex,
                amphurIndex,
                isNearly
        )
    }

    fun showroom(index: Int = -1) {
        GlobalScope.launch(Dispatchers.Main) {
            val showroom = getDealer(index)
            whenLoadShowroom.postValue(showroom?.map { it.name + it.location })
        }
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

    private suspend fun getDealer(index: Int = -1) = suspendCoroutine<List<DealerOnline>?> {
        val showrooms = masterDataManager.getDealerOnlineList()

        it.resume(listOf())
    }

    data class Model(
            val dealerName: String = "",
            val isSelectOffice: Boolean = false,
            val isSelectShowRoom: Boolean = false,
            val isSearchNearlyEnable: Boolean = false,
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

    data class DealerOnline(
            val id: String = "",
            val name: String = "" ,
            val location: String = ""
    )
}