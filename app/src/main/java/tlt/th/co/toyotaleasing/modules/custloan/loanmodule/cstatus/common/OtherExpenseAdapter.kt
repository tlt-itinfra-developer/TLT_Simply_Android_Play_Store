package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.cstatus.common


import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_other_expense.view.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.custinfo.CInfoCreditResultViewModel

class OtherExpenseAdapter(private val items: List<OtherExpense>) : RecyclerView.Adapter<OtherExpenseAdapter.OtherExpenseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  OtherExpenseViewHolder  {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_other_expense, parent, false)
        return OtherExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: OtherExpenseViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = items.size


    inner class OtherExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            val item = items[adapterPosition]
            //itemView.txt_title.text = item.offerId
            itemView.txt_name.text = item.itemName
            itemView.txt_amt.text = item.itemAmt
        }

    }

    interface Listener {
        fun onListClick(index: Int, item: CInfoCreditResultViewModel.dOffer)
    }


}
