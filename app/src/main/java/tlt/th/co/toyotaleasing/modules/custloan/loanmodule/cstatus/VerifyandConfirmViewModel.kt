package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.cstatus

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.LocationManager
import tlt.th.co.toyotaleasing.manager.api.tltloan.TLTLoanApiManager
import tlt.th.co.toyotaleasing.manager.db.MasterDataManager
import tlt.th.co.toyotaleasing.model.entity.LoanDataManager
import tlt.th.co.toyotaleasing.model.request.GetVerifyRequest
import tlt.th.co.toyotaleasing.model.request.SyncVerifyRequest
import tlt.th.co.toyotaleasing.model.response.GetStepResponse
import tlt.th.co.toyotaleasing.model.response.GetVerifyResponse
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.center.common.MenuStepData
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.cstatus.common.VerifyConfirmData

class VerifyandConfirmViewModel  : BaseViewModel() {
    private val apiLoanManager = TLTLoanApiManager.getInstance()
    val whenDataLoaded = MutableLiveData<VerConirmDataModel>()
    val whenDataLoadedTemp = MutableLiveData<VerifyConfirmData>()
    val whenDataLoadedSuccess = MutableLiveData<Boolean>()
    val whenDataLoadedFailure = MutableLiveData<String>()
    val whenSyncSuccess = MutableLiveData<Boolean>()
    val whenDataSpinnerLoaded = MutableLiveData<AddressAllTypeIndexModel>()
    val whenDataSpinnerAddressLoaded  = MutableLiveData<AddressAllTypeIndexModel>()
    val whenDataSpinnerRegisLoaded = MutableLiveData<AddressAllTypeIndexModel>()
    val whenDataSpinnerMailingLoaded = MutableLiveData<AddressAllTypeIndexModel>()

    val whenAddrProvinceLoaded = MutableLiveData<List<String>>()
    val whenAddrAumphurLoaded = MutableLiveData<List<String>>()
    val whenAddrPostcodeLoaded = MutableLiveData<String>()

    val whenRegisProvinceLoaded = MutableLiveData<List<String>>()
    val whenRegisAumphurLoaded = MutableLiveData<List<String>>()
    val whenRegisPostcodeLoaded = MutableLiveData<String>()

    val whenMailProvinceLoaded = MutableLiveData<List<String>>()
    val whenMailAumphurLoaded = MutableLiveData<List<String>>()
    val whenMailPostcodeLoaded = MutableLiveData<String>()

    val whenSyncFailureShowMsg = MutableLiveData<MenuStepData>()
    val whenSyncSuccessData = MutableLiveData<MenuStepData>()

    val whenLoadRegisChangePostcode =  MutableLiveData<AddressIndexModel>()
    val whenLoadAddrChangePostcode =  MutableLiveData<AddressIndexModel>()
    val whenLoadMailChangePostcode =  MutableLiveData<AddressIndexModel>()

    private val provinceList = ArrayList<Province>()
    private val amphurList = ArrayList<Amphur>()
    private val provinceRegisList = ArrayList<Province>()
    private val amphurRegisList = ArrayList<Amphur>()
    private val provinceMailList = ArrayList<Province>()
    private val amphurMailList = ArrayList<Amphur>()

    val whenDataLoadedMessage = MutableLiveData<String>()

    fun getDefaultData(item : VerifyConfirmData  ) {
        val defineData = ArrayList<String>()
        defineData.add(VerifyandConfirmActivity.ADDRESS)
        defineData.add(VerifyandConfirmActivity.REGIS)
        defineData.add(VerifyandConfirmActivity.MAILING)



        val addIndex = ArrayList<AddressIndexModel>()
        defineData.forEach{
            provinceChange(0 ,it)
            amphurChange(0 ,it)
            defaultPositionData(item,it)?.let { it1 -> addIndex.add(it1) }
        }

        whenDataSpinnerLoaded.postValue(AddressAllTypeIndexModel( address = addIndex.get(0) , regis = addIndex.get(1) , mailing =  addIndex.get(2) ))
    }

