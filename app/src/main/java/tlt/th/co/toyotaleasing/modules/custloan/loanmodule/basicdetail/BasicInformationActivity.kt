package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.basicdetail


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
import kotlinx.android.synthetic.main.fragment_loan_require_fill_information.*
import kotlinx.android.synthetic.main.fragment_loan_require_fill_information.btn_next_confirm
import kotlinx.android.synthetic.main.fragment_loan_require_fill_information.map_online_require_upper_sheet
import kotlinx.android.synthetic.main.loan_require_upper_sheet.view.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.dialog.CustomHintDialog
import tlt.th.co.toyotaleasing.common.dialog.MsgdescNormalDialog
import tlt.th.co.toyotaleasing.common.dialog.NormalDialogFragment
import tlt.th.co.toyotaleasing.model.entity.FillInfoDataEntity
import tlt.th.co.toyotaleasing.modules.custloan.MenuStepController
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.basicdetail.common.DopaStatusDialog
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.basicdetail.common.FillInfoData
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.center.CarLoanFragment
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.geo.GeoLocationActivity

class BasicInformationActivity   : BaseActivity() , DopaStatusDialog.Listener  {

    private var setProvinceByDefault: Boolean = false
    private var setAmphurByDefault: Boolean = false
    var fillinfoData : List<FillInfoData> = listOf()
    private var dataDopaStatus = BasicInformationViewModel.StatusSyncInfoModel()

    private val data_extra by lazy {
        intent?.getStringExtra(REF_ID) ?: ""
    }

    private val data_url by lazy {
        intent?.getStringExtra(REF_URL) ?: ""
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(BasicInformationViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_loan_require_fill_information)
        AnalyticsManager.trackScreen(AnalyticsScreenName.ONLINE_INFORMATION)
        initInstance()
        initViewModel()
        viewModel.initProvideData(data_extra)

    }

