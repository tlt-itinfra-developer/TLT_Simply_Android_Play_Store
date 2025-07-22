package tlt.th.co.toyotaleasing.template

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseFragment

class TemplateFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_recycler_test, container, false)
    }

    companion object {
        fun newInstance() = TemplateFragment()
    }
}
