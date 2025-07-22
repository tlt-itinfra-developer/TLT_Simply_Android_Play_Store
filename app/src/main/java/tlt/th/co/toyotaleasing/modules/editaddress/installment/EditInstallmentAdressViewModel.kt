package tlt.th.co.toyotaleasing.modules.editaddress.installment

import androidx.lifecycle.MutableLiveData
import android.net.Uri
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager
import tlt.th.co.toyotaleasing.manager.db.CacheManager
import tlt.th.co.toyotaleasing.manager.db.MasterDataManager
import tlt.th.co.toyotaleasing.model.request.ChangeProfileRequest
import tlt.th.co.toyotaleasing.util.ImageUtils

class EditInstallmentAdressViewModel : BaseViewModel() {
    val whenDataLoaded = MutableLiveData<Model>()
    val whenSubmitFormSuccess = MutableLiveData<Boolean>()
    val whenSubmitFormFailure = MutableLiveData<String>()
    val whenProvinceLoaded = MutableLiveData<List<String>>()
    val whenDistrictReload = MutableLiveData<List<String>>()
    val whenPostcodeLoaded = MutableLiveData<String>()

    private val provinceList = ArrayList<Province>()
    private val amphurList = ArrayList<Amphur>()
    private val installment = CacheManager.getCacheInstallment()!!

    fun getDefaultData() {
        provinceChange()
        amphurChange()
        defaultData()
    }

    fun provinceChange(position: Int = 0) {

        if (provinceList.isEmpty()) {
            val provinces = MasterDataManager.getInstance()
                    .getProvinceList()?.map {
                        Province(it.pROVINCECODE!!, it.getProvinceName()!!)
                    }

            provinceList.addAll(provinces!!)

            whenProvinceLoaded.postValue(provinceList.map { it.name })
        }

        val province = provinceList[position]
        val amphurs = MasterDataManager.getInstance()
                .getAmphurByProvinceCode(province.id)?.map {
                    Amphur(it.aMPHURCODE!!, it.getAmphurName()!!, it.pOSTCODE!!)
                }

        amphurList.clear()
        amphurList.addAll(amphurs!!)

        whenDistrictReload.postValue(amphurList.map { "${it.name} (${it.postcode})" })
    }

    fun amphurChange(position: Int = 0) {
        val amphur = amphurList[position]
        whenPostcodeLoaded.postValue(amphur.postcode)
    }

    private fun defaultData() {
        var provincePosition = 0
        var districtPosition = 0

//        if (installment.pROVINCECODE.isNotEmpty()) {
//            provincePosition = provinceList.indexOfFirst { it.id == installment.pROVINCECODE }
//        }

        provinceChange(provincePosition)

//        if (installment.aMPHURCODE.isNotEmpty()) {
//            districtPosition = amphurList.indexOfFirst { it.id == installment.aMPHURCODE }
//        }

        /*** Sirivimon : Set All default Simply#2 13/03/2020 ***/

//        val data = Model(
//                installment.aDDRESSLINE1,
//                installment.aDDRESSLINE2,
//                installment.aDDRESSLINE3,
//                installment.aDDRESSLINE4,
//                provincePosition,
//                districtPosition,
//                postcode = installment.pOSTCODE
//        )

        val data = Model(
                "",
                "",
                "",
               "",
                0,
                0,
                postcode = ""
        )

                whenDataLoaded.postValue(data)
    }

    fun submitForm(address_line_1: String,
                   address_line_2: String,
                   address_line_3: String,
                   address_line_4: String,
                   districtPosition: Int,
                   provincePosition: Int,
                   postcode: String,
                   isUseInEveryContract: Boolean,
                   imageUri: Uri) {
        whenLoading.value = true

        val district = amphurList[districtPosition]
        val province = provinceList[provincePosition]

        val request = ChangeProfileRequest.buildForContractAddress(
                isUseInEveryContract = isUseInEveryContract,
                contractNumber = installment.eXTCONTRACT,
                addressLine1 = address_line_1,
                addressLine2 = address_line_2,
                addressLine3 = address_line_3,
                addressLine4 = address_line_4,
                provinceCode = province.id,
                amphurCode = district.id,
                postcode = postcode,
                imageBase64 = ImageUtils.encodeToBase64(imageUri))

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
            val postcode: String = ""
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