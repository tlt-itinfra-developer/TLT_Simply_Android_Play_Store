package tlt.th.co.toyotaleasing.view.imagehistory

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_image_history.view.*
import tlt.th.co.toyotaleasing.R

class ImageHistoriesAdapter(private val historyList: List<ImageHistoriesWidget.History>) : RecyclerView.Adapter<ImageHistoriesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image_history, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = historyList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding(historyList[position])
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun binding(item: ImageHistoriesWidget.History) {
            view.imageHistory.setData(item)
        }
    }
}