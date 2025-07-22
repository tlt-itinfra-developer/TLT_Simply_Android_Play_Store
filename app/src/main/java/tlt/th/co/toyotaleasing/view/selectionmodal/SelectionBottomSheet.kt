package tlt.th.co.toyotaleasing.view.selectionmodal

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DividerItemDecoration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.bottomsheet_selection.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseBottomSheetDialogFragment
import tlt.th.co.toyotaleasing.common.base.BaseFragment

class SelectionBottomSheet : BaseBottomSheetDialogFragment() {

    private val list by lazy {
        arguments?.getStringArrayList(LIST_EXTRA)
    }

    private val defaultSelected by lazy {
        arguments?.getIntegerArrayList(DEFAULT_SELECTED_EXTRA)
    }

    private val disableSelected by lazy {
        arguments?.getIntegerArrayList(DISABLE_SELECTED_EXTRA)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottomsheet_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstances()
    }

    private fun initInstances() {
        val items = list?.mapIndexed { index, value ->
            val newItem = SelectionBottomSheetAdapter.Model(name = value)

            if (defaultSelected?.contains(index) == true) {
                newItem.isSelected = true
            }

            if (disableSelected?.contains(index) == true) {
                newItem.isDisabled = true
            }

            newItem
        }?.toMutableList()

        recycler_view.apply {
            addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
            adapter = SelectionBottomSheetAdapter(items ?: mutableListOf(), onItemClickListener)
        }
    }

    private val onItemClickListener = { index : Int, item : SelectionBottomSheetAdapter.Model ->
        if (!item.isDisabled) {
            fragmentManager?.let {
                dismiss()
                getListener()?.onItemSelectionClick(index, item)
            }
        }
    }

    private fun getListener() : Listener? {
        targetFragment?.let {
            return (it as Listener)
        }

        if (context is Listener) {
            return (context as Listener)
        }

        return null
    }

    interface Listener {
        fun onItemSelectionClick(index : Int, item : SelectionBottomSheetAdapter.Model)
    }

    companion object {
        private const val LIST_EXTRA = "LIST_EXTRA"
        private const val DEFAULT_SELECTED_EXTRA = "DEFAULT_SELECTED_EXTRA"
        private const val DISABLE_SELECTED_EXTRA = "DISABLE_SELECTED_EXTRA"

        fun show(fragmentManager: FragmentManager,
                 targetFragment: BaseFragment? = null,
                 list: ArrayList<String> = arrayListOf(),
                 defaultSelected: ArrayList<Int> = arrayListOf(),
                 disableSelected: ArrayList<Int> = arrayListOf()) {
            SelectionBottomSheet().apply {
                arguments = Bundle().apply {
                    putStringArrayList(LIST_EXTRA, list)
                    putIntegerArrayList(DEFAULT_SELECTED_EXTRA, defaultSelected)
                    putIntegerArrayList(DISABLE_SELECTED_EXTRA, disableSelected)
                }

                setTargetFragment(targetFragment, 1)
                show(fragmentManager, this::class.java.simpleName)
            }
        }
    }
}