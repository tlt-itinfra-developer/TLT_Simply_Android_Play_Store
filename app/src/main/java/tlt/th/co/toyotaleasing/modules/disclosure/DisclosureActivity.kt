package tlt.th.co.toyotaleasing.modules.disclosure

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_disclosure.*
import tlt.th.co.toyotaleasing.BuildConfig
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.modules.register.RegisterFormActivity

class DisclosureActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(DisclosureViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disclosure)

        initViewModel()
        initInstances()
    }

    override fun onPause() {
        super.onPause()
        AnalyticsManager.trackScreen(AnalyticsScreenName.REGISTER_PRIVACY_POLICY)
    }

    private fun initViewModel() {
        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!)
        })

        viewModel.whenUserAllow.observe(this, Observer {
            RegisterFormActivity.open(this@DisclosureActivity)
        })

        viewModel.whenUserDenied.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun initInstances() {
        checkStateButton()
        txt_description.apply {
            enableSupportZoom()
            loadUrl(BuildConfig.DISCLOSURE_URL)
        }

        radio_accept_policy.setOnClickListener { AnalyticsManager.policyAgreeClicked() }
        radio_unaccept_policy.setOnClickListener { AnalyticsManager.policyDisAgreeClicked() }

        btn_accept_policy_success.setOnClickListener {
            AnalyticsManager.policyNextClicked()
            val isAccept = radio_terms.checkedRadioButtonId == radio_accept_policy.id

            viewModel.sendDisclosure(isAccept)
        }

        radio_terms.setOnCheckedChangeListener { group, checkedId ->
            checkStateButton()
        }
    }

    private fun checkStateButton() {
        btn_accept_policy_success.isEnabled = radio_terms.checkedRadioButtonId != -1
    }

    companion object {
        fun open(context: Context) {
            val intent = Intent(context, DisclosureActivity::class.java)
            context.startActivity(intent)
        }
    }
}
