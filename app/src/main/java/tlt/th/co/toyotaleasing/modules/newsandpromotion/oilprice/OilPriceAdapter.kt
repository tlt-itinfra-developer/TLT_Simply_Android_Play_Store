package tlt.th.co.toyotaleasing.modules.newsandpromotion.oilprice

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_oil_price_layout.view.*
import tlt.th.co.toyotaleasing.R

class OilPriceAdapter : RecyclerView.Adapter<OilPriceAdapter.ViewHolder>() {

    private val items = ArrayList<OilPrice>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_oil_price_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun updateItem(items: List<OilPrice>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }


    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: OilPrice) {
            view.price.text = item.price

            Glide.with(view.context)
                    .load(item.image)
                    .into(view.image)
        }

    }
}