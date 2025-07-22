package tlt.th.co.toyotaleasing.common.extension

import com.google.android.material.textfield.TextInputLayout
import androidx.appcompat.widget.AppCompatEditText
import android.text.Editable
import android.text.TextWatcher
import android.widget.FrameLayout

fun com.google.android.material.textfield.TextInputLayout.enableErrorMessage(message: String) {
    error = message
    isErrorEnabled = true
}

fun com.google.android.material.textfield.TextInputLayout.disableErrorMessage() {
    error = null
    isErrorEnabled = false
}

fun com.google.android.material.textfield.TextInputLayout.enableClearErrorWhenTextChanged() {
    val frameLayout = getChildAt(0) as FrameLayout
    val edittext = frameLayout.getChildAt(0) as AppCompatEditText

    edittext.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(message: CharSequence?, start: Int, before: Int, count: Int) {
            disableErrorMessage()
        }
    })
}