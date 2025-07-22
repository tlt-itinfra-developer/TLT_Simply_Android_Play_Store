package tlt.th.co.toyotaleasing.common.extension

import androidx.appcompat.widget.AppCompatEditText
import android.text.Editable
import android.text.TextWatcher

fun AppCompatEditText.onWatcherAfterTextChanged(callback: (text: String) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(message: CharSequence?, start: Int, before: Int, count: Int) {
            callback.invoke(message.toString())
        }
    })
}