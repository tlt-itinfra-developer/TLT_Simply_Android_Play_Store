package tlt.th.co.toyotaleasing.modules.faq.detail

import android.text.util.Linkify
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_news_detail.*
import kotlinx.android.synthetic.main.item_faq_detail.view.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.extension.gone
import tlt.th.co.toyotaleasing.common.extension.ifFalse
import tlt.th.co.toyotaleasing.common.extension.ifTrue
import tlt.th.co.toyotaleasing.common.extension.visible

class FAQDetailAdapter(private val items: List<FAQDetailViewModel.Item>,
                       private val listener: Listener) : RecyclerView.Adapter<FAQDetailAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_faq_detail, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind() {
            val item = items[adapterPosition]

            itemView.txt_title.text = item.title
            itemView.txt_detail.setLinkTextColor(ContextCompat.getColor(itemView.context, R.color.blue))
            itemView.txt_detail.text = item.detail
            Linkify.addLinks(itemView.txt_detail, Linkify.WEB_URLS or Linkify.PHONE_NUMBERS)
        }

        private val onDetailClick = View.OnClickListener {
            val item = items[adapterPosition].apply {
                this.isExpand = !this.isExpand
            }

            item.isExpand.ifTrue {
                itemView.img_arrow.setImageResource(R.drawable.ic_angle_down_green)
                itemView.txt_mark.visible()
                itemView.txt_detail.visible()
            }

            item.isExpand.ifFalse {
                itemView.img_arrow.setImageResource(R.drawable.ic_angle_right_green)
                itemView.txt_mark.gone()
                itemView.txt_detail.gone()
            }

            listener.onTopicClick(adapterPosition, item)
        }

        init {
            itemView.layout.setOnClickListener(onDetailClick)
        }
    }

    interface Listener {
        fun onTopicClick(index: Int, item: FAQDetailViewModel.Item)
    }
}