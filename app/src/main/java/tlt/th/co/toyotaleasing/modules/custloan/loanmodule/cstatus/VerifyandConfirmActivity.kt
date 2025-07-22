package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.cstatus

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.SpinnerAdapter
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_loan_sts_ver_confirm.*
import kotlinx.android.synthetic.main.loan_check_status_upper_sheet.view.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.dialog.MsgdescNormalDialog
import tlt.th.co.toyotaleasing.common.dialog.NormalDialogFragment
import tlt.th.co.toyotaleasing.common.extension.gone
import tlt.th.co.toyotaleasing.common.extension.isVisible
import tlt.th.co.toyotaleasing.common.extension.setDrawableEnd
import tlt.th.co.toyotaleasing.common.extension.visible
import tlt.th.co.toyotaleasing.modules.custloan.MenuStepController
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.center.CarLoanFragment
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.cstatus.common.VerifyConfirmData
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.geo.GeoLocationActivity
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.geo.location.LoanLocationActivity
import java.text.SimpleDateFormat
import java.util.*

class VerifyandConfirmActivity   : BaseActivity()  , MsgdescNormalDialog.Listener {
    var calendarData = Calendar.getInstance()
    private var setProvinceByDefault: Boolean = false
    private var setAmphurByDefault: Boolean = false
    private var setRegisProvinceByDefault: Boolean = false
    private var setRegisAmphurtByDefault: Boolean = false
    private var setMailingProvinceByDefault: Boolean = false
    private var setMailingAmphurByDefault: Boolean = false

    private var tempDataType : VerifyConfirmData? = null

    private val data_extra by lazy {
        intent?.getStringExtra(REF_ID) ?: ""
    }


    private val data_url by lazy {
        intent?.getStringExtra(REF_URL) ?: ""
    }


    private val viewModel by lazy {
        ViewModelProviders.of(this).get(VerifyandConfirmViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_loan_sts_ver_confirm)
        AnalyticsManager.trackScreen(AnalyticsScreenName.ONLINE_VERIFY_CONFIRM)
        initInstance()
        initViewModel()

        viewModel.initProvideData(data_extra)
    }

