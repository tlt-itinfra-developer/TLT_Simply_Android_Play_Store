package tlt.th.co.toyotaleasing.view

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import androidx.constraintlayout.widget.Group
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.item_header_insurance_status.view.*
import kotlinx.android.synthetic.main.widget_insurance_document_status.view.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.extension.gone
import tlt.th.co.toyotaleasing.common.extension.visible

class CheckStatusTaxWidget : FrameLayout {

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
        inflate(context, R.layout.widget_insurance_document_status, this)
        val a = context.obtainStyledAttributes(attrs,
                R.styleable.CheckStatusTaxWidget)

        attrs?.let {
            val title = a.getString(R.styleable.CheckStatusTaxWidget_title_header) ?: ""
            val isVisibleHeader = a.getInt(R.styleable.CheckStatusTaxWidget_visibility_header, View.VISIBLE)
            val isVisibleFooter = a.getInt(R.styleable.CheckStatusTaxWidget_visibility_footer, View.VISIBLE)

            setHeaderTitle(title)
            setVisibilityHeaderLayout(isVisibleHeader)
            setVisibilityFooterLayout(isVisibleFooter)
        }

        a.recycle()
    }

    fun setHeaderTitle(title: String) {
        header_layout.title.text = title
    }

    fun setVisibilityHeaderLayout(isVisible: Int) {
        header_layout.visibility = isVisible
    }

    fun setVisibilityFooterLayout(isVisible: Int) {
        status_title_tv.visibility = isVisible
        space.visibility = isVisible
        line.visibility = isVisible
        pay_tax_success.visibility = isVisible
        pay_tax_success_iv.visibility = isVisible
    }

    fun setData(data: Model) {
        date_tv.text = data.date

        setPorlorborStatus(data.porlorborStatus)
        setTorloraorStatus(data.torloraorStatus)
        setGasStatus(data.gasStatus)

        if (data.isShowPaymentComplete) {
            setVisibilityFooterLayout(View.VISIBLE)
        } else {
            setVisibilityFooterLayout(View.GONE)
        }

        if (isShowDocumentTitle(data.porlorborStatus, data.torloraorStatus, data.gasStatus)) {
            group_document_status.visible()
        } else {
            group_document_status.gone()
        }
    }

    private fun setPorlorborStatus(status: Status) {
        setStatus(
                status,
                ctpl_group,
                insurance_ctpl_status_iv,
                ctpl_insurance_title,
                ctpl_insurance_button,
                context.getString(R.string.document_status_ctpl_title),
                context.getString(R.string.insurance_status_complete)
        )
    }

    private fun setTorloraorStatus(status: Status) {
        setStatus(
                status,
                inspection_group,
                insurance_inspection_status_iv,
                inspection_insurance_title,
                inspection_insurance_button,
                context.getString(R.string.insurance_status_warning_inspection),
                context.getString(R.string.insurance_status_complete)
        )
    }

    private fun setGasStatus(status: Status) {
        setStatus(
                status,
                ngv_group,
                insurance_ngv_status_iv,
                ngv_insurance_title,
                ngv_insurance_button,
                context.getString(R.string.gas_status_warning_inspection),
                context.getString(R.string.gas_status_complete)
        )
    }

    private fun isShowDocumentTitle(vararg status: Status) = status.any { it != Status.HIDE }

    private fun setStatus(status: Status,
                          layoutGroup: Group,
                          imageViewStatus: AppCompatImageView,
                          titleView: AppCompatTextView,
                          buttonView: AppCompatButton,
                          warnringMessage: String,
                          completeMessage: String) {
        layoutGroup.visible()

        when (status) {
            Status.COMPLETE -> {
                imageViewStatus.setImageResource(R.drawable.icon_complete_attach)
                titleView.text = completeMessage
                buttonView.gone()
            }
            Status.PENDING -> {
                imageViewStatus.setImageResource(R.drawable.ic_pending_attach)
                titleView.text = context.getString(R.string.insurance_status_pending)
                buttonView.visible()
            }
            Status.WARNING -> {
                imageViewStatus.setImageResource(R.drawable.ic_alert_warning)
                titleView.text = warnringMessage
                buttonView.visible()
            }
            Status.WRONG -> {
                imageViewStatus.setImageResource(R.drawable.icon_alert_incorrect_attach)
                titleView.text = context.getString(R.string.insurance_status_incorrect)
                buttonView.visible()
            }
            else -> {
                layoutGroup.gone()
                buttonView.gone()
            }
        }
    }

    fun onPorlorborClickListener(listener: () -> Unit) {
        ctpl_insurance_button.setOnClickListener { listener.invoke() }
    }

    fun onTorloraorClickListener(listener: () -> Unit) {
        inspection_insurance_button.setOnClickListener { listener.invoke() }
    }

    fun onGasClickListener(listener: () -> Unit) {
        ngv_insurance_button.setOnClickListener { listener.invoke() }
    }

    data class Model(
            val date: String = "",
            val porlorborStatus: Status = Status.HIDE,
            val torloraorStatus: Status = Status.HIDE,
            val gasStatus: Status = Status.HIDE,
            val isShowPaymentComplete: Boolean = false
    )

    enum class Status {
        COMPLETE,
        PENDING,
        WARNING,
        WRONG,
        HIDE
    }
}