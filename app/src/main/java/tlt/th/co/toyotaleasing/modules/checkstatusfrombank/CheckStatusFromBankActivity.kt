package tlt.th.co.toyotaleasing.modules.checkstatusfrombank

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_check_status_from_bank.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.extension.gone
import tlt.th.co.toyotaleasing.common.extension.ifFalse
import tlt.th.co.toyotaleasing.common.extension.ifTrue
import tlt.th.co.toyotaleasing.common.extension.visible
import tlt.th.co.toyotaleasing.model.response.CheckStatusFromBankResponse
import tlt.th.co.toyotaleasing.modules.payment.receipt.ReceiptDelayActivity

class CheckStatusFromBankActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(CheckStatusFromBankViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_status_from_bank)

        initViewModel()

        viewModel.getReceiptData()
    }

    private fun initViewModel() {
        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreen(it!!)
        })

        viewModel.whenPushReceiptPage.observe(this, Observer {
            pushActivity(it!!)
        })

        viewModel.whenNeedFinish.observe(this, Observer {
            it!!.ifTrue {
                finish()
            }
        })

        viewModel.whenNoInternetConnection.observe(this, Observer {
            finish()
        })
    }

    private fun pushActivity(it: List<CheckStatusFromBankResponse>) {
        it.forEach {
            ReceiptDelayActivity.start(
                    context = this,
                    isDelay = true,
                    item = it)
        }
        finish()
    }

    private fun toggleLoadingScreen(isShow: Boolean = false) {
        isShow.ifTrue {
            showLoadingScreen()
        }

        isShow.ifFalse {
            hideLoadingScreen()
        }
    }

    private fun showLoadingScreen() {
        loading_screen.visible()
        loading_screen.scale = 0.5f
        loading_screen.playAnimation()
    }

    private fun hideLoadingScreen() {
        loading_screen.clearAnimation()
        loading_screen.gone()
    }

    companion object {
        fun start(context: Context?) {
            val intent = Intent(context, CheckStatusFromBankActivity::class.java)
            context?.startActivity(intent)
        }
    }
}