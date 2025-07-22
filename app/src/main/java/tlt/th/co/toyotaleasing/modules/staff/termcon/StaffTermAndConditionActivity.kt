package tlt.th.co.toyotaleasing.modules.staff.termcon

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_staff_term_and_condition.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseActivity

import tlt.th.co.toyotaleasing.modules.home.noncustomer.MainNonCustomerActivity

class StaffTermAndConditionActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(StaffTermAndConditionViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_staff_term_and_condition)

        initInstance()
        initViewModel()
    }

    private fun initViewModel() {
        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!)
        })

        viewModel.whenUserAllowSuccess.observe(this, Observer {
            MainNonCustomerActivity.open(this@StaffTermAndConditionActivity)
        })
    }

    private fun initInstance() {
        txt_description.apply {
            enableSupportZoom()
            loadUrl("")
        }

        btn_accept_policy_success.setOnClickListener {
            viewModel.sendTermAndCondition()
        }
    }

    companion object {
        fun open(context: Context?) {
            val intent = Intent(context, StaffTermAndConditionActivity::class.java)
            context?.startActivity(intent)
        }
    }
}
