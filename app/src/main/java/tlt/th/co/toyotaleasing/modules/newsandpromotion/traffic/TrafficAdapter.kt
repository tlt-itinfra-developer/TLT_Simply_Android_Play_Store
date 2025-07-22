package tlt.th.co.toyotaleasing.modules.newsandpromotion.traffic

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_js_hundred.view.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.extension.toTwitterDatetime
import tlt.th.co.toyotaleasing.model.response.GetUserTwitterTimelineResponse

class TrafficAdapter : RecyclerView.Adapter<TrafficAdapter.ViewHolder>() {

    private val items = ArrayList<GetUserTwitterTimelineResponse>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_js_hundred, parent, false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int = items.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun updateItems(items: ArrayList<GetUserTwitterTimelineResponse>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }


    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: GetUserTwitterTimelineResponse) {
            view.time_stamp.text = item.createdAt.toTwitterDatetime()
            view.detail.text = item.text
        }
    }
}