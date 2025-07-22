package tlt.th.co.toyotaleasing.modules.insurance.hotline.service

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_hotline_service.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseFragment
import tlt.th.co.toyotaleasing.modules.home.customer.MainCustomerActivity
import tlt.th.co.toyotaleasing.modules.home.noncustomer.MainNonCustomerActivity

class ServiceFragment : BaseFragment() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(ServiceViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_hotline_service, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstances()
    }

    private fun initInstances() {
        btn_confirm.setOnClickListener {
            if (viewModel.isCustomer()) {
                MainCustomerActivity.startWithClearStack(
                        context,
                        MainCustomerActivity.OFFICE_LOCATION_MENU_POSITION
                )
            } else {
                MainNonCustomerActivity.openWithClearStack(
                        context,
                        MainNonCustomerActivity.OFFICE_LOCATION_MENU_POSITION
                )
            }
        }
    }

    companion object {
        fun newInstance() = ServiceFragment()
    }
}