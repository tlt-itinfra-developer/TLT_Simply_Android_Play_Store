package tlt.th.co.toyotaleasing.modules.filtercar

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_filter_car.view.*

import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.extension.ifFalse
import tlt.th.co.toyotaleasing.common.extension.ifTrue

class FilterCarAdapter(private val itemList: ArrayList<FilterCar>,
                       private val listener: OnInteractionListener) : RecyclerView.Adapter<FilterCarAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_filter_car, parent, false)
        return ViewHolder(view, listener)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    fun addItems(items: List<FilterCar>) {
        itemList.addAll(items)
        notifyDataSetChanged()
    }

    class ViewHolder(private val view: View,
                     private val listener: OnInteractionListener) : RecyclerView.ViewHolder(view) {
        fun bind(item: FilterCar) {
            view.txt_license.text = item.license
            view.txt_province.text = item.province

            item.isSelected.ifTrue {
                view.layout_constraint.setBackgroundResource(R.drawable.shape_filter_car_selected)
            }

            item.isSelected.ifFalse {
                view.layout_constraint.setBackgroundResource(R.drawable.shape_filter_car_unselect)
            }

            view.layout_item.setOnClickListener { listener.onClick(item) }
        }
    }

    interface OnInteractionListener {
        fun onClick(item: FilterCar)
    }
}