package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.center.common

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_loan_guildline_list.view.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.carloan.GuidlineRequireDocViewModel


class GuildlineRequireAdapter (private val items: List<GuidlineRequireDocViewModel.Item> ) : RecyclerView.Adapter<GuildlineRequireAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_loan_guildline_list, parent, false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            val item = items[adapterPosition]

            itemView.txt_detail.text = item.detail
        }


    }


}