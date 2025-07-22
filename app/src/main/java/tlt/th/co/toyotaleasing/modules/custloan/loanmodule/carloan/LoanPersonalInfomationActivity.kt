package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.carloan

import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.fragment_loan_car_loan_personal_info.*
import kotlinx.android.synthetic.main.fragment_loan_car_loan_personal_info.btn_geo
import kotlinx.android.synthetic.main.fragment_loan_car_loan_personal_info.btn_next_confirm
import kotlinx.android.synthetic.main.fragment_loan_car_loan_personal_info.edittext_current_address
import kotlinx.android.synthetic.main.fragment_loan_car_loan_personal_info.edittext_postcode
import kotlinx.android.synthetic.main.fragment_loan_car_loan_personal_info.map_online_require_upper_sheet
import kotlinx.android.synthetic.main.fragment_loan_car_loan_personal_info.spinner_amphur
import kotlinx.android.synthetic.main.fragment_loan_car_loan_personal_info.spinner_province
import kotlinx.android.synthetic.main.loan_car_loan_upper_sheet.view.*
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
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.carloan.common.PersonalData
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.center.CarLoanFragment
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.geo.GeoLocationActivity
import java.util.*


class LoanPersonalInfomationActivity   : BaseActivity(), MsgdescNormalDialog.Listener {

    private var setProvinceByDefault: Boolean = false
    private var setAmphurByDefault: Boolean = false
    var dataMaritals : List<LoanPersonalInfomationViewModel.maritalItem> = listOf()
    var dataPropertys : List<LoanPersonalInfomationViewModel.propertyItem>  = listOf()
    var dataOccupations : List<LoanPersonalInfomationViewModel.occupationItem> = listOf()
    var datasubOccupations : List<LoanPersonalInfomationViewModel.suboccupationItem> = listOf()
    var personalData : List<PersonalData> = listOf()
    var masterHint : List<LoanPersonalInfomationViewModel.HintInfoModel> = listOf()

    private val data_extra by lazy {
        intent?.getStringExtra(REF_ID) ?: ""
    }

    private val data_url by lazy {
        intent?.getStringExtra(REF_URL) ?: ""
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(LoanPersonalInfomationViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_loan_car_loan_personal_info)
        AnalyticsManager.trackScreen(AnalyticsScreenName.ONLINE_PERSONAL)
        initViewModel()
        initInstance()

        viewModel.initProvideData(data_extra)
        viewModel.getMasterHint()

    }

