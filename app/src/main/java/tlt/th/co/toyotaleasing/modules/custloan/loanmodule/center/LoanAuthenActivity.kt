package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.center

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_loan_login.*
import kotlinx.android.synthetic.main.fragment_loan_login.btngocal
import kotlinx.android.synthetic.main.fragment_online_car_loan.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.base.BaseFragment
import tlt.th.co.toyotaleasing.common.listener.OnHambergerClickListener
import tlt.th.co.toyotaleasing.manager.db.MasterDataManager
import tlt.th.co.toyotaleasing.manager.db.UserManager
import tlt.th.co.toyotaleasing.modules.home.customer.MainCustomerActivity
import tlt.th.co.toyotaleasing.modules.home.noncustomer.MainNonCustomerActivity
import tlt.th.co.toyotaleasing.modules.web.WebActivity


class LoanAuthenActivity  : BaseActivity() {

    private lateinit var uniqeID: String
    private val urlWebOnline = MasterDataManager.getInstance().getWebCarLoan()//!!.get(0).cARLOANWEB.toString()

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(LoanAuthenViewModel::class.java)
    }

    override fun onPause() {
        super.onPause()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_loan_login)

        initInstance()
        initViewModel()
    }

    private fun initInstance() {

        edittext_id_card.setText("") //1101500466758
        btn_confirm.setOnClickListener {
            var idcard = edittext_id_card.text.toString().trim()
            var refID = "" //""RF-201900000002"

            viewModel.getPhonenumberList(idcard ,refID )
        }

        btngocal.setOnClickListener {
            try{

            }catch (e : Exception) {
                e.printStackTrace()
            }
        }

    }

    private fun initViewModel() {

        viewModel.whenPhoneNumberListLoaded.observe(this, Observer {

            LoanSelectPhoneActivity.open(this@LoanAuthenActivity, it!!)
        })

        viewModel.whenPhoneNumberListNoLoaded.observe(this, Observer {

        })
    }

    companion object {
        const val UNI_ID = "UNI_ID"
        const val IS_SHOW_DIALOG_FROM_DEEPLINK_CAR_LOAN = "IS_SHOW_DIALOG_FROM_DEEPLINK_CAR_LOAN"
        fun newInstance() = LoanAuthenActivity()

        fun startByInsight(context: Context?) {
            if (UserManager.getInstance().isCustomer()) {
                MainCustomerActivity.openInsightWithDeep(
                        context = context,
                        position = MainCustomerActivity.CAR_LOAN,
                        data = Bundle().apply {
                        }
                )
            } else {
                MainNonCustomerActivity.openInsightWithDeep(
                        context = context,
                        authen = "N" ,
                        position = MainNonCustomerActivity.CAR_LOAN,
                        data = Bundle().apply {
                        }
                )
            }
        }

        fun open(context: Context ) {
            val intent = Intent(context, LoanAuthenActivity::class.java)
            context.startActivity(intent)
        }

    }
}