    fun initInstance() {

        map_online_require_upper_sheet.stepVerify.background = ContextCompat.getDrawable(this, R.drawable.step_verify_active)
        map_online_require_upper_sheet.stp1.setTextColor(ContextCompat.getColor(this@VerifyandConfirmActivity, R.color.cherry_red))
        map_online_require_upper_sheet.txt_ref_value.text = data_extra


        // Default
        h_mycar_info_card_view.visible()
        h_mycar_info.setDrawableEnd(R.drawable.expand_down)
        h_delivery_dealer_card_view.visible()
        h_delivery_car.setDrawableEnd(R.drawable.expand_down)
        h_address_card_view.visible()
        h_address.setDrawableEnd(R.drawable.expand_up)

        h_mycar_info.setOnClickListener {
           if(  h_mycar_info_card_view.isVisible() == true) {
               h_mycar_info.setDrawableEnd(R.drawable.expand_up)
               h_mycar_info_card_view.gone()
           }else {
               h_mycar_info.setDrawableEnd(R.drawable.expand_down)
               h_mycar_info_card_view.visible()
           }

        }
        h_delivery_car.setOnClickListener {
            if(  h_delivery_dealer_card_view.isVisible() == true) {
                h_delivery_car.setDrawableEnd(R.drawable.expand_up)
                h_delivery_dealer_card_view.gone()
            } else {
                h_delivery_car.setDrawableEnd(R.drawable.expand_down)
                h_delivery_dealer_card_view.visible()
            }
        }
        h_address.setOnClickListener {
            if(  h_address_card_view.isVisible() == true) {
                h_address.setDrawableEnd(R.drawable.expand_up)
                h_address_card_view.gone()
            }else {
                h_address.setDrawableEnd(R.drawable.expand_down)
                h_address_card_view.visible()
            }
        }

        /*   Address Tab  */

        // Default
        btn_current.setBackground(ContextCompat.getDrawable(this@VerifyandConfirmActivity, R.drawable.selector_btn_tab))
        btn_current.setTextColor(ContextCompat.getColor(this@VerifyandConfirmActivity, R.color.white))
        h_address_current_address_card.visible()
        h_address_regis_address_card.gone()
        h_address_mailing_address_card.gone()

        btn_current.setOnClickListener {
            btn_current.setBackground(ContextCompat.getDrawable(this@VerifyandConfirmActivity, R.drawable.selector_btn_tab))
            btn_regis.setBackgroundColor(ContextCompat.getColor(this@VerifyandConfirmActivity, R.color.grey_4))
            btn_mailing.setBackgroundColor(ContextCompat.getColor(this@VerifyandConfirmActivity, R.color.grey_4))
            btn_current.setTextColor(ContextCompat.getColor(this@VerifyandConfirmActivity, R.color.white))
            btn_regis.setTextColor(ContextCompat.getColor(this@VerifyandConfirmActivity, R.color.black))
            btn_mailing.setTextColor(ContextCompat.getColor(this@VerifyandConfirmActivity, R.color.black))
            h_address_current_address_card.visible()
            h_address_regis_address_card.gone()
            h_address_mailing_address_card.gone()
        }

        btn_regis.setOnClickListener {
            btn_current.setBackgroundColor(ContextCompat.getColor(this@VerifyandConfirmActivity, R.color.grey_4))
            btn_regis.setBackground(ContextCompat.getDrawable(this@VerifyandConfirmActivity, R.drawable.selector_btn_tab))
            btn_mailing.setBackgroundColor(ContextCompat.getColor(this@VerifyandConfirmActivity, R.color.grey_4))
            btn_current.setTextColor(ContextCompat.getColor(this@VerifyandConfirmActivity, R.color.black))
            btn_regis.setTextColor(ContextCompat.getColor(this@VerifyandConfirmActivity, R.color.white))
            btn_mailing.setTextColor(ContextCompat.getColor(this@VerifyandConfirmActivity, R.color.black))
            h_address_current_address_card.gone()
            h_address_regis_address_card.visible()
            h_address_mailing_address_card.gone()
        }

        btn_mailing.setOnClickListener {
            btn_current.setBackgroundColor(ContextCompat.getColor(this@VerifyandConfirmActivity, R.color.grey_4))
            btn_regis.setBackgroundColor(ContextCompat.getColor(this@VerifyandConfirmActivity, R.color.grey_4))
            btn_mailing.setBackground(ContextCompat.getDrawable(this@VerifyandConfirmActivity, R.drawable.selector_btn_tab))
            btn_current.setTextColor(ContextCompat.getColor(this@VerifyandConfirmActivity, R.color.black))
            btn_regis.setTextColor(ContextCompat.getColor(this@VerifyandConfirmActivity, R.color.black))
            btn_mailing.setTextColor(ContextCompat.getColor(this@VerifyandConfirmActivity, R.color.white))
            h_address_current_address_card.gone()
            h_address_regis_address_card.gone()
            h_address_mailing_address_card.visible()
        }

        btn_next_confirm.setOnClickListener {
            if(formValidate()){
                var list =   getVeriyData()
                if(list != null) {
                    viewModel.SyncVerify(data_extra, list)
                }
            }else{
                NormalDialogFragment.show(
                        fragmentManager = supportFragmentManager,
                        description = getString(R.string.fillout_personal_data),
                        confirmButtonMessage = getString(R.string.dialog_button_ok)
                )
            }
        }

        btn_showroom.setOnClickListener {
            nextGeo(isSaveInfo = false)
            LoanLocationActivity.open(this@VerifyandConfirmActivity, data_extra, tempDataType!!.offer_dealercode, tempDataType!!.showroomCode)
        }

        btn_geo_address.setOnClickListener {
            nextGeo(isSaveInfo = false)
            AnalyticsManager.onlineVerifyConfirmMapClick()
            GeoLocationActivity.StartWithType(this@VerifyandConfirmActivity, data_extra, VerifyandConfirmActivity.ADDRESS)
        }

        btn_geo_regis_address.setOnClickListener {
            nextGeo(isSaveInfo = false)
            GeoLocationActivity.StartWithType(this@VerifyandConfirmActivity, data_extra, VerifyandConfirmActivity.REGIS)
        }

        btn_geo_mailing_address.setOnClickListener {
            nextGeo(isSaveInfo = false)
            GeoLocationActivity.StartWithType(this@VerifyandConfirmActivity, data_extra, VerifyandConfirmActivity.MAILING)
        }

        spinner_province.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (!setProvinceByDefault) {
                    viewModel.provinceChange(if (position > 0) position - 1 else 0, VerifyandConfirmActivity.ADDRESS)
                    formValidate( )
                }
                setProvinceByDefault = false
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        spinner_amphur.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (!setAmphurByDefault) {
                    viewModel.amphurChange(if (position > 0) position - 1 else 0, VerifyandConfirmActivity.ADDRESS)
                    formValidate()
                }
                setAmphurByDefault = false
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        spinner_regis_province.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (!setRegisProvinceByDefault) {
                    if(!checkbox_address_regis_same_current.isChecked){
                        viewModel.provinceChange(if (position > 0) position - 1 else 0, VerifyandConfirmActivity.REGIS)
                    }
                    formValidate()
                }
                setRegisProvinceByDefault = false
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        spinner_regis_amphur.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (!setRegisAmphurtByDefault) {
                    if(!checkbox_address_regis_same_current.isChecked){
                        viewModel.amphurChange(if (position > 0) position - 1 else 0, VerifyandConfirmActivity.REGIS)
                    }
                    formValidate()
                }
                setRegisAmphurtByDefault = false
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        spinner_mailing_province.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (!setMailingProvinceByDefault) {
                    if(!checkbox_address_mailing_same_current.isChecked){
                        viewModel.provinceChange(if (position > 0) position - 1 else 0, VerifyandConfirmActivity.MAILING)
                    }
                    formValidate()
                }
                setMailingProvinceByDefault = false
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        spinner_mailing_amphur.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (!setMailingAmphurByDefault) {
                    if(!checkbox_address_mailing_same_current.isChecked) {
                        viewModel.amphurChange(if (position > 0) position - 1 else 0, VerifyandConfirmActivity.MAILING)
                    }
                    formValidate()
                }
                setMailingAmphurByDefault = false
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        val calenderListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

            calendarData.set(Calendar.YEAR, year)
            calendarData.set(Calendar.MONTH, monthOfYear)
            calendarData.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val myFormat = "dd/MM/yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            txt_appointment.text = sdf.format(calendarData.time)
             tempDataType!!.appointment = sdf.format(calendarData.time)
        }



//        checkbox_accept_all_address_current.setOnClickListener {
//            tempDataType!!.flagCurrent = checkbox_accept_all_address_current.isChecked
//            setChkAll(checkbox_accept_all_address_current.isChecked)
//            formValidate()
//        }

