package tlt.th.co.toyotaleasing.modules.home.customer

import androidx.lifecycle.MutableLiveData
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.common.extension.ifTrue
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager
import tlt.th.co.toyotaleasing.model.request.BlockNotifyRequest
import tlt.th.co.toyotaleasing.util.AppUtils

class NoticeViewModel : BaseViewModel() {

    val whenBlockedNotifySuccess = MutableLiveData<Boolean>()

    fun sendAnswerBlockNotify(type: String, sequenceId: String) {

        type.isEmpty().ifTrue {
            whenBlockedNotifySuccess.postValue(false)
            return
        }

        sequenceId.isEmpty().ifTrue {
            whenBlockedNotifySuccess.postValue(false)
            return
        }

        whenLoading.postValue(true)

        val request = BlockNotifyRequest.build(type, sequenceId)

        TLTApiManager.getInstance().blockNotify(request) { isError: Boolean, result: String ->
            whenLoading.postValue(false)

            if (isError) {
                whenBlockedNotifySuccess.postValue(false)
                return@blockNotify
            }

            whenBlockedNotifySuccess.postValue(true)
        }

    }

    data class Model(
            val type: String = "",
            val title: String = "",
            val imageUrl: String = "",
            val description: String = "",
            val isShowImage: Boolean = false,
            val isShowIgnore: Boolean = false,
            val isStaffApp: Boolean = AppUtils.isStaffApp()
    )
}