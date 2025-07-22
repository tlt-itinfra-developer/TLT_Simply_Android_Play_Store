package tlt.th.co.toyotaleasing.model.entity

open class AnalyticsEntity(
        var id: String = "",
        var cust_id: String = "",
        var extContract: String = "",
        var os_version: String = "",
        var os: String = "",
        var screenName: String = "",
        var logType: String = "",
        var startTimeStamp: String = "",
        var endTimeStamp: String = "",
        var durationTime: String = "",
        var type: String = "",
        var label: String = "",
        var value: String = "",
        var info: String = "",
        var action: String = "",
        var language: String = "",
        var device_name: String = ""
) {

    fun transformToRealm(state: AnalyticsEntity) {
        this@AnalyticsEntity.id = state.id
        this@AnalyticsEntity.cust_id = state.cust_id
        this@AnalyticsEntity.extContract = state.extContract
        this@AnalyticsEntity.os_version = state.os_version
        this@AnalyticsEntity.os = state.os
        this@AnalyticsEntity.screenName = state.screenName
        this@AnalyticsEntity.logType = state.logType
        this@AnalyticsEntity.startTimeStamp = state.startTimeStamp
        this@AnalyticsEntity.endTimeStamp = state.endTimeStamp
        this@AnalyticsEntity.durationTime = state.durationTime
        this@AnalyticsEntity.type = state.type
        this@AnalyticsEntity.label = state.label
        this@AnalyticsEntity.value = state.value
        this@AnalyticsEntity.info = state.info
        this@AnalyticsEntity.action = state.action
        this@AnalyticsEntity.language = state.language
        this@AnalyticsEntity.device_name = state.device_name
    }

    fun transform(): AnalyticsEntity {
        return AnalyticsEntity().apply {
            this.id = this@AnalyticsEntity.id
            this.cust_id = this@AnalyticsEntity.cust_id
            this.extContract = this@AnalyticsEntity.extContract
            this.os_version = this@AnalyticsEntity.os_version
            this.os = this@AnalyticsEntity.os
            this.screenName = this@AnalyticsEntity.screenName
            this.logType = this@AnalyticsEntity.logType
            this.startTimeStamp = this@AnalyticsEntity.startTimeStamp
            this.endTimeStamp = this@AnalyticsEntity.endTimeStamp
            this.durationTime = this@AnalyticsEntity.durationTime
            this.type = this@AnalyticsEntity.type
            this.label = this@AnalyticsEntity.label
            this.value = this@AnalyticsEntity.value
            this.info = this@AnalyticsEntity.info
            this.action = this@AnalyticsEntity.action
            this.language = this@AnalyticsEntity.language
            this.device_name = this@AnalyticsEntity.device_name
        }
    }

}