package tlt.th.co.toyotaleasing.modules.insurance.quotation

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_quotation_policy_dialog_fragment.view.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.model.entity.masterdata.InsPolicy

class QuotationDialogAdapter : RecyclerView.Adapter<QuotationDialogAdapter.ViewHolder>() {

    private val items = ArrayList<InsPolicy>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_quotation_policy_dialog_fragment, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun updateItems(items: List<InsPolicy>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: InsPolicy) {
            view.title_txt.text = item.getName()
            view.description_txt.text = item.getDescription()
        }
    }
}