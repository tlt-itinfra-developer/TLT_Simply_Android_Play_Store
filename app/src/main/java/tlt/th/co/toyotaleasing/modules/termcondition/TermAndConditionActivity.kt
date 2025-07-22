package tlt.th.co.toyotaleasing.modules.termcondition

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_term_and_condition.*
import kotlinx.android.synthetic.main.layout_lottie_loading_screen.*
import tlt.th.co.toyotaleasing.BuildConfig
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.extension.gone
import tlt.th.co.toyotaleasing.common.extension.ifFalse
import tlt.th.co.toyotaleasing.common.extension.ifTrue
import tlt.th.co.toyotaleasing.common.extension.visible
import tlt.th.co.toyotaleasing.modules.disclosure.DisclosureActivity

class TermAndConditionActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(TermAndConditionViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_term_and_condition)

        initViewModel()
        initInstances()
    }

    override fun onPause() {
        super.onPause()
        AnalyticsManager.trackScreen(AnalyticsScreenName.REGISTER_TERMS_CONDITIONS)
    }

    private fun initViewModel() {
        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!)
        })

        viewModel.whenUserAllow.observe(this, Observer {
            DisclosureActivity.open(this@TermAndConditionActivity)
        })

        viewModel.whenUserDenied.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun initInstances() {
        txt_description.apply {
            enableSupportZoom()
            loadUrl(BuildConfig.POLICY_URL)
        }

        checkbox_accept.setOnClickListener {
            AnalyticsManager.termAndConditionCheckBox()
            btn_accept_policy_success.isEnabled = checkbox_accept.isChecked
        }

        btn_accept_policy_success.setOnClickListener {
            AnalyticsManager.termAndConditionNextClicked()
            viewModel.sendTermCondition(true)
        }
    }

    companion object {
        fun open(context: Context) {
            val intent = Intent(context, TermAndConditionActivity::class.java)
            context.startActivity(intent)
        }
    }
}
