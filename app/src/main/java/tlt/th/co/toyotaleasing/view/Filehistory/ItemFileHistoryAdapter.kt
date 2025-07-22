package tlt.th.co.toyotaleasing.view.imagehistory

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_file_gallery.view.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.extension.gone
import tlt.th.co.toyotaleasing.common.extension.loadImageByBase64
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.carloan.common.ItemFilesData

class ItemFileHistoryAdapter(private val imageList: List<ItemFilesData>) : RecyclerView.Adapter<ItemFileHistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_file_gallery, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = imageList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding(imageList.get(position))
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun binding(item: ItemFilesData) {
            view.gallery_close_iv.gone()
            view.gallery_iv.loadImageByBase64(item.fBase64)
            if(item.fName != "")
                view.txt_name.visibility = View.VISIBLE
            else
                view.txt_name.visibility = View.GONE
            view.txt_name.text = item.fName
        }
    }
}