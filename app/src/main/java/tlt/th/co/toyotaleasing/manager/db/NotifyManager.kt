package tlt.th.co.toyotaleasing.manager.db

import org.greenrobot.eventbus.EventBus
import tlt.th.co.toyotaleasing.common.eventbus.NotifyEvent
import tlt.th.co.toyotaleasing.common.eventbus.PushPopupEvent
import tlt.th.co.toyotaleasing.common.extension.ifTrue
import tlt.th.co.toyotaleasing.common.extension.toDateByPattern
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.model.entity.NotifyEntity
import tlt.th.co.toyotaleasing.model.response.ItemNotifyPushResponse
import tlt.th.co.toyotaleasing.model.response.NotificationJsonResponse

object NotifyManager {

    fun saveNotify(data: MutableMap<String, String>, primaryKey: String): ItemNotifyPushResponse {
        val json = JsonMapperManager.getInstance()
                .gson.toJson(data)
        val setNotifyPushResponse = JsonMapperManager.getInstance()
                .gson.fromJson(json, ItemNotifyPushResponse::class.java)

        val item = NotificationJsonResponse(
                title = setNotifyPushResponse.title ?: "",
                message = setNotifyPushResponse.msg ?: "",
                imageUrl = setNotifyPushResponse.image ?: "",
                flagCheckBox = setNotifyPushResponse.flagChkbox ?: "",
                notifyType = setNotifyPushResponse.msgType ?: "",
                sequenceId = setNotifyPushResponse.seqId ?: ""
        )

        setNotifyPushResponse.navigation!!.isEmpty().ifTrue {
            setNotifyPushResponse.navigation = "tlt://etc?"
        }

        val entity = NotifyEntity().apply {
            notifyKey = primaryKey
            title = setNotifyPushResponse.title ?: ""
            sequenceId = setNotifyPushResponse.seqId ?: ""
            optionName = setNotifyPushResponse.optionName ?: ""
            flagCheckbox = setNotifyPushResponse.flagChkbox ?: ""
            expireDate = setNotifyPushResponse.expDate?.toDateByPattern()!!
            messageType = setNotifyPushResponse.msgType ?: ""
            registerNumber = setNotifyPushResponse.regNo ?: ""
            datetime = setNotifyPushResponse.dateTime?.toDateByPattern()!!
            installmentAmount = setNotifyPushResponse.installAmt ?: ""
            description = setNotifyPushResponse.msg ?: ""
            image = setNotifyPushResponse.image ?: ""
            media = setNotifyPushResponse.media ?: ""
            message = setNotifyPushResponse.msg ?: ""
            iconColor = setNotifyPushResponse.iconColor ?: ""
            navigation = "${setNotifyPushResponse.navigation}&jsonString=${item.toJsonString()}".replace("+A16", "")
        }

        setNotifyPushResponse.navigation = entity.navigation

        //DatabaseManager.getInstance().saveNotify(entity)
        UserManager.getInstance().addUnreadNotification()

        return setNotifyPushResponse
    }

    fun getNotifyList() = DatabaseManager.getInstance()
            .getNotifyList()
//            .filter { it.sequenceId.isEmpty() }

    fun getPushPopupLastest() = DatabaseManager.getInstance()
            .getNotifyList()
            .first { it.sequenceId.isNotEmpty() }

    fun getLastest() = DatabaseManager.getInstance()
            .getNotifyList()
            .first()

    fun isShowPushPopup(): Boolean {
        DatabaseManager.getInstance()
                .getNotifyList()
                .firstOrNull()?.let {
                    return it.sequenceId.isNotEmpty()
                }

        return false
    }

    fun showPushPopupImmediately() {
        EventBus.getDefault().post(PushPopupEvent())
    }

    fun triggerNotifyImmediately() {
        EventBus.getDefault().post(NotifyEvent())
    }

    fun clearPushPopupData() {
        //DatabaseManager.getInstance().deletePushPopupNotify()
    }
}