        checkbox_address_regis_same_current.setOnClickListener {
            tempDataType!!.flagRegisSameCurrent = checkbox_address_regis_same_current.isChecked
            setRegisSame(checkbox_address_regis_same_current.isChecked)
            formValidate()
        }

        checkbox_address_mailing_same_current.setOnClickListener {
            tempDataType!!.flagMailingSameCurrent = checkbox_address_mailing_same_current.isChecked
            setMailingSame(checkbox_address_mailing_same_current.isChecked)
            formValidate()
        }

        btn_calendar.setOnClickListener {
            val dialog =  DatePickerDialog(
                    this@VerifyandConfirmActivity, calenderListener,
                    calendarData.get(Calendar.YEAR),
                    calendarData.get(Calendar.MONTH),
                    calendarData.get(Calendar.DAY_OF_MONTH)
            )
           dialog!!.datePicker.minDate = System.currentTimeMillis() - 1000
           dialog!!.show()
            formValidate()
        }


        edittext_postcode.addTextChangedListener(onEditPostcodeChanged)
        edittext_regis_postcode.addTextChangedListener(onEditRegisPostcodeChanged)
        edittext_mailing_postcode.addTextChangedListener(onEditMailingPostcodeChanged)

        edittext_current_address.addTextChangedListener(onEdittextTextWatcher)
        edittext_showroom.addTextChangedListener(onEdittextTextWatcher)
        txt_appointment.addTextChangedListener(onEdittextTextWatcher)
        edittext_regis_address.addTextChangedListener(onEdittextTextWatcher)
        edittext_mailing_address.addTextChangedListener(onEdittextTextWatcher)

    }

    private val onEditPostcodeChanged = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if( start == 4 && count == 1 ) {
                viewModel.ChangePostcodeAddressData(edittext_postcode.text.toString(), ADDRESS)
            }
            formValidate()
        }
    }

    private val onEditRegisPostcodeChanged = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if( start == 4 && count == 1 ) {
                viewModel.ChangePostcodeAddressData(edittext_regis_postcode.text.toString(), REGIS)
            }
            formValidate()
        }
    }

    private val onEditMailingPostcodeChanged = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if( start == 4 && count == 1 ) {
                viewModel.ChangePostcodeAddressData(edittext_mailing_postcode.text.toString(), MAILING)
            }
            formValidate()
        }
    }

    private val onEdittextTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(message: CharSequence?, start: Int, before: Int, count: Int) {
            formValidate()
        }
    }


    private fun initViewModel() {

        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!)
        })

        viewModel.whenDataLoaded.observe(this, Observer {
            it?.let {
                initUpdateView(it)
            }
        })


        viewModel.whenAddrProvinceLoaded.observe(this, Observer {
            spinner_province.setItems(it!!)
        })

        viewModel.whenAddrAumphurLoaded.observe(this, Observer {
            spinner_amphur.setItems(it!!)
        })

        viewModel.whenAddrPostcodeLoaded.observe(this, Observer {
            edittext_postcode.setText(it)
        })

        viewModel.whenRegisProvinceLoaded.observe(this, Observer {
            spinner_regis_province.setItems(it!!)

        })

        viewModel.whenRegisAumphurLoaded.observe(this, Observer {
            spinner_regis_amphur.setItems(it!!)
        })

        viewModel.whenRegisPostcodeLoaded.observe(this, Observer {
            edittext_regis_postcode.setText(it)
        })

        viewModel.whenMailProvinceLoaded.observe(this, Observer {
            spinner_mailing_province.setItems(it!!)
        })

        viewModel.whenMailAumphurLoaded.observe(this, Observer {
            spinner_mailing_amphur.setItems(it!!)
        })

        viewModel.whenMailPostcodeLoaded.observe(this, Observer {
            edittext_mailing_postcode.setText(it)

        })

        viewModel.whenDataSpinnerLoaded.observe(this, Observer {
            initUpdateView(it!!)
        })

        viewModel.whenDataSpinnerAddressLoaded.observe(this, Observer {
            initUpdateView(it!!)
        })

        viewModel.whenDataSpinnerRegisLoaded.observe(this, Observer {
            initUpdateView(it!!)
        })

        viewModel.whenDataSpinnerMailingLoaded.observe(this, Observer {
            initUpdateView(it!!)
        })

        viewModel.whenSyncSuccessData.observe(this, Observer {
            it?.let {
                MenuStepController.open(this@VerifyandConfirmActivity, it.ref_id, it.step, it.ref_url)
//                SummaryCheckStatusActivity.start(this@VerifyandConfirmActivity!!, it.ref_id ,it.ref_url )
            }
        })

        viewModel.whenDataLoadedTemp.observe(this, Observer {
            it?.let {
                tempDataType = it
                formValidate()
            }
        })

        viewModel.whenLoadAddrChangePostcode.observe(this, Observer {
            it.let {
                setProvinceByDefault = true
                setAmphurByDefault = true
                initUpdateSpinnerView(it!!)
            }
        })

        viewModel.whenLoadRegisChangePostcode.observe(this, Observer {
            it.let {
                setRegisProvinceByDefault = true
                setRegisAmphurtByDefault = true
                initUpdateSpinnerView(it!!)
            }
        })

        viewModel.whenLoadMailChangePostcode.observe(this, Observer {
            it.let {
                setMailingProvinceByDefault = true
                setMailingAmphurByDefault = true
                initUpdateSpinnerView(it!!)
            }
        })


        viewModel.whenDataLoadedMessage.observe(this, Observer {
            it?.let {
                MsgdescNormalDialog.show(
                        fragmentManager = supportFragmentManager,
                        description = it,
                        confirmButtonMessage = getString(R.string.dialog_button_ok)
                )
            }
        })
    }

    fun initUpdateSpinnerView(it: VerifyandConfirmViewModel.AddressIndexModel) {
        if (it != null) {
            if(it.type == ADDRESS) {
                spinner_province.setSelection(it.indx_pro + 1)
                spinner_amphur.setSelection(it.indx_aum + 1)
            }
            if(it.type == REGIS){
                spinner_regis_province.setSelection(it.indx_pro + 1)
                spinner_regis_amphur.setSelection(it.indx_aum + 1)
            }
            if(it.type == MAILING){
                spinner_mailing_province.setSelection(it.indx_pro + 1)
                spinner_mailing_amphur.setSelection(it.indx_aum + 1)
            }
        }
        formValidate()
    }

