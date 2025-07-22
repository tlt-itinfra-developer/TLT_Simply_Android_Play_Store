package tlt.th.co.toyotaleasing.modules.notify

import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import android.text.Html
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.item_faq_detail.view.*
import kotlinx.android.synthetic.main.item_notify_type_1.view.*
import kotlinx.android.synthetic.main.item_notify_type_2.view.*
import kotlinx.android.synthetic.main.item_notify_type_3.view.*
import kotlinx.android.synthetic.main.item_notify_type_4.view.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.extension.*

class NotifyAdapter(private val listener: Listener) : RecyclerView.Adapter<NotifyAdapter.ViewHolder>() {

    private val items = ArrayList<Notify>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            Notify.Type.FIRST -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notify_type_1, parent, false)
                ViewHolder(view, listener)
            }
            Notify.Type.SECOND -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notify_type_2, parent, false)
                ViewHolder(view, listener)
            }
            Notify.Type.THRID -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notify_type_3, parent, false)
                ViewHolder(view, listener)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notify_type_4, parent, false)
                ViewHolder(view, listener)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            Notify.Type.FIRST -> holder.bindByType1(items[position])
            Notify.Type.SECOND -> holder.bindByType2(items[position])
            Notify.Type.THRID -> holder.bindByType3(items[position])
            else -> holder.bindByType4(items[position])
        }
    }

    override fun getItemViewType(position: Int) = items[position].type

    fun updateItems(items: List<Notify>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    class ViewHolder(private val view: View,
                     private val listener: Listener) : RecyclerView.ViewHolder(view) {
        fun bindByType1(item: Notify) {
//            view.txt_notify_type_1_detail.text = item.detail.replace("\\n", System.lineSeparator())
            view.txt_notify_type_1_datetime.text = item.datetime

            view.txt_notify_type_1_detail.setLinkTextColor(ContextCompat.getColor(itemView.context, R.color.blue))
            view.txt_notify_type_1_detail.text = item.detail.replace("\\n", System.lineSeparator())
            Linkify.addLinks(view.txt_notify_type_1_detail, Linkify.WEB_URLS or Linkify.PHONE_NUMBERS)

            view.btn_notify_type_1_more.setOnClickListener { onMoreMenuClick(it, item) }

            item.isPromotion().ifTrue {
                view.ic_notify_type_1.setImageResource(R.drawable.ic_greenbell)
            }

            if (canNotNavigation(item.navigation)) {
                view.btn_notify_type_1_see_more.gone()
            } else {
                view.btn_notify_type_1_see_more.visible()
                view.btn_notify_type_1_see_more.setOnClickListener { onSeemoreDetailClick(it, item) }
            }
        }

        fun bindByType2(item: Notify) {
//            view.txt_notify_type_2_detail.text = item.detail.replace("\\n", System.lineSeparator())
            view.txt_notify_type_2_detail.setLinkTextColor(ContextCompat.getColor(itemView.context, R.color.blue))
            view.txt_notify_type_2_detail.text = item.detail.replace("\\n", System.lineSeparator())
            Linkify.addLinks(view.txt_notify_type_2_detail, Linkify.WEB_URLS or Linkify.PHONE_NUMBERS)

            view.txt_notify_type_2_datetime.text = item.datetime
            view.img_notify.loadImageByUrl(item.imageUrl)
            view.btn_notify_type_2_more.setOnClickListener { onMoreMenuClick(it, item) }

            if (canNotNavigation(item.navigation)) {
                view.btn_notify_type_2_see_more_detail.gone()
            } else {
                view.btn_notify_type_2_see_more_detail.visible()
                view.btn_notify_type_2_see_more_detail.setOnClickListener { onSeemoreDetailClick(it, item) }
            }
        }

        fun bindByType3(item: Notify) {
//            view.txt_notify_type_3_detail.text = item.detail.replace("\\n", System.lineSeparator())
            view.txt_notify_type_3_detail.setLinkTextColor(ContextCompat.getColor(itemView.context, R.color.blue))
            view.txt_notify_type_3_detail.text = item.detail.replace("\\n", System.lineSeparator())
            Linkify.addLinks(view.txt_notify_type_3_detail, Linkify.WEB_URLS or Linkify.PHONE_NUMBERS)
            if (canNotNavigation(item.navigation)) {
                view.btn_see_more.gone()
            } else {
                view.btn_see_more.visible()
                view.btn_see_more.setOnClickListener { onSeemoreDetailClick(it, item) }
            }

        }

        fun bindByType4(item: Notify) {
            view.txt_notify_type_4_datetime.text = item.datetime

            view.btn_notify_type_4_more.setOnClickListener { onMoreMenuClick(it, item) }

            item.isPromotion().ifTrue {
                view.ic_notify_type_4.setImageResource(R.drawable.ic_greenbell)
            }

            item.imageUrl.isNotEmpty().ifTrue {
                view.img_notify_type_4.visible()
                view.img_notify_type_4.loadImageByUrl(item.imageUrl)
            }

            if (item.detail.isNotEmpty()) {
                view.txt_notify_type_4_detail.visible()
//                view.txt_notify_type_4_detail.text = item.detail.replace("\\n", System.lineSeparator())
                view.txt_notify_type_4_detail.setLinkTextColor(ContextCompat.getColor(itemView.context, R.color.blue))
                view.txt_notify_type_4_detail.text = item.detail.replace("\\n", System.lineSeparator())
                Linkify.addLinks(view.txt_notify_type_4_detail, Linkify.WEB_URLS or Linkify.PHONE_NUMBERS)
            } else {
                view.txt_notify_type_4_detail.gone()
            }

            if (canNotNavigation(item.navigation)) {
                view.btn_notify_type_4_see_more_detail.gone()
            } else {
                view.btn_notify_type_4_see_more_detail.visible()
                view.btn_notify_type_4_see_more_detail.setOnClickListener { onSeemoreDetailClick(it, item) }
            }
        }

        private fun canNotNavigation(navigation: String) = navigation.contains("tlt://etc") && navigation.contains("\"flagCheckBox\":\"N\"")

        private val onSeemoreDetailClick = { v: View, item: Notify ->
            listener.onSeemoreClicked(item)
        }

        private val onMoreMenuClick = { v: View, item: Notify ->
            PopupMenu(v.context, v)
                    .apply {
                        setOnMenuItemClickListener {
                            onRemoveClick(item)
                            true
                        }

                        menuInflater.inflate(R.menu.item_notify_menu, menu)
                    }
                    .show()
        }

        private val onRemoveClick = { item: Notify ->
            listener.onRemoveClicked(item)
            true
        }
    }

    interface Listener {
        fun onRemoveClicked(notify: Notify)
        fun onSeemoreClicked(notify: Notify)
    }
}