package tlt.th.co.toyotaleasing.modules.home.customer

import android.app.Dialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.fragment_dialog_notice.*
import kotlinx.android.synthetic.main.item_faq_detail.view.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseDialogFragment
import tlt.th.co.toyotaleasing.common.extension.ifTrue
import tlt.th.co.toyotaleasing.common.extension.loadImageByUrl
import tlt.th.co.toyotaleasing.common.extension.visible
import tlt.th.co.toyotaleasing.model.response.NotificationJsonResponse

class NoticeDialogFragment : BaseDialogFragment() {

    private var listenerActivity: Listener? = null

    private val listenerFragment by lazy {
        targetFragment?.let { it as Listener }
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(NoticeViewModel::class.java)
    }

    private val isShowButton by lazy {
        arguments?.getBoolean(IS_SHOW_BUTTTON, false)
    }

    private lateinit var item: NotificationJsonResponse

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
       dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
       dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    override fun onPause() {
        super.onPause()
        AnalyticsManager.trackScreenDialog(AnalyticsScreenName.PUSH_POPUP)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dialog_notice, container, false)
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
        viewModel.whenBlockedNotifySuccess.observe(this, Observer {
            if (it!!) {

            }
        })
    }

    private fun setDataIntoView(it: NotificationJsonResponse) {
        it.title.isNotEmpty().ifTrue {
            txt_title.visible()
            txt_title.text = it.title
        }

        it.message.isNotEmpty().ifTrue {
            txt_description.visible()
            txt_description.autoLinkMask = Linkify.PHONE_NUMBERS
            txt_description.movementMethod = LinkMovementMethod.getInstance()
//            txt_description.text = it.message.replace("\\n", System.lineSeparator())
            txt_description.text = it.message.replace("\\n", System.lineSeparator())
            txt_description.setLinkTextColor(ContextCompat.getColor(context!!, R.color.blue))
            Linkify.addLinks(txt_description, Linkify.WEB_URLS or Linkify.PHONE_NUMBERS)
        }

        it.imageUrl.isNotEmpty().ifTrue {
            thumbnail_popup.visible()
            thumbnail_popup.loadImageByUrl(it.imageUrl)
        }

        it.flagCheckBox.isNotEmpty().ifTrue {
            if (it.flagCheckBox.toLowerCase() == "y") {
                checkbox_ignore.visible()
            }
        }

        isShowButton?.ifTrue {
            btn_detail.visible()
        }
    }

    private fun initInstances() {
        item = arguments!!.getParcelable(NOTIFICATION_ITEM)!!
        setDataIntoView(item)

        form_quotation_popup_btn_close.bringToFront()
        form_quotation_popup_btn_close.setOnClickListener {
            AnalyticsManager.popupNotifyClose()
            checkbox_ignore.isChecked.ifTrue {
                AnalyticsManager.popupNotifyCheckbox()
                viewModel.sendAnswerBlockNotify(item.notifyType, item.sequenceId)
            }
            fragmentManager?.let { dismiss() }
            listenerActivity?.onCloseButtonClicked()
            listenerFragment?.onCloseButtonClicked()
        }

        btn_detail.setOnClickListener {
            AnalyticsManager.popupNotifyDetail(adName = item.title)
            checkbox_ignore.isChecked.ifTrue {
                AnalyticsManager.popupNotifyCheckbox()
                viewModel.sendAnswerBlockNotify(item.notifyType, item.sequenceId)
            }
            fragmentManager?.let { dismiss() }
            listenerActivity?.onDetailButtonClicked()
            listenerFragment?.onDetailButtonClicked()
        }
    }

    interface Listener {
        fun onDetailButtonClicked()
        fun onCloseButtonClicked()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listenerActivity = context as Listener
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }
    }

    companion object {
        val TAG = this::class.java.simpleName

        const val NOTIFICATION_ITEM = "itemNotificationData"
        const val IS_SHOW_BUTTTON = "isShowButton"

        fun newInstance() = NoticeDialogFragment()

        fun show(fragmentManager: FragmentManager, item: NotificationJsonResponse,
                 isShowButton: Boolean) {
            newInstance().apply {
                arguments = Bundle().apply {
                    putParcelable(NOTIFICATION_ITEM, item)
                    putBoolean(IS_SHOW_BUTTTON, isShowButton)
                }
                show(fragmentManager, TAG)
            }
        }

        fun show(fragmentManager: FragmentManager,
                 fragment: Fragment?,
                 item: NotificationJsonResponse,
                 isShowButton: Boolean) {
            newInstance().apply {
                arguments = Bundle().apply {
                    putParcelable(NOTIFICATION_ITEM, item)
                    putBoolean(IS_SHOW_BUTTTON, isShowButton)
                }

                setTargetFragment(fragment, 1)
                show(fragmentManager, TAG)
            }
        }
    }
}