    fun provinceChange(position: Int = -1  , type : String ) {
      if(type == VerifyandConfirmActivity.ADDRESS){
          if (provinceList.isEmpty()) {
              val provinces = MasterDataManager.getInstance()
                      .getProvinceList()?.map {
                          Province(it.pROVINCECODE!!, it.getProvinceName()!!)
                      }

              provinceList.addAll(provinces!!)
              whenAddrProvinceLoaded.postValue(provinceList.map { it.name })
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
          whenAddrAumphurLoaded.postValue(amphurList.map { "${it.name} (${it.postcode})" })
      }else if (type == VerifyandConfirmActivity.REGIS){
          /// Regis
          if (provinceRegisList.isEmpty()) {
              val provinces = MasterDataManager.getInstance()
                      .getProvinceList()?.map {
                          Province(it.pROVINCECODE!!, it.getProvinceName()!!)
                      }

              provinceRegisList.addAll(provinces!!)
              whenRegisProvinceLoaded.postValue(provinceRegisList.map { it.name })
          }
          if(position != -1) {
              val province = provinceRegisList[position]
              val amphurs = MasterDataManager.getInstance()
                      .getAmphurByProvinceCode(province.id)?.map {
                          Amphur(it.aMPHURCODE!!, it.getAmphurName()!!, it.pOSTCODE!!)
                      }

              amphurRegisList.clear()
              amphurRegisList.addAll(amphurs!!)
          }
          whenRegisAumphurLoaded.postValue(amphurRegisList.map { "${it.name} (${it.postcode})" })
      } else if (type ==VerifyandConfirmActivity.MAILING){

          //// Mail
          if (provinceMailList.isEmpty()) {
              val provinces = MasterDataManager.getInstance()
                      .getProvinceList()?.map {
                          Province(it.pROVINCECODE!!, it.getProvinceName()!!)
                      }

              provinceMailList.addAll(provinces!!)
              whenMailProvinceLoaded.postValue(provinceMailList.map { it.name })
          }
          if(position != -1) {
              val province = provinceMailList[position]
              val amphurs = MasterDataManager.getInstance()
                      .getAmphurByProvinceCode(province.id)?.map {
                          Amphur(it.aMPHURCODE!!, it.getAmphurName()!!, it.pOSTCODE!!)
                      }

              amphurMailList.clear()
              amphurMailList.addAll(amphurs!!)
          }
          whenMailAumphurLoaded.postValue(amphurMailList.map { "${it.name} (${it.postcode})" })
      }
    }

    fun amphurChange(position: Int = -1 , type : String) {

        if(type == VerifyandConfirmActivity.ADDRESS) {
            if(position != -1) {
                val amphur = amphurList[position]
                whenAddrPostcodeLoaded.postValue(amphur.postcode)
            }else{
                whenAddrPostcodeLoaded.postValue("")
            }
        }else if (type == VerifyandConfirmActivity.REGIS) {
            if(position != -1) {
                val amphur = amphurRegisList[position]
                whenRegisPostcodeLoaded.postValue(amphur.postcode)
            }else{
                whenRegisPostcodeLoaded.postValue("")
            }
        } else if (type == VerifyandConfirmActivity.MAILING) {
            if(position != -1) {
                val amphur = amphurMailList[position]
                whenMailPostcodeLoaded.postValue(amphur.postcode)
            }else{
                whenMailPostcodeLoaded.postValue("")
            }

        }
    }

    private fun defaultPositionData( item :  VerifyConfirmData , type : String ) : AddressIndexModel? {
        var adrs_provincePosition = -1
        var adrs_aumphurPosition = -1
        var regis_provincePosition = -1
        var regis_aumphurPosition = -1
        var mailing_provincePosition = -1
        var mailing_aumphurPosition = -1

        if(type == VerifyandConfirmActivity.ADDRESS){

            if(item.Curr_pOSTCODE.isNotEmpty()){
                var amp_pro = MasterDataManager.getInstance()
                        .getAmphurProvince(item.Curr_pOSTCODE)?.map {
                            ProvincebyPostcode( proCode = it.pROVINCECODE!!,
                                    ampCode  = it.aMPHURCODE!!)
                        }
                if(amp_pro!!.size > 0 ) {
                    if (item.Curr_pROVINCECODE.isEmpty() &&  amp_pro.get(0).proCode.isNotEmpty()) {
                        item.Curr_pROVINCECODE = amp_pro.get(0).proCode
                    }
                    if (item.Curr_aMPHURCODE.isEmpty() &&  amp_pro.get(0).ampCode.isNotEmpty()) {
                        item.Curr_aMPHURCODE = amp_pro.get(0).ampCode
                    }
                }
            }
            if (item.Curr_pROVINCECODE.isNotEmpty()) {
                adrs_provincePosition = provinceList.indexOfFirst { it.id == item.Curr_pROVINCECODE }
            }
            provinceChange(adrs_provincePosition ,type )
            if (item.Curr_aMPHURCODE.isNotEmpty()) {
                adrs_aumphurPosition= amphurList.indexOfFirst { it.id == item.Curr_aMPHURCODE }
            }
            return AddressIndexModel ( type =  type , indx_pro =  adrs_provincePosition ,  indx_aum  = adrs_aumphurPosition , postcode = item.Curr_pOSTCODE)

        } else if (type == VerifyandConfirmActivity.REGIS){
            if(item.Regis_pOSTCODE.isNotEmpty()){
                var amp_pro = MasterDataManager.getInstance()
                        .getAmphurProvince(item.Regis_pOSTCODE)?.map {
                            ProvincebyPostcode( proCode = it.pROVINCECODE!!,
                                    ampCode  = it.aMPHURCODE!!)
                        }
                if(amp_pro!!.size > 0 ) {
                    if (item.Regis_pROVINCECODE.isEmpty() &&  amp_pro.get(0).proCode.isNotEmpty()) {
                        item.Regis_pROVINCECODE = amp_pro.get(0).proCode
                    }
                    if (item.Regis_aMPHURCODE.isEmpty() &&  amp_pro.get(0).ampCode.isNotEmpty()) {
                        item.Regis_aMPHURCODE = amp_pro.get(0).ampCode
                    }
                }
            }

            if (item.Regis_pROVINCECODE.isNotEmpty()) {
                regis_provincePosition = provinceRegisList.indexOfFirst { it.id == item.Regis_pROVINCECODE}
            }
            provinceChange(regis_provincePosition , type )
            if (item.Regis_aMPHURCODE.isNotEmpty()) {
                regis_aumphurPosition = amphurRegisList.indexOfFirst { it.id == item.Regis_aMPHURCODE }
            }
            return AddressIndexModel(type =  type ,  indx_pro =  regis_provincePosition ,  indx_aum  = regis_aumphurPosition , postcode = item.Regis_pOSTCODE)

        } else if (type == VerifyandConfirmActivity.MAILING){

            if(item.Mailing_pOSTCODE.isNotEmpty()){
                var amp_pro = MasterDataManager.getInstance()
                        .getAmphurProvince(item.Mailing_pOSTCODE)?.map {
                            ProvincebyPostcode( proCode = it.pROVINCECODE!!,
                                    ampCode  = it.aMPHURCODE!!)
                        }
                if(amp_pro!!.size > 0 ) {
                    if (item.Mailing_pROVINCECODE.isEmpty() &&  amp_pro.get(0).proCode.isNotEmpty()) {
                        item.Mailing_pROVINCECODE = amp_pro.get(0).proCode
                    }
                    if (item.Mailing_aMPHURCODE.isEmpty() &&  amp_pro.get(0).ampCode.isNotEmpty()) {
                        item.Mailing_aMPHURCODE = amp_pro.get(0).ampCode
                    }
                }
            }
            if (item.Mailing_pROVINCECODE.isNotEmpty()) {
                mailing_provincePosition = provinceMailList.indexOfFirst { it.id == item.Mailing_pROVINCECODE}
            }
            provinceChange(mailing_provincePosition , type)
            if (item.Mailing_aMPHURCODE.isNotEmpty()) {
                mailing_aumphurPosition = amphurMailList.indexOfFirst { it.id == item.Mailing_aMPHURCODE }
            }
            return AddressIndexModel ( type =  type , indx_pro =  mailing_provincePosition ,  indx_aum  = mailing_aumphurPosition , postcode = item.Mailing_pOSTCODE)
        }
        return null
    }


    /////// Change Only Postcode /////////
    fun ChangePostcodeAddressData( postcode :  String , type : String) {
        GlobalScope.launch(Dispatchers.Main) {
            provinceAmphurChangeByPostcode(postcode , type)
            var dataAmphurChange = ChangePostcode(postcode , type)
            if (type == VerifyandConfirmActivity.ADDRESS){
                whenLoadAddrChangePostcode.postValue(dataAmphurChange)
            } else if (type == VerifyandConfirmActivity.REGIS){
                whenLoadRegisChangePostcode.postValue(dataAmphurChange)
            } else if (type == VerifyandConfirmActivity.MAILING){
                whenLoadMailChangePostcode.postValue(dataAmphurChange)
            }
        }
    }

    fun provinceAmphurChangeByPostcode( postcode :  String  , type : String) {
        if (type == VerifyandConfirmActivity.ADDRESS){
            if (provinceList.isEmpty()) {
                val provinces = MasterDataManager.getInstance()
                        .getProvinceList()?.map {
                            Province(postcode!!, it.getProvinceName()!!)
                        }

                provinceList.addAll(provinces!!)
                whenAddrProvinceLoaded.postValue(provinceList.map { it.name })
            }
            val province = provinceList[0]
            val amphurs = MasterDataManager.getInstance()
                    .getAmphurByProvinceCode(province.id)?.map {
                        Amphur(it.aMPHURCODE!!, it.getAmphurName()!!, postcode)
                    }
            amphurList.clear()
            amphurList.addAll(amphurs!!)
            whenAddrAumphurLoaded.postValue(amphurList.map { "${it.name} (${postcode})" })
        } else if (type == VerifyandConfirmActivity.REGIS){
            if (provinceRegisList.isEmpty()) {
                val provinces = MasterDataManager.getInstance()
                        .getProvinceList()?.map {
                            Province(postcode!!, it.getProvinceName()!!)
                        }

                provinceRegisList.addAll(provinces!!)
                whenRegisProvinceLoaded.postValue(provinceRegisList.map { it.name })
            }
            val province = provinceRegisList[0]
            val amphurs = MasterDataManager.getInstance()
                    .getAmphurByProvinceCode(province.id)?.map {
                        Amphur(it.aMPHURCODE!!, it.getAmphurName()!!, postcode)
                    }
            amphurRegisList.clear()
            amphurRegisList.addAll(amphurs!!)
            whenRegisAumphurLoaded.postValue(amphurRegisList.map { "${it.name} (${postcode})" })
        } else if (type == VerifyandConfirmActivity.MAILING){
            if (provinceMailList.isEmpty()) {
                val provinces = MasterDataManager.getInstance()
                        .getProvinceList()?.map {
                            Province(postcode!!, it.getProvinceName()!!)
                        }

                provinceMailList.addAll(provinces!!)
                whenMailProvinceLoaded.postValue(provinceMailList.map { it.name })
            }
            val province = provinceMailList[0]
            val amphurs = MasterDataManager.getInstance()
                    .getAmphurByProvinceCode(province.id)?.map {
                        Amphur(it.aMPHURCODE!!, it.getAmphurName()!!, postcode)
                    }
            amphurMailList.clear()
            amphurMailList.addAll(amphurs!!)
            whenMailAumphurLoaded.postValue(amphurMailList.map { "${it.name} (${postcode})" })
        }
    }

    fun ChangePostcode( postcode :  String , type : String )  :  AddressIndexModel{
            var provincePosition = -1
            var districtPosition = -1

            var amp_pro = MasterDataManager.getInstance()
                    .getAmphurProvince(postcode)?.map {
                        ProvincebyPostcode( proCode = it.pROVINCECODE!!,
                                ampCode  = it.aMPHURCODE!!)
                    }

            if(amp_pro!!.size > 0 ) {
                if (type == VerifyandConfirmActivity.ADDRESS){
                    if (amp_pro.get(0).proCode.isNotEmpty()) {
                        provincePosition = provinceList.indexOfFirst { it.id == amp_pro.get(0).proCode }
                    }
                } else if (type == VerifyandConfirmActivity.REGIS){
                    if (amp_pro.get(0).proCode.isNotEmpty()) {
                        provincePosition = provinceRegisList.indexOfFirst { it.id == amp_pro.get(0).proCode }
                    }
                } else if (type == VerifyandConfirmActivity.MAILING){
                    if (amp_pro.get(0).proCode.isNotEmpty()) {
                        provincePosition = provinceMailList.indexOfFirst { it.id == amp_pro.get(0).proCode }
                    }
                }
                provinceChange(provincePosition, type)

                if (amp_pro.get(0).ampCode.isNotEmpty()) {
                    if (type == VerifyandConfirmActivity.ADDRESS){
                        districtPosition = amphurList.indexOfFirst { it.id == amp_pro.get(0).ampCode }
                    } else if (type == VerifyandConfirmActivity.REGIS){
                        districtPosition = amphurRegisList.indexOfFirst { it.id == amp_pro.get(0).ampCode }
                    } else if (type == VerifyandConfirmActivity.MAILING){
                        districtPosition = amphurMailList.indexOfFirst { it.id == amp_pro.get(0).ampCode }
                    }
                }
            }
            val data = AddressIndexModel(
                   indx_pro = provincePosition,
                   indx_aum = districtPosition ,
                   type =  type ,
                   postcode = postcode )

            return data
    }

    /////// End Change Only Postcode /////////




    fun initProvideData(refNo : String = "") {
        GlobalScope.launch(Dispatchers.Main) {
            getDataVerify(refNo)
        }
    }

    fun getDataVerify(refNo : String = "") {
        val request = GetVerifyRequest.build(ref_no = refNo)

        whenLoading.value = true

        apiLoanManager.verify(request) {isError, result , step, msg  ->
            whenLoading.value = false

            if (isError) {
                if (result != "device logon") {
                    whenDataLoadedFailure.value = result
                }
                if(msg.length > 0 ) {
                    whenDataLoadedMessage.postValue(msg)
                }
                return@verify
            }

            if(msg.length > 0 ) {
                whenDataLoadedMessage.postValue(msg)
            }

            val items = JsonMapperManager.getInstance()
                    .gson.fromJson(result, Array<GetVerifyResponse>::class.java)

            if (items.isEmpty()) {
                whenDataLoadedFailure.value = ""
                return@verify
            }

            try {
                val verifyList = VerifyConirmLastItem(items.toList())
                val dataModel = setData(verifyList.get(0))
                getDefaultData(dataModel!!)
                whenDataLoadedTemp.postValue(dataModel)
                whenDataLoadedSuccess.value = true
                whenDataLoaded.postValue(verifyList.get(0))
            }catch (e : Exception){
                e.message
                whenDataLoadedSuccess.value = false
            }
        }
    }

    fun saveList(data : VerifyConfirmData) {
        var item =  getPositionProvinceAumphur(data)
        clearDataVerCon()
        LoanDataManager.saveLoanVerifyConfirmList(item)
    }

    fun clearDataVerCon() {

    }

    fun  VerifyConirmLastItem(res: List<GetVerifyResponse>) : List<VerConirmDataModel> {
        return listOf()
    }
    fun getPositionProvinceAumphur(items :  VerifyConfirmData) : VerifyConfirmData {
        if(items.Curr_aMPHURCODE !="" && items.Curr_aMPHURCODE.toInt() >= 0 ) {
            val amphur = amphurList[items.Curr_aMPHURCODE.toInt()]
            items.Curr_aMPHURCODE = amphur.id
        }
        if(items.Curr_pROVINCECODE !="" &&  items.Curr_pROVINCECODE.toInt() >= 0 ) {
            val province = provinceList[items.Curr_pROVINCECODE.toInt()]
            items.Curr_pROVINCECODE = province.id
        }
        if(items.Regis_pROVINCECODE !="" &&  items.Regis_pROVINCECODE.toInt() >= 0) {
            val provinceregis = provinceRegisList[items.Regis_pROVINCECODE.toInt()]
            items.Regis_pROVINCECODE = provinceregis.id
        }
        if(items.Regis_aMPHURCODE !="" &&  items.Regis_aMPHURCODE.toInt() >= 0 ) {
            val amphur_regis = amphurRegisList[items.Regis_aMPHURCODE.toInt()]
            items.Regis_aMPHURCODE = amphur_regis.id
        }
        if(items.Mailing_aMPHURCODE !="" &&  items.Mailing_aMPHURCODE.toInt() >= 0) {
            val amphur_mail = amphurMailList[items.Mailing_aMPHURCODE.toInt()]
            items.Mailing_aMPHURCODE = amphur_mail.id
        }
        if(items.Mailing_pROVINCECODE !="" &&  items.Mailing_pROVINCECODE.toInt() >= 0) {
            val province_mail = provinceMailList[items.Mailing_pROVINCECODE.toInt()]
            items.Mailing_pROVINCECODE = province_mail.id
        }
        return  items
    }


    fun SyncVerify( refNo : String , data :  VerifyConfirmData ) {
        var flag = data.flagCurrent
        var flagRegis = data.flagRegisSameCurrent
        var flagMail = data.flagRegisSameCurrent
        var item = getPositionProvinceAumphur(data)
        val request =
                 SyncVerifyRequest.build(
                        refNo = refNo,
                        current =  SyncVerifyRequest.Address.build(
                                address  = item.Curr_rEALADDRESS,
                                postcode  = item.Curr_pOSTCODE ,
                                amphur  = (item.Curr_aMPHUR).substringBefore("(")  ,
                                amphurCode  = item.Curr_aMPHURCODE ,
                                province  = item.Curr_pROVINCE ,
                                provinceCode  = item.Curr_pROVINCECODE ,
                                lat  = item.Curr_lat ,
                                lng  = item.Curr_lng ) ,
                        mailing = SyncVerifyRequest.Address.build(
                                address  =  if(flagMail){ item.Curr_rEALADDRESS } else { item. Mailing_rEALADDRESS } ,
                                postcode  = if(flagMail){ item.Curr_pOSTCODE } else { item. Mailing_pOSTCODE } ,
                                amphur  = (if(flagMail){ item.Curr_aMPHUR} else { item. Mailing_aMPHUR} ).substringBefore("(") ,
                                amphurCode = if(flagMail){ item.Curr_aMPHURCODE } else { item. Mailing_aMPHURCODE} ,
                                province  = if(flagMail){ item.Curr_pROVINCE } else { item. Mailing_pROVINCE } ,
                                provinceCode = if(flagMail){ item.Curr_pROVINCECODE } else { item. Mailing_pROVINCECODE } ,
                                lat  = if(flagMail){ item.Curr_lat } else { item. Mailing_lat } ,
                                lng  = if(flagMail){ item.Curr_lng } else { item. Mailing_lng} ) ,
                        regis =  SyncVerifyRequest.Address.build(
                                address  = if(flagRegis){  item.Curr_rEALADDRESS } else { item.Regis_rEALADDRESS } ,
                                postcode  = if(flagRegis){  item.Curr_pOSTCODE } else { item.Regis_pOSTCODE } ,
                                amphur  = ( if(flagRegis){  item.Curr_aMPHUR } else { item.Regis_aMPHUR}).substringBefore("(")  ,
                                amphurCode  = if(flagRegis){  item.Curr_aMPHURCODE } else { item.Regis_aMPHURCODE} ,
                                province  = if(flagRegis){  item.Curr_pROVINCE } else { item.Regis_pROVINCE} ,
                                provinceCode  = if(flagRegis){  item.Curr_pROVINCECODE } else { item.Regis_pROVINCECODE } ,
                                lat  = if(flagRegis){  item.Curr_lat  } else { item.Regis_lat } ,
                                lng  = if(flagRegis){  item.Curr_lng} else { item.Regis_lng} ) ,
                        dealerCar   =  SyncVerifyRequest.DealerCar.build(
                                showroomID = item.showroomCode ,
                                showroom  = item.showroomName,
                                appointment  =  item.appointment ,
                                duedate  =  item.dueDate
                        )
                )

        whenLoading.value = true
        apiLoanManager.syncVerify(request) {isError, result , step, msg  ->
            if (isError) {
                whenLoading.value = false
                if (result != "device logon") {
                    whenDataLoadedFailure.value = result
                }
                if(msg.length > 0 ) {
                    whenDataLoadedMessage.postValue(msg)
                }
                return@syncVerify
            }

            if(msg.length > 0 ) {
                whenDataLoadedMessage.postValue(msg)
            }

            clearDataVerCon()

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
                return@syncVerify
            }else{
                whenLoading.value = false
                whenSyncSuccess.value = true
                whenSyncSuccessData.postValue(data)
            }
        }
    }

