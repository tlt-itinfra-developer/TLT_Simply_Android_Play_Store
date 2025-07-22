package tlt.th.co.toyotaleasing.modules.pincode

import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_change_pincode_success.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.modules.home.customer.MainCustomerActivity

class ChangePincodeSuccessActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(ChangePincodeViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_pincode_success)

        initViewModel()
        initInstances()

        viewModel.resetLoginAttempt()
    }

    private fun initViewModel() {

    }

    private fun initInstances() {
        btn_confirm.setOnClickListener {
            if (viewModel.isResetPincodeFlow()) {
                MainCustomerActivity.startWithClearStack(
                        context = this@ChangePincodeSuccessActivity,
                        position = MainCustomerActivity.INSTALLMENT_MENU_POSITION
                )
            } else {
                MainCustomerActivity.startWithClearStack(this@ChangePincodeSuccessActivity)
            }
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ChangePincodeSuccessActivity::class.java)
            context.startActivity(intent)
        }
    }
}
