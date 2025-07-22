package tlt.th.co.toyotaleasing.modules.sidebarmenu

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_sidebar_menu.view.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.extension.invisible
import tlt.th.co.toyotaleasing.common.extension.visible

class SidebarMenuAdapter(private val sidebarList: ArrayList<SidebarMenu>) : RecyclerView.Adapter<SidebarMenuAdapter.MenuViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        return when (viewType) {
            SidebarMenu.Type.MENU -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sidebar_menu, parent, false)
                MenuViewHolder(view)
            }
            SidebarMenu.Type.DIVIDER -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sidebar_divider, parent, false)
                OtherViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sidebar_empty, parent, false)
                OtherViewHolder(view)
            }
        }
    }

    override fun getItemCount() = sidebarList.size

    override fun getItemViewType(position: Int) = sidebarList[position].type

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        when (getItemViewType(position)) {
            SidebarMenu.Type.MENU -> holder.bind(sidebarList[position])
        }
    }

    open inner class MenuViewHolder(open val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(menu: SidebarMenu) {
            itemView.icon_front.setImageResource(menu.iconLeft)
            itemView.txt_title.text = menu.title

            if (menu.isIconRight()) {
                itemView.icon_back.visible()
                itemView.icon_back.setImageResource(menu.iconRight)
            } else {
                itemView.icon_back.invisible()
            }

            itemView.layout.setOnClickListener {
                menu.function.invoke()
            }
        }
    }

    inner class OtherViewHolder(override val view: View) : MenuViewHolder(view) {
        fun bind() {}
    }
}