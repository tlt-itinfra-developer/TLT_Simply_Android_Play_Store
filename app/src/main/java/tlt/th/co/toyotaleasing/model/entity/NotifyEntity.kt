package tlt.th.co.toyotaleasing.model.entity

import java.util.*

open class NotifyEntity(
        var notifyKey: String = "",
        var title: String = "",
        var sequenceId: String = "",
        var optionName: String = "",
        var flagCheckbox: String = "N",
        var expireDate: Date = Date(),
        var messageType: String = "",
        var registerNumber: String = "",
        var datetime: Date = Date(),
        var installmentAmount: String = "",
        var description: String = "",
        var image: String = "",
        var media: String = "",
        var message: String = "",
        var iconColor: String = "R",
        var navigation: String = ""
) {

    fun transformToRealm(state: NotifyEntity) {
        this@NotifyEntity.notifyKey = state.notifyKey
        this@NotifyEntity.title = state.title
        this@NotifyEntity.sequenceId = state.sequenceId
        this@NotifyEntity.optionName = state.optionName
        this@NotifyEntity.flagCheckbox = state.flagCheckbox
        this@NotifyEntity.expireDate = state.expireDate
        this@NotifyEntity.messageType = state.messageType
        this@NotifyEntity.registerNumber = state.registerNumber
        this@NotifyEntity.datetime = state.datetime
        this@NotifyEntity.installmentAmount = state.installmentAmount
        this@NotifyEntity.description = state.description
        this@NotifyEntity.image = state.image
        this@NotifyEntity.media = state.media
        this@NotifyEntity.message = state.message
        this@NotifyEntity.iconColor = state.iconColor
        this@NotifyEntity.navigation = state.navigation
    }

    fun transform(): NotifyEntity {
        return NotifyEntity().apply {
            this.notifyKey = this@NotifyEntity.notifyKey
            this.title = this@NotifyEntity.title
            this.sequenceId = this@NotifyEntity.sequenceId
            this.optionName = this@NotifyEntity.optionName
            this.flagCheckbox = this@NotifyEntity.flagCheckbox
            this.expireDate = this@NotifyEntity.expireDate
            this.messageType = this@NotifyEntity.messageType
            this.registerNumber = this@NotifyEntity.registerNumber
            this.datetime = this@NotifyEntity.datetime
            this.installmentAmount = this@NotifyEntity.installmentAmount
            this.description = this@NotifyEntity.description
            this.image = this@NotifyEntity.image
            this.media = this@NotifyEntity.media
            this.message = this@NotifyEntity.message
            this.iconColor = this@NotifyEntity.iconColor
            this.navigation = this@NotifyEntity.navigation
        }
    }
}