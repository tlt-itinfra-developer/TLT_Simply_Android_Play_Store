package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.cstatus


import android.Manifest
import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.view.View
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import kotlinx.android.synthetic.main.activity_loan_check_status.*
import kotlinx.android.synthetic.main.loan_check_status_upper_sheet.view.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.dialog.MsgdescNormalDialog
import tlt.th.co.toyotaleasing.common.extension.gone
import tlt.th.co.toyotaleasing.common.extension.isVisible
import tlt.th.co.toyotaleasing.common.extension.setDrawableEnd
import tlt.th.co.toyotaleasing.common.extension.visible
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.center.CarLoanFragment
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.cstatus.extend.ScannerQRcodeActivity
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.geo.location.LoanLocationActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.cstatus.common.OtherExpense

class SummaryLoanActivity  : BaseActivity() , MsgdescNormalDialog.Listener{

    var scannedResult: String = ""
    var FlagScanBtn : String = "Y"
    var expenseData : SummaryLoanViewModel.ExpensesDataModel? = null


    private val data_extra by lazy {
        intent?.getStringExtra(REF_ID) ?: ""
    }

    private val data_url by lazy {
        intent?.getStringExtra(REF_URL) ?: ""
    }

    private val data_shwcode by lazy {
        intent?.getStringExtra(DATA_SHWCODE) ?: ""
    }

    private val data_shwname by lazy {
        intent?.getStringExtra(DATA_SHWNAME) ?: ""
    }


