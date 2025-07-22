package tlt.th.co.toyotaleasing.modules.newsandpromotion.news

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_news_promotion_layout.view.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.extension.loadImageByUrl

class NewsAdapter(private val items: ArrayList<News> = arrayListOf(),
                  private val listener: Listener) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news_promotion_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    fun updateItems(items: List<News>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun bookmarkUnsuccessful(position: Int) {
        this.items[position].isBooked = false
        notifyItemChanged(position)
    }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind() {
            val item = items[adapterPosition]

            view.title.text = item.title.replaceAfter("\n", "")
            view.period.text = item.period

            if (item.isBooked) {
                view.status.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_bookmark_active))
            } else {
                view.status.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_bookmark_unactive))
            }

            view.image.loadImageByUrl(item.image)
        }

        private val onNewsClick = View.OnClickListener {
            val item = items[adapterPosition]
            listener.onNewsClick(adapterPosition, item)
        }

        private val onBookmarkClick = View.OnClickListener {
            val item = items[adapterPosition]

            item.isBooked = !item.isBooked
            notifyItemChanged(adapterPosition)

            listener.onBookmarkClick(adapterPosition, item)
        }

        init {
            itemView.card_view.setOnClickListener(onNewsClick)
            itemView.status.setOnClickListener(onBookmarkClick)
        }
    }

    interface Listener {
        fun onNewsClick(position: Int, item: News)
        fun onBookmarkClick(position: Int, item: News)
    }
}