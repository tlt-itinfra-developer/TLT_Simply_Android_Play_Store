package tlt.th.co.toyotaleasing.modules.editaddress.taxdelivery

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager
import tlt.th.co.toyotaleasing.manager.db.CacheManager
import tlt.th.co.toyotaleasing.manager.db.MasterDataManager
import tlt.th.co.toyotaleasing.model.request.ChangeProfileRequest
import tlt.th.co.toyotaleasing.model.response.GetDataTaxResponse
import tlt.th.co.toyotaleasing.modules.editaddress.installment.EditInstallmentAdressViewModel
import tlt.th.co.toyotaleasing.util.AppUtils

class EditTaxDeliveryAdressViewModel : ViewModel() {
    val whenLoading = MutableLiveData<Boolean>()
    val whenDataLoaded = MutableLiveData<Model>()
    val whenSubmitFormSuccess = MutableLiveData<Boolean>()
    val whenSubmitFormFailure = MutableLiveData<String>()
    val whenProvinceLoaded = MutableLiveData<List<String>>()
    val whenDistrictReload = MutableLiveData<List<String>>()
    val whenPostcodeLoaded = MutableLiveData<String>()

    private var tax: GetDataTaxResponse? = null
    private val provinceList = ArrayList<EditInstallmentAdressViewModel.Province>()
    private val amphurList = ArrayList<EditInstallmentAdressViewModel.Amphur>()

    fun getDefaultData() {
        tax = CacheManager.getCacheTax()

        tax?.let {
            provinceChange()
            amphurChange()
            defaultData()
        }
    }

    private fun defaultData() {
        var provincePosition = 0
        var districtPosition = 0

        tax?.let { tax ->
            if (tax.pROVINCECODE?.isNotEmpty() == true) {
                provincePosition = provinceList.indexOfFirst { it.id == tax.pROVINCECODE }
            }

            provinceChange(provincePosition)

            if (tax.aMPHURCODE?.isNotEmpty() == true) {
                districtPosition = amphurList.indexOfFirst { it.id == tax.aMPHURCODE }
            }

            /*** Sirivimon : Set All default Simply#2 13/03/2020 ***/
//            val data = Model(
//                    tax.aDDRESSLINE1 ?: "",
//                    tax.aDDRESSLINE2 ?: "",
//                    tax.aDDRESSLINE3 ?: "",
//                    tax.aDDRESSLINE4 ?: "",
//                    provincePosition,
//                    districtPosition,
//                    tax.pOSTCODE ?: ""
//            )

            val data = Model(
                 "",
                     "",
                    "",
                    "",
                    0,
                    0,
                     ""
            )

            whenDataLoaded.postValue(data)
        }
    }

    fun provinceChange(position: Int = 0) {
        if (provinceList.isEmpty()) {
            val provinces = MasterDataManager.getInstance()
                    .getProvinceList()?.map {
                        EditInstallmentAdressViewModel.Province(it.pROVINCECODE!!, it.getProvinceName()!!)
                    }
            provinceList.addAll(provinces!!)

            whenProvinceLoaded.postValue(provinceList.map { it.name })
        }

        val province = provinceList[position]
        val amphurs = MasterDataManager.getInstance()
                .getAmphurByProvinceCode(province.id)?.map {
                    EditInstallmentAdressViewModel.Amphur(it.aMPHURCODE!!, it.getAmphurName()!!, it.pOSTCODE!!)
                }

        amphurList.clear()
        amphurList.addAll(amphurs!!)

        whenDistrictReload.postValue(amphurList.map { "${it.name} (${it.postcode})" })
    }

    fun amphurChange(position: Int = 0) {
        val amphur = amphurList[position]
        whenPostcodeLoaded.postValue(amphur.postcode)
    }

    fun submitForm(address_line_1: String,
                   address_line_2: String,
                   address_line_3: String,
                   address_line_4: String,
                   districtPosition: Int,
                   provincePosition: Int,
                   postcode: String) {
        whenLoading.value = true

        val district = amphurList[districtPosition]
        val province = provinceList[provincePosition]

        val carContractNumber = CacheManager.getCacheCar()?.contractNumber ?: ""
        val request = ChangeProfileRequest.buildForTaxDelivery(
                contractNumber = carContractNumber,
                addressLine1 = address_line_1,
                addressLine2 = address_line_2,
                addressLine3 = address_line_3,
                addressLine4 = address_line_4,
                postcode = postcode,
                provinceCode = province.id,
                amphurCode = district.id)

        TLTApiManager.getInstance()
                .changeProfile(request) { isError, result ->
                    whenLoading.value = false

                    if (isError) {
                        if (result != "device logon") {
                            whenSubmitFormFailure.value = result
                        }
                        return@changeProfile
                    }

                    whenSubmitFormSuccess.value = true
                }
    }

    data class Model(
            val address: String = "",
            val soi: String = "",
            val road: String = "",
            val subdistrict: String = "",
            val provincePosition: Int = 0,
            val districtPosition: Int = 0,
            val postcode: String = "",
            val isStaffApp: Boolean = AppUtils.isStaffApp()
    )
}