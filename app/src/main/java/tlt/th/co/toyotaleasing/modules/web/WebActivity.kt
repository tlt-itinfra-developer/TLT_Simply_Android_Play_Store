package tlt.th.co.toyotaleasing.modules.web

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import im.delight.android.webview.AdvancedWebView
import kotlinx.android.synthetic.main.activity_web.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.center.CarLoanFragment
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.listener.OnHambergerClickListener
import tlt.th.co.toyotaleasing.manager.ContextManager
import tlt.th.co.toyotaleasing.manager.LocalizeManager
import tlt.th.co.toyotaleasing.manager.db.MasterDataManager
import tlt.th.co.toyotaleasing.util.AppUtils

class WebActivity : BaseActivity(), AdvancedWebView.Listener {

    private val title by lazy {
        intent.getStringExtra(TITLE_EXTRA) ?: ""
    }

    private val onHambergerClickListener by lazy {
        applicationContext as OnHambergerClickListener
    }

    private val url by lazy {
        intent.getStringExtra(URL_EXTRA) ?: ""
    }

    private val isShowShareMenu by lazy {
        intent.getBooleanExtra(SHARE_EXTRA, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        initInstances()
    }

    private fun initInstances() {
        toolbar.setTitle(title)
        webview.setListener(this, this)
        webview.webViewClient = getWebClient()
        toggleLoadingScreenDialog(isEnable = true)

        if (isShowShareMenu) {
            toolbar.showIconRightMenu(R.drawable.ic_share_white)
            webview.loadUrl(url)

        } else {
            toolbar.hideIconRightMenu()
            webview.loadUrl(url , getHeaders())

        }


        toolbar.setOnRightMenuIconClickListener {
            AppUtils.shareTextContent(
                    context = this@WebActivity,
                    text = url
            )
        }
    }

    private fun getHeaders(): HashMap<String, String> {
        val language =  if (LocalizeManager.isThai()) { "TH" } else { "EN" }
        val extraHeaders = HashMap<String, String>()
        extraHeaders["platform"] = "simply_webview"
        extraHeaders["gaga_language"] = language
        return extraHeaders
    }

    private fun getWebClient() = object : WebViewClient() {

        override fun onReceivedSslError(view: WebView?,
                                        handler: SslErrorHandler?,
                                        error: SslError?) {

            val message = error?.let { getSslErrorMessage(it) }
            AlertDialog.Builder(this@WebActivity)
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

//    override fun onBackPressedSupport() {
//        if (!webview.onBackPressed()) {
//            return
//        }
//        super.onBackPressedSupport()
//    }

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
        callDeep(url)

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

    private fun callDeep(url: String?): Boolean {

        if(url!!.contains("tlt://car_loan")) {
            val item = url.split("unique_id=").get(1)
            CarLoanFragment.startByInsightDeeplink(
                    context = this@WebActivity,
                    item = item
            )
        }
        return true
    }


    companion object {
        const val TITLE_EXTRA = "TITLE_EXTRA"
        const val URL_EXTRA = "URL_EXTRA"
        const val SHARE_EXTRA = "SHARE_EXTRA"

        fun start(context: Context?,
                  title: String,
                  url: String,
                  isShowShareMenu: Boolean = false) {
            val intent = Intent(context, WebActivity::class.java).apply {
                putExtra(TITLE_EXTRA, title)
                putExtra(URL_EXTRA, url)
                putExtra(SHARE_EXTRA, isShowShareMenu)
            }

            context?.startActivity(intent)
        }

        fun startWebCalculate(context: Context?) {
            start(
                    context = context,
                    title = context?.getString(R.string.sidebar_menu_cal_installment) ?: "",
                    url = "http://www.tlt.co.th/calculator/car_model",
                    isShowShareMenu = false
            )
        }


        fun startOnlineApp(context: Context? , url : String) {
            start(
                    context = context,
                    title = context?.getString(R.string.menu_main_car_loan) ?: "",
                    url = url , //"https://wu2.tlt.co.th/online-application/Car_selection",
                    isShowShareMenu = false

            )
        }


    }
}
