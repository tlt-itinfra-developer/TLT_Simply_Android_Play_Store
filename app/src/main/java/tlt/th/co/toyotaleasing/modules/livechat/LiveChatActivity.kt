package tlt.th.co.toyotaleasing.modules.livechat

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import im.delight.android.webview.AdvancedWebView
import kotlinx.android.synthetic.main.activity_live_chat.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.manager.db.CacheManager
import tlt.th.co.toyotaleasing.manager.db.UserManager
import java.util.*

class LiveChatActivity : BaseActivity(), AdvancedWebView.Listener {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(LiveChatViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_chat)

        initInstance()
        initViewModel()
    }

    private fun initInstance() {
        webview.setListener(this, this)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getData()
    }

    private fun initViewModel() {
//        viewModel.whenDataLoaded.observe(this, Observer {
//
//            val car = CacheManager.getCacheCar()
//            var encodeName: String = ""
//            var email: String = ""
//            var phone: String = ""
//            var contractNumber: String = ""
//            var regNumber: String = ""
//
//            if (UserManager.getInstance().isCustomer()) {
//                val catchProfile = CacheManager.getCacheProfile()
//                UserManager.getInstance().saveUuidForNonCustomer(catchProfile!!.chatbotId.toString())
//            } else {
//                if (UserManager.getInstance().getProfile().uuidForNonCustomer.isEmpty()) {
//                    UserManager.getInstance().saveUuidForNonCustomer(UUID.randomUUID().toString())
//                }
//            }
//
//            val uid = UserManager.getInstance().getProfile().uuidForNonCustomer
//
//            if (UserManager.getInstance().isCustomer()) {
//                encodeName = it?.name!!.replace(" ", "%20")
//                email = it.email
//                phone = it.phone
//                contractNumber = car!!.contractNumber ?: ""
//                regNumber = car.regNumber ?: ""
//            }
//
//
//            webview.loadUrl(url)
//        })


        viewModel.whenDataLoadedChatBotSuccess.observe(this, Observer {
            it?.let {
                webview.loadUrl(it)
            }
        })


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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        webview.onActivityResult(requestCode, resultCode, intent)
    }

    companion object {
        fun start(context: Context?) {
            context?.startActivity(Intent(context, LiveChatActivity::class.java))
        }
    }
}

