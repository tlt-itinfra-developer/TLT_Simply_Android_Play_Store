package tlt.th.co.toyotaleasing.modules.newsandpromotion.oilprice


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_web.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseFragment
import tlt.th.co.toyotaleasing.common.extension.gone
import tlt.th.co.toyotaleasing.common.extension.visible

class OilPriceWebFragment : BaseFragment()  {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_web, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initInstance()
    }

    private fun initViewModel() {

    }

    private fun initInstance() {
        toolbar.gone()
        try {
            webview.loadUrl("https://www.bangchak.co.th/oilprice.html")
        }catch (e : Exception){
            e.printStackTrace()
        }
    }

    companion object {
        fun newInstance() = OilPriceWebFragment()
    }
}
