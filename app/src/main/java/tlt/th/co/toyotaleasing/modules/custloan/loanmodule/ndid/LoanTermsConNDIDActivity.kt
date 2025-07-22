package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.ndid

import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.util.Base64
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.dialog.MsgdescNormalDialog
import tlt.th.co.toyotaleasing.manager.db.UserManager
import tlt.th.co.toyotaleasing.modules.custloan.MenuStepController
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.center.CarLoanFragment
import kotlinx.android.synthetic.main.fragment_loan_car_ndid_termscon.btn_next_confirm
import kotlinx.android.synthetic.main.fragment_loan_car_ndid_termscon.map_online_require_upper_sheet
import kotlinx.android.synthetic.main.fragment_loan_car_ndid_termscon.radio_accept_policy
import kotlinx.android.synthetic.main.fragment_loan_car_ndid_termscon.radio_terms
import kotlinx.android.synthetic.main.fragment_loan_car_ndid_termscon.webview
import kotlinx.android.synthetic.main.loan_car_loan_upper_sheet.view.*
import kotlinx.android.synthetic.main.loan_require_upper_sheet.view.step3
import kotlinx.android.synthetic.main.loan_require_upper_sheet.view.txt_ref_value
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName

class LoanTermsConNDIDActivity   : BaseActivity() , MsgdescNormalDialog.Listener {
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(LoanTermsConNDIDViewModel::class.java)
    }

    private val data_extra by lazy {
        intent?.getStringExtra(REF_ID) ?: ""
    }

    private val data_url by lazy {
        intent?.getStringExtra(REF_URL) ?: ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_loan_car_ndid_termscon)
         AnalyticsManager.trackScreen(AnalyticsScreenName.ONLINE_NDID_TERMCON)
        initViewModel()
        initInstances()
        viewModel.GetData(data_extra , data_url)
    }

    private fun initInstances() {

        map_online_require_upper_sheet.stpEconsent.background = ContextCompat.getDrawable(this, R.drawable.step_econsent_active)
        map_online_require_upper_sheet.step3.setTextColor(ContextCompat.getColor(this@LoanTermsConNDIDActivity, R.color.cherry_red))
        map_online_require_upper_sheet.txt_ref_value.text = data_extra

        checkStateButton()

        try{

             toggleLoadingScreenDialog(isEnable = true)
            radio_terms.setOnCheckedChangeListener { group, checkedId ->
                checkStateButton()
            }

            btn_next_confirm.setOnClickListener {
                AnalyticsManager.onlineTermconAccept()
                val isAccept = radio_terms.checkedRadioButtonId == radio_accept_policy.id
                viewModel.sendTermCondition(isAccept , data_extra)
//                LoanEconsentActivity.start(this@LoanTermsConNDIDActivity!!, data_extra )
            }

        }catch (e : Exception){
            e.printStackTrace()
            toggleLoadingScreenDialog(isEnable = false)
        }

    }


    private fun initViewModel() {

        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!)
        })

        viewModel.whenSyncSuccessData.observe(this, Observer {
            it?.let {
                MenuStepController.open(this@LoanTermsConNDIDActivity, it.ref_id , it.step  , it.ref_url)
            }
        })


        viewModel.whenDataLoaded.observe(this, Observer { it ->
            toggleLoadingScreenDialog(isEnable = false)
            webview.apply {
                enableSupportZoom()
                //                loadData(it, "text/html; charset=utf-8", "UTF-8")
                val base64version = Base64.encodeToString(it!!.toByteArray(), Base64.DEFAULT)
                loadData(base64version, "text/html; charset=UTF-8", "base64")
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

        viewModel.whenDataLoadFail.observe(this, Observer {
            toggleLoadingScreenDialog(false)
        })

    }

    private fun getSslErrorMessage(error: SslError): String {
        when (error.primaryError) {
            SslError.SSL_DATE_INVALID -> return "The certificate date is invalid."
            SslError.SSL_EXPIRED -> return "The certificate has expired."
            SslError.SSL_IDMISMATCH -> return "The certificate hostname mismatch."
            SslError.SSL_INVALID -> return "The certificate is invalid."
            SslError.SSL_NOTYETVALID -> return "The certificate is not yet valid"
            SslError.SSL_UNTRUSTED -> return "The certificate is untrusted."
            else -> return "SSL Certificate error."
        }
    }


    override fun onPause() {
        webview.onPause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        webview.onResume()
    }

    private fun checkStateButton() {
        btn_next_confirm.isEnabled = radio_terms.checkedRadioButtonId != -1
    }

    private fun getHeaders(): HashMap<String, String> {
        val accessToken = UserManager.getInstance().getProfile().token
        val extraHeaders = HashMap<String, String>()
        extraHeaders["Authorization"] = "Bearer $accessToken"
        return extraHeaders
    }

    override fun onDialogConfirmClick() {
    }

    override fun onDialogCancelClick() {
    }

    companion object {
        const val REF_ID = "REF_ID"
        const val REF_URL = "REF_URL"

        fun start(activity: Activity?, data: String , url: String) {
            val intent = Intent(activity, LoanTermsConNDIDActivity::class.java)
            intent.putExtra(REF_ID, data)
            intent.putExtra(REF_URL, url)
            activity!!.startActivity(intent)
        }

        fun OpenWithLocation(
                context: Context?,
                DesLocation: String,
                desLat : String,
                desLong : String,
                mainID : String) {
            val intent = Intent(context, LoanTermsConNDIDActivity::class.java)
            intent.putExtra("NEW_ADDRESS", DesLocation)
            intent.putExtra("NEW_LAT", desLat)
            intent.putExtra("NEW_LNG", desLong)
            intent.putExtra("MAIN_ID", mainID)
            context?.startActivity(intent)
        }
    }

}

