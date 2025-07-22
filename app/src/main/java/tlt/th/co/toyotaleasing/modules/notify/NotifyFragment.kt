package tlt.th.co.toyotaleasing.modules.notify

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_notify.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.common.base.BaseFragment
import tlt.th.co.toyotaleasing.common.eventbus.NotificationInstallmentTypeEvent
import tlt.th.co.toyotaleasing.common.eventbus.NotifyEvent
import tlt.th.co.toyotaleasing.common.eventbus.UpdateBadgeNotificationEvent
import tlt.th.co.toyotaleasing.common.extension.getTypeNotification
import tlt.th.co.toyotaleasing.common.extension.gone
import tlt.th.co.toyotaleasing.common.extension.visible
import tlt.th.co.toyotaleasing.manager.BusManager
import tlt.th.co.toyotaleasing.manager.LocalizeManager

import tlt.th.co.toyotaleasing.manager.db.UserManager
import tlt.th.co.toyotaleasing.model.response.NotificationJsonResponse
import tlt.th.co.toyotaleasing.modules.home.customer.NoticeDialogFragment
import tlt.th.co.toyotaleasing.util.NotificationConstants

class NotifyFragment : BaseFragment(), NoticeDialogFragment.Listener {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(NotifyViewModel::class.java)
    }

    private var yearFilter: String = ""

    private var navigation: String = ""

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_notify, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        EventBus.getDefault().register(this)

        initViewModel()
        initInstances()
    }

    override fun onDestroyView() {
        EventBus.getDefault().unregister(this)
        super.onDestroyView()
    }

    @Subscribe
    fun onPushNotifyEvent(event: NotifyEvent) {
        viewModel.getNotifyList()
    }

    private fun initViewModel() {
        viewModel.whenLoading.observe(this, Observer {

        })

        viewModel.whenDataLoaded.observe(this, Observer {
            setupDataIntoViews(it!!)
        })

        viewModel.whenGetNotifyList.observe(this, Observer {
            swipe_refresh.isRefreshing = false
            if (it!!.isNotEmpty()) {
                text_error.gone()
                recycler_view.visible()
                val adapter = recycler_view.adapter as NotifyAdapter
                adapter.updateItems(it)
            } else {
                recycler_view.gone()
                text_error.visible()
            }
        })
    }

    private fun initInstances() {
        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.adapter = NotifyAdapter(onNotifyListener)

        swipe_refresh.setOnRefreshListener {
            viewModel.getNotifyList(yearFilter)
        }
    }

    private fun setupDataIntoViews(item: NotifyViewModel.Model) {
        title.text = item.carLicense
        txt_car_license.text = item.currentDate

        btn_filter.setOnClickListener {
            AnalyticsManager.notificationFilter()
            FilterDialogFragment.show(
                    fragmentManager!!,
                    getString(R.string.notify_filter_dialog_title),
                    item.filterList,
                    onResultFilterListener
            )
        }
    }

    private val onResultFilterListener = object : FilterDialogFragment.Listener {
        override fun onResult(index: Int, selectContent: String) {

            AnalyticsManager.notificationFilterEnter()
            AnalyticsManager.notificationFilterFilter()

            if (selectContent == "ทั้งหมด" || selectContent == "All") {
                yearFilter = ""
            } else {
                yearFilter = if (LocalizeManager.isThai()) {
                    (selectContent.toInt() - 543).toString()
                } else {
                    selectContent
                }
            }
            txt_filter.text = selectContent
            viewModel.getNotifyList(yearFilter)
        }
    }

    private val onNotifyListener = object : NotifyAdapter.Listener {
        override fun onRemoveClicked(notify: Notify) {
            //DatabaseManager.getInstance().deleteNotifyByKey(notify.id)
            viewModel.getNotifyList(yearFilter)
        }

        override fun onSeemoreClicked(notify: Notify) {
            AnalyticsManager.notificationDetial()
            navigation = notify.navigation
            showNotifyPopup(notify.navigation)
        }
    }

    private fun showNotifyPopup(navigation: String) {
        when (navigation.getTypeNotification()) {
            NotificationConstants.FAQ_PAGE -> {
                NoticeDialogFragment.show(
                    fragmentManager = fragmentManager!!,
                        fragment = this@NotifyFragment,
                        isShowButton = true,
                        item = getNotifyJsonDialog(navigation)
                )
            }
            NotificationConstants.MYCAR_PAGE -> {
                if (navigation.contains("nav=contract&action=refinance")) {
                    NoticeDialogFragment.show(
                    fragmentManager = fragmentManager!!,
                            fragment = this@NotifyFragment,
                            item = getNotifyJsonDialog(navigation),
                            isShowButton = true
                    )
                } else {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(navigation)
                    }
                    startActivity(intent)
                }
            }
            NotificationConstants.TAX_PAGE -> {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(navigation)
                }
                startActivity(intent)
            }
            NotificationConstants.INSURANCE_PAGE -> {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(navigation)
                }
                startActivity(intent)
            }
            NotificationConstants.TIB_PAGE -> {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(navigation)
                }
                startActivity(intent)
            }
            NotificationConstants.ETC -> {
                NoticeDialogFragment.show(
                    fragmentManager = fragmentManager!!,
                        fragment = this@NotifyFragment,
                        isShowButton = false,
                        item = getNotifyJsonDialog(navigation)
                )
            }
            else -> {
                NoticeDialogFragment.show(
                    fragmentManager = fragmentManager!!,
                        fragment = this@NotifyFragment,
                        isShowButton = false,
                        item = getNotifyJsonDialog(navigation)
                )
            }
        }
    }

    override fun onDetailButtonClicked() {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(navigation)
        }
        startActivity(intent)
    }

    override fun onCloseButtonClicked() {}

    private fun getNotifyJsonDialog(data: String): NotificationJsonResponse {
        val dataForSplit = data.replace("jsonString=", CHAR_FOR_SPLIT_TEXT)
        val dataSplited = dataForSplit.split(CHAR_FOR_SPLIT_TEXT)
        val jsonString = Gson().fromJson(dataSplited[1], NotificationJsonResponse::class.java)
        return jsonString
    }

    companion object {
        const val CHAR_FOR_SPLIT_TEXT = "CHAR_FOR_SPLIT_TEXT"

        fun newInstance() = NotifyFragment()
    }
}
