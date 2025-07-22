package tlt.th.co.toyotaleasing.modules.noncustomer

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
import kotlinx.android.synthetic.main.fragment_notify_non_customer.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseFragment
import tlt.th.co.toyotaleasing.common.eventbus.UpdateBadgeNotificationEvent
import tlt.th.co.toyotaleasing.common.extension.getTypeNotification
import tlt.th.co.toyotaleasing.common.listener.OnHambergerClickListener
import tlt.th.co.toyotaleasing.manager.BusManager

import tlt.th.co.toyotaleasing.manager.db.UserManager
import tlt.th.co.toyotaleasing.model.response.NotificationJsonResponse
import tlt.th.co.toyotaleasing.modules.home.customer.NoticeDialogFragment
import tlt.th.co.toyotaleasing.modules.notify.Notify
import tlt.th.co.toyotaleasing.modules.notify.NotifyAdapter
import tlt.th.co.toyotaleasing.modules.notify.NotifyFragment
import tlt.th.co.toyotaleasing.modules.notify.NotifyViewModel
import tlt.th.co.toyotaleasing.util.NotificationConstants

class NotifyNonCustomerFragment : BaseFragment(),
        NoticeDialogFragment.Listener {

    private var navigation: String = ""

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(NotifyViewModel::class.java)
    }

    private val onHambergerClickListener by lazy {
        context as OnHambergerClickListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_notify_non_customer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initInstance()
        initViewModel()

        viewModel.getNotifyList()
    }

    private fun initViewModel() {
        viewModel.whenGetNotifyList.observe(this, Observer {
            swipe_refresh.isRefreshing = false
            val adapter = recycler_view.adapter as NotifyAdapter
            adapter.updateItems(it!!)
        })
    }

    private fun initInstance() {
        toolbar.setOnHambergerMenuClickListener {
            onHambergerClickListener.onHambergerClick()
        }

        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.adapter = NotifyAdapter(onNotifyListener)

        swipe_refresh.setOnRefreshListener {
            viewModel.getNotifyList()
        }
    }

    private val onNotifyListener = object : NotifyAdapter.Listener {
        override fun onRemoveClicked(notify: Notify) {
            //DatabaseManager.getInstance().deleteNotifyByKey(notify.id)
            viewModel.getNotifyList()
        }

        override fun onSeemoreClicked(notify: Notify) {
            navigation = notify.navigation
            showNotifyPopup(notify.navigation)
        }
    }

    private fun showNotifyPopup(navigation: String) {
        when (navigation.getTypeNotification()) {
            NotificationConstants.FAQ_PAGE -> {
                NoticeDialogFragment.show(
                    fragmentManager = fragmentManager!!,
                        fragment = this@NotifyNonCustomerFragment,
                        isShowButton = true,
                        item = getNotifyJsonDialog(navigation)
                )
            }
            NotificationConstants.MYCAR_PAGE -> {
                if (navigation.contains("nav=contract&action=refinance")) {
                    NoticeDialogFragment.show(
                    fragmentManager = fragmentManager!!,
                            fragment = this@NotifyNonCustomerFragment,
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
                        fragment = this@NotifyNonCustomerFragment,
                        isShowButton = false,
                        item = getNotifyJsonDialog(navigation)
                )
            }
            else -> {
                NoticeDialogFragment.show(
                    fragmentManager = fragmentManager!!,
                        fragment = this@NotifyNonCustomerFragment,
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
        val dataForSplit = data.replace("jsonString=", NotifyFragment.CHAR_FOR_SPLIT_TEXT)
        val dataSplited = dataForSplit.split(NotifyFragment.CHAR_FOR_SPLIT_TEXT)
        val jsonString = Gson().fromJson(dataSplited[1], NotificationJsonResponse::class.java)
        return jsonString
    }

    companion object {
        fun newInstance() = NotifyNonCustomerFragment()
    }
}