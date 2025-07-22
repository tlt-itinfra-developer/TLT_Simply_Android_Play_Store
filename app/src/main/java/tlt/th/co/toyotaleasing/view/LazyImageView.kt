package tlt.th.co.toyotaleasing.view

import android.content.Context
import androidx.appcompat.widget.AppCompatImageView
import android.util.AttributeSet
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.extension.loadImageByDrawableRes

class LazyImageView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : AppCompatImageView(context, attrs, defStyleAttr) {

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.LazyImageView, 0, 0)
            val imageDrawable = typedArray.getResourceId(R.styleable.LazyImageView_imageResource, 0)

            typedArray.recycle()

            if (imageDrawable != 0) {
                loadImageByDrawableRes(imageDrawable)
            }
        }
    }
}