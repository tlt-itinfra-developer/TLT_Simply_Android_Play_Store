package tlt.th.co.toyotaleasing.view

import android.content.Context
import androidx.appcompat.widget.AppCompatTextView
import android.util.AttributeSet

class OwaspTextView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        init()
    }

    private fun init() {
        isLongClickable = false
    }

}