package tlt.th.co.toyotaleasing.modules.noncustomer

import tlt.th.co.toyotaleasing.R

class TaxNonCustomerFragment : NonCustomerFragment() {

    override fun onInitTitle(): String {
        return getString(R.string.sidebar_menu_tax)
    }

    companion object {
        fun newInstance() = TaxNonCustomerFragment()
    }
}
