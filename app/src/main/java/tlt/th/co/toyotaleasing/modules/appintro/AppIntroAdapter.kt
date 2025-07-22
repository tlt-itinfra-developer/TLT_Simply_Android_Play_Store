package tlt.th.co.toyotaleasing.modules.appintro

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_appintro.view.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.extension.loadImageByDrawableRes

class AppIntroAdapter(private val itemList: ArrayList<AppIntro>) : RecyclerView.Adapter<AppIntroAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_appintro, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: AppIntro) {
            itemView.txt_title.text = item.title
            itemView.txt_description.text = item.description
            itemView.bg_appintro.loadImageByDrawableRes(item.backgroundRes)
            itemView.ic_icon.loadImageByDrawableRes(item.iconRes)
        }
    }
}