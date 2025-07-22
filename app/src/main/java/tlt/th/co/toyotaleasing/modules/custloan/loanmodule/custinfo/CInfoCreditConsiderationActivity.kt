package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.custinfo

import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.core.content.ContextCompat
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_loan_info_credit_consideration.*
import kotlinx.android.synthetic.main.loan_cust_info_upper_sheet.view.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.modules.custloan.MenuStepController
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.center.CarLoanFragment

class CInfoCreditConsiderationActivity : BaseActivity() {

    private val data_extra by lazy {
        intent?.getStringExtra(REF_ID) ?: ""
    }

    private val data_url by lazy {
        intent?.getStringExtra(REF_URL) ?: ""
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(CInfoCreditConsiderationViewModel::class.java)
    }

    private val FIVE_SECOND = 5000L
    private val handlerAutoCallNDIDRes = Handler()
    private var animatImg: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_loan_info_credit_consideration)
        AnalyticsManager.trackScreen(AnalyticsScreenName.ONLINE_CREDIT_CONSIDER)
        initViewModel()
        initInstance()
        viewModel.getDecisionEngine(data_extra)
    }

    fun initInstance() {

        map_online_require_upper_sheet.stepCreditConsider.background = ContextCompat.getDrawable(this, R.drawable.step_consodilation_active)
        map_online_require_upper_sheet.step1.setTextColor(ContextCompat.getColor(this@CInfoCreditConsiderationActivity, R.color.cherry_red))
        map_online_require_upper_sheet.txt_ref_value.text = data_extra

//        btn_next_confirm.setOnClickListener {
//            DeliveryQRActivity.show(this@CInfoCreditConsiderationActivity!!, DATA_EXTRA )
//        }
        animatImg = findViewById(R.id.ld_wating) as ImageView

//        Glide.with(this)
//                .load(R.drawable.loading) // load file from drawable
//                .into(animatImg!!) // .. into image view xml file

        Glide.with(this)
                .load(R.drawable.icon_processing)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(animatImg!!)

        btn_back.setOnClickListener {
            CarLoanFragment.startByInsight(
                    context = this@CInfoCreditConsiderationActivity, authen = "Y"
            )
        }

    }



    fun initViewModel(){

        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!)
        })

        viewModel.whenSyncSuccessData.observe(this, Observer {
            it?.let {
                if( it.step != "DENGINE" ) {
                    MenuStepController.open(this, it.ref_id, it.step, "")
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        handlerAutoCallNDIDRes.post(autoVerifyEngine)
    }

    override fun onStop() {
        handlerAutoCallNDIDRes.removeCallbacks(autoVerifyEngine)
        super.onStop()
    }

    private val autoVerifyEngine = object : Runnable {
        override fun run() {
            viewModel.CheckStepAPI(data_extra)
            handlerAutoCallNDIDRes.postDelayed(this, FIVE_SECOND)

        }
    }

    companion object {

        const val REF_ID = "REF_ID"
        const val REF_URL = "REF_URL"

        fun start(activity: Activity?, data: String , url : String ) {
            val intent = Intent(activity, CInfoCreditConsiderationActivity::class.java)
            intent.putExtra(REF_ID, data)
            intent.putExtra(REF_URL, url)
            activity!!.startActivity(intent)
        }
    }
}

