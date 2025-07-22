package tlt.th.co.toyotaleasing.modules.newsandpromotion.traffic

import androidx.lifecycle.MutableLiveData
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.api.twitter.TwitterAuthManager
import tlt.th.co.toyotaleasing.model.response.GetTwitterTokenResponse
import tlt.th.co.toyotaleasing.model.response.GetUserTwitterTimelineResponse

class TrafficViewModel: BaseViewModel() {

    private val apiManager = TwitterAuthManager.getInstance()
    val whenDataLoaded = MutableLiveData<String>()
    val whenUserTimelineLoaded = MutableLiveData<List<GetUserTwitterTimelineResponse>>()

    fun getData() {
        whenLoading.postValue(true)

        apiManager.getTwitterToken("client_credentials") { isError, result ->
            whenLoading.postValue(false)

            if (isError) {
                return@getTwitterToken
            }

            val item = JsonMapperManager.getInstance()
                    .gson.fromJson(result, GetTwitterTokenResponse::class.java)

            whenDataLoaded.postValue(item.accessToken)
        }
    }

    fun getUserTimeLine(bearerKey: String) {
        whenLoading.postValue(true)

        apiManager.getUserTimeLine(bearerKey = bearerKey) {isError, result ->
            whenLoading.postValue(false)

            if (isError) {
                return@getUserTimeLine
            }

            val items = JsonMapperManager.getInstance()
                    .gson.fromJson(result, Array<GetUserTwitterTimelineResponse>::class.java)
                    .toList()

            whenUserTimelineLoaded.postValue(items)
        }
    }
}