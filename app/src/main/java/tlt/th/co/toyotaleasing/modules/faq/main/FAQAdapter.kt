package tlt.th.co.toyotaleasing.modules.faq.main

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_faq_main.view.*
import tlt.th.co.toyotaleasing.R

class FAQAdapter(private val items: List<FAQViewModel.Item>,
                 private val listener: Listener) : RecyclerView.Adapter<FAQAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_faq_main, parent, false)
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
        }

        private val onDetailClick = View.OnClickListener {
            val item = items[adapterPosition]
            listener.onTopicClick(adapterPosition, item)
        }

        init {
            itemView.layout.setOnClickListener(onDetailClick)
        }
    }

    interface Listener {
        fun onTopicClick(index: Int, item: FAQViewModel.Item)
    }
}