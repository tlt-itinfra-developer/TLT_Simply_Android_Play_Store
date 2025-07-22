package tlt.th.co.toyotaleasing.modules.sidebarmenu

import androidx.annotation.DrawableRes

data class SidebarMenu(
        val title: String,
        @DrawableRes val iconLeft: Int,
        @DrawableRes val iconRight: Int = 0,
        val type: Int = Type.MENU,
        val function: () -> Unit
) {
    constructor(type: Int = Type.DIVIDER) : this("", 0, 0, type, {})
    constructor(title: String, @DrawableRes iconLeft: Int, type: Int = Type.DIVIDER, function: () -> Unit) : this(title, iconLeft, 0, type, function)

    fun isIconRight() = iconRight != 0

    object Type {
        const val EMPTY = 0
        const val MENU = 1
        const val DIVIDER = 2
    }
}
