package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.basicdetail

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.api.tltloan.TLTLoanApiManager
import tlt.th.co.toyotaleasing.manager.db.MasterDataManager
import tlt.th.co.toyotaleasing.model.entity.FillInfoDataEntity
import tlt.th.co.toyotaleasing.model.entity.LoanDataManager
import tlt.th.co.toyotaleasing.model.request.CheckStepRequest
import tlt.th.co.toyotaleasing.model.request.FillInfoRequest
import tlt.th.co.toyotaleasing.model.request.SyncInfoRequest
import tlt.th.co.toyotaleasing.model.response.FillInfoResponse
import tlt.th.co.toyotaleasing.model.response.GetStepResponse
import tlt.th.co.toyotaleasing.model.response.SyncInfoResponse
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.basicdetail.common.FillInfoData
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.center.common.MenuStepData

class BasicInformationViewModel  : BaseViewModel() {

    private val apiLoanManager = TLTLoanApiManager.getInstance()
    val whenDataLoadedFailure = MutableLiveData<String>()
    val whenDataLoadedMessage = MutableLiveData<String>()
    val whenSyncSuccessData = MutableLiveData<MenuStepData>()
    val whenSyncFailureShowMsg = MutableLiveData<MenuStepData>()
    val whenSyncSuccess = MutableLiveData<Boolean>()
    private val provinceList = ArrayList<Province>()
    private val amphurList = ArrayList<Amphur>()
    val whenProvinceLoaded = MutableLiveData<List<String>>()
    val whenAmphurload = MutableLiveData<List<String>>()
    val whenPostcodeLoaded = MutableLiveData<String>()
    val whenDataFillInfoLoad = MutableLiveData<FillInfoData>()
    val whenDataSpinnerLoaded = MutableLiveData<AddressIndexModel>()
    val whenLoadChangePostcode =  MutableLiveData<AddressIndexModel>()
    val whenDataLoadedSuccess = MutableLiveData<Boolean>()
    val whenDataDopaLoadedSuccess = MutableLiveData<StatusSyncInfoModel>()

    fun initProvideData(refNo : String = "") {
        GlobalScope.launch(Dispatchers.Main) {
            getFillInfo(refNo)
        }
    }

    fun getFillInfo(refNo : String = "") {

        val request = FillInfoRequest.build(ref_no = refNo)
        whenLoading.value = true

        apiLoanManager.fillInfo(request) {isError, result , step, msg  ->
            whenLoading.value = false
            if (isError) {
                if (result != "device logon") {
                    whenDataLoadedFailure.value = result
                }
                return@fillInfo
            }

            if(msg.length > 0 ) {
                whenDataLoadedMessage.postValue(msg)
            }

            val items = JsonMapperManager.getInstance()
                    .gson.fromJson(result, FillInfoResponse::class.java)

            try {
                val fillInfoList = FillInfoLastItem(items)
                fillInfoList.let {
                    getDefaultAddressData(fillInfoList)
                }
                whenDataLoadedSuccess.value = true
//                    whenDataLoadedData.postValue(personalList)
            }catch (e : Exception){
                e.message
            }
        }
    }

    fun  FillInfoLastItem(res: FillInfoResponse) : FillInfoData {
        val list = null
        var isHave = false

        return FillInfoData()

    }

    fun getDefaultAddressData( item : FillInfoData) {
        GlobalScope.launch(Dispatchers.Main) {
            provinceChange()
            amphurChange()
            defaultData(item)
        }
    }
    fun provinceChange(position: Int = -1) {
        try{
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
        }catch (e: Exception){
            e.message
        }
    }

    fun amphurChange(position: Int = -1) {
        if(position != -1) {
            val amphur = amphurList[position]
            whenPostcodeLoaded.postValue(amphur.postcode)
        }else{
            whenPostcodeLoaded.postValue("")
        }
    }

