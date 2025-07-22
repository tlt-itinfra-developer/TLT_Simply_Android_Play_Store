package tlt.th.co.toyotaleasing.view

import android.content.Context
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.TextViewCompat
import androidx.appcompat.widget.AppCompatTextView
import android.util.AttributeSet
import tlt.th.co.toyotaleasing.R

class TLTTextView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : AppCompatTextView(context, attrs, defStyleAttr) {

    private val fonts = hashMapOf(0 to R.font.custom_regular,
            1 to R.font.custom_bold,
            2 to R.font.custom_bold)
    private var useFont = fonts[0]

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.TLTTextView, 0, 0)
            useFont = fonts[typedArray.getInt(R.styleable.TLTTextView_fontType, 0)]

            typedArray.recycle()
        }

        initTypeface()
        initAutosizing()
    }

    private fun initTypeface() {
        val typeface = ResourcesCompat.getFont(context, useFont!!)
        setTypeface(typeface)
    }

    private fun initAutosizing() {
        TextViewCompat.setAutoSizeTextTypeWithDefaults(this, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM)
    }
}