//    fun setChkAll(flag : Boolean){
//        if(flag) {
//            edittext_regis_address.setText(edittext_current_address.text.toString())
//            edittext_mailing_address.setText(edittext_current_address.text.toString())
//            edittext_regis_postcode.setText(edittext_postcode.text.toString())
//            edittext_mailing_postcode.setText(edittext_postcode.text.toString())
//            spinner_regis_province.setSelection(spinner_province.selectedItemId.toInt())
//            spinner_mailing_province.setSelection(spinner_province.selectedItemId.toInt())
////            spinner_regis_amphur.setSelection(spinner_regis_amphur.selectedItemId.toInt())
////            spinner_mailing_amphur.setSelection(spinner_mailing_amphur.selectedItemId.toInt())
//            spinner_regis_amphur.setItems(listOf(spinner_amphur.selectedItem.toString()))
//            spinner_regis_amphur.setSelection(1)
//            spinner_mailing_amphur.setItems(listOf(spinner_amphur.selectedItem.toString()))
//            spinner_mailing_amphur.setSelection(1)
//            edittext_regis_address.isEnabled = false
//            edittext_mailing_address.isEnabled = false
//            spinner_regis_amphur.isEnabled = false
//            spinner_mailing_amphur.isEnabled = false
//            spinner_regis_province.isEnabled = false
//            spinner_mailing_province.isEnabled = false
//            edittext_regis_postcode.isEnabled = false
//            edittext_mailing_postcode.isEnabled = false
//            btn_geo_regis_address.isEnabled = false
//            btn_geo_mailing_address.isEnabled = false
//        }else{
//            edittext_regis_address.isEnabled = true
//            edittext_mailing_address.isEnabled = true
//            spinner_regis_amphur.isEnabled = true
//            spinner_mailing_amphur.isEnabled = true
//            spinner_regis_province.isEnabled = true
//            spinner_mailing_province.isEnabled = true
//            edittext_regis_postcode.isEnabled = true
//            edittext_mailing_postcode.isEnabled = true
//            btn_geo_regis_address.isEnabled = true
//            btn_geo_mailing_address.isEnabled = true
//        }
//    }

    fun setRegisSame(flag: Boolean){
        if(flag) {
            edittext_regis_address.setText(edittext_current_address.text.toString())
            edittext_regis_postcode.setText(edittext_postcode.text.toString())
            spinner_regis_province.setSelection(spinner_province.selectedItemId.toInt())
            spinner_regis_amphur.setItems(listOf(spinner_amphur.selectedItem.toString()))
            spinner_regis_amphur.setSelection(1)
            edittext_regis_address.isEnabled = false
            edittext_regis_postcode.isEnabled = false
            btn_geo_regis_address.isEnabled = false
            spinner_regis_province.isEnabled = false
            spinner_regis_amphur.isEnabled = false
        }else{
            edittext_regis_address.isEnabled = true
            spinner_regis_province.isEnabled = true
            edittext_regis_postcode.isEnabled = true
            btn_geo_regis_address.isEnabled = true
            spinner_regis_amphur.isEnabled = true
        }
    }

    fun setMailingSame(flag: Boolean){
        if(flag) {

            edittext_mailing_address.setText(edittext_current_address.text.toString())
            edittext_mailing_postcode.setText(edittext_postcode.text.toString())
            spinner_mailing_province.setSelection(spinner_province.selectedItemId.toInt())
            spinner_mailing_amphur.setItems(listOf(spinner_amphur.selectedItem.toString()))
            spinner_mailing_amphur.setSelection(1)
            edittext_mailing_address.isEnabled = false
            spinner_mailing_province.isEnabled = false
            edittext_mailing_postcode.isEnabled = false
            btn_geo_mailing_address.isEnabled = false
            spinner_mailing_amphur.isEnabled = false
        }else{
            edittext_mailing_address.isEnabled = true
            spinner_mailing_amphur.isEnabled = true
            spinner_mailing_province.isEnabled = true
            edittext_mailing_postcode.isEnabled = true
            btn_geo_mailing_address.isEnabled = true
        }
    }

    private fun formValidate(): Boolean {
//        if(checkbox_accept_all_address_current.isChecked){
//            if( edittext_current_address.text.toString().trim().isEmpty()
//                    || spinner_amphur.isSelectHint()
//                    || spinner_province.isSelectHint()
//                    || edittext_showroom.text.toString().trim().isEmpty()
//                    || txt_appointment.text.toString().trim().isEmpty()
//                    || edittext_mailing_postcode.text.toString().trim().isEmpty()   ) {
//                btn_next_confirm.isEnabled = false
//                return false
//            }
//            btn_next_confirm.isEnabled = true
//            return true
//        }else {
        if (edittext_current_address.text.toString().trim().isEmpty()
                    || edittext_regis_address.text.toString().trim().isEmpty()
                    || edittext_mailing_address.text.toString().trim().isEmpty()
                    || spinner_amphur.isSelectHint()
                    || spinner_regis_amphur.isSelectHint()
                    || spinner_mailing_amphur.isSelectHint()
                    || spinner_province.isSelectHint()
                    || spinner_regis_province.isSelectHint()
                    || spinner_mailing_province.isSelectHint()
                    || edittext_showroom.text.toString().trim().isEmpty()
                    || txt_appointment.text.toString().trim().isEmpty()
                    || edittext_postcode.text.toString().trim().isEmpty()
                    || edittext_mailing_postcode.text.toString().trim().isEmpty()
                    || edittext_regis_postcode.text.toString().trim().isEmpty()) {
                btn_next_confirm.isEnabled = false
                return false
        }
        btn_next_confirm.isEnabled = true
        return true
    }

    fun initUpdateView(it: VerifyandConfirmViewModel.VerConirmDataModel?) {

        setProvinceByDefault = true
        setAmphurByDefault = true
        setRegisProvinceByDefault = true
        setRegisAmphurtByDefault = true
        setMailingProvinceByDefault = true
        setMailingAmphurByDefault = true

        if (it != null)
        {
            txt_status.setText(it.main.statusApp)
            txt_loan_no.setText(it.main.loanNo)
            txt_loan_name.setText(it.main.name)
            txt_model.setText(it.mCI.model)
            txt_grade.setText(it.mCI.grade)
            txt_car_price.setText(it.mCI.carPrice)
            txt_down_pay.setText(it.mCI.downPayment)
            txt_payment_term.setText(it.mCI.paymentTerms)
            txt_interest.setText(it.mCI.interests)
            txt_monthly.setText(it.mCI.installment)
            txt_contract_hand_fee.setText(it.mCI.contract_hanf_fee)
            txt_other_expense.setText(it.mCI.otherExpense)
            edittext_showroom.setText(it.delivercar.showroomName)
            if(it.delivercar.appointment!= ""){
                txt_appointment.setText(it.delivercar.appointment)
            }
            checkbox_accept_all_address_current.isChecked = it.flagCheckAll
            checkbox_address_regis_same_current.isChecked = it.flagRegisSame
            checkbox_address_mailing_same_current.isChecked = it.flagMailingsSame
            edittext_current_address.setText(it.address.rEALADDRESS)
            edittext_regis_address.setText(it.regis.rEALADDRESS)
            edittext_mailing_address.setText(it.mailing.rEALADDRESS)
            if(it.address.pOSTCODE.isNotEmpty()) {
                viewModel.ChangePostcodeAddressData(edittext_postcode.text.toString(), ADDRESS)
            }
            if(it.regis.pOSTCODE.isNotEmpty()) {
                viewModel.ChangePostcodeAddressData(edittext_regis_postcode.text.toString(), REGIS)
            }
            if(it.mailing.pOSTCODE.isNotEmpty()) {
                viewModel.ChangePostcodeAddressData(edittext_mailing_postcode.text.toString(), MAILING)
            }
        }
    }

    fun initUpdateView(it: VerifyandConfirmViewModel.AddressAllTypeIndexModel) {
        if (it != null) {
            if(it.address != null) {
                spinner_province.setSelection(it.address.indx_pro + 1)
                spinner_amphur.setSelection(it.address.indx_aum + 1)
                edittext_postcode.setText(it.address.postcode)
            }
            if(it.regis != null) {
                if(it.regis.indx_pro != 0){
                    spinner_regis_province.setSelection(it.regis.indx_pro + 1)
                    spinner_regis_amphur.setSelection(it.regis.indx_aum + 1)
                }else{
                    spinner_regis_province.setSelection(it.regis.indx_pro)
                    spinner_regis_amphur.setSelection(it.regis.indx_aum)
                }
                edittext_regis_postcode.setText(it.regis.postcode)
            }

            if(it.mailing != null) {
                if(it.regis.indx_pro != 0){
                    spinner_mailing_province.setSelection(it.mailing.indx_pro + 1)
                    spinner_mailing_amphur.setSelection(it.mailing.indx_aum + 1)
                }else{
                    spinner_mailing_province.setSelection(it.mailing.indx_pro)
                    spinner_mailing_amphur.setSelection(it.mailing.indx_aum)
                }
                edittext_mailing_postcode.setText(it.mailing.postcode)
            }

        }
    }
    private fun nextGeo(isSaveInfo: Boolean = false) {
        val items   =    getVeriyData()
        if (items!=null){
            viewModel.saveList(items)
        }
    }

    fun getVeriyData() : VerifyConfirmData? {
        try{
//            val flagSameAddr = checkbox_accept_all_address_current.isChecked
            val flagRegisSame = checkbox_address_regis_same_current.isChecked
            val flagMailingSame = checkbox_address_mailing_same_current.isChecked
            val personalDataList =
                    VerifyConfirmData(
                            appointment = tempDataType!!.appointment,
                            dueDate = tempDataType!!.dueDate,
                            showroomCode = tempDataType!!.showroomCode,
                            showroomName = tempDataType!!.showroomName,
                            flagCurrent = false,
                            flagRegisSameCurrent = flagRegisSame,
                            flagMailingSameCurrent = flagMailingSame,
                            Curr_rEALADDRESS = edittext_current_address.text.toString(),
                            Curr_aMPHURCODE = spinner_amphur.getSelectedPositionWithoutHint().toString(),
                            Curr_aMPHUR = if (spinner_amphur.selectedItemId > 0) {
                                spinner_amphur.selectedItem.toString() ?: ""
                            } else "",
                            Curr_pROVINCECODE = spinner_province.getSelectedPositionWithoutHint().toString(),
                            Curr_pROVINCE = if (spinner_province.selectedItemId > 0) {
                                spinner_province.selectedItem.toString() ?: ""
                            } else "",
                            Curr_pOSTCODE = edittext_postcode.text.toString() ?: "",
                            Curr_lat = tempDataType!!.Curr_lat,
                            Curr_lng = tempDataType!!.Curr_lng,
                            Regis_rEALADDRESS = if (flagRegisSame) {
                                edittext_current_address.text.toString()
                            } else edittext_regis_address.text.toString(),
                            Regis_aMPHURCODE = if (flagRegisSame) {
                                spinner_amphur.getSelectedPositionWithoutHint().toString()
                            } else {
                                spinner_regis_amphur.getSelectedPositionWithoutHint().toString()
                            },
                            Regis_aMPHUR = if (flagRegisSame) {
                                if (spinner_amphur.selectedItemId > 0) {
                                    spinner_amphur.selectedItem.toString() ?: ""
                                } else ""
                            } else {
                                if (spinner_regis_amphur.selectedItemId > 0) {
                                    spinner_regis_amphur.selectedItem.toString() ?: ""
                                } else ""
                            },
                            Regis_pROVINCECODE = if (flagRegisSame) {
                                spinner_province.getSelectedPositionWithoutHint().toString()
                            } else {
                                spinner_regis_province.getSelectedPositionWithoutHint().toString()
                            },
                            Regis_pROVINCE = if (flagRegisSame) {
                                if (spinner_province.selectedItemId > 0) {
                                    spinner_province.selectedItem.toString() ?: ""
                                } else ""
                            } else {
                                if (spinner_regis_province.selectedItemId > 0) {
                                    spinner_regis_province.selectedItem.toString() ?: ""
                                } else ""
                            },
                            Regis_pOSTCODE = if (flagRegisSame) {
                                edittext_postcode.text.toString() ?: ""
                            } else {
                                edittext_regis_postcode.text.toString() ?: ""
                            },
                            Regis_lat = if (flagRegisSame) {
                                tempDataType!!.Curr_lat
                            } else {
                                tempDataType!!.Regis_lat
                            },
                            Regis_lng = if (flagRegisSame) {
                                tempDataType!!.Curr_lng
                            } else {
                                tempDataType!!.Regis_lng
                            },
                            Mailing_rEALADDRESS = if (flagMailingSame) {
                                edittext_current_address.text.toString()
                            } else {
                                edittext_mailing_address.text.toString()
                            },
                            Mailing_aMPHURCODE = if (flagMailingSame) {
                                spinner_amphur.getSelectedPositionWithoutHint().toString()
                            } else {
                                spinner_mailing_amphur.getSelectedPositionWithoutHint().toString()
                            },
                            Mailing_aMPHUR = if (flagMailingSame) {
                                if (spinner_amphur.selectedItemId > 0) {
                                    spinner_amphur.selectedItem.toString() ?: ""
                                } else ""
                            } else {
                                if (spinner_mailing_amphur.selectedItemId > 0) {
                                    spinner_mailing_amphur.selectedItem.toString() ?: ""
                                } else ""
                            },
                            Mailing_pROVINCECODE = if (flagMailingSame) {
                                spinner_province.getSelectedPositionWithoutHint().toString()
                            } else {
                                spinner_mailing_province.getSelectedPositionWithoutHint().toString()
                            },
                            Mailing_pROVINCE = if (flagMailingSame) {
                                if (spinner_province.selectedItemId > 0) {
                                    spinner_province.selectedItem.toString() ?: ""
                                } else ""
                            } else {
                                if (spinner_mailing_province.selectedItemId > 0) {
                                    spinner_mailing_province.selectedItem.toString() ?: ""
                                } else ""
                            },
                            Mailing_pOSTCODE = if (flagMailingSame) {
                                edittext_postcode.text.toString() ?: ""
                            } else {
                                edittext_mailing_postcode.text.toString() ?: ""
                            },
                            Mailing_lat = if (flagMailingSame) {
                                tempDataType!!.Curr_lat
                            } else {
                                tempDataType!!.Mailing_lat
                            },
                            Mailing_lng = if (flagMailingSame) {
                                tempDataType!!.Curr_lng
                            } else {
                                tempDataType!!.Mailing_lat
                            }
                    )

            return personalDataList
        }catch (e: Exception){
            e.message
            return null
        }
    }

    override fun onDialogConfirmClick() {
    }

    override fun onDialogCancelClick() {
    }

    companion object {

        const val ADDRESS = "ADDRESS"
        const val REGIS = "REGIS"
        const val MAILING = "MAILING"

        const val REF_ID = "REF_ID"
        const val REF_URL = "REF_URL"

        fun OPEN(activity: Context?, data: String, url: String) {
            val intent = Intent(activity, VerifyandConfirmActivity::class.java)
            intent.putExtra(REF_ID, data)
            intent.putExtra(REF_URL, url)
            activity!!.startActivity(intent)
        }

        fun start(activity: Activity?, data: String, url: String) {
            val intent = Intent(activity, VerifyandConfirmActivity::class.java)
            intent.putExtra(REF_ID, data)
            intent.putExtra(REF_URL, url)
            activity!!.startActivity(intent)
        }

        fun OpenWithLocation(
                context: Context?,
                data: String, url: String) {
            val intent = Intent(context, VerifyandConfirmActivity::class.java)
            intent.putExtra(REF_ID, data)
            intent.putExtra(REF_URL, data)
            context?.startActivity(intent)
        }
    }
}