    fun initInstance() {

        map_online_require_upper_sheet.stepInformation.background = ContextCompat.getDrawable(this, R.drawable.step_fillinfo_active)
        map_online_require_upper_sheet.step3.setTextColor(ContextCompat.getColor(this@BasicInformationActivity, R.color.cherry_red))
        map_online_require_upper_sheet.txt_ref_value.text = data_extra



        btn_geo.setOnClickListener {
            nextGeo(isSaveInfo = false)
            GeoLocationActivity.StartWithType(this@BasicInformationActivity , data_extra , "basic" )
        }

        btn_next_confirm.setOnClickListener {
            if(formValidate()){
                var list =  getFillInfo()
                list?.let {
                    viewModel.SyncInfo(refNo = data_extra , data = list )
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
                        context = this@BasicInformationActivity, authen = "Y"
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


        notice_dialog_laser_btn.setOnClickListener {
            val msg = getString(R.string.hint_laser_id)
            CustomHintDialog.show(
                    fragmentManager = supportFragmentManager,
                    description = "IDBACK",
                    cancelButtonMessage = getString(R.string.dialog_button_close)
            )
          }


        edittext_laser_code1.addTextChangedListener(onEditLaserCodeChanged)
        edittext_postcode.addTextChangedListener(onEditPostcodeChanged)
//        edittext_household_id.addTextChangedListener(onEdittextTextWatcher)
        edittext_current_address.addTextChangedListener(onEdittextTextWatcher)

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
            formValidate()
        }
    }

    private val onEditLaserCodeChanged = object : TextWatcher {
        var isEdiging: Boolean = false
        override fun afterTextChanged(s: Editable?) {
            if (isEdiging) return
            isEdiging = true
            // removing old dashes
            val sb = StringBuilder()
            sb.append(s.toString().replace("-", ""))

            if (sb.length > 3)
                sb.insert(3, "-")
            if (sb.length > 11)
                sb.insert(11, "-")
            if (sb.length > 14)
                sb.delete(14, sb.length)

            s!!.replace(0, s!!.length, sb.toString())
            isEdiging = false
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
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

    fun initViewModel(){

        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!)
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

        viewModel.whenDataFillInfoLoad.observe(this, Observer {
            it.let{
                fillinfoData = listOf(it!!)
                initUpdateView(it!!)
                if(it.pOSTCODE.isNotEmpty()){
                    viewModel.ChangePostcodeAddressData(it.pOSTCODE)
                }
            }
        })

        viewModel.whenSyncSuccessData.observe(this, Observer {
            it?.let{
                MenuStepController.open(this@BasicInformationActivity, it.ref_id , it.step  , it.ref_url)
            }
        })

        viewModel.whenLoadChangePostcode.observe(this, Observer {
            it.let{
                setProvinceByDefault = true
                setAmphurByDefault = true
                initUpdateSpinnerView(it!!)
            }
        })

        viewModel.whenDataDopaLoadedSuccess.observe(this, Observer {
            it.let{
                dataDopaStatus = it!!
                DopaStatusDialog.show(
                            fragmentManager = supportFragmentManager,
                            description = it.dopaDesc ,
                            confirmButtonMessage = getString(R.string.dialog_button_ok)
                )

            }
        })
    }


    fun initUpdateView(it: FillInfoData) {
        setProvinceByDefault = true
        setAmphurByDefault = true
        if (it != null) {
            edittext_id_card.setText(it.cITIZENID)
            edittext_current_address.setText(it.rEALADDRESS)
            edittext_postcode.setText(it.pOSTCODE)
            edittext_name.setText(it.nAME)
            edittext_surname.setText(it.sURNAME)
            edittext_birthdate.setText(it.dOB)
            edittext_household_id.setText(it.hOUSEHOLDID)
            edittext_laser_code1.setText(it.lASERID)
            if(it.sEX == "M")
                radio_male.isChecked = true
            else
                radio_female.isChecked = true
            formValidate()
        }
    }

    fun initUpdateSpinnerView(it: BasicInformationViewModel.AddressIndexModel) {
        if (it != null) {
            spinner_province.setSelection(it.indx_pro + 1)
            spinner_amphur.setSelection(it.indx_aum + 1)
            formValidate()
        }
    }

    private fun nextGeo(isSaveInfo :  Boolean = false) {
        val items   =    getFillInfo()
        if (items!=null){
            viewModel.saveList(items)
        }
    }

    fun getFillInfo() : List<FillInfoDataEntity> {
        try{
            val fillinfoDataList =listOf(
                    FillInfoDataEntity(
                         name =  edittext_name.text.toString()  ,
                         surname = edittext_surname.text.toString()  ,
                         aMPHUR = if(spinner_amphur.selectedItemId > 0  ) { spinner_amphur.selectedItem.toString()?: "" }  else "",
                         aMPHURCODE =  spinner_amphur.getSelectedPositionWithoutHint().toString(),
                         lASERID = edittext_laser_code1.text.toString()  ,
                         hOUSEHOLDID =  edittext_household_id.text.toString() ,
                         pOSTCODE = edittext_postcode.text.toString()?: "",
                         pROVINCE  = if(spinner_province.selectedItemId > 0  ) { spinner_province.selectedItem.toString()?: "" }  else "",
                         pROVINCECODE = spinner_province.getSelectedPositionWithoutHint().toString(),
                         rEALADDRESS = edittext_current_address.text.toString()?: "",
                         lat = fillinfoData.get(0).lat  ,
                         lng  = fillinfoData.get(0).lng
                    )
            )
            return fillinfoDataList
//        }.toList()
        }catch (e : Exception){
            e.message
            return listOf()
        }
    }

    private fun formValidate(): Boolean {
         if(edittext_current_address.text.toString().trim().isEmpty()
                    || edittext_name.text.toString().trim().isEmpty()
                    || edittext_surname.text.toString().trim().isEmpty()
                    || spinner_amphur.isSelectHint()
                    || spinner_province.isSelectHint()
                    || edittext_postcode.text.toString().trim().isEmpty()
                    || edittext_laser_code1.text.toString().trim().isEmpty()) {
             btn_next_confirm.isEnabled = false
             return false
         }else {
             btn_next_confirm.isEnabled = true
             return true
         }
    }

    override fun onDialogConfirmClick() {
       if(dataDopaStatus!=null){
//           if((dataDopaStatus.status == "N" && dataDopaStatus.count == "3") || dataDopaStatus.status == "Y" ){
//               viewModel.CheckStepAPI(data_extra)
//           }
           if(dataDopaStatus.step != "DOPAINFO") {
               AnalyticsManager.onlineDopaSuccessAdd()
               MenuStepController.open(this@BasicInformationActivity, data_extra, dataDopaStatus.step, "")
           }else{
               AnalyticsManager.onlineDopaUnsuccessAdd()
           }
       }
        dataDopaStatus = BasicInformationViewModel.StatusSyncInfoModel()
    }

    override fun onDialogCancelClick() {

    }

    companion object {

        const val REF_ID = "REF_ID"
        const val REF_URL = "REF_URL"

        fun start(activity: Activity?, data: String , url : String) {
            val intent = Intent(activity, BasicInformationActivity::class.java)
            intent.putExtra(REF_ID, data)
            intent.putExtra(REF_URL, url)
            activity!!.startActivity(intent)
        }


        fun OpenWithLocation(
                context: Context?,
                data: String
                , url : String ) {
            val intent = Intent(context, BasicInformationActivity::class.java)
            intent.putExtra(REF_ID, data)
            intent.putExtra(REF_URL, data)
            context?.startActivity(intent)
        }
    }
}


