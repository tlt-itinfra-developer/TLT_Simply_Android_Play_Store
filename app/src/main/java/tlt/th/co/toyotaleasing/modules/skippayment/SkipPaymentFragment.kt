package tlt.th.co.toyotaleasing.modules.skippayment

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.net.http.SslError
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_skippayment.*
import kotlinx.android.synthetic.main.fragment_news_and_promotion.toolbar
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.common.base.BaseFragment
import tlt.th.co.toyotaleasing.common.eventbus.ReloadSkippaymentEvent
import tlt.th.co.toyotaleasing.common.listener.OnHambergerClickListener
import tlt.th.co.toyotaleasing.manager.BusManager
import tlt.th.co.toyotaleasing.manager.ContextManager
import tlt.th.co.toyotaleasing.manager.db.MasterDataManager
import android.os.Build
import android.webkit.*
import kotlinx.android.synthetic.main.activity_ecommerce.*
import kotlinx.android.synthetic.main.activity_skippayment.webview
import tlt.th.co.toyotaleasing.manager.db.CacheManager
import tlt.th.co.toyotaleasing.manager.db.UserManager


class SkipPaymentFragment : BaseFragment() {


    private val onHambergerClickListener by lazy {
        context as OnHambergerClickListener
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(SkippaymentViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_skippayment, container, false)
    }

    var URL_LINK  = ""
    var TITLE : String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstances()
        initViewModel()

    }

    private fun initInstances() {
        toolbar.setOnHambergerMenuClickListener {
            onHambergerClickListener.onHambergerClick()
        }



//        val master = MasterDataManager.getInstance().getSideMenu()
//        if (master != null) {
//            if(master.size>0) {
//                URL_LINK = master!!.get(0).mENULINK
//                toolbar.setTitle(master!!.get(0).mENUNAME().toString())
//            }
//        }

        val master = MasterDataManager.getInstance().getSideMenu()

        try {
            webview.webViewClient = getWebClient()
            val profile = CacheManager.getCacheProfile()


        } catch (e: Exception) {
            e.printStackTrace()
        }


    }


    private fun initViewModel() {

        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!, this)
        })

        viewModel.whenDataLoaded.observe(this, Observer {
            try {

                if(it != ""){
                    val settings = webview.getSettings()
                    settings.setBuiltInZoomControls(true)
                    settings.setSupportZoom(true)
                    settings.javaScriptEnabled = true
                    settings.setAppCacheEnabled(true)
                    webview.apply {
                        webview.loadDataWithBaseURL(URL_LINK, it, "text/html", "UTF-8", "");
                    }

                }
            }catch (e : Exception){
                e.printStackTrace()
            }
        })
    }


    private fun getWebClient() = object : WebViewClient() {

        override fun onReceivedSslError(view: WebView?,
                                        handler: SslErrorHandler?,
                                        error: SslError?) {

//            handler?.proceed()
            val message = error?.let { getSslErrorMessage(it) }
            AlertDialog.Builder(context!!)
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

        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            if(url!!.contains("DownloadPage") ) {
                Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                    startActivity(this)
                }
                return true
            }
            return false
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

    @Subscribe(threadMode = ThreadMode.MAIN)

    fun onReloadSkippayment(event: ReloadSkippaymentEvent) {
        viewModel.GetData()
    }

    override fun onStart() {
        super.onStart()
        BusManager.subscribe(this)
    }

    override fun onStop() {
        BusManager.unsubscribe(this)
        super.onStop()
    }

    companion object {

        fun newInstance() = SkipPaymentFragment()

    }
}