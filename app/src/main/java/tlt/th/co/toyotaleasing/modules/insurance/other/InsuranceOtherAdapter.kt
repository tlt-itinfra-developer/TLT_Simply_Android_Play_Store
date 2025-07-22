package tlt.th.co.toyotaleasing.modules.insurance.other

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_tib_club.view.*
import kotlinx.android.synthetic.main.item_tib_club_header.view.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.extension.*

class InsuranceOtherAdapter(private val itemList: ArrayList<InsuranceOtherViewModel.Model>,
                            private val listener: OnEventClickListener) : RecyclerView.Adapter<InsuranceOtherAdapter.ViewHolder>() {

    private val HEADER = 1
    private val CONTENT = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        when (viewType) {
            HEADER -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tib_club_header, parent, false)
                return ViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tib_club, parent, false)
                return ViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int = itemList.size

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> HEADER
            else -> CONTENT
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            CONTENT -> holder.bind()
            else -> holder.bindHeader()
        }
    }

    fun updateItems(items: ArrayList<InsuranceOtherViewModel.Model>) {
        itemList.clear()
        itemList.addAll(items)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bindHeader() {
            val item = itemList[adapterPosition]
            view.title_tib_header_club_detail.text = item.detail
            view.img_tib_header_club_tib.loadImageByUrl(item.imageUrl)
        }

        fun bind() {
            val item = itemList[adapterPosition]

            view.img_event.loadImageByUrl(item.imageUrl)
            view.txt_event_title.text = item.title
            view.txt_event_detail.text = item.detail

            item.isShowEventOtherOnHeader.ifTrue {
                view.title_event.visible()
            }

            item.isShowEventOtherOnHeader.ifFalse {
                view.title_event.gone()
            }

            item.title.isEmpty().ifTrue {
                view.txt_event_title.gone()
            }

            item.title.isEmpty().ifFalse {
                view.txt_event_title.visible()
            }

            view.layout.setOnClickListener {
                listener.onClick(item)
            }
        }
    }

    interface OnEventClickListener {
        fun onClick(item: InsuranceOtherViewModel.Model)
    }
}