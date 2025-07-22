package tlt.th.co.toyotaleasing.modules.ecommerce

import android.content.Context
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.common.base.BaseFragment
import tlt.th.co.toyotaleasing.common.listener.OnHambergerClickListener
import tlt.th.co.toyotaleasing.manager.BusManager
import tlt.th.co.toyotaleasing.manager.ContextManager
import tlt.th.co.toyotaleasing.manager.db.MasterDataManager
import android.webkit.*
import im.delight.android.webview.AdvancedWebView
import kotlinx.android.synthetic.main.activity_ecommerce.*
import kotlinx.android.synthetic.main.activity_ecommerce.toolbar
import kotlinx.android.synthetic.main.activity_ecommerce.webview
import kotlinx.android.synthetic.main.activity_web.*
import kotlinx.android.synthetic.main.fragment_news_and_promotion.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.eventbus.ReloadEcommerceEvent
import tlt.th.co.toyotaleasing.manager.LocalizeManager
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.center.CarLoanFragment
import tlt.th.co.toyotaleasing.modules.sidebarmenu.SidebarMenu
import tlt.th.co.toyotaleasing.modules.web.WebActivity
import tlt.th.co.toyotaleasing.util.AppUtils


class ECommerceActiviy : BaseActivity(), AdvancedWebView.Listener {

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

    var URL_LINK  = ""
    var TITLE : String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ecommerce)

        initInstances()
    }

    private fun initInstances() {

        val master = MasterDataManager.getInstance().getSideMenu()

        webview.setListener(this, this)
        webview.webViewClient = getWebClient()
        toggleLoadingScreenDialog(isEnable = true)
        toolbar.showIconRightMenu(R.drawable.ic_share_white)
//        toolbar.hideIconRightMenu()
        webview.loadUrl(URL_LINK)

        toolbar.setOnRightMenuIconClickListener {
            AppUtils.shareTextContent(
                    context = this@ECommerceActiviy,
                    text = URL_LINK
            )
        }
    }


    private fun getWebClient() = object : WebViewClient() {

        override fun onReceivedSslError(view: WebView?,
                                        handler: SslErrorHandler?,
                                        error: SslError?) {

            val message = error?.let { getSslErrorMessage(it) }
            AlertDialog.Builder(this@ECommerceActiviy)
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


    companion object {
        const val TITLE_EXTRA = "TITLE_EXTRA"
        const val URL_EXTRA = "URL_EXTRA"
        const val SHARE_EXTRA = "SHARE_EXTRA"

        fun startEcommerce(context: Context?) {
            val intent = Intent(context, ECommerceActiviy::class.java)
            context?.startActivity(intent)
        }

    }
}
