package tlt.th.co.toyotaleasing.modules.debug

import android.app.Dialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import kotlinx.android.synthetic.main.fragment_dialog_debug.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseDialogFragment
import tlt.th.co.toyotaleasing.manager.LocalizeManager
import tlt.th.co.toyotaleasing.modules.main.MainActivity

class DebugDialogFragment : BaseDialogFragment() {

    private lateinit var viewModel: DebugViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
       dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
       dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dialog_debug, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initInstances()
    }

    override fun onResume() {
        super.onResume()
       dialog!!.window!!.setLayout(resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels)
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(DebugViewModel::class.java)

        viewModel.whenTokenChanged.observe(this, Observer {
            MainActivity.openWithClearStack(context!!)
        })
    }

    private fun initInstances() {
        form_quotation_popup_btn_close.setOnClickListener {
            fragmentManager?.let { dismiss() }
        }

        btn_confirm.setOnClickListener {
            viewModel.changeToken(edittext_token.toString().trim())
        }

        btn_change_to_th.setOnClickListener {
            LocalizeManager.changeToTH(context!!)
            MainActivity.openWithClearStack(context!!)
        }

        btn_change_to_en.setOnClickListener {
            LocalizeManager.changeToEN(context!!)
            MainActivity.openWithClearStack(context!!)
        }
    }

    companion object {
        private val TAG = this::class.java.simpleName!!

        fun show(fragmentManager: FragmentManager) {
            fragmentManager.findFragmentByTag(TAG)?.let {
                fragmentManager.beginTransaction()
                        .show(it)
                        .commitAllowingStateLoss()
                return
            }

            fragmentManager.beginTransaction()
                    .add(DebugDialogFragment(), TAG)
                    .commitAllowingStateLoss()
        }
    }
}