    private fun defaultData( item : FillInfoData) {
        var provincePosition = -1
        var districtPosition = -1

        if(item.pOSTCODE.isNotEmpty()){
            var amp_pro = MasterDataManager.getInstance()
                    .getAmphurProvince(item.pOSTCODE)?.map {
                        ProvincebyPostcode( proCode = it.pROVINCECODE!!,
                                ampCode  = it.aMPHURCODE!!)
                    }

            if(amp_pro!!.size > 0 ) {
                if (item.pROVINCECODE.isEmpty() &&  amp_pro.get(0).proCode.isNotEmpty()) {
                    item.pROVINCECODE = amp_pro.get(0).proCode
                }
                if (item.aMPHURCODE.isEmpty() &&  amp_pro.get(0).ampCode.isNotEmpty()) {
                    item.aMPHURCODE = amp_pro.get(0).ampCode
                }
            }
        }

        if (item.pROVINCECODE.isNotEmpty()) {
            provincePosition = provinceList.indexOfFirst { it.id == item.pROVINCECODE }
        }

        provinceChange(provincePosition)

        if (item.aMPHURCODE.isNotEmpty()) {
            districtPosition = amphurList.indexOfFirst { it.id == item.aMPHURCODE }
        }

        val data = AddressIndexModel(
                provincePosition,
                districtPosition
        )

        whenDataFillInfoLoad.postValue(item)
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



    fun getPositionProvinceAumphur(items :  List<FillInfoDataEntity>) : List<FillInfoDataEntity> {
        if(items.get(0).aMPHURCODE!="" && items.get(0).aMPHURCODE.toInt() >= 0 ) {
            val amphur = amphurList[items.get(0).aMPHURCODE.toInt()]
            items.get(0).aMPHURCODE = amphur.id
        }
        if(items.get(0).pROVINCECODE!="" && items.get(0).pROVINCECODE.toInt() >= 0 ) {
            val province = provinceList[items.get(0).pROVINCECODE.toInt()]
            items.get(0).pROVINCECODE = province.id
        }
        return  items
    }



    fun saveList(it: List<FillInfoDataEntity>) {
        clearDataInfo()
        var items = getPositionProvinceAumphur(it)
        LoanDataManager.saveFillInfoList(items)
    }

    fun clearDataInfo() {

    }

    fun SyncInfo( refNo : String , data : List<FillInfoDataEntity>) {
        var item = getPositionProvinceAumphur(data)
        val request = SyncInfoRequest.build(
                refID = refNo ,
                householdID = item.get(0).hOUSEHOLDID ,
                laserID   = (item.get(0).lASERID).replace("-" , "") ,
                address   = item.get(0).rEALADDRESS ,
                postcode  =  item.get(0).pOSTCODE ,
                lat =  item.get(0).lat ,
                long =  item.get(0).lng ,
                amphur = item.get(0).aMPHUR.substringBefore("(")  ,
                amphurCode =item.get(0).aMPHURCODE ,
                province = item.get(0).pROVINCE ,
                provinceCode = item.get(0).pROVINCECODE ,
                name = item.get(0).name ,
                surname = item.get(0).surname)
        whenLoading.value = true
        apiLoanManager.syncInfo(request) {isError, result , step, msg  ->
                 whenLoading.value = false
                    if (isError) {
                        if (result != "device logon") {
                            whenDataLoadedFailure.value = result
                        }
                        if(msg.length > 0 ) {
                            whenDataLoadedMessage.postValue(msg)
                        }
                        return@syncInfo
                    }

                    if(msg.length > 0 ) {
                        whenDataLoadedMessage.postValue(msg)
                    }

                    val item = JsonMapperManager.getInstance()
                            .gson.fromJson(result, SyncInfoResponse::class.java)

                    var data =  StatusSyncInfoModel(
                            status = item.sTATUS ,
                            count = item.cOUNT ,
                            dopaDesc = item.dopaDesc() ,
                            step = step )

                     whenDataDopaLoadedSuccess.postValue(data)

                }
    }

    fun CheckStepAPI(refID : String)  {
        whenLoading.postValue(true)
        apiLoanManager
                .checkStep(CheckStepRequest.build(refID =  refID))  { isError, result, step, msg  ->
                    whenLoading.value = false
                    if (isError) {
                        whenLoading.value = false
                        if (result != "device logon") {
                            whenDataLoadedFailure.value = result
                        }
                        if(msg.length > 0 ) {
                            whenDataLoadedMessage.postValue(msg)
                        }
                        return@checkStep
                    }

                    if(msg.length > 0 ) {
                        whenDataLoadedMessage.postValue(msg)
                    }

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
                        return@checkStep
                    }else{
                        whenLoading.value = false
                        whenSyncSuccess.value = true
                        whenSyncSuccessData.postValue(data)
                    }
                }

    }

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

    data class StatusSyncInfoModel(
            var count: String = "",
            var dopaDesc: String = "",
            var status: String = "" ,
            var step: String = ""
    )
}