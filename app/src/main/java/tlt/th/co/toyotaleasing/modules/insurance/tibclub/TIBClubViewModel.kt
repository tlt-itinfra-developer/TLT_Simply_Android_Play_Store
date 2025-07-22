package tlt.th.co.toyotaleasing.modules.insurance.tibclub

import androidx.lifecycle.MutableLiveData
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager
import tlt.th.co.toyotaleasing.manager.db.CacheManager
import tlt.th.co.toyotaleasing.manager.db.MasterDataManager
import tlt.th.co.toyotaleasing.model.request.GetTIBClubDetailRequest
import tlt.th.co.toyotaleasing.model.response.GetTIBClubDetailResponse
import tlt.th.co.toyotaleasing.util.AppUtils

class TIBClubViewModel : BaseViewModel() {

    private val apiManager = TLTApiManager.getInstance()
    private lateinit var getTIBClubDetailResponse: Array<GetTIBClubDetailResponse>

    val whenSingleDetailAlready = MutableLiveData<List<GetTIBClubDetailResponse.TIBCLUBDETAIL?>>()
    val whenDataLoaded = MutableLiveData<ArrayList<Model>>()

    fun getData() {
        whenLoading.postValue(true)

        apiManager.getTIBClubDetail(GetTIBClubDetailRequest.build()) { isError, result ->
            whenLoading.postValue(false)

            if (isError) {
                return@getTIBClubDetail
            }

            val model = Model(
                    "",
                    "",
                    "",
                    "",
                    false
            )

            val items = arrayListOf<Model>()
            items.add(model)

            getTIBClubDetailResponse = JsonMapperManager.getInstance()
                    .gson.fromJson(result, Array<GetTIBClubDetailResponse>::class.java)

            getTIBClubDetailResponse.first().tIBCLUBDETAIL?.forEachIndexed { index, tibclubdetail ->
                val model = Model(
                        tibclubdetail?.tIBCODE ?: "",
                        "",
                        tibclubdetail?.hEADERDESC ?: "",
                        tibclubdetail?.hEADERLINK ?: ""
                )

                if (index == 0) {
                    model.isShowEventOtherOnHeader = true
                    items.add(model)
                } else {
                    model.isShowEventOtherOnHeader = false
                    items.add(model)
                }
            }

            whenDataLoaded.postValue(items)
        }
    }

    fun selectTIBClubDetail(tibCode: String) {
        val tibDetailData = getTIBClubDetailResponse.first().tIBCLUBDETAIL?.filter {
            it?.tIBCODE == tibCode
        }
        whenSingleDetailAlready.postValue(tibDetailData)
    }

    fun isStaffApp() = AppUtils.isStaffApp()

    data class Model(
            val id: String = "",
            val title: String = "",
            val detail: String = "",
            val imageUrl: String = "",
            var isShowEventOtherOnHeader: Boolean = false
    )
}