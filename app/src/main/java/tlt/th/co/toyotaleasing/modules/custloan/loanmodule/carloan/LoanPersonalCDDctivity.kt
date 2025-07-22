package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.carloan

import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AlertDialog
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import im.delight.android.webview.AdvancedWebView
import kotlinx.android.synthetic.main.fragment_loan_car_loan_cdd.*
import kotlinx.android.synthetic.main.loan_car_loan_upper_sheet.view.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.dialog.NormalDialogFragment
import tlt.th.co.toyotaleasing.manager.ContextManager
import tlt.th.co.toyotaleasing.manager.LocalizeManager
import tlt.th.co.toyotaleasing.manager.db.UserManager
import tlt.th.co.toyotaleasing.modules.custloan.MenuStepController


class LoanPersonalCDDctivity   : BaseActivity()  , AdvancedWebView.Listener  , NormalDialogFragment.Listener{

    private val data_extra by lazy {
        intent?.getStringExtra(REF_ID) ?: ""
    }

    private val data_url by lazy {
        intent?.getStringExtra(REF_URL) ?: ""
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(LoanPersonalCDDViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_loan_car_loan_cdd)
        AnalyticsManager.trackScreen(AnalyticsScreenName.ONLINE_CDD)
        initViewModel()
        initInstances()
    }

    private fun initInstances() {
        map_online_require_upper_sheet.stpPerInfo.background = ContextCompat.getDrawable(this, R.drawable.step_personal_active)
        map_online_require_upper_sheet.step2.setTextColor(ContextCompat.getColor(this@LoanPersonalCDDctivity, R.color.cherry_red))
        map_online_require_upper_sheet.txt_ref_value.text = data_extra

        try {
            toggleLoadingScreenDialog(isEnable = true)
            webview.setListener(this, this)
            webview.webViewClient = getWebClient()
            viewModel.GetData(data_extra , data_url)
//            webview.loadUrl(data_url, getHeaders())

        } catch (e: Exception) {
            e.printStackTrace()
            toggleLoadingScreenDialog(isEnable = false)
        }
//        btn_next_confirm.setOnClickListener {
//            LoanTermsConNDIDActivity.start(this@LoanPersonalCDDctivity!!, data_extra , "")
//        }
    }

    private fun getHeaders(): HashMap<String, String> {
        val accessToken = UserManager.getInstance().getProfile().token
        val language =  if (LocalizeManager.isThai()) { "TH" } else { "EN" }
        val extraHeaders = HashMap<String, String>()
        extraHeaders["Authorization"] = "Basic $accessToken"
        extraHeaders["online_language"] = language
        extraHeaders["online_ref"] = data_extra
        return extraHeaders
    }

    private fun initViewModel() {

        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!)
        })

        viewModel.whenDataLoaded.observe(this, Observer { it ->
            toggleLoadingScreenDialog(isEnable = false)
            webview.loadDataWithBaseURL(data_url, it, "text/html", "UTF-8", "");
        })

    }

    private fun getWebClient() = object : WebViewClient() {

        override fun onReceivedSslError(view: WebView?,
                                        handler: SslErrorHandler?,
                                        error: SslError?) {

//            handler?.proceed()
            val message = error?.let { getSslErrorMessage(it) }
            AlertDialog.Builder(this@LoanPersonalCDDctivity)
                    .setCancelable(false)
                    .setMessage(message)
                    .setPositiveButton(ContextManager.getInstance().getApplicationContext().resources.getString(R.string.dialog_button_ok)) { dialog, id ->
                        handler?.proceed()
                       dialog!!.dismiss()
                    }
                    .setNegativeButton(ContextManager.getInstance().getApplicationContext().resources.getString(R.string.dialog_button_ok)) { dialog, id ->
                        handler?.cancel()
                       dialog!!.dismiss()
                    }
                    .show()
        }
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

    override fun onDestroy() {
        webview.onDestroy()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        webview.onActivityResult(requestCode, resultCode, intent)
    }

    override fun onPageFinished(url: String?) {

    }

    override fun onPageError(errorCode: Int, description: String?, failingUrl: String?) {

    }

    override fun onDownloadRequested(url: String?, suggestedFilename: String?, mimeType: String?, contentLength: Long, contentDisposition: String?, userAgent: String?) {

    }

    override fun onExternalPageRequest(url: String?) {

    }

    override fun onPageStarted(url: String?, favicon: Bitmap?) {
        toggleLoadingScreenDialog(isEnable = false)
        if (checkStageParam(url)) {
            setResult(Activity.RESULT_OK)
            CheckScreenPage(url)
            finish()
        }
    }

    fun CheckScreenPage(url : String?){
        if( url!!.contains("CICDDForm")) {
            if( url.split("CICDDForm?").get(1) != null &&  url.split("CICDDForm?").get(1) != ""){
                val param =  url.split("CICDDForm?").get(1)
                val refID = (param.split("&").get(0))
                val refRes = (param.split("&").get(1))
                val step = (param.split("&").get(2))
                val url = (param.split("&").get(3))
                if(refRes.split("=").get(1) == "202"){
                    val refValue = refID.split("=").get(1)
                    val stepValue = step.split("=").get(1)
                    val refURL = url.split("=").get(1)
                    MenuStepController.open(this@LoanPersonalCDDctivity, refValue , stepValue , refURL)
                }
            }
        }
    }

    override fun onDialogConfirmClick() {
    }

    override fun onDialogCancelClick() {
    }

    private fun checkStageParam(url: String?): Boolean {
        return url?.contains("ref_resp=202", true) == true
    }

    companion object {

        const val REF_ID = "REF_ID"
        const val REF_URL = "REF_URL"

        fun start(activity: Activity?, data: String , url : String ) {
            val intent = Intent(activity, LoanPersonalCDDctivity::class.java)
            intent.putExtra(REF_ID, data)
            intent.putExtra(REF_URL, url)
            activity!!.startActivity(intent)
        }

    }

}

