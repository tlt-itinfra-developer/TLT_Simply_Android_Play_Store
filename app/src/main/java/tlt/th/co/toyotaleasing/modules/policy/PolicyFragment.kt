package tlt.th.co.toyotaleasing.modules.policy

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_policy.*
import tlt.th.co.toyotaleasing.BuildConfig
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseFragment
import tlt.th.co.toyotaleasing.common.listener.OnHambergerClickListener

class PolicyFragment : BaseFragment() {

    private val onHambergerClickListener by lazy {
        context as OnHambergerClickListener
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(PolicyViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_policy, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initInstances()
    }

    private fun initInstances() {
        toolbar.setOnHambergerMenuClickListener {
            onHambergerClickListener.onHambergerClick()
        }

        webview.apply {
            enableSupportZoom()
            loadUrl(BuildConfig.POLICY_URL)
        }
    }



        companion object {
        fun newInstance() = PolicyFragment()
    }
}
