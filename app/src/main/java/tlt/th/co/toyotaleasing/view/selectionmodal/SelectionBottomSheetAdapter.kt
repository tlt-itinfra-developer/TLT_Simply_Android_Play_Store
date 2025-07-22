package tlt.th.co.toyotaleasing.view.selectionmodal

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_selection.view.*
import tlt.th.co.toyotaleasing.R

class SelectionBottomSheetAdapter(private val items: MutableList<Model> = mutableListOf(),
                                  private val onItemClickListener: (Int, Model) -> Unit) : RecyclerView.Adapter<SelectionBottomSheetAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_selection, parent, false)
        return ViewHolder(view, onItemClickListener)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(val view: View,
                           private val onItemClickListener: (Int, Model) -> Unit) : RecyclerView.ViewHolder(view) {
        fun bind(item: Model) {
            view.name.text = item.name

            if (item.isSelected && !item.isDisabled) {
                view.name.setTextColor(ContextCompat.getColor(view.context, R.color.colorPrimary))
            } else {
                view.name.setTextColor(ContextCompat.getColor(view.context, R.color.text_normal))
            }

            if (item.isDisabled) {
                view.layout.setBackgroundResource(R.color.text_normal_lighter)
            } else {
                view.layout.setBackgroundResource(R.color.white)
            }

            view.layout.setOnClickListener {
                onItemClickListener.invoke(adapterPosition, item)
            }
        }
    }

    data class Model(
            var name: String = "",
            var isSelected: Boolean = false,
            var isDisabled: Boolean = false
    )
}