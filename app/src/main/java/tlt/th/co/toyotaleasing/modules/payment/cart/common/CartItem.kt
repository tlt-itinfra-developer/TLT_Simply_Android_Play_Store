package tlt.th.co.toyotaleasing.modules.payment.cart.common

import tlt.th.co.toyotaleasing.common.extension.toNumberFormat

data class CartItem(
        var type: String = "",
        var code: String = "",
        var title: String = "",
        var _price: String = "",
        var isChecked: Boolean = true,
        var isRecentlyAdd: Boolean = false,
        var defaultValue: String = "" ,
        var cStatus_code: String = ""

) {
    val price: String
        get() = _price.toNumberFormat()
}