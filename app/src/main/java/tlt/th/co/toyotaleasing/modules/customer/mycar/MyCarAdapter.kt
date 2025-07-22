package tlt.th.co.toyotaleasing.modules.customer.mycar

import android.animation.ValueAnimator
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_my_car.view.*
import kotlinx.android.synthetic.main.layout_mycar_status_legal_other.view.*
import kotlinx.android.synthetic.main.layout_mycar_status_normal.view.*
import kotlinx.android.synthetic.main.layout_mycar_status_updating.view.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.common.extension.*

class MyCarAdapter(private val itemList: ArrayList<MyCar>,
                   private val onCarContractClick: (mycar: MyCar) -> Unit,
                   private val onPeriodDetailClick: (mycar: MyCar) -> Unit,
                   private val onPaymentClick: (mycar: MyCar) -> Unit) : RecyclerView.Adapter<MyCarAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_my_car, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    fun addData(items: List<MyCar>) {
        itemList.clear()
        itemList.addAll(items)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind() {
            val item = itemList[adapterPosition]

            itemView.txt_car_license.text = item.license
            itemView.txt_installment_date.text = item.currentDate
            showCarProgess(item)
            itemView.txt_full_payment_installment.text = view.context.getString(R.string.my_car_txt_period, item.paidTerm)
            itemView.txt_balance_installment.text = view.context.getString(R.string.my_car_txt_period, item.remainTerm)
            itemView.txt_period_payment.text = view.context.getString(R.string.my_car_txt_period_payment, item.periodPayment)
            itemView.txt_total_payment.setTextWithHtml(view.context.getString(R.string.my_car_txt_total_payment,
                    item.totalPayment.toNumberFormat(),
                    ContextCompat.getColor(view.context, R.color.terracotta_200).toString()
            ))
            itemView.txt_contract_number.text = view.context.getString(R.string.my_car_txt_contract_number, item.contractNumber)

            if (item.carImageBase64.isNotEmpty()) {
                itemView.img_car.loadImageByBase64(item.carImageBase64)
            } else {
                itemView.img_car.loadImageByUrl(item.defaultCarImageUrl)
            }

            if ( item.totalPayment.toNumberFormat() == "0.00") {
                itemView.btn_check_payment.isEnabled = false
            }

            when (item.status) {
                MyCar.Status.NORMAL -> showNormalStatus(item.flagPayProcess)
                MyCar.Status.UPDATING -> showUpdating()
                MyCar.Status.CLOSE -> showCloseStatus()
                MyCar.Status.OTHER -> showLegalAndOtherStatus()
                MyCar.Status.LEGAL -> showLegalAndOtherStatus()
                MyCar.Status.DIRECT_DEBIT -> showDirectDebitStatus()
            }

            item.isStaffApp.ifTrue { supportForStaff(item) }
        }

        private fun supportForStaff(item: MyCar) {

        }

        private fun showCarProgess(item: MyCar) {
            val progress = try {
                ((item.paidTerm.toInt() * 100) / item.totalTerm.toInt()) / 100f
            } catch (e: ArithmeticException) {
                0f
            }

            val animator = ValueAnimator
                    .ofFloat(0f, progress)
                    .setDuration(2000)

            animator.addUpdateListener { animation ->
                itemView.progress_installment.progress = animation.animatedValue as Float
            }

            animator.start()
        }

        private fun showNormalStatus(payProcess: String) {
            itemView.progress_installment.visible()
            itemView.txt_full_payment.visible()
            itemView.txt_full_payment_installment.visible()
            itemView.txt_balance.visible()
            itemView.txt_balance_installment.visible()
            itemView.divider.visible()

            itemView.layout_mycar_status_normal.visible()
            itemView.layout_mycar_status_updating.gone()
            itemView.layout_mycar_status_close.gone()
            itemView.layout_mycar_status_legal_other.gone()
            itemView.layout_mycar_status_direct_debit.gone()

            itemView.btn_check_payment.isEnabled = payProcess != "Y"
        }

        private fun showDirectDebitStatus() {
            itemView.progress_installment.visible()
            itemView.txt_full_payment.visible()
            itemView.txt_full_payment_installment.visible()
            itemView.txt_balance.visible()
            itemView.txt_balance_installment.visible()
            itemView.divider.visible()

            itemView.layout_mycar_status_normal.gone()
            itemView.layout_mycar_status_updating.gone()
            itemView.layout_mycar_status_close.gone()
            itemView.layout_mycar_status_legal_other.gone()
            itemView.layout_mycar_status_direct_debit.visible()
        }

        private fun showUpdating() {
            itemView.progress_installment.visible()
            itemView.txt_full_payment.visible()
            itemView.txt_full_payment_installment.visible()
            itemView.txt_balance.visible()
            itemView.txt_balance_installment.visible()
            itemView.divider.visible()

            itemView.layout_mycar_status_normal.gone()
            itemView.layout_mycar_status_updating.visible()
            itemView.layout_mycar_status_close.gone()
            itemView.layout_mycar_status_legal_other.gone()
            itemView.layout_mycar_status_direct_debit.gone()

            itemView.btn_check_unpayment.isEnabled = false
        }

        private fun showCloseStatus() {
            itemView.progress_installment.visible()
            itemView.txt_full_payment.visible()
            itemView.txt_full_payment_installment.visible()
            itemView.txt_balance.visible()
            itemView.txt_balance_installment.visible()
            itemView.divider.visible()

            itemView.layout_mycar_status_normal.gone()
            itemView.layout_mycar_status_updating.gone()
            itemView.layout_mycar_status_close.visible()
            itemView.layout_mycar_status_legal_other.gone()
            itemView.layout_mycar_status_direct_debit.gone()
        }

        private fun showLegalAndOtherStatus() {
            itemView.txt_legal_description.movementMethod = LinkMovementMethod.getInstance()
            itemView.txt_legal_description.setOnClickListener {
                AnalyticsManager.installmentMyCarCallClicked()
            }

            itemView.progress_installment.gone()
            itemView.txt_full_payment.gone()
            itemView.txt_full_payment_installment.gone()
            itemView.txt_balance.gone()
            itemView.txt_balance_installment.gone()
            itemView.divider.gone()

            itemView.layout_mycar_status_normal.gone()
            itemView.layout_mycar_status_updating.gone()
            itemView.layout_mycar_status_close.gone()
            itemView.layout_mycar_status_legal_other.visible()
            itemView.layout_mycar_status_direct_debit.gone()
        }

        private val onCarContractClickListener = View.OnClickListener {
            val item = itemList[adapterPosition]
            onCarContractClick.invoke(item)
        }

        private val onPeriodDetailClickListener = View.OnClickListener {
            val item = itemList[adapterPosition]
            onPeriodDetailClick.invoke(item)
        }

        private val onPaymentClickListener = View.OnClickListener {
            val item = itemList[adapterPosition]
            onPaymentClick.invoke(item)
        }

        init {
            itemView.btn_home_contract_detail.setOnClickListener(onCarContractClickListener)
            itemView.btn_home_installment_detail.setOnClickListener(onPeriodDetailClickListener)
            itemView.btn_check_payment.setOnClickListener(onPaymentClickListener)
        }
    }
}