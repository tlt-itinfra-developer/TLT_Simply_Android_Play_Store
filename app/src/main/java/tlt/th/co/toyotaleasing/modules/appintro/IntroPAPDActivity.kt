package tlt.th.co.toyotaleasing.modules.appintro

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_papd_webview.*
import tlt.th.co.toyotaleasing.BuildConfig
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import android.webkit.WebSettings
import android.webkit.WebView


class IntroPAPDActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(IntroPAPDViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_papd_webview)

        initViewModel()
        initInstances()
    }

    private fun initViewModel() {
        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!)
        })


        viewModel.whenURLDataLoaded.observe(this, Observer {
            val webSettings = webview.settings
            webSettings.javaScriptEnabled = true
            webview.loadUrl(it)

        })

        viewModel.whenUserDenied.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun initInstances() {
       viewModel.getData()
        btn_next_intro.setOnClickListener {
            viewModel.disableShowForNextOpenApp()
            AppIntroActivity.open(this)
            finish()
        }
    }

    companion object {
        fun open(context: Context) {
            val intent = Intent(context, IntroPAPDActivity::class.java)
            context.startActivity(intent)
        }
    }
}
