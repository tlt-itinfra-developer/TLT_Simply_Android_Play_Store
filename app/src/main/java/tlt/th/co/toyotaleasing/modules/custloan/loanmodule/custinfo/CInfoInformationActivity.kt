package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.custinfo

import android.app.Activity
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.fragment_loan_info_information_car_loan.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.center.CarLoanFragment

class CInfoInformationActivity : BaseActivity() {

    private val data_extra by lazy {
        intent?.getStringExtra(DATA_EXTRA) ?: ""
    }


    private val viewModel by lazy {
        ViewModelProviders.of(this).get(CInfoInformationViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_loan_info_information_car_loan)

        initInstance()
    }


    fun initInstance() {

        btn_next_confirm.setOnClickListener {
            CInfoCreditConsiderationActivity.start(this@CInfoInformationActivity!!, data_extra , "" )
        }
    }

    companion object {

        const val DATA_EXTRA = "DATA_EXTRA"

        fun start(activity: Activity?, data: String) {
            val intent = Intent(activity, CInfoInformationActivity::class.java)
            intent.putExtra(DATA_EXTRA, data)
            activity!!.startActivity(intent)
        }
    }
}

