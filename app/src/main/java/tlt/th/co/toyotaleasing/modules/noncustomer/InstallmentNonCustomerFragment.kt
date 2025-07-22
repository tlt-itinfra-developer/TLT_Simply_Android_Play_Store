package tlt.th.co.toyotaleasing.modules.noncustomer

import tlt.th.co.toyotaleasing.R

class InstallmentNonCustomerFragment : NonCustomerFragment() {

    override fun onInitTitle(): String {
        return getString(R.string.installment_status_title)
    }

    companion object {
        fun newInstance() = InstallmentNonCustomerFragment()
    }
}
