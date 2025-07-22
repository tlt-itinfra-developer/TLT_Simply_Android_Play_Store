package tlt.th.co.toyotaleasing.modules.tax

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import im.delight.android.webview.AdvancedWebView
import kotlinx.android.synthetic.main.activity_web.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.listener.OnHambergerClickListener
import tlt.th.co.toyotaleasing.util.AppUtils


class TaxWebActivity : BaseActivity(), AdvancedWebView.Listener {

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
        webview.loadUrl(url)

        if (isShowShareMenu) {
            toolbar.showIconRightMenu(R.drawable.ic_share_white)
        } else {
            toolbar.hideIconRightMenu()
        }


        toolbar.setOnRightMenuIconClickListener {
            AppUtils.shareTextContent(
                    context = this@TaxWebActivity,
                    text = url
            )
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

    }

    companion object {
        const val TITLE_EXTRA = "TITLE_EXTRA"
        const val URL_EXTRA = "URL_EXTRA"
        const val SHARE_EXTRA = "SHARE_EXTRA"

        fun start(context: Context?,
                  title: String,
                  url: String,
                  isShowShareMenu: Boolean = false) {
            val intent = Intent(context, TaxWebActivity::class.java).apply {
                putExtra(TITLE_EXTRA, title)
                putExtra(URL_EXTRA, url)
                putExtra(SHARE_EXTRA, isShowShareMenu)
            }

            context?.startActivity(intent)
        }

        fun startExternalWeb(context: Context? , url : String ) {
            start(
                    context = context,
                    title = context?.getString(R.string.txt_web_thaipost) ?: "",
                    url = url,
                    isShowShareMenu = false
            )
        }



    }
}