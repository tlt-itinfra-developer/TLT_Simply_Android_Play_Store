package tlt.th.co.toyotaleasing.modules.document

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_document_download.view.*
import tlt.th.co.toyotaleasing.R

class DocumentDownloadAdapter(private val items: List<DocumentDownloadViewModel.Document>,
                              private val listener: Listener) : RecyclerView.Adapter<DocumentDownloadAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_document_download, parent, false)
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
            listener.onDocumentClick(adapterPosition, item)
        }

        init {
            itemView.layout.setOnClickListener(onDetailClick)
        }
    }

    interface Listener {
        fun onDocumentClick(index: Int, item: DocumentDownloadViewModel.Document)
    }
}