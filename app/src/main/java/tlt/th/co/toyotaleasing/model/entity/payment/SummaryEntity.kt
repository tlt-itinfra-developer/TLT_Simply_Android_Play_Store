package tlt.th.co.toyotaleasing.model.entity.payment



open class SummaryEntity(
        var type: String = "",
        var code: String = "",
        var title: String = "",
        var price: String = "",
        var isChecked: Boolean = true,
        var isRecentlyAdd: Boolean = false,
        var defaultValue: String = "" ,
        var cStatus_code: String = ""
) 