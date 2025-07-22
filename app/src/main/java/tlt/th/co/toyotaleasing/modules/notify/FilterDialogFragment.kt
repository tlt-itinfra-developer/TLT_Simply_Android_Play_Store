package tlt.th.co.toyotaleasing.modules.notify

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import kotlinx.android.synthetic.main.fragment_dialog_filter.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseDialogFragment

class FilterDialogFragment : BaseDialogFragment() {

    private lateinit var listener: FilterDialogFragment.Listener

    private val titleDialog by lazy {
        arguments?.getString(TITLE_EXTRA, "")
    }

    private val content by lazy {
        arguments?.getStringArrayList(CONTENT_EXTRA)?.toTypedArray() ?: arrayOf()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
       dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
       dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dialog_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstances()
    }

    override fun onResume() {
        super.onResume()
       dialog!!.window!!.setLayout(resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels)
    }

    private fun initInstances() {
        title.text = titleDialog

        filter_picker.apply {
            minValue = 0
            maxValue = if (content.isEmpty()) {
                0
            } else {
                content.size - 1
            }

            displayedValues = content
            wrapSelectorWheel = false
        }

        btn_confirm.setOnClickListener {
            if (content.isEmpty()) {
                fragmentManager?.let { dismiss() }
                return@setOnClickListener
            }

            listener.onResult(
                    filter_picker.value,
                    content[filter_picker.value]
            )

            fragmentManager?.let { dismiss() }
        }
    }

    companion object {
        private const val TITLE_EXTRA = "TITLE_EXTRA"
        private const val CONTENT_EXTRA = "CONTENT_EXTRA"
        private const val TAG = "FilterDialogFragment"

        fun newInstance() = FilterDialogFragment()

        fun show(fragmentManager: FragmentManager,
                 title: String,
                 content: List<String>,
                 listener: Listener) {
            newInstance().apply {
                val items = ArrayList<String>(content)

                arguments = Bundle().apply {
                    putString(TITLE_EXTRA, title)
                    putStringArrayList(CONTENT_EXTRA, items)
                }

                this.listener = listener
                show(fragmentManager, TAG)
            }
        }
    }

    interface Listener {
        fun onResult(index: Int, selectContent: String)
    }
}