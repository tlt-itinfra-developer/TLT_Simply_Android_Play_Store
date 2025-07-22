package tlt.th.co.toyotaleasing.modules.register

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import kotlinx.android.synthetic.main.fragment_dialog_select_register.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseDialogFragment

class SelectRegisterDialogFragment : BaseDialogFragment() {

    private var listenerActivity: Listener? = null
    private val listenerFragment by lazy {
        targetFragment?.let { it as Listener }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
       dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
       dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dialog_select_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstance()
    }

    private fun initInstance() {
        simply_id_type_cv.setOnClickListener {
            fragmentManager?.let { dismiss() }
            listenerActivity?.onSimplyIdTypeClicked()
            listenerFragment?.onSimplyIdTypeClicked()
        }

        id_card_type_cv.setOnClickListener {
            fragmentManager?.let { dismiss() }
            listenerActivity?.onIdCardTypeClicked()
            listenerFragment?.onIdCardTypeClicked()
        }

        ext_contract_type_cv.setOnClickListener {
            fragmentManager?.let { dismiss() }
            listenerActivity?.onContractNumberTypeClicked()
            listenerFragment?.onContractNumberTypeClicked()
        }

        btn_close.setOnClickListener {
            fragmentManager?.let { dismiss() }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listenerActivity = context as Listener
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
       dialog!!.window!!.setLayout(resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels)
    }

    interface Listener {
        fun onSimplyIdTypeClicked()
        fun onIdCardTypeClicked()
        fun onContractNumberTypeClicked()
    }

    companion object {
        private const val TAG = "SelectRegisterDialogFragment"

        fun newInstance() = SelectRegisterDialogFragment()

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