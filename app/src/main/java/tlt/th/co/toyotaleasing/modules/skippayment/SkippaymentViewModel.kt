package tlt.th.co.toyotaleasing.modules.skippayment

import androidx.lifecycle.MutableLiveData
import android.util.Log
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.db.UserManager
import okhttp3.*
import java.io.IOException
import tlt.th.co.toyotaleasing.manager.db.*
import tlt.th.co.toyotaleasing.model.request.SkippaymentRequest
import tlt.th.co.toyotaleasing.model.response.SuccessResponse

class SkippaymentViewModel : BaseViewModel() {

    private val profile = UserManager.getInstance().getProfile()
    val whenDataLoaded = MutableLiveData<String>()


//    fun getData() {
//        val master = MasterDataManager.getInstance().getSideMenu()
//        val url = master!!.get(0).mENULINK
//
//            val tltRequest = SkippaymentRequest.build(
//                    authorization = "Bearer ${profile.token}"
//            )
//
//            val model = Model(
//                    url = url,
//                    jsonData = JsonMapperManager.getInstance().gson.toJson(tltRequest)
//            )
//
//            whenDataLoaded.postValue(model)
//    }

    fun GetData() {
        try{
            var url : String = ""
            whenLoading.postValue(true)
            val master = MasterDataManager.getInstance().getSideMenu()

            var jsonData: String = ""
            val accessToken = UserManager.getInstance().getProfile().token
            val skipRequest = SkippaymentRequest.build(
                    authorization = "Bearer ${profile.token}"
            )
            jsonData = JsonMapperManager.getInstance().gson.toJson(skipRequest)
            val client = OkHttpClient()
            val mediaType = MediaType.parse("application/json")
            val body = RequestBody.create(mediaType, jsonData)
            val request = Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Content-Type", "text/xml")
                    .addHeader("Authorization", "Bearer $accessToken")
                    .build()


            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    whenLoading.postValue(false)
                    Log.e("Call Fail :" , "")
                    whenDataLoaded.postValue("")
                }


                override fun onResponse(call: Call, response: Response) {
                    try{
                        whenLoading.postValue(false)
                        whenDataLoaded.postValue(response.body()?.string())
                    }catch (e : java.lang.Exception){
                        whenLoading.postValue(false)
                        Log.e("Call Fail :" , "")
                        whenDataLoaded.postValue("")
                    }


                }
            })
        }catch ( e : Exception ){
            Log.e("Can't call :" , "")
        }

    }

    data class Model(
            val url: String = "",
            val jsonData: String = ""
    )
}

