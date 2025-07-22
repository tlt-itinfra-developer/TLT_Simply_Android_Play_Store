package tlt.th.co.toyotaleasing.modules.installmentdetail

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_installment_detail_debt_header.view.*
import kotlinx.android.synthetic.main.item_installment_detail_debt_normal.view.*
import tlt.th.co.toyotaleasing.R

class DebtAdapter(private val itemList: ArrayList<InstallmentDetailViewModel.Model.Debt>) : RecyclerView.Adapter<DebtAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            Type.HEADER -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_installment_detail_debt_header, parent, false)
                ViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_installment_detail_debt_normal, parent, false)
                ViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int = itemList.size

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            Type.HEADER
        } else {
            Type.NORMAL
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position], getItemViewType(position))
    }

    fun addItems(items: List<InstallmentDetailViewModel.Model.Debt>) {
        itemList.addAll(items)
        notifyDataSetChanged()
    }

    object Type {
        const val HEADER = 1
        const val NORMAL = 2
    }

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: InstallmentDetailViewModel.Model.Debt, type: Int) {
            val detail = item.title
            val price = view.context.getString(R.string.installment_detail_currency, item.price)

            if (type == Type.HEADER) {
                view.title_behind_in_payment_header.text = detail
                view.txt_behind_in_payment_header.text = price
            } else {
                view.title_behind_in_payment_normal.text = detail
                view.txt_behind_in_payment_normal.text = price
            }
        }
    }
}