    private val viewModel by lazy {
        ViewModelProviders.of(this).get(SummaryLoanViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loan_check_status)
        AnalyticsManager.trackScreen(AnalyticsScreenName.ONLINE_SUMMMARY)

        initViewModel()
        initInstance()
        viewModel.getDataVerify(data_extra)

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

        viewModel.whenLoadPDFData.observe(this, Observer {
            it?.let {
//                MenuStepController.open(this@SummaryLoanActivity, it.ref_id , it.step  , it.ref_url)
                userGrantPermissionPDF(this@SummaryLoanActivity){

                    PreviewEContractActivity.open(this@SummaryLoanActivity ,  data_extra )
                }

            }
        })


        viewModel.whenDataDealerChangeLoaded.observe(this, Observer {
            it?.let {
                toggleLoadingScreenDialog(false)
                AnalyticsManager.onlineSummaryDealerAdd()
                updateDealer(it)
            }
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
    }

    fun initInstance(){

        map_online_require_upper_sheet.stepExpense.background = ContextCompat.getDrawable(this, R.drawable.step_summary_active)
        map_online_require_upper_sheet.stp2.setTextColor(ContextCompat.getColor(this@SummaryLoanActivity, R.color.cherry_red))
        map_online_require_upper_sheet.txt_ref_value.text = data_extra

        userGrantPermission(this@SummaryLoanActivity) {

            btnScan.setOnClickListener {
                try {
                    if(FlagScanBtn == "Y") {
                        AnalyticsManager.onlineSummaryScanQR()
                        run {
                            IntentIntegrator(this@SummaryLoanActivity)
                                    .setCaptureActivity(ScannerQRcodeActivity::class.java)
                                    .setPrompt("Scan a QR code")
                                    .setOrientationLocked(false)
                                    .setTimeout(20000)
                                    .initiateScan()
                        }
                    }else{
                        MsgdescNormalDialog.show(
                                fragmentManager = supportFragmentManager,
                                description = getString(R.string.dialog_please_wait_process) ,
                                confirmButtonMessage = getString(R.string.dialog_button_ok)
                        )
                    }
                }catch (e : Exception){
                    e.message
                }
            }
        }

        // Default
        h_mycar_info_card_view.visible()
        h_mycar_info.setDrawableEnd(R.drawable.expand_down)
        h_delivery_dealer_card_view.visible()
        h_delivery_car.setDrawableEnd(R.drawable.expand_down)
        h_address_card_view.visible()
        h_address.setDrawableEnd(R.drawable.expand_down)

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

        h_sign_e_contract.setOnClickListener {
            if(  h_sign_e_card_view.isVisible() == true) {
                h_sign_e_contract.setDrawableEnd(R.drawable.expand_up)
                h_sign_e_card_view.gone()
            }else {
                h_sign_e_contract.setDrawableEnd(R.drawable.expand_down)
                h_sign_e_card_view.visible()
            }
        }

        /*   Address Tab  */

        // Default
        btn_current.setBackground(ContextCompat.getDrawable(this@SummaryLoanActivity, R.drawable.selector_btn_tab))
        btn_current.setTextColor(ContextCompat.getColor(this@SummaryLoanActivity, R.color.white))
        h_address_current_address_card.visible()
        h_address_regis_address_card.gone()
        h_address_mailing_address_card.gone()

        btn_current.setOnClickListener {
            btn_current.setBackground(ContextCompat.getDrawable(this@SummaryLoanActivity, R.drawable.selector_btn_tab))
            btn_regis.setBackground(ContextCompat.getDrawable(this@SummaryLoanActivity, R.color.grey_4))
            btn_mailing.setBackground(ContextCompat.getDrawable(this@SummaryLoanActivity, R.color.grey_4))
            btn_current.setTextColor(ContextCompat.getColor(this@SummaryLoanActivity, R.color.white))
            btn_regis.setTextColor(ContextCompat.getColor(this@SummaryLoanActivity, R.color.black))
            btn_mailing.setTextColor(ContextCompat.getColor(this@SummaryLoanActivity, R.color.black))
            h_address_current_address_card.visible()
            h_address_regis_address_card.gone()
            h_address_mailing_address_card.gone()

//            if(spinner_province.adapter.count  < 2 ){
//                viewModel.getDataAfterChangeTab(tempDataType!! , ADDRESS)
//            }
        }

        btn_regis.setOnClickListener {
            btn_current.setBackground(ContextCompat.getDrawable(this@SummaryLoanActivity, R.color.grey_4))
            btn_regis.setBackground(ContextCompat.getDrawable(this@SummaryLoanActivity, R.drawable.selector_btn_tab))
            btn_mailing.setBackground(ContextCompat.getDrawable(this@SummaryLoanActivity, R.color.grey_4))
            btn_current.setTextColor(ContextCompat.getColor(this@SummaryLoanActivity, R.color.black))
            btn_regis.setTextColor(ContextCompat.getColor(this@SummaryLoanActivity, R.color.white))
            btn_mailing.setTextColor(ContextCompat.getColor(this@SummaryLoanActivity, R.color.black))
            h_address_current_address_card.gone()
            h_address_regis_address_card.visible()
            h_address_mailing_address_card.gone()

        }

        btn_mailing.setOnClickListener {
            btn_current.setBackground(ContextCompat.getDrawable(this@SummaryLoanActivity, R.color.grey_4))
            btn_regis.setBackground(ContextCompat.getDrawable(this@SummaryLoanActivity, R.color.grey_4))
            btn_mailing.setBackground(ContextCompat.getDrawable(this@SummaryLoanActivity, R.drawable.selector_btn_tab))
            btn_current.setTextColor(ContextCompat.getColor(this@SummaryLoanActivity, R.color.black))
            btn_regis.setTextColor(ContextCompat.getColor(this@SummaryLoanActivity, R.color.black))
            btn_mailing.setTextColor(ContextCompat.getColor(this@SummaryLoanActivity, R.color.white))
            h_address_current_address_card.gone()
            h_address_regis_address_card.gone()
            h_address_mailing_address_card.visible()

        }



        btn_showroom.setOnClickListener {
            LoanLocationActivity.openForChange(this@SummaryLoanActivity , data_extra , "CHANGE" )
        }

        btn_change_showroom.setOnClickListener {
            viewModel.SyncExpenseDealerRequest(data_extra ,data_shwcode , data_shwname )
        }

        h_other_expense.setOnClickListener {
            AnalyticsManager.onlineSummaryOtherExpense()
            if(expenseData!!.OTHERDETAILLIST.size > 0 ) {
                OtherExpenseLoanActivity.start(this@SummaryLoanActivity!!, data_extra, "")
            }
        }

    }

    fun initUpdateView(it: SummaryLoanViewModel.ExpensesDataModel?) {
        if (it != null)
        {
             expenseData = it
             txt_loan_name.text = it.pROPOSALNAME
             txt_loan_no.text = it.pROPOSALNO
             txt_status.text = it.sTATUSAPP
             txt_appointment.text = it.aPPOINTMENT
             txt_duedate.text = it.dUEDATE
             edittext_showroom.setText( if(data_shwname!=""){ data_shwname } else  { it.sHOWROOMNAME  } )
             txt_car_price.text = it.cARPRICE
             txt_down_pay.text = it.dOWNPAYMENT
             txt_contract_hand_fee.text =  it.contractHandlingFee
             txt_grade.text = it.gRADE
             txt_monthly.text = it.iNSTALLMENT
             txt_interest.text = it.iNTERESTS
             txt_model.text = it.mODEL
             txt_payment_term.text = it.pAYMENTTERM
             text_current_address.text = it.Addr_rEALADDRESS
             text_mailing_address.text = it.Mailing_rEALADDRESS
             text_regis_address.text = it.regis_rEALADDRESS
             btn_change_showroom.setText(it.cHANGESTATUSBTN)
             h_change_status.setText(it.cHANGEDESC)
             txt_other_expense.text = it.otherExpense
             txt_other_expense.visibility = View.GONE
             h_other_expense.visibility = View.GONE
             des_other_expense.visibility = View.GONE
            if(it.cANCHANGE.equals("N")){
                btn_change_showroom.visibility = View.GONE
                h_change_status.visibility = View.GONE
                btn_showroom.visibility = View.GONE
                edittext_showroom.isEnabled = false
                txt_other_expense.visibility = View.VISIBLE
                h_other_expense.visibility = View.VISIBLE
//                des_other_expense.visibility = View.VISIBLE
            }else{
                btn_change_showroom.visibility = View.VISIBLE
                h_change_status.visibility = View.VISIBLE
                btn_showroom.visibility = View.VISIBLE
                edittext_showroom.isEnabled = true
            }

            if (h_change_status.length()== 0 ){
                h_change_status.visibility = View.GONE
            }
            if(it.OTHERDETAILLIST.size > 0 ) {
                otherDataList = it.OTHERDETAILLIST
                h_other_expense.isEnabled = true
            }else{
                h_other_expense.isEnabled = false
            }

        }
    }

    fun updateDealer(it: SummaryLoanViewModel.DealerChangeModel){

        if (it != null)
        {
            btn_change_showroom.setText(it.cHANGESTATUSBTN)
            h_change_status.setText(it.cHANGEDESC)

            FlagScanBtn =  it.cANCHANGE
            if(it.cANCHANGE.equals("N")) {
                btn_change_showroom.visibility = View.GONE
                h_change_status.visibility = View.GONE
                btn_showroom.visibility = View.GONE
                edittext_showroom.isEnabled = false
            }else  if(it.cANCHANGE.equals("W")){
                btn_change_showroom.visibility = View.VISIBLE
                h_change_status.visibility = View.VISIBLE
                btn_showroom.visibility = View.VISIBLE
                edittext_showroom.isEnabled = true
                btn_change_showroom.setBackground(ContextCompat.getDrawable(this@SummaryLoanActivity, R.drawable.btn_white))
                btn_change_showroom.setTextColor(ContextCompat.getColor(this@SummaryLoanActivity, R.color.black))
            }else{
                btn_change_showroom.visibility = View.VISIBLE
                h_change_status.visibility = View.VISIBLE
                btn_showroom.visibility = View.VISIBLE
                edittext_showroom.isEnabled = true
                btn_change_showroom.setBackground(ContextCompat.getDrawable(this@SummaryLoanActivity, R.drawable.selector_btn_confirm_edit_mailing_address))
                btn_change_showroom.setTextColor(ContextCompat.getColor(this@SummaryLoanActivity, R.color.white))
            }
        }
    }

//     For Scanner
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

    var result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if(result != null){

            if(result.contents != null){
                scannedResult = result.contents
                viewModel.SyncExpense(data_extra , scannedResult )
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun userGrantPermission(activity: Activity?, callback: () -> Unit) {
        Dexter.withActivity(activity)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.isAnyPermissionPermanentlyDenied) {
                            return
                        }

                        callback.invoke()
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                        token.continuePermissionRequest()
                    }
                }).check()
    }

