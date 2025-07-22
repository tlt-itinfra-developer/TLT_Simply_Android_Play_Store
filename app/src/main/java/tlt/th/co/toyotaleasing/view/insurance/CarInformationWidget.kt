package tlt.th.co.toyotaleasing.view.insurance

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.widget_car_information.view.*
import tlt.th.co.toyotaleasing.R

/**
 * Created by beer on 21/6/2018 AD.
on MAC TLT
 */
class CarInformationWidget : FrameLayout {

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
        inflate(context, R.layout.widget_car_information, this)
    }

    fun initialInformationData(model: CarInformationData) {
        car_information_no_tv.text = model.licenseCar
        car_information_name_tv.text = model.licenseOwnerName
        car_information_contract_number_tv.text = context.getString(R.string.my_car_txt_contract_number, model.contractNumber)
        car_information_date_no_tv.text = model.currentDate
    }

    data class CarInformationData(var licenseCar: String = "",
                                  var licenseOwnerName: String = "",
                                  var contractNumber: String = "",
                                  var currentDate: String = "")

}