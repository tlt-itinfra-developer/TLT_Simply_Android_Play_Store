package tlt.th.co.toyotaleasing.modules.faq.main

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_faq_main.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseFragment
import tlt.th.co.toyotaleasing.common.listener.OnHambergerClickListener
import tlt.th.co.toyotaleasing.manager.db.UserManager
import tlt.th.co.toyotaleasing.model.response.NotificationJsonResponse
import tlt.th.co.toyotaleasing.modules.faq.detail.FAQDetailActivity
import tlt.th.co.toyotaleasing.modules.home.customer.MainCustomerActivity
import tlt.th.co.toyotaleasing.modules.home.customer.NoticeDialogFragment
import tlt.th.co.toyotaleasing.modules.home.noncustomer.MainNonCustomerActivity
import tlt.th.co.toyotaleasing.view.DividerItemDecoration

class FAQFragment : BaseFragment(), FAQAdapter.Listener,
        NoticeDialogFragment.Listener {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(FAQViewModel::class.java)
    }

    private val topicId by lazy {
        activity?.intent
                ?.getBundleExtra(MainCustomerActivity.DATA_POSITION_EXTRA)
                ?.getString(NAV_EXTRA)
    }

    private val isShowDialog by lazy {
        activity?.intent
                ?.getBundleExtra(MainCustomerActivity.DATA_POSITION_EXTRA)
                ?.getBoolean(IS_SHOW_DIALOG_FROM_DEEPLINK_FAQ, false)
    }

    private val notifyItem by lazy {
        activity?.intent
                ?.getBundleExtra(MainCustomerActivity.DATA_POSITION_EXTRA)
                ?.getParcelable(DATA_BY_DEEPLINK_DIALOG) ?: NotificationJsonResponse()
    }

    private val onHambergerClickListener by lazy {
        context as OnHambergerClickListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_faq_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initInstances()

        viewModel.getData()

        topicId?.let {
            if (isShowDialog == true) {
                NoticeDialogFragment.show(
                        fragmentManager = fragmentManager!!,
                        fragment = this,
                        isShowButton = true,
                        item = notifyItem
                )
            } else {
                FAQDetailActivity.start(context, it)
            }
        }
    }

    private fun initViewModel() {
        viewModel.whenDataLoaded.observe(this, Observer {
            setupDataIntoViews(it)
        })
    }

    private fun setupDataIntoViews(it: FAQViewModel.Model?) {
        toolbar.setOnHambergerMenuClickListener {
            AnalyticsManager.faqMenuHome()
            onHambergerClickListener.onHambergerClick()
        }

        recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(ContextCompat.getDrawable(context, R.drawable.item_divider_recycler_view)))
            adapter = FAQAdapter(it?.items
                    ?: listOf(), this@FAQFragment)
        }
    }

    override fun onDetailButtonClicked() {
        AnalyticsManager.faqTopic()
        FAQDetailActivity.start(context, topicId!!)
    }

    override fun onCloseButtonClicked() {

    }

    private fun initInstances() {

    }

    override fun onTopicClick(index: Int, item: FAQViewModel.Item) {
        AnalyticsManager.faqTopic()
        FAQDetailActivity.start(context, item.id, item.title)
    }

    companion object {
        const val DATA_BY_DEEPLINK_DIALOG = "DATA_BY_DEEPLINK_DIALOG"
        const val NAV_EXTRA = "NAV_EXTRA"
        const val IS_SHOW_DIALOG_FROM_DEEPLINK_FAQ = "IS_SHOW_DIALOG_FROM_DEEPLINK_FAQ"

        fun newInstance() = FAQFragment()

        fun startByDeeplink(context: Context?,
                            topic: String,
                            item: NotificationJsonResponse,
                            isShowDialog: Boolean) {
            if (UserManager.getInstance().isCustomer()) {
                MainCustomerActivity.startWithClearStack(
                        context = context,
                        position = MainCustomerActivity.FAQ_MENU_POSITION,
                        data = Bundle().apply {
                            putParcelable(DATA_BY_DEEPLINK_DIALOG, item)
                            putString(NAV_EXTRA, topic)
                            putBoolean(IS_SHOW_DIALOG_FROM_DEEPLINK_FAQ, isShowDialog)
                        }
                )
            } else {
                MainNonCustomerActivity.openWithClearStack(
                        context = context,
                        position = MainCustomerActivity.FAQ_MENU_POSITION,
                        data = Bundle().apply {
                            putParcelable(DATA_BY_DEEPLINK_DIALOG, item)
                            putString(NAV_EXTRA, topic)
                            putBoolean(IS_SHOW_DIALOG_FROM_DEEPLINK_FAQ, isShowDialog)
                        }
                )
            }
        }
    }
}
