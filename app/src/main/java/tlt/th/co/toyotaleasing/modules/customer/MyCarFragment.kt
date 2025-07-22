package tlt.th.co.toyotaleasing.modules.customer

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_my_car.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseFragment
import tlt.th.co.toyotaleasing.common.eventbus.NotificationInstallmentSelectCarEvent
import tlt.th.co.toyotaleasing.manager.BusManager
import tlt.th.co.toyotaleasing.modules.contract.ContractDetailActivity
import tlt.th.co.toyotaleasing.modules.customer.mycar.MyCar
import tlt.th.co.toyotaleasing.modules.customer.mycar.MyCarAdapter
import tlt.th.co.toyotaleasing.modules.customer.mycar.MyCarViewModel
import tlt.th.co.toyotaleasing.modules.home.customer.MainCustomerActivity
import tlt.th.co.toyotaleasing.modules.home.customer.NoticeDialogFragment
import tlt.th.co.toyotaleasing.modules.installment.CarInstallmentViewModel
import tlt.th.co.toyotaleasing.modules.installmentdetail.InstallmentDetailActivity
import tlt.th.co.toyotaleasing.modules.payment.cart.installment.CartInstallmentActivity

class MyCarFragment : BaseFragment(), NoticeDialogFragment.Listener {

    private lateinit var contractNoExtra: String
    private var currentPosition = 0

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(MyCarViewModel::class.java)
    }

    private val installmentViewModel by lazy {
        ViewModelProviders.of(this).get(CarInstallmentViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_my_car, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initInstances()

        viewModel.getCarList()
    }

    private fun initViewModel() {
        installmentViewModel.whenLoading.observe(this, Observer {
            swipe_refresh.isRefreshing = true
        })

        installmentViewModel.whenGetCarListLoaded.observe(this, Observer {
            viewModel.getCarList()
        })

        viewModel.whenDataNotFound.observe(this, Observer {
            swipe_refresh.isRefreshing = false
        })

        viewModel.whenNoInternetConnection.observe(this, Observer {
            swipe_refresh.isRefreshing = false
        })

        viewModel.whenGetCarList.observe(this, Observer {
            swipe_refresh.isRefreshing = false

            val adapter = recycler_view.adapter as MyCarAdapter
            adapter.addData(it!!)
            selectDefaultCar()
            recycler_view.scrollToPosition(currentPosition)
        })
    }

    private fun initInstances() {
        contractNoExtra = activity?.intent
                ?.getBundleExtra(MainCustomerActivity.DATA_POSITION_EXTRA)
                ?.getString(CONTRACT_NO_EXTRA, "") ?: ""

        recycler_view.apply {
            LinearSnapHelper().attachToRecyclerView(this)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = MyCarAdapter(ArrayList(), onCarContractClick, onPeriodDetailClick, onPaymentClick)
            //indicator.attachToRecyclerView(this)
        }

        recycler_view.addOnPageChangedListener { i, position ->
            currentPosition = position
        }

        swipe_refresh.setOnRefreshListener {
            installmentViewModel.getCarList()
        }
    }

    private fun selectDefaultCar(contractNo: String = "") {
        val position = viewModel.getPositionDefaultCar(contractNo)
        recycler_view.scrollToPosition(position)
    }

    private fun saveCurrentMyCar() {
        val currentPosition = recycler_view.currentPosition

        viewModel.getCarByPosition(currentPosition)?.let {
            viewModel.selectCar(it)
        }
    }

    private val onCarContractClick = { mycar: MyCar ->
        AnalyticsManager.installmentMyCarMyContractClicked()
        viewModel.selectCar(mycar)
        ContractDetailActivity.start(context = context,
                contractStatus = mycar.contractStatusDesc)
    }


    private val onPeriodDetailClick = { mycar: MyCar ->
        AnalyticsManager.installmentMyCarInstallmentDetailClicked()
        viewModel.selectCar(mycar)
        InstallmentDetailActivity.open(context!!, mycar.flagPayProcess)
    }

    private val onPaymentClick = { mycar: MyCar ->
        AnalyticsManager.installmentMyCarCheckingPaymentClicked()
        viewModel.selectCar(mycar)
        CartInstallmentActivity.start(context)
    }

    override fun onDetailButtonClicked() {

    }

    override fun onCloseButtonClicked() {

    }

    companion object {
        private const val CONTRACT_NO_EXTRA = "CONTRACT_NO_EXTRA"
        private const val IS_SHOW_DIALOG_FROM_DEEPLINK_MYCAR = "IS_SHOW_DIALOG_FROM_DEEPLINK_MYCAR"

        fun newInstance() = MyCarFragment()

        fun startByDeeplink(context: Context?,
                            nav: String,
                            action: String,
                            contractNo: String) {
            if (contractNo.isNotEmpty()) {
                MainCustomerActivity.startWithClearStack(
                        context = context,
                        position = MainCustomerActivity.INSTALLMENT_MENU_POSITION,
                        data = Bundle().apply {
                            putString(CONTRACT_NO_EXTRA, contractNo)
                        }
                )
            }

            if (nav == "contract" && action == "refinance") {
                ContractDetailActivity.start(
                        context = context,
                        contractNumber = contractNo,
                        position = ContractDetailActivity.REFINANCE_POSITION
                )
            }
        }
    }
}
