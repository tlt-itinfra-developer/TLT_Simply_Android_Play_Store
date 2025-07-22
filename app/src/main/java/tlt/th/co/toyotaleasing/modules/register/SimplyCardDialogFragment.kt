package tlt.th.co.toyotaleasing.modules.register

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import kotlinx.android.synthetic.main.fragment_dialog_simply_card.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseDialogFragment

class SimplyCardDialogFragment : BaseDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
       dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
       dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dialog_simply_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstance()
    }

    private fun initInstance() {
        btn_close.setOnClickListener {
            fragmentManager.let { dismiss() }
        }
    }

    override fun onResume() {
        super.onResume()
       dialog!!.window!!.setLayout(resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels)
    }

    companion object {
        private const val TAG = "SimplyCardDialogFragment"

        fun newInstance() = SimplyCardDialogFragment()

        fun show(fragmentManager: FragmentManager) {
            newInstance().apply {
                show(fragmentManager, TAG)
            }
        }

        fun show(fragmentManager: FragmentManager,
                 fragment: Fragment) {
            newInstance().apply {
                setTargetFragment(fragment, 1)
                show(fragmentManager, TAG)
            }
        }
    }
}