package tlt.th.co.toyotaleasing.view.insurance

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.widget_insurance_payment_view.view.*
import tlt.th.co.toyotaleasing.R

class InsurancePaymentWidget : FrameLayout {
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
        inflate(context, R.layout.widget_insurance_payment_view, this)
    }

    fun initialInformationData(model: InsurancePaymentInfomationData) {
        insurance_company_field_tv.text = model.companyName
        insurance_type_protection_field_tv.text = model.typeProtection
        insurance_budget_field_tv.text = model.budget
        insurance_condition_field_tv.text = model.condition
        insurance_end_of_protection_field_tv.text = model.endOfProtectionDate
    }

    data class InsurancePaymentInfomationData(val companyName: String = "",
                                              val typeProtection: String = "",
                                              val budget: String = "",
                                              val condition: String = "",
                                              val endOfProtectionDate: String = "")
}