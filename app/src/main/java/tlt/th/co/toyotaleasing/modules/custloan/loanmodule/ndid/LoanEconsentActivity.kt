package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.ndid

import android.Manifest
import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.util.Base64
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.fragment_loan_car_loan_e_consent.*
import kotlinx.android.synthetic.main.loan_car_loan_upper_sheet.view.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.dialog.MsgdescNormalDialog
import tlt.th.co.toyotaleasing.common.extension.captureScreenToBitmap
import tlt.th.co.toyotaleasing.common.extension.captureToExternalStorage
import tlt.th.co.toyotaleasing.manager.db.UserManager
import tlt.th.co.toyotaleasing.modules.custloan.MenuStepController
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.carloan.CarLoanDetailOTPActivity
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.center.CarLoanFragment


class LoanEconsentActivity : BaseActivity(), MsgdescNormalDialog.Listener {

    private lateinit var imageUri: Uri
    private lateinit var imageBitmap: Bitmap

    private val data_extra by lazy {
        intent?.getStringExtra(REF_ID) ?: ""
    }

    private val data_url by lazy {
        intent?.getStringExtra(REF_URL) ?: ""
    }

    private val signature by lazy {
        intent?.getStringExtra(SIGN) ?: ""
    }


    private val viewModel by lazy {
        ViewModelProviders.of(this).get(LoanEconsentViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_loan_car_loan_e_consent)
         AnalyticsManager.trackScreen(AnalyticsScreenName.ONLINE_E_CONSENT)
        initInstances()
        initViewModel()
        viewModel.GetData(data_extra , data_url)
    }


    private fun initInstances() {

        map_online_require_upper_sheet.stpEconsent.background = ContextCompat.getDrawable(this, R.drawable.step_econsent_active)
        map_online_require_upper_sheet.step3.setTextColor(ContextCompat.getColor(this@LoanEconsentActivity, R.color.cherry_red))
        map_online_require_upper_sheet.txt_ref_value.text = data_extra

        checkStateButton()
        toggleLoadingScreenDialog(isEnable = false)

        try{

            radio_terms.setOnCheckedChangeListener { group, checkedId ->
                checkStateButton()
            }

            btn_next_confirm.setOnClickListener {
//                AnalyticsManager.termAndConditionNextClicked()

                viewModel.sendEConsentCondition(true , data_extra)
//                LoanWaitNDIDActivity.start(this@LoanEconsentActivity!!, data_extra)
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
                MenuStepController.open(this@LoanEconsentActivity, it.ref_id , it.step  , it.ref_url)
//                LoanWaitNDIDActivity.start(this@LoanEconsentActivity!!, it.ref_id , it.ref_url)
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

        viewModel.whenDataLoaded.observe(this, Observer { it ->
            toggleLoadingScreenDialog(isEnable = false)
            webview.apply {
                enableSupportZoom()
//                loadData(it, "text/html; charset=utf-8", "UTF-8")
                val base64version = Base64.encodeToString(it!!.toByteArray(), Base64.DEFAULT)
                loadData(base64version, "text/html; charset=UTF-8", "base64")
            }

        })

        viewModel.whenDataLoadFail.observe(this, Observer {
            toggleLoadingScreenDialog(false)
        })


    }

    private fun checkStateButton() {
        btn_next_confirm.isEnabled = radio_terms.checkedRadioButtonId != -1
    }

    private fun captureScreenShot() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {
                        imageUri = layout_consent.captureToExternalStorage(applicationContext)
                        imageBitmap = layout_consent.captureScreenToBitmap(applicationContext)

                        CarLoanDetailOTPActivity.show(this@LoanEconsentActivity!!, data_extra)
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse) {
                        imageBitmap = layout_consent.captureScreenToBitmap(applicationContext)
                        CarLoanDetailOTPActivity.show(this@LoanEconsentActivity!!, data_extra)
                    }

                    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) {
                        token.continuePermissionRequest()
                    }
                }).check()
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

        const val SIGN = "SIGN"
        const val REF_ID = "REF_ID"
        const val REF_URL = "REF_URL"

        fun start(activity: Activity?, data: String , url : String ) {
            val intent = Intent(activity, LoanEconsentActivity::class.java)
            intent.putExtra(REF_ID, data)
            intent.putExtra(REF_URL, url)
            activity!!.startActivity(intent)
        }

        fun show(activity: Activity?,  data: String , signature: String) {
            val intent = Intent(activity, LoanEconsentActivity::class.java)
            intent.putExtra(REF_ID, data)
            intent.putExtra(SIGN, signature)
            activity!!.startActivity(intent)
        }
    }

}