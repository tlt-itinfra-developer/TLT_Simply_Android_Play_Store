package tlt.th.co.toyotaleasing.modules.appintro

import androidx.annotation.DrawableRes

data class AppIntro(
        val title: String = "",
        val description: String = "",
        @DrawableRes val backgroundRes: Int,
        @DrawableRes val iconRes: Int
)