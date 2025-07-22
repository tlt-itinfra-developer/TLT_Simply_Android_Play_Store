package tlt.th.co.toyotaleasing.model.entity



open class NotificationClickedEntity(var msg: String = "",
                                     var title: String = "",
                                     var imageUrl: String = "",
                                     var flagCheckbox: String = "")  {

    fun transformToRealm(notification: NotificationClickedEntity) {
        this@NotificationClickedEntity.msg = notification.msg
        this@NotificationClickedEntity.title = notification.title
        this@NotificationClickedEntity.imageUrl = notification.imageUrl
        this@NotificationClickedEntity.flagCheckbox = notification.flagCheckbox
    }

    fun transform(): NotificationClickedEntity {
        return NotificationClickedEntity().apply {
            this.msg = this@NotificationClickedEntity.msg
            this.title = this@NotificationClickedEntity.title
            this.imageUrl = this@NotificationClickedEntity.imageUrl
            this.flagCheckbox = this@NotificationClickedEntity.flagCheckbox
        }
    }

    fun toJsonString(): String {
        return NotificationClickedEntity().toString()
    }
}