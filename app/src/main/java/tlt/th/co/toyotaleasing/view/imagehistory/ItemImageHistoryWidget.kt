package tlt.th.co.toyotaleasing.view.imagehistory

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.widget_item_image_history.view.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.extension.toDatetime
import tlt.th.co.toyotaleasing.manager.ContextManager

class ItemImageHistoryWidget : FrameLayout {

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
        LayoutInflater.from(context).inflate(R.layout.widget_item_image_history, this, true)
    }

    fun setData(data: ImageHistoriesWidget.History) {
        if(data.title == ""){
            title_date.text = ContextManager.getInstance().getApplicationContext().resources.getString(R.string.upload_history)
        }else {
            title_date.text = data.title.toDatetime()
        }
        recyclerview_image_histories.adapter = ItemImageHistoryAdapter(data.images)
    }
}