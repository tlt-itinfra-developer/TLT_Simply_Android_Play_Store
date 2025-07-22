package tlt.th.co.toyotaleasing.modules.paymenthistory

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_payment_history.view.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.common.extension.gone
import tlt.th.co.toyotaleasing.common.extension.invisible
import tlt.th.co.toyotaleasing.common.extension.toDatetime
import tlt.th.co.toyotaleasing.common.extension.visible

class PaymentHistoryAdapter(private val listener: Listener) : RecyclerView.Adapter<PaymentHistoryAdapter.ViewHolder>() {

    private val items = ArrayList<PaymentHistory>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_payment_history, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    fun updateItems(items: List<PaymentHistory>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind() {
            val item = items[adapterPosition]

            if (item.isExpanded) {
                expand()
            } else {
                collapse()
            }

            itemView.payment_total.text = itemView.context.getString(R.string.payment_history_total, item.total)
            itemView.payment_date.text = item.paymentDate.toDatetime()
            itemView.payment_bank.text = item.paymentBank

            if (item.paymentReceiptDate.isEmpty()) {
                itemView.payment_receipt_date.gone()
            } else {
                itemView.payment_receipt_date.visible()
                itemView.payment_receipt_date.text = itemView.context.getString(R.string.payment_history_receipt_date, item.paymentReceiptDate)
            }

            if (item.canDownloadReceipt) {
                itemView.payment_download.visible()
            } else {
                itemView.payment_download.invisible()
            }
        }

        private val onExpandOrCollapseClick = View.OnClickListener {
            val item = items[adapterPosition]

            if (!item.isExpanded) {
                expand(isAnimation = true)
            } else {
                collapse(isAnimation = true)
            }

            item.isExpanded = !item.isExpanded
        }

        private val onDownloadReceiptClick = View.OnClickListener {
            AnalyticsManager.historyDownloadReceipt()
            val item = items[adapterPosition]
            listener.onDownloadReceipt(adapterPosition)
        }

        private fun expand(isAnimation: Boolean = false) {
            AnalyticsManager.historyPaymentAmount()
            itemView.arrow_down.setImageResource(R.drawable.ic_arrow_up)
            itemView.expand_layout.visible()
        }

        private fun collapse(isAnimation: Boolean = false) {
            AnalyticsManager.historyPaymentAmount()
            itemView.arrow_down.setImageResource(R.drawable.ic_arrow_down)
            itemView.expand_layout.gone()
        }

        init {
            itemView.fake_button.setOnClickListener(onExpandOrCollapseClick)
            itemView.payment_download.setOnClickListener(onDownloadReceiptClick)
        }
    }

    interface Listener {
        fun onDownloadReceipt(position: Int)
    }
}