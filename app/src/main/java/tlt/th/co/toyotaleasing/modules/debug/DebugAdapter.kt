package tlt.th.co.toyotaleasing.modules.debug

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_debug.view.*

import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.model.response.PassportResponse

class DebugAdapter(private val itemList: List<PassportResponse>,
                   private val listener: OnInteractionListener) : RecyclerView.Adapter<DebugAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_debug, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind() {
            val item = itemList[adapterPosition]

            view.txt_popup.text = item.name
            view.layout_item.setOnClickListener { listener.onClick(adapterPosition) }
        }
    }

    interface OnInteractionListener {
        fun onClick(position: Int)
    }
}