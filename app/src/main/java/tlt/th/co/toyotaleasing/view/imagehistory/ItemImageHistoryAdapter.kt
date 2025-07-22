package tlt.th.co.toyotaleasing.view.imagehistory

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_gallery_view.view.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.extension.gone
import tlt.th.co.toyotaleasing.common.extension.loadImageByBase64

class ItemImageHistoryAdapter(private val imageList: List<String>) : RecyclerView.Adapter<ItemImageHistoryAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_gallery_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = imageList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding(imageList[position])
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun binding(item: String) {
            view.gallery_close_iv.gone()
            view.gallery_iv.loadImageByBase64(item)
        }
    }
}