    fun initInstance() {
        try{

            map_online_require_upper_sheet.stpPerInfo.background = ContextCompat.getDrawable(this, R.drawable.step_personal_active)
            map_online_require_upper_sheet.step2.setTextColor(ContextCompat.getColor(this@LoanPersonalInfomationActivity, R.color.cherry_red))
            map_online_require_upper_sheet.txt_ref_value.text = data_extra



            // Default
            h_basic_info.visible()
            h_basic_info.setDrawableEnd(R.drawable.expand_down)
            h_address_info.visible()
            h_address_info.setDrawableEnd(R.drawable.expand_down)
            h_income_info.visible()
            h_income_info.setDrawableEnd(R.drawable.expand_down)

            h_basic_info.setOnClickListener {
                if(  h_basic_info_card_view.isVisible() == true) {
                    h_basic_info.setDrawableEnd(R.drawable.expand_up)
                    h_basic_info_card_view.gone()
                }else {
                    h_basic_info.setDrawableEnd(R.drawable.expand_down)
                    h_basic_info_card_view.visible()
                }

            }
            h_address_info.setOnClickListener {
                if(  h_address_info_card_view.isVisible() == true) {
                    h_address_info.setDrawableEnd(R.drawable.expand_up)
                    h_address_info_card_view.gone()
                } else {
                    h_address_info.setDrawableEnd(R.drawable.expand_down)
                    h_address_info_card_view.visible()
                }
            }
            h_income_info.setOnClickListener {
                if(  h_income_info_card_view.isVisible() == true) {
                    h_income_info.setDrawableEnd(R.drawable.expand_up)
                    h_income_info_card_view.gone()
                }else {
                    h_income_info.setDrawableEnd(R.drawable.expand_down)
                    h_income_info_card_view.visible()
                }
            }


            btn_geo.setOnClickListener {
                nextGeo(isSaveInfo = false)
                AnalyticsManager.onlinePersonalMapClick()
                GeoLocationActivity.StartWithType(this@LoanPersonalInfomationActivity , data_extra , "personal" )
            }

            btn_next_confirm.setOnClickListener {
                if(formValidate()){
                    var list =  getPersonal()
                    list?.let {
                        viewModel.SyncPersonal( data_extra,list)
                    }
                }else{
                    NormalDialogFragment.show(
                            fragmentManager = supportFragmentManager,
                            description = getString(R.string.fillout_personal_data),
                            confirmButtonMessage = getString(R.string.dialog_button_ok)
                    )
                }
            }
            btn_back.setOnClickListener{
                try{
                    CarLoanFragment.startByInsight(
                            context = this@LoanPersonalInfomationActivity, authen = "Y"
                    )
                }catch ( e : Exception){
                    e.printStackTrace()
                }
            }


            spinner_province.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if (!setProvinceByDefault) {
                        viewModel.provinceChange(if (position > 0) position - 1 else 0  )
                        formValidate()
                    }
                    setProvinceByDefault = false
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

            spinner_amphur.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if (!setAmphurByDefault) {
                        viewModel.amphurChange(if (position > 0) position - 1 else 0  )
                        formValidate()
                    }
                    setAmphurByDefault = false
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

            notice_dialog_income_btn.setOnClickListener {
               var msg : String = ""
                masterHint.forEach{
                    if(it.type == "income")
                        msg =  it.description
                }

                if(msg== "") {
                    return@setOnClickListener
                }
                MsgdescNormalDialog.show(
                        fragmentManager = supportFragmentManager,
                        description = msg,
                        confirmButtonMessage = getString(R.string.dialog_button_close)
                )
            }

            notice_dialog_other_btn.setOnClickListener {
                var msg : String = ""
                masterHint.forEach{
                    if(it.type == "otherincome")
                        msg =  it.description
                }

                if(msg== "") {
                    return@setOnClickListener
                }
                MsgdescNormalDialog.show(
                        fragmentManager = supportFragmentManager,
                        description = msg,
                        confirmButtonMessage = getString(R.string.dialog_button_close)
                )
            }


            edittext_postcode.addTextChangedListener(onEditPostcodeChanged)
            edittext_current_address.addTextChangedListener(onEdittextTextWatcher)
            edittext_postcode.addTextChangedListener(onEdittextTextWatcher)
            text_income.addTextChangedListener(onIncomeTextChanged)
            text_other_income.addTextChangedListener(onOtherIncomeTextChanged)



            spinner_living_month.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        formValidate()
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

            spinner_living_year.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        formValidate()
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }


            spinner_employment_month.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        formValidate()
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

            spinner_living_year.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        formValidate()
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }


        }catch ( e : Exception) {
            e.printStackTrace()
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

    private val onIncomeTextChanged = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(message: CharSequence?, start: Int, before: Int, count: Int) {
            try {
                text_income.removeTextChangedListener(this)
                val value = text_income.getText().toString()

                if (value != "") {

                    if (value.startsWith(".")) {
                        text_income.setText("0.")
                    }
                    if (value.startsWith("0") && !value.startsWith("0.")) {
                        text_income.setText("")

                    }

                    val str = text_income.getText().toString().replace(",", "")
                    if (value != "")
                        text_income.setText(getDecimalFormattedString(str))
                    text_income.setSelection(text_income.getText().toString().length)
                }
                text_income.addTextChangedListener(this)
            } catch (ex: Exception) {
                ex.printStackTrace()
                text_income.addTextChangedListener(this)
            }

            formValidate()
        }
    }

    private val onOtherIncomeTextChanged = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(message: CharSequence?, start: Int, before: Int, count: Int) {
            try {
                text_other_income.removeTextChangedListener(this)
                val value = text_other_income.getText().toString()

                if (value != "") {

                    if (value.startsWith(".")) {
                        text_other_income.setText("0.")
                    }
                    if (value.startsWith("0") && !value.startsWith("0.")) {
                        text_other_income.setText("")

                    }

                    val str = text_other_income.getText().toString().replace(",", "")
                    if (value != "")
                        text_other_income.setText(getDecimalFormattedString(str))
                    text_other_income.setSelection(text_other_income.getText().toString().length)
                }
                text_other_income.addTextChangedListener(this)
            } catch (ex: Exception) {
                ex.printStackTrace()
                text_other_income.addTextChangedListener(this)
            }

            formValidate()
        }
    }


    private val onEditPostcodeChanged = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if( start == 4 && count == 1 ) {
                viewModel.ChangePostcodeAddressData(edittext_postcode.text.toString())

            }
        }
    }

    private fun initViewModel() {

        spinner_occupation.isEnabled  = false
        spinner_sub_occupation.isEnabled  = false

        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!)
        })

        viewModel.whenDataLoadedMessage.observe(this, Observer {
            it?.let {
                MsgdescNormalDialog.show(
                        fragmentManager = supportFragmentManager,
                        description = it ,
                        confirmButtonMessage = getString(R.string.dialog_button_ok)
                )
            }
        })

        viewModel.whenMariatalDataLoaded.observe(this, Observer {
            it?.let {
                dataMaritals = it.list
                spinner_marital.setItems(dataMaritals?.map { it.mARITAL } ?: listOf())
                spinner_marital.setSelection(it.id)
                formValidate()
            }
        })

        viewModel.whenPropertyDataLoaded.observe(this, Observer {
            it?.let {
                dataPropertys = it.list
                spinner_property_type.setItems(dataPropertys?.map { it.pROPERTY } ?: listOf())
                spinner_property_type.setSelection(it.id)
                formValidate()
            }
        })

        viewModel.whenSuboccupatoinDataLoaded.observe(this, Observer {
            it?.let{
//                datasubOccupations = it.list
//                spinner_sub_occupation.setItems(datasubOccupations?.map { it.sUBOCNAME } ?: listOf())
//                spinner_sub_occupation.setSelection(it.id)
//
                txt_sub_occupation.setText(it)
            }
        })

        viewModel.whenOccupationDataLoaded.observe(this, Observer {
            it?.let{
//                dataOccupations = it.list
//                spinner_occupation.setItems(dataOccupations?.map { it.oCNAME } ?: listOf())
//                spinner_occupation.setSelection(it.id)
                txt_occupation.setText(it)
            }
        })

        viewModel.whenLoadChangePostcode.observe(this, Observer {
            it.let{
                setProvinceByDefault = true
                setAmphurByDefault = true
                 initUpdateSpinnerView(it!!)
            }
        })

        viewModel.whenDataPersonalLoad.observe(this, Observer {
            it.let{
                personalData = listOf(it!!)
                initUpdateView(it!!)
            }
        })

        viewModel.whenSyncSuccessData.observe(this, Observer {
            it?.let{
                MenuStepController.open(this@LoanPersonalInfomationActivity, it.ref_id , it.step  , it.ref_url)
            }
        })

        viewModel.whenEmpLivDM.observe(this, Observer {
            it.let{
                if (it != null) {
                    getDataMonth(it.get(0).emp_month , it.get(0).liv_month)
                }
                if (it != null) {
                    getDataYear(it.get(0).emp_year , it.get(0).liv_year)
                }
                formValidate()
            }
        })

        viewModel.whenDataSpinnerLoaded.observe(this, Observer {
            it.let{
                initUpdateSpinnerView(it!!)
            }
        })

        viewModel.whenProvinceLoaded.observe(this, Observer {
            spinner_province.setItems(it!!)
        })

        viewModel.whenAmphurload.observe(this, Observer {
            spinner_amphur.setItems(it!!)
        })

        viewModel.whenPostcodeLoaded.observe(this, Observer {
            edittext_postcode.setText(it)
        })

        viewModel.whenDataLoadedMessage.observe(this, Observer {
            it?.let {
                MsgdescNormalDialog.show(
                        fragmentManager = supportFragmentManager,
                        description = it ,
                        confirmButtonMessage = getString(R.string.dialog_button_ok)
                )
            }
        })

        viewModel.whenDataMasterHintLoad.observe(this, Observer {
            it?.let {
                masterHint = it
            }
        })
    }

    fun getDataMonth(emp : String , liv : String){
        val month = (0..11).toList()
        val list = month.map { it.toString() }
//        spinner_employment_month.setItems( items.map { it.toString() })
//        spinner_living_month.setItems( items.map { it.toString() })
        val items = mutableListOf<String>()
        items.add(getString(R.string.txt_living_select_month))
        items.addAll(list)
        val spnliveAdapter = ArrayAdapter(this, R.layout.spinner_row, items)
        spinner_employment_month.setAdapter(spnliveAdapter)

        val spnempAdapter = ArrayAdapter(this, R.layout.spinner_row, items)
        spinner_living_month.setAdapter(spnempAdapter)


        if(emp != "")
            spinner_employment_month.setSelection( ( emp.toInt() +1))
        else
            spinner_employment_month.setSelection(0)
        if(liv != "")
            spinner_living_month.setSelection( ( liv.toInt() +1) )
        else
            spinner_living_month.setSelection(0)



    }

    fun getDataYear(emp : String , liv : String){
        val year = (0..60).toList()
        val list = year.map { it.toString() }
//        spinner_employment_year.setItems(items)
//        spinner_living_year.setItems(items)

        val items = mutableListOf<String>()
        items.add(getString(R.string.txt_living_select_year))
        items.addAll(list)
        val spnliveAdapter = ArrayAdapter(this, R.layout.spinner_row, items)
        spinner_employment_year.setAdapter(spnliveAdapter)

        val spnempAdapter = ArrayAdapter(this, R.layout.spinner_row, items)
        spinner_living_year.setAdapter(spnempAdapter)

        if(emp != "")
            spinner_employment_year.setSelection(( emp.toInt() +1))
        else
            spinner_employment_year.setSelection(0)
        if(liv != "")
            spinner_living_year.setSelection((liv.toInt() +1))
        else
            spinner_living_year.setSelection(0)


    }


    fun initUpdateView(it: PersonalData) {
        setProvinceByDefault = true
        setAmphurByDefault = true
        if (it != null) {
            text_cust_name.setText(it.name)
            text_id_card.setText(it.citizen)
            text_birthdate.setText(it.dateBirth)
            edittext_current_address.setText(it.real_address)
            edittext_postcode.setText(it.postcode)
            text_income.setText(it.income)
            text_other_income.setText(it.other_income)
            text_company_detail.setText(it.company_detail)
            formValidate()
        }
        if(it.postcode.isNotEmpty()){
            viewModel.ChangePostcodeAddressData(it.postcode)
        }

    }

    fun initUpdateSpinnerView(it: LoanPersonalInfomationViewModel.AddressIndexModel) {
        if (it != null) {
           spinner_province.setSelection(it.indx_pro + 1)
           spinner_amphur.setSelection(it.indx_aum + 1)
           formValidate()
        }
    }

    private fun nextGeo(isSaveInfo :  Boolean = false) {

        val items =   getPersonal()
        viewModel.saveList(items)

    }

    private fun formValidate() : Boolean{
        if (edittext_current_address.text.toString().trim().isEmpty()
                || spinner_amphur.isSelectHint()
                || spinner_province.isSelectHint()
                || edittext_postcode.text.toString().trim().isEmpty()
                || spinner_property_type.isSelectHint()
                || ( spinner_living_year.isSelectHint() && spinner_living_month.isSelectHint())
                || (spinner_employment_year.isSelectHint() && spinner_employment_month.isSelectHint() )
                || text_income.text.toString().trim().isEmpty()) {
            btn_next_confirm.isEnabled = false
            return false
        }
        btn_next_confirm.isEnabled = true
        return true
    }

    fun getPersonal() : List<PersonalData> {
        try{
            val personalDataList =listOf<PersonalData>(
                    PersonalData(
                        marital_status = if(spinner_marital.selectedItemId > 0) { spinner_marital.selectedItem.toString()?: ""  }else "",
                        marital_id = getMaterialID() ,
                        real_address = edittext_current_address.text.toString()?: "",
                        district =  "",
                        distric_code =  "",
                        aumphur = if(spinner_amphur.selectedItemId > 0  ) { spinner_amphur.selectedItem.toString()?: "" }  else "",
                        aumphur_code =   spinner_amphur.getSelectedPositionWithoutHint().toString(),
                        province =  if(spinner_province.selectedItemId > 0  ) { spinner_province.selectedItem.toString()?: "" }  else "",
                        province_code =  spinner_province.getSelectedPositionWithoutHint().toString(),
                        postcode =  edittext_postcode.text.toString()?: "",
                        prop_name = if(spinner_property_type.selectedItemId > 0  ) { spinner_property_type.selectedItem.toString()?: "" }  else "",
                        prop_id = getPropID() ,
                        liv_year = if(spinner_living_year.selectedItemId > 0) spinner_living_year.selectedItem.toString()?: "" else "0",
                        liv_month = if(spinner_living_month.selectedItemId > 0  ) { spinner_living_month.selectedItem.toString()?: "" }  else "0",
                        sub_oc = txt_sub_occupation.text.toString(),
                        sub_oc_id = "",
                        emp_year = if(spinner_employment_year.selectedItemId > 0  ) { spinner_employment_year.selectedItem.toString()?: "" }  else "0",
                        emp_month  = if(spinner_employment_month.selectedItemId > 0  ) { spinner_employment_month.selectedItem.toString()?: "" }  else "0",
                        income = text_income.text.toString()?: "",
                        other_income = text_other_income.text.toString()?: "" ,
                        lat = personalData.get(0).lat  ,
                        lng = personalData.get(0).lng ,
                        company_detail = text_company_detail.text.toString()?: ""
                    )
               )
            return personalDataList
//        }.toList()
        }catch (e : Exception){
            e.message
            return listOf()
        }
    }


    fun getMaterialID() : String {
        try {
                return ""
        }catch (e : Exception){
            return ""
        }

    }

    fun getPropID() : String {
        try {
                return ""
        }catch (e : Exception){
            return ""
        }
    }

    fun getSubID() : String {
        try {
                return ""
        }catch (e : Exception){
            return ""
        }
    }


    private fun getDecimalFormattedString(value: String): String {
        val lst = StringTokenizer(value, ".")
        var str1 = value
        var str2 = ""
        if (lst.countTokens() > 1) {
            str1 = lst.nextToken()
            str2 = lst.nextToken()
        }
        var str3 = ""
        var i = 0
        var j = -1 + str1.length
        if (str1[-1 + str1.length] == '.') {
            j--
            str3 = "."
        }
        var k = j
        while (true) {
            if (k < 0) {
                if (str2.length > 0)
                    str3 = "$str3.$str2"
                return str3
            }
            if (i == 3) {
                str3 = ",$str3"
                i = 0
            }
            str3 = str1[k] + str3
            i++
            k--
        }

    }
    override fun onDialogConfirmClick() {
    }

    override fun onDialogCancelClick() {
    }

    companion object {

        const val REF_ID = "REF_ID"
        const val REF_URL = "REF_URL"

        fun start(activity: Activity?, data: String  , url : String ) {
            val intent = Intent(activity, LoanPersonalInfomationActivity::class.java)
            intent.putExtra(REF_ID, data)
            intent.putExtra(REF_URL, data)
            activity!!.startActivity(intent)
        }


        fun OPEN(activity: Context?, data: String , url : String ) {
            val intent = Intent(activity, LoanPersonalInfomationActivity::class.java)
            intent.putExtra(REF_ID, data)
            intent.putExtra(REF_URL, url)
            activity!!.startActivity(intent)
        }

        fun OpenWithLocation(
                context: Context?,
                data: String
                , url : String ) {
            val intent = Intent(context, LoanPersonalInfomationActivity::class.java)
            intent.putExtra(REF_ID, data)
            intent.putExtra(REF_URL, data)
            context?.startActivity(intent)
        }

    }

}