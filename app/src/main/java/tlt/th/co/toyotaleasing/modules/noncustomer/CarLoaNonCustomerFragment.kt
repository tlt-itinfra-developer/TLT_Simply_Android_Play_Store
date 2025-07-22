package tlt.th.co.toyotaleasing.modules.noncustomer

import tlt.th.co.toyotaleasing.R

class CarLoaNonCustomerFragment : NonCustomerFragment() {

    override fun onInitTitle(): String {
        return getString(R.string.sidebar_menu_car_loan)
    }

    companion object {
        fun newInstance() = CarLoaNonCustomerFragment()
    }
}
