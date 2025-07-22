package tlt.th.co.toyotaleasing.modules.newsandpromotion.newsdetail

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.common.extension.toDatetime
import tlt.th.co.toyotaleasing.manager.ContextManager
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager
import tlt.th.co.toyotaleasing.model.request.GetNewsRequest
import tlt.th.co.toyotaleasing.model.request.UpdateBookmarkRequest
import tlt.th.co.toyotaleasing.model.response.GetNewsResponse
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class NewsDetailViewModel : BaseViewModel() {

    val whenDataLoaded = MutableLiveData<Model>()
    val whenUpdateBookmark = MutableLiveData<Boolean>()

    private val apiManager by lazy { TLTApiManager.getInstance() }

    fun getData(id: String) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                whenLoading.postValue(true)

                val getNewsResponse = getNews()

                val newsDetail = getNewsResponse?.nEWS
                        ?.asSequence()
                        ?.filter { it?.aDID == id }
                        ?.map {
                            Model(
                                    id = it?.aDID ?: "",
                                    title = it?.hEADERNEWS ?: "",
                                    period = "${ContextManager.getInstance().getStringByRes(R.string.news_and_promotion_today)} - ${it?.eXPIRE?.toDatetime()}",
                                    isBooked = it?.bOOKMARK == "Y",
                                    detail = it?.dETAILNEWS ?: "",
                                    image = it?.dETAILIMG ?: "",
                                    shareContent = it?.lINKPREF ?: ""
                            )
                        }?.firstOrNull()

                whenDataLoaded.postValue(newsDetail)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                whenLoading.postValue(false)
            }
        }
    }

    fun updateBookmark() {
        val item = whenDataLoaded.value?.copy().apply {
            this?.isBooked = !this!!.isBooked
        }
        val request = UpdateBookmarkRequest.build(item?.id ?: "")

        apiManager.updateBookmark(request) { isError: Boolean, result: String ->
            if (isError) {
                return@updateBookmark
            }

            if (result == "fail") {
                whenUpdateBookmark.postValue(false)
            } else {
                whenUpdateBookmark.postValue(true)
            }
        }

        whenDataLoaded.postValue(item)
    }

    private suspend fun getNews() = suspendCoroutine<GetNewsResponse?> {
        apiManager.getNews(GetNewsRequest.build()) { isError: Boolean, result: String ->
            if (isError) {
                it.resumeWithException(Exception(result))
                return@getNews
            }

            val getNewsResponse = JsonMapperManager.getInstance()
                    .gson.fromJson(result, Array<GetNewsResponse>::class.java)

            if (getNewsResponse.isEmpty()
                    || getNewsResponse.firstOrNull()?.nEWS?.isEmpty() == true) {
                it.resumeWithException(Exception())
                return@getNews
            }

            it.resume(getNewsResponse.firstOrNull())
        }
    }

    data class Model(
            var id: String = "",
            var image: String = "",
            var title: String = "",
            var period: String = "",
            var isBooked: Boolean = false,
            var detail: String = "",
            var shareContent: String = ""
    )
}