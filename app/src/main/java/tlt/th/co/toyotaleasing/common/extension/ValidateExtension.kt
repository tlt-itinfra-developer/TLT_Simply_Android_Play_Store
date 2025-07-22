package tlt.th.co.toyotaleasing.common.extension

import android.util.Patterns

fun String.isEmail(): Boolean {
    return this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isIdCardThai(): Boolean {
    if (this.length != 13) {
        return false
    }

    var sum = 0
    for (i in 0..11) {
        sum += Character.getNumericValue(this[i]) * (13 - i)
    }

    return (11 - sum % 11) % 10 == Character.getNumericValue(this[12])
}

fun String.isDoesNotContainSpecialCharacter(): Boolean {
    val regex = "[A-Za-z0-9]+".toRegex()
    return regex.matches(this)
}

fun String.isDoesNotContainCharacter(): Boolean {
    val regex = "[0-9]+".toRegex()
    return regex.matches(this)
}