    fun setData(it : VerConirmDataModel )  : VerifyConfirmData? {
        try{
            var data =  VerifyConfirmData(
                    appointment = it.delivercar.appointment ,
                    dueDate =  it.delivercar.dueDate ,
                    showroomCode = it.delivercar.showroomCode ,
                    showroomName =  it.delivercar.showroomName ,
                    offer_dealercode = it.delivercar.offerdealercode ,
                    flagCurrent = false ,
                    flagRegisSameCurrent = it.flagRegisSame ,
                    flagMailingSameCurrent = it.flagMailingsSame ,
                    Curr_rEALADDRESS =  it.address.rEALADDRESS ,
                    Curr_aMPHURCODE = it.address.aMPHURCODE ,
                    Curr_aMPHUR =  it.address.aMPHUR ,
                    Curr_pROVINCECODE= it.address.pROVINCECODE ,
                    Curr_pROVINCE= it.address.pROVINCECODE ,
                    Curr_pOSTCODE= it.address.pOSTCODE ,
                    Curr_lat = it.address.lat ,
                    Curr_lng = it.address.lng ,
                    Regis_rEALADDRESS = it.regis.rEALADDRESS ,
                    Regis_aMPHURCODE= it.regis.aMPHURCODE ,
                    Regis_aMPHUR= it.regis.aMPHUR ,
                    Regis_pROVINCECODE= it.regis.pROVINCECODE ,
                    Regis_pROVINCE= it.regis.pROVINCE ,
                    Regis_pOSTCODE= it.regis.pOSTCODE ,
                    Regis_lat = it.regis.lat ,
                    Regis_lng = it.regis.lng ,
                    Mailing_rEALADDRESS =  it.mailing.rEALADDRESS ,
                    Mailing_aMPHURCODE = it.mailing.aMPHURCODE ,
                    Mailing_aMPHUR = it.mailing.aMPHUR ,
                    Mailing_pROVINCECODE = it.mailing.pROVINCECODE ,
                    Mailing_pROVINCE = it.mailing.pROVINCE ,
                    Mailing_pOSTCODE = it.mailing.pOSTCODE ,
                    Mailing_lat = it.mailing.lat ,
                    Mailing_lng = it.mailing.lng
            )

//            try{
//                saveDeafaultByDealerCode(
//                        dealerName = "",
//                        showroomCode = "974386" , //it.delivercar.showroomCode ,
//                        dealerCode = "3991" ,// it.delivercar.offerdealercode ,
//                        provinceIndex = - 1,
//                        amphurIndex = - 1,
//                        isNearly = false
//                )
//            }catch (e : Exception){
//                e.message
//            }
            return data
        }catch (e : Exception){
            e.message
            return null
        }
    }

