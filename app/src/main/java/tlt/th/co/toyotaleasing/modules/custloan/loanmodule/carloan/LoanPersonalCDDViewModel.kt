package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.carloan

import androidx.lifecycle.MutableLiveData
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.db.UserManager
import tlt.th.co.toyotaleasing.model.request.CICDDFormRequest
import okhttp3.*
import tlt.th.co.toyotaleasing.manager.LocalizeManager
import java.io.IOException
import java.util.concurrent.TimeUnit


class LoanPersonalCDDViewModel  : BaseViewModel() {
    private val profile = UserManager.getInstance().getProfile()
    val whenDataLoaded = MutableLiveData<String>()

    data class Model(
            val json: String = ""
    )

    fun GetData(refID : String , url : String ) {
        var jsonData: String = ""
        val accessToken = UserManager.getInstance().getProfile().token
        val language = if (LocalizeManager.isThai()) {
            "TH"
        } else {
            "EN"
        }
        val loanCDDRequest = CICDDFormRequest.build(
                refID = refID,
                token = "Basic ${profile.token}"
        )
        jsonData = JsonMapperManager.getInstance().gson.toJson(loanCDDRequest)

        val client = OkHttpClient()
        val mediaType = MediaType.parse("application/json")
        val body = RequestBody.create(mediaType, jsonData)
        val request = Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "text/xml")
                .addHeader("Authorization", "Basic $accessToken")
                .addHeader("online_language", language)
                .addHeader("online_ref", refID)
                .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                whenDataLoaded.postValue(e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                whenDataLoaded.postValue(response.body()?.string())

            }
        })
    }
}