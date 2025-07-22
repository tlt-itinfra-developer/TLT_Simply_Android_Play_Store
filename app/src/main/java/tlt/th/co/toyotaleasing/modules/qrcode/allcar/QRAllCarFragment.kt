package tlt.th.co.toyotaleasing.modules.qrcode.allcar

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.core.view.ViewCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_qrcode_all_car.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseFragment
import tlt.th.co.toyotaleasing.common.extension.ifTrue
import tlt.th.co.toyotaleasing.common.listener.OnHambergerClickListener
import tlt.th.co.toyotaleasing.modules.qrcode.detail.QRActivity
import tlt.th.co.toyotaleasing.util.VerticalSpaceItemDecoration

class QRAllCarFragment : BaseFragment(), QRAllCarAdapter.Listener {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(QRAllCarViewModel::class.java)
    }

    private val onHambergerClickListener by lazy {
        context as OnHambergerClickListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_qrcode_all_car, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initInstances()
        initViewModel()
    }

    private fun initInstances() {
        val spaceBetween = resources.getDimensionPixelSize(R.dimen.margin_normal)

        toolbar.setOnHambergerMenuClickListener {
            onHambergerClickListener.onHambergerClick()
        }

        recycler_view.apply {
            ViewCompat.setNestedScrollingEnabled(this, false)
            adapter = QRAllCarAdapter(listener = this@QRAllCarFragment)
            addItemDecoration(VerticalSpaceItemDecoration(spaceBetween))
        }
    }

    private fun initViewModel() {
        viewModel.whenDataLoaded.observe(this, Observer {
            it?.let {
                val adapter = recycler_view.adapter as QRAllCarAdapter
                adapter.updateItems(it?.list ?: mutableListOf())

                it.isStaffApp.ifTrue { supportForStaff(it) }
            }
        })
    }

    private fun supportForStaff(it: QRAllCarViewModel.Model) {

    }

    override fun onInstallmentClick(position: Int, item: QRAllCarViewModel.Car) {
        viewModel.saveCar(position)
        QRActivity.start(this, QRActivity.QR_ALLCAR_REQUEST_CODE, QRActivity.TransactionType.INSTALLMENT)
    }

    override fun onTaxClick(position: Int, item: QRAllCarViewModel.Car) {
        viewModel.saveCar(position)
        QRActivity.start(this, QRActivity.QR_ALLCAR_REQUEST_CODE, QRActivity.TransactionType.TAX)
    }

    override fun onInsuranceClick(position: Int, item: QRAllCarViewModel.Car) {
        viewModel.saveCar(position)
        QRActivity.start(this, QRActivity.QR_ALLCAR_REQUEST_CODE, QRActivity.TransactionType.INSURANCE)
    }

    override fun onOthersClick(position: Int, item: QRAllCarViewModel.Car) {
        viewModel.saveCar(position)
        QRActivity.start(this, QRActivity.QR_ALLCAR_REQUEST_CODE, QRActivity.TransactionType.OTHER)
    }

    companion object {
        fun newInstance() = QRAllCarFragment()
    }
}