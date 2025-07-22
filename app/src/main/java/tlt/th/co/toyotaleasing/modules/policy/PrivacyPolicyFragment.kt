package tlt.th.co.toyotaleasing.modules.policy

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_privacy_policy.*
import tlt.th.co.toyotaleasing.BuildConfig
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseFragment
import tlt.th.co.toyotaleasing.common.listener.OnHambergerClickListener

class PrivacyPolicyFragment : BaseFragment() {

    private val onHambergerClickListener by lazy {
        context as OnHambergerClickListener
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(PrivacyPolicyViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_privacy_policy , container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstances()
        initViewModel()
        viewModel.getData()
    }

    private fun initInstances() {
        toolbar.setOnHambergerMenuClickListener {
            onHambergerClickListener.onHambergerClick()
        }

    }

    private fun initViewModel() {
        viewModel.whenDataLoaded.observe(this, Observer {
            if(it!= ""){
                val webSettings = webview.settings
                webSettings.javaScriptEnabled = true
                webview.loadUrl(it)
            }
        })

    }

    companion object {
        fun newInstance() = PrivacyPolicyFragment()
    }
}
