package tlt.th.co.toyotaleasing.modules.ownership.common


import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_ownership_tracking_list.view.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.common.extension.gone
import tlt.th.co.toyotaleasing.common.extension.visible
import tlt.th.co.toyotaleasing.modules.faq.detail.FAQDetailViewModel
import tlt.th.co.toyotaleasing.modules.ownership.OwnershipTransferTrackingFragment
import tlt.th.co.toyotaleasing.modules.ownership.OwnershipTransferTrackingViewModel
import java.util.ArrayList

class TOTrackingAdapter (private val items: ArrayList<OwnershipTransferTrackingViewModel.StepInfoModel> ,
                         private val listener: Listener) : RecyclerView.Adapter<TOTrackingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ownership_tracking_list, parent, false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            val item = items[adapterPosition]

            itemView.text_desc.text = item.textDesc
            itemView.txt_datetime.text = item.dateTime

            itemView.txt_issue.text =  item.issueDesc.replace("\\n", System.lineSeparator())

            if(item.textColor == "Red"){
                itemView.text_desc.setTextColor(ContextCompat.getColor(itemView.context, R.color.cherry_red))
                itemView.txt_issue.setTextColor(ContextCompat.getColor(itemView.context, R.color.cherry_red))
            }else if(item.textColor == "Green"){
                itemView.text_desc.setTextColor(ContextCompat.getColor(itemView.context, R.color.Green))
                itemView.txt_issue.setTextColor(ContextCompat.getColor(itemView.context, R.color.Green))
            }else if(item.textColor == "Gray"){
                itemView.text_desc.setTextColor(ContextCompat.getColor(itemView.context, R.color.grey_3))
                itemView.txt_issue.setTextColor(ContextCompat.getColor(itemView.context, R.color.grey_3))
            }else {
                itemView.text_desc.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
                itemView.txt_issue.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
            }

            if(item.imgType == "correct"){
                 if(item.imgColor == "Green"){
                    itemView.view_type.background = ContextCompat.getDrawable(itemView.context, R.drawable.ic_green_correct)
                }else if(item.imgColor == "Gray"){
                    itemView.view_type.background = ContextCompat.getDrawable(itemView.context, R.drawable.ic_gray_correct)
                }
            }else if(item.imgType == "issue"){
                if(item.imgColor == "Red"){
                    itemView.view_type.background = ContextCompat.getDrawable(itemView.context, R.drawable.ic_red_issue)
                }else{
                    itemView.view_type.background = ContextCompat.getDrawable(itemView.context, R.drawable.ic_gray_issue)
                }
            }else{
                if(item.imgColor == "Green"){
                    itemView.view_type.background = ContextCompat.getDrawable(itemView.context, R.drawable.ic_green_waiting)
                }else{
                    itemView.view_type.background = ContextCompat.getDrawable(itemView.context, R.drawable.ic_gray_waiting)
                }
            }

            if(item.carImg == "Y"){
                itemView.view_car.visible()
            }else{
                itemView.view_car.gone()
            }

            if(item.documentDownload!= "" && item.documentDownload!= null ){
                itemView.img_download.visibility = View.VISIBLE
                itemView.file_download.visibility = View.VISIBLE
                itemView.file_download.text = item.desLoad ?:itemView.context.getString(R.string.ownership_pdf_download)
            }else{
                itemView.img_download.visibility = View.GONE
                itemView.file_download.visibility = View.GONE
                itemView.file_download.text = itemView.context.getString(R.string.ownership_pdf_download)
            }

            if(item.issueDesc !=""){
                itemView.txt_issue.visibility = View.VISIBLE
            }else{
                itemView.txt_issue.visibility = View.GONE
            }
        }

        private val onDownloadClick = View.OnClickListener {
            val item = items[adapterPosition]
            listener.onDownloadClick(item)
        }


        init {
//            itemView.file_download.setOnClickListener(onDownloadClick)
            itemView.img_download.setOnClickListener(onDownloadClick)
        }
    }

    interface Listener {
        fun onDownloadClick( item : OwnershipTransferTrackingViewModel.StepInfoModel )
    }
}