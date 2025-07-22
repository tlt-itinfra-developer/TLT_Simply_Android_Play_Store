package tlt.th.co.toyotaleasing.view.imagehistory

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import androidx.recyclerview.widget.DividerItemDecoration
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.widget_image_histories.view.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.carloan.common.ItemFilesData


class FileHistoriesWidget : FrameLayout {

    @JvmOverloads
    constructor(
            context: Context,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = 0)
            : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
            context: Context,
            attrs: AttributeSet?,
            defStyleAttr: Int,
            defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(R.layout.widget_image_histories, this, true)
        recyclerview_image_histories.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    fun setData(histories: List<History>) {
        recyclerview_image_histories.adapter = FileHistoriesAdapter(histories)
    }

    data class History(
            val title: String = "",
            val images: List<ItemFilesData> = listOf()
    )

}