    private fun userGrantPermissionPDF(activity: Activity?, callback: () -> Unit) {
        Dexter.withActivity(activity)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.isAnyPermissionPermanentlyDenied) {
                            return
                        }

                        callback.invoke()
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                        token.continuePermissionRequest()
                    }
                }).check()
    }


    override fun onSaveInstanceState(outState: Bundle) {

        outState?.putString("scannedResult", scannedResult)
        super.onSaveInstanceState(outState)
    }

//    End Scanner

    override fun onDialogConfirmClick() {
    }

    override fun onDialogCancelClick() {
    }

    companion object {
        var otherDataList  : List<OtherExpense>? = null
        const val REF_ID = "REF_ID"
        const val REF_URL = "REF_URL"
        const val DATA_SHWCODE = "DATA_SHWCODE"
        const val DATA_SHWNAME = "DATA_SHWNAME"


        fun start(activity: Activity?, data: String , url : String ) {
            val intent = Intent(activity, SummaryLoanActivity::class.java)
            intent.putExtra(REF_ID, data)
            intent.putExtra(REF_URL, url)
            activity!!.startActivity(intent)
        }

        fun openWithData(activity: Activity?, data: String , shw_code : String  , shw_name : String ) {
            val intent = Intent(activity, SummaryLoanActivity::class.java)
            intent.putExtra(REF_ID, data)
            intent.putExtra(DATA_SHWCODE, shw_code)
            intent.putExtra(DATA_SHWNAME, shw_name)
            activity!!.startActivity(intent)
        }

    }
}