    fun saveDeafaultByDealerCode(
            dealerName: String = "",
            showroomCode: String = "",
            dealerCode: String = "",
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

    data class VerConirmDataModel(
            var main: MainItem,
            var delivercar: DelivercarItem,
            var mCI: MycarinfoItem,
            var mailing: AddressItem,
            var address: AddressItem,
            var regis: AddressItem ,
            var flagCheckAll: Boolean,
            var flagRegisSame: Boolean ,
            var flagMailingsSame: Boolean
   )

    data class AddressItem(
       var aMPHURCODE: String ,
       var aMPHUR: String ,
       var pOSTCODE: String ,
       var pROVINCECODE: String ,
       var pROVINCE: String ,
       var lat: String ,
       var lng: String ,
       var rEALADDRESS: String
    )

    data class DelivercarItem(
        var appointment: String ,
        var dueDate: String ,
        var showroomCode: String ,
        var showroomName: String,
        var offerdealercode: String,
        var flagCurrent: Boolean ,
        var flagRegisSame: Boolean ,
        var flagMailingsSame: Boolean
    )

    data class MainItem(
       var loanNo: String = "",
       var name: String = "",
       var statusApp: String
    )

    data class MycarinfoItem(
       var carPrice: String = "",
       var downPayment: String = "",
       var grade: String = "",
       var installment: String = "",
       var interests: String = "",
       var paymentTerms: String = "",
       var model: String = "",
       var contract_hanf_fee: String  = "",
       var otherExpense: String  = ""
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

    data class AddressIndexModel(
            val indx_pro: Int = 0,
            val indx_aum: Int = 0 ,
            val type : String ,
            val postcode : String = ""
    )

    data class AddressAllTypeIndexModel(
            val address : AddressIndexModel,
            val regis : AddressIndexModel ,
            val mailing : AddressIndexModel
    )

    data class ProvincebyPostcode(
            val proCode: String = "",
            val ampCode: String = ""
    )
}