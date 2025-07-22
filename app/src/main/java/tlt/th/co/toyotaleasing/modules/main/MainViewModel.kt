package tlt.th.co.toyotaleasing.modules.main

import androidx.lifecycle.MutableLiveData
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.common.extension.decodeBase64
import tlt.th.co.toyotaleasing.common.extension.ifEmpty
import tlt.th.co.toyotaleasing.common.extension.unzip
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager

import tlt.th.co.toyotaleasing.manager.db.MasterDataManager
import tlt.th.co.toyotaleasing.manager.db.UserManager
import tlt.th.co.toyotaleasing.model.entity.masterdata.*
import tlt.th.co.toyotaleasing.model.request.GetMasterDataRequest
import tlt.th.co.toyotaleasing.model.request.RegisterNonCustomerRequest
import tlt.th.co.toyotaleasing.model.request.UpdateMasterDataRequest
import tlt.th.co.toyotaleasing.model.response.CheckMasterDataResponse
import tlt.th.co.toyotaleasing.util.AppUtils
import java.lang.Exception
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MainViewModel : BaseViewModel() {

    val whenUserIsNotCustomer_PAPD = MutableLiveData<Boolean>()
    val whenUserIsNotCustomer = MutableLiveData<Boolean>()
    val whenUserIsCustomer = MutableLiveData<Boolean>()
    val isStaffApp = AppUtils.isStaffApp()
    val whenMasterSizeDataLoad = MutableLiveData<Model>()

    private lateinit var checkMasterDataResponse: CheckMasterDataResponse

    fun getIntialByApi() {
        whenUserIsCustomer.postValue(true)

//        if (FirebaseInstanceId.getInstance().token == null) {
//            return
//        }

//        val user = UserManager.getInstance()
//
//        if (user.getProfile().token.isNotEmpty()) {
//            checkMasterData()
//            return
//        }
//
//        if (isStaffApp) {
//            TLTApiManager.getInstance()
//                    .registerNonStaff(RegisterNonCustomerRequest.buildForStaff()) {isError, result ->
//                        if (isError) {
//                            return@registerNonStaff
//                        }
//
//                        checkMasterData()
//                    }
//        } else {
//            TLTApiManager.getInstance()
//                    .registerNonCustomer(RegisterNonCustomerRequest.build()) { _, _ ->
//                        checkMasterData()
//                    }
//        }

    }

    private fun sendUpdateMasterDataSuccessToApi() {
        var currentVersion = MasterDataManager.getInstance().getCurrentVersion()
        if (currentVersion.isEmpty()) {
            currentVersion = checkMasterDataResponse.filename
        }
        val request = UpdateMasterDataRequest.build(currentVersion)

        TLTApiManager.getInstance().updateMasterData(request) { isError, result ->
            if (isError) {
                return@updateMasterData
            }

            MasterDataManager.getInstance()
                    .updateVersion(checkMasterDataResponse.filename)

            masterDataLastestVersion()
        }
    }

    private fun masterDataLastestVersion() {
        val user = UserManager.getInstance()

        whenLoading.postValue(false)

        if (user.isCustomer()) {
            whenUserIsCustomer.postValue(true)
        } else {
           whenUserIsNotCustomer.postValue(user.isShowAppIntro())
        }
    }

    private fun checkMasterData() {
        var version = MasterDataManager.getInstance().getCurrentVersion()

        if(UpdateDataAfterMigration() && version !="")
            version =  "V"+((version.replace("V","").replace(".bz2","")).toInt() - 1 ).toString()+".bz2"
        else
            version = version

        TLTApiManager.getInstance()
                .getMasterData(GetMasterDataRequest.build(version)) { isError, result ->
                    if (isError) {
                        if (result == "noInternet") {
                            whenNoInternetConnection.postValue(true)
                        }
                        return@getMasterData
                    }

                    val items = JsonMapperManager.getInstance()
                            .gson.fromJson(result, Array<CheckMasterDataResponse>::class.java)

                    if (items == null || items.isEmpty()) {
                        return@getMasterData
                    }

                    checkMasterDataResponse = items.first()


                    if (checkMasterDataResponse.filename.isEmpty()) { // Canceled  because Migration Update Database : || MasterDataManager.getInstance().isSameAsVersion(checkMasterDataResponse.filename)
                        masterDataLastestVersion()
                        return@getMasterData
                    }else{
                        val model = Model(
                                dDescription = items.first().dataSize
                        )

                        whenMasterSizeDataLoad.postValue(model)
                        saveMasterData(checkMasterDataResponse)

                    }
                }
    }


    private fun UpdateDataAfterMigration() : Boolean{
//        -  Privacy policy
        try {



            return false
        }catch (e : Exception){
            return true
        }
    }

    private fun saveMasterData(checkMasterDataResponse: CheckMasterDataResponse) {
        GlobalScope.launch(Dispatchers.Main) {
            val json = upzipMasterDataBase64(checkMasterDataResponse.base64)
            saveMasterDataIntoDB(json)
            sendUpdateMasterDataSuccessToApi()
        }
    }

    private suspend fun upzipMasterDataBase64(base64: String?) = suspendCoroutine<String> {
        base64?.ifEmpty {
            it.resume("")
            return@suspendCoroutine
        }

        val json = base64?.decodeBase64()?.unzip() ?: ""

        it.resume(json)
    }

    private fun saveMasterDataIntoDB(json: String) {
        val masterData = JsonMapperManager.getInstance()
                .gson.fromJson(json, MasterData::class.java)

        //DatabaseManager.getInstance().saveMasterData(masterData)
    }


    data class Model(
            val dDescription : String = ""
    )

}