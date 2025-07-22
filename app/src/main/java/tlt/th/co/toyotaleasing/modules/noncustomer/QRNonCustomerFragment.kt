package tlt.th.co.toyotaleasing.modules.noncustomer

import tlt.th.co.toyotaleasing.R

class QRNonCustomerFragment : NonCustomerFragment() {

    override fun onInitTitle(): String {
        return getString(R.string.sidebar_menu_qrcode)
    }

    companion object {
        fun newInstance() = QRNonCustomerFragment()
    }
}
