package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.carloan

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.api.tltloan.TLTLoanApiManager
import tlt.th.co.toyotaleasing.manager.db.MasterDataManager
import tlt.th.co.toyotaleasing.model.entity.LoanDataManager
import tlt.th.co.toyotaleasing.model.entity.masterdata.AlertWording
import tlt.th.co.toyotaleasing.model.request.PersonalRequest
import tlt.th.co.toyotaleasing.model.request.SyncPersonalRequest
import tlt.th.co.toyotaleasing.model.response.GetStepResponse
import tlt.th.co.toyotaleasing.model.response.PersonalResponse
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.carloan.common.PersonalData
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.center.common.MenuStepData
import java.lang.Exception
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LoanPersonalInfomationViewModel  : BaseViewModel() {
    private val apiLoanManager = TLTLoanApiManager.getInstance()
    private val masterDataManager = MasterDataManager.getInstance()
    val whenMariatalDataLoaded = MutableLiveData<maritalModel>()
    val whenPropertyDataLoaded = MutableLiveData<propertyModel>()
    val whenSuboccupatoinDataLoaded = MutableLiveData<String>()
    val whenOccupationDataLoaded = MutableLiveData<String>()
    val whenDataLoadedSuccess = MutableLiveData<Boolean>()
    val whenDataLoadedFailure = MutableLiveData<String>()
    val whenDataPersonalLoad = MutableLiveData<PersonalData>()

    val whenSyncSuccessData = MutableLiveData<MenuStepData>()
    val whenSyncFailureShowMsg = MutableLiveData<MenuStepData>()
    val whenSyncSuccess = MutableLiveData<Boolean>()

    val whenEmpLivDM = MutableLiveData<List<PersonalData>>()
    val whenDataSpinnerLoaded = MutableLiveData<AddressIndexModel>()

    var dataMaritals : List<maritalItem> = listOf()
    var dataPropertys : List<propertyItem>  = listOf()
    var dataOccupations : List<occupationItem> = listOf()
    var datasubOccupations : List<suboccupationItem> = listOf()

    val whenProvinceLoaded = MutableLiveData<List<String>>()
    val whenAmphurload = MutableLiveData<List<String>>()
    val whenPostcodeLoaded = MutableLiveData<String>()
    private val provinceList = ArrayList<Province>()
    private val amphurList = ArrayList<Amphur>()

    val whenLoadChangePostcode =  MutableLiveData<AddressIndexModel>()
    val whenDataLoadedMessage = MutableLiveData<String>()
    val whenDataMasterHintLoad = MutableLiveData<List<HintInfoModel>>()


    fun initProvideData(refNo : String = "") {
        GlobalScope.launch(Dispatchers.Main) {
            getPersonal(refNo)
        }
    }


    fun getMasterHint() {
        GlobalScope.launch(Dispatchers.Main) {
        }
    }

    fun getData(item : List<PersonalData>) {
        GlobalScope.launch(Dispatchers.Main) {
            val index = -1
            val maritals = getMaritalItem(item.get(0).marital_id , index)
            val propertys = getPropertyItem(item.get(0).prop_id , index )

            if (maritals != null) {
                dataMaritals = maritals
            }
            if (propertys != null) {
                dataPropertys = propertys
            }

            var inxM = 1
            var inxP = 1

            var lindex = 0
            lindex = 1
            dataMaritals.forEach{
                    if( it.mARITALID == item.get(0).marital_id ) {
                        inxM = lindex
                    }
                lindex +=1
            }
            lindex = 1
            dataPropertys.forEach{
                if( it.pROPERTYID == item.get(0).prop_id ) {
                    inxP = lindex
                }
                lindex +=1
            }
            lindex = 1

            whenMariatalDataLoaded.postValue( maritalModel(maritals?.map { maritalItem( mARITAL = it.mARITAL ,  mARITALID = it.mARITALID )}!!, inxM ))
            whenPropertyDataLoaded.postValue( propertyModel(propertys?.map { propertyItem( pROPERTY = it.pROPERTY ,  pROPERTYID = it.pROPERTYID )}!!, inxP ))
            whenSuboccupatoinDataLoaded.postValue( item.get(0).sub_oc)
            whenOccupationDataLoaded.postValue( item.get(0).oc)
            whenEmpLivDM.postValue(item)
        }
    }

    private suspend fun getMaritalItem(id : String , index : Int = -1) = suspendCoroutine<List<maritalItem>?> {
        val maritalList = masterDataManager.getMaritalList()
        it.resume(listOf())
    }

    private suspend fun getPropertyItem(id : String , index : Int = -1) = suspendCoroutine<List<propertyItem>?> {
        val propertyList = masterDataManager.getPropertyList()
        it.resume(listOf())
    }


    private suspend fun getSuboccupationItem(id : String , ocID : String , index : Int = -1) = suspendCoroutine<List<suboccupationItem>?> {
        val subOccupationList = masterDataManager.getSubOccupationList()
            it.resume(listOf())
    }

    private suspend fun getOccupationItem(id : String , index : Int = -1) = suspendCoroutine<List<occupationItem>?> {
        val occupationList = masterDataManager.getOccupationList()
        it.resume(listOf())
    }


    fun getPersonal(refNo : String = "") {
      val request = PersonalRequest.build(ref_no = refNo)

      whenLoading.value = true

      apiLoanManager.personal(request) {isError, result , step, msg  ->
                  whenLoading.value = false
                  if (isError) {
                      if (result != "device logon") {
                          whenDataLoadedFailure.value = result
                      }
                      return@personal
                  }

                  if(msg.length > 0 ) {
                      whenDataLoadedMessage.postValue(msg)
                  }

                  val items = JsonMapperManager.getInstance()
                          .gson.fromJson(result, Array<PersonalResponse>::class.java)

                  if (items.isEmpty()) {
                      whenDataLoadedFailure.value = ""
                      return@personal
                  }

                 try {
                    val personalList = PersonalLastItem(items.toList())
                    getData(personalList)
                    personalList.let {
                        getDefaultAddressData(personalList.get(0))
                    }
                    whenDataLoadedSuccess.value = true
//                    whenDataLoadedData.postValue(personalList)
                 }catch (e : Exception){
                    e.message
                 }
       }
    }

      fun  PersonalLastItem(res: List<PersonalResponse>) : List<PersonalData> {

          return listOf()

        }

    /////////////// Start Address Filter ////////////////

    fun getDefaultAddressData( item : PersonalData) {
        GlobalScope.launch(Dispatchers.Main) {
            provinceChange()
            amphurChange()
            defaultData(item)
        }
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
        if(position != -1) {
            val province = provinceList[position]
            val amphurs = MasterDataManager.getInstance()
                    .getAmphurByProvinceCode(province.id)?.map {
                        Amphur(it.aMPHURCODE!!, it.getAmphurName()!!, it.pOSTCODE!!)
                    }

            amphurList.clear()
            amphurList.addAll(amphurs!!)
        }
        whenAmphurload.postValue(amphurList.map { "${it.name} (${it.postcode})" })
    }

    fun amphurChange(position: Int = 0) {
        if(position != -1) {
            val amphur = amphurList[position]
            whenPostcodeLoaded.postValue(amphur.postcode)
        }else{
            whenPostcodeLoaded.postValue("")
        }
    }

    private fun defaultData( item : PersonalData) {
        var provincePosition = -1
        var districtPosition = -1

        if(item.postcode.isNotEmpty()){
           var amp_pro = MasterDataManager.getInstance()
                    .getAmphurProvince(item.postcode)?.map {
                        ProvincebyPostcode( proCode = it.pROVINCECODE!!,
                                            ampCode  = it.aMPHURCODE!!)
                    }

            if(amp_pro!!.size > 0 ) {
                if (item.province_code.isEmpty() &&  amp_pro.get(0).proCode.isNotEmpty()) {
                    item.province_code = amp_pro.get(0).proCode
                }
                if (item.aumphur_code.isEmpty() &&  amp_pro.get(0).ampCode.isNotEmpty()) {
                    item.aumphur_code = amp_pro.get(0).ampCode
                }
            }
        }

        if (item.province_code.isNotEmpty()) {
            provincePosition = provinceList.indexOfFirst { it.id == item.province_code }
        }

        provinceChange(provincePosition)

        if (item.aumphur_code.isNotEmpty()) {
            districtPosition = amphurList.indexOfFirst { it.id == item.aumphur_code }
        }

        val data = AddressIndexModel(
                provincePosition,
                districtPosition
        )

        whenDataPersonalLoad.postValue(item)
        whenDataSpinnerLoaded.postValue(data)
    }


    /////// Change Only Postcode /////////
    fun ChangePostcodeAddressData( postcode :  String) {
        GlobalScope.launch(Dispatchers.Main) {
            provinceAmphurChangeByPostcode(postcode)
            ChangePostcode(postcode)
        }
    }

    fun provinceAmphurChangeByPostcode( postcode :  String ) {

        if (provinceList.isEmpty()) {
            val provinces = MasterDataManager.getInstance()
                    .getProvinceList()?.map {
                        Province(postcode!!, it.getProvinceName()!!)
                    }

            provinceList.addAll(provinces!!)
            whenProvinceLoaded.postValue(provinceList.map { it.name })
        }
        val province = provinceList[0]
        val amphurs = MasterDataManager.getInstance()
                .getAmphurByProvinceCode(province.id)?.map {
                    Amphur(it.aMPHURCODE!!, it.getAmphurName()!!, postcode)
                }
        amphurList.clear()
        amphurList.addAll(amphurs!!)

        whenAmphurload.postValue(amphurList.map { "${it.name} (${postcode})" })
    }

    fun ChangePostcode( postcode :  String) {
        var provincePosition = -1
        var districtPosition = -1


        if(postcode.isNotEmpty()){
            var amp_pro = MasterDataManager.getInstance()
                    .getAmphurProvince(postcode)?.map {
                        ProvincebyPostcode( proCode = it.pROVINCECODE!!,
                                ampCode  = it.aMPHURCODE!!)
                    }

            if(amp_pro!!.size > 0 ) {
                if (amp_pro.get(0).proCode.isNotEmpty()) {
                    provincePosition = provinceList.indexOfFirst { it.id == amp_pro.get(0).proCode }
                }

                provinceChange(provincePosition)

                if (amp_pro.get(0).ampCode.isNotEmpty()) {
                    districtPosition = amphurList.indexOfFirst { it.id == amp_pro.get(0).ampCode }
                }
            }

            val data = AddressIndexModel(
                    provincePosition,
                    districtPosition
            )
            whenLoadChangePostcode.postValue(data)
        }
    }


    /////////////// End Address Filter ////////////////

    fun getPositionProvinceAumphur(items :  List<PersonalData>) : List<PersonalData> {
        if(items.get(0).aumphur_code!="" && items.get(0).aumphur_code.toInt() >= 0 ) {
            val amphur = amphurList[items.get(0).aumphur_code.toInt()]
            items.get(0).aumphur_code = amphur.id
        }
        if(items.get(0).province_code!="" && items.get(0).province_code.toInt() >= 0 ) {
            val province = provinceList[items.get(0).province_code.toInt()]
            items.get(0).province_code = province.id
        }
        return  items
    }

    fun saveList(it: List<PersonalData>) {
        clearDataInfo()
        var items = getPositionProvinceAumphur(it)
        LoanDataManager.savePersonalList(items)
    }

    fun clearDataInfo() {

    }


    fun SyncPersonal( refNo : String , data : List<PersonalData>) {
        var item = getPositionProvinceAumphur(data)
        val request = SyncPersonalRequest.build(
                                         ref_no = refNo ,
                                         marital_status   = item.get(0).marital_status ,
                                         marital_id   = item.get(0).marital_id,
                                         prop_type   = item.get(0).prop_name ,
                                         prop_id   = item.get(0).prop_id ,
                                         living_year  =  item.get(0).liv_year ,
                                         living_month  =      item.get(0).liv_month ,
                                         sub_occupation   = item.get(0).sub_oc ,
                                         sub_occupation_id  = item.get(0).sub_oc_id ,
                                         emp_year   = item.get(0).emp_year ,
                                         emp_month   = item.get(0).emp_month ,
                                         income   = item.get(0).income ,
                                         outcome   = item.get(0).other_income ,
                                         address   = item.get(0).real_address ,
                                         postcode  =  item.get(0).postcode ,
                                         lat =  item.get(0).lat ,
                                         long =  item.get(0).lng ,
                                         amphur = item.get(0).aumphur.substringBefore("(")  ,
                                         amphurCode =item.get(0).aumphur_code ,
                                         province = item.get(0).province ,
                                         provinceCode = item.get(0).province_code ,
                                         company_detail = item.get(0).company_detail)
        whenLoading.value = true
        apiLoanManager.syncPersonal(request) {isError, result , step, msg  ->
            whenLoading.value = false
            if (isError) {
                whenLoading.value = false
                if (result != "device logon") {
                    whenDataLoadedFailure.value = result
                }
                if(msg.length > 0 ) {
                    whenDataLoadedMessage.postValue(msg)
                }
                return@syncPersonal
            }

            if(msg.length > 0 ) {
                whenDataLoadedMessage.postValue(msg)
            }

            clearDataInfo()

            val item = JsonMapperManager.getInstance()
                    .gson.fromJson(result, GetStepResponse::class.java)

            var data =  MenuStepData(
                    status = item.status ,
                    ref_id = item.ref_id ,
                    ref_url = item.ref_url ,
                    step = step)

            if(data.status == "N") {
                whenLoading.value = false
                whenSyncFailureShowMsg.postValue(data)
                return@syncPersonal
            }else{
                whenLoading.value = false
                whenSyncSuccess.value = true
                whenSyncSuccessData.postValue(data)
            }
        }
    }


    data class maritalItem(
            val mARITALID: String = "",
            val mARITAL: String = ""
    )

    data class maritalModel(
            val list: List<maritalItem> ,
            val id: Int
    )

    data class propertyItem(
            val pROPERTYID: String = "",
            val pROPERTY: String = ""
    )

    data class propertyModel(
            val list: List<propertyItem>  ,
            val id: Int
    )
    data class suboccupationItem(
            val oCID: String = "",
            val sUBOCCODE: String = "",
            val sUBOCNAME: String = ""
    )

    data class suboccupationModel(
            val list: List<suboccupationItem>   ,
            val id: Int
    )

    data class occupationItem(
            val oCID: String = "",
            val oCNAME: String = ""
    )

    data class occupationModel(
            val list:  List<occupationItem>   ,
            val id: Int
    )

    data class Province(
            val id: String = "",
            val name: String = ""
    )

    data class ProvincebyPostcode(
            val proCode: String = "",
            val ampCode: String = ""
    )

    data class Amphur(
            val id: String = "",
            val name: String = "",
            val postcode: String
    )

    data class AddressIndexModel(
            val indx_pro: Int = 0,
            val indx_aum: Int = 0
    )

    data class HintInfoModel(
            val type: String = "" ,
            val description : String = ""
    )


}
