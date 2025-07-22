package tlt.th.co.toyotaleasing.modules.ibanking

import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import android.util.Log
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import im.delight.android.webview.AdvancedWebView
import kotlinx.android.synthetic.main.activity_ibanking.*
import kotlinx.android.synthetic.main.widget_toolbar.view.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.manager.ContextManager
import tlt.th.co.toyotaleasing.manager.db.PaymentManager
import tlt.th.co.toyotaleasing.util.ExternalAppUtils
import java.lang.Exception

class IBankingActivity : BaseActivity(), AdvancedWebView.Listener {

    private val paymentManager = PaymentManager
    private val BARCODE_ID = "099"
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(IBankingViewModel::class.java)
    }

    private val bankCode by lazy {
        paymentManager.getPaymentMethod()?.paymentMethodId ?: ""
    }

    private val callBankDesc by lazy {
        paymentManager.getPaymentMethod()?.callbankDesc ?: ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ibanking)

        initViewModel()
        initInstances()
        if (bankCode != BARCODE_ID){
            toggleLoadingScreenDialog(isEnable = true)
            viewModel.getData()
        }else {
            try {
                AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setMessage(ContextManager.getInstance().getApplicationContext().resources.getString(R.string.error_please_connect_again))
                    .setPositiveButton(ContextManager.getInstance().getApplicationContext().resources.getString(R.string.dialog_button_ok))
                    { dialog, id ->
                       dialog!!.dismiss()
                        toolbar.widget_toolbar_navigation.callOnClick()
                    }
                    .show()
            } catch (e: Exception) {
                e.message
            }
        }

    }

    private fun initInstances() {
         try{
         webview.setListener(this, this)
         webview.webViewClient = getWebClient()
         }catch (e : Exception){
             e.printStackTrace()
         }
         toolbar.setOnHambergerMenuClickListener {
//                viewModel.cancelTransaction()
                setResult(PAY_FAIL)
                finish()
         }

    }

    private fun initViewModel() {
        viewModel.whenFailure.observe(this, Observer {
            toggleLoadingScreenDialog(isEnable = false)
            Toast.makeText(this, it ?: "", Toast.LENGTH_SHORT).show()
        })

        viewModel.whenDataLoaded.observe(this, Observer {
            toggleLoadingScreenDialog(isEnable = true)

        })

        viewModel.whenMbankDataLoaded.observe(this, Observer {
           it?.let{
               toggleLoadingScreenDialog(isEnable = false)
               if(it != "") {
                   ExternalAppUtils.openMBankDeepLink(this, it, bankCode)
               }else{
               }
           }

        })
    }


    private fun getWebClient() = object : WebViewClient() {

        override fun onReceivedSslError(view: WebView?,
                                        handler: SslErrorHandler?,
                                        error: SslError?) {

//            handler?.proceed()
            val message = error?.let { getSslErrorMessage(it) }
            AlertDialog.Builder(this@IBankingActivity)
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


    private fun isPaymentSuccess(url: String?): Boolean {
        if(bankCode.equals("014") && url!!.contains("scbeasy://partnerpayment")) {
            if(url.split("URI=").count() != 1) {
                if (url.split("URI=").get(1) != null)
                    ExternalAppUtils.openBankDeepLink(this, url.split("URI=").get(1), url)
            }
        }else if (bankCode.equals("102") &&  url!!.contains("deepuri=bualuangmbanking") ){
            if(url.split("deepuri=").count() != 1) {
                if (url.split("deepuri=").get(1) != null) {
                    ExternalAppUtils.openBBLBankDeepLink(this, url.split("deepuri=").get(1), bankCode)
                }
            }
        }
        return url?.contains("response=success", true) == true
    }

    private fun isPaymentCancel(url: String?): Boolean {
        return url?.contains("response=cancel", true) == true
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
        if (requestCode == 102 )
        {
            val flagPay = intent?.getStringExtra("payment_status") ?: "Faillure"
            if(flagPay == "success"){
                setResult(Activity.RESULT_OK)
//                finish()
            }else{
                setResult(PAY_FAIL)
            }
        }else if(callBankDesc == "MBAPI"){
            setResult(PAY_FAIL)
        }
    }

    override fun onPageFinished(url: String?) {
        toggleLoadingScreenDialog(isEnable = false)
//        Log.e("WEBVIEWTEST", "onPageFinished: $url")
    }

    override fun onPageError(errorCode: Int, description: String?, failingUrl: String?) {
//        Log.e("WEBVIEWTEST", "onPageError")

    }

    override fun onDownloadRequested(url: String?, suggestedFilename: String?, mimeType: String?, contentLength: Long, contentDisposition: String?, userAgent: String?) {
//        Log.e("WEBVIEWTEST", "onDownloadRequested")

    }

    override fun onExternalPageRequest(url: String?) {
//        Log.e("WEBVIEWTEST", "onExternalPageRequest")

    }

    override fun onPageStarted(url: String?, favicon: Bitmap?) {
        toggleLoadingScreenDialog(isEnable = false)
//        toggleLoadingScreenDialog(isEnable = true)
//        Log.e("WEBVIEWTEST", "onPageStarted : $url")
//        Log.d("TEST", "iBankingActivity onPageStarted.")
//        Log.d("BANK_URL", url)

        if (isPaymentSuccess(url)) {
            setResult(Activity.RESULT_OK)
            finish()
        }


        if (isPaymentCancel(url)) {
//            viewModel.cancelTransaction()
            setResult(PAY_FAIL)
            finish()
        }
    }

    companion object {
        const val IBANKING = 997
        const val IBANKING_BBL = 998
        const val PAY_FAIL = 996


        fun startWithResult(activity: Activity) {
            val intent = Intent(activity, IBankingActivity::class.java)
            activity.startActivityForResult(intent, IBANKING)
        }


    }
}