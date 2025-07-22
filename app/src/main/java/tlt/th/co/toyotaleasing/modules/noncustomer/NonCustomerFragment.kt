package tlt.th.co.toyotaleasing.modules.noncustomer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_non_customer.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseFragment
import tlt.th.co.toyotaleasing.common.listener.OnHambergerClickListener
import tlt.th.co.toyotaleasing.modules.home.noncustomer.MainNonCustomerActivity

abstract class NonCustomerFragment : BaseFragment() {

    private val onHambergerClickListener by lazy {
        context as OnHambergerClickListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_non_customer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstances()
    }

    private fun initInstances() {
        val title = onInitTitle()

        toolbar.setTitle(title)

        toolbar.setOnHambergerMenuClickListener {
            onHambergerClickListener.onHambergerClick()
        }

        btn_confirm.setOnClickListener {
            MainNonCustomerActivity.openWithClearStack(context)
        }
    }

    abstract fun onInitTitle(): String
}
