package tlt.th.co.toyotaleasing.modules.selectphone

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_select_phone.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.dialog.CallCenterDialogFragment
import tlt.th.co.toyotaleasing.modules.otp.OTPActivity

class SelectPhoneActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(SelectPhoneViewModel::class.java)
    }

    override fun onPause() {
        super.onPause()
        AnalyticsManager.trackScreen(AnalyticsScreenName.REGISTER_SELECT_PHONE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_phone)

        val phoneList: ArrayList<PhoneNumber> = ArrayList<PhoneNumber>()

        initViewModel()
        initInstances()
//        getPhonenumberList()

        val adapter = recycler_phonenumber_list.adapter as SelectPhoneAdapter
        adapter.addItems(phoneList)
    }

    private fun getPhonenumberList() {
        viewModel.getPhonenumberList()
    }

    private fun initViewModel() {
        viewModel.whenLoading.observe(this, Observer {
            toggleLoading(it!!)
        })

        viewModel.whenPhoneNumberListLoaded.observe(this, Observer {
            val adapter = recycler_phonenumber_list.adapter as SelectPhoneAdapter
            adapter.addItems(it!!)
        })
    }

    private fun initInstances() {
        recycler_phonenumber_list.isNestedScrollingEnabled = false
        recycler_phonenumber_list.layoutManager = LinearLayoutManager(this)
        recycler_phonenumber_list.adapter = SelectPhoneAdapter(ArrayList(), onPhoneNumberClickListener)

        txt_contact_staff.setOnClickListener {
            AnalyticsManager.selectPhoneContactStaffClicked()
            CallCenterDialogFragment.show(fragmentManager = supportFragmentManager,
                    openBy = CallCenterDialogFragment.SELECT_PHONE)
        }
    }

    private val onPhoneNumberClickListener = object : SelectPhoneAdapter.OnPhoneNumberClickListener {
        override fun onClick(phoneNumber: PhoneNumber) {
            AnalyticsManager.selectPhoneClicked()
            viewModel.savePhonenumber(phoneNumber)
            OTPActivity.open(this@SelectPhoneActivity)
        }
    }

    companion object {
        const val PHONE_NUMBER_LIST = "phone_number_list"

        fun open(context: Context, items: ArrayList<PhoneNumber>) {
            val intent = Intent(context, SelectPhoneActivity::class.java)
            intent.putExtra(PHONE_NUMBER_LIST, items)
            context.startActivity(intent)
        }
    }
}
