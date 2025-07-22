package tlt.th.co.toyotaleasing.modules.contract.specialoffer

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_special_offer.view.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.extension.toDatetime
import tlt.th.co.toyotaleasing.manager.ContextManager
import tlt.th.co.toyotaleasing.model.response.GetPrivilegeResponse

class SpecialOfferAdapter : RecyclerView.Adapter<SpecialOfferAdapter.ViewHolder>() {

    private val items = ArrayList<GetPrivilegeResponse.Privilege>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_special_offer, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    fun updateItem(items: List<GetPrivilegeResponse.Privilege>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }


    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind() {
            itemView.title_txt.text = items[adapterPosition].aDSUBJECT
            itemView.send_date_txt.text = ContextManager.getInstance().getApplicationContext()
                    .resources.getString(R.string.special_offer_car_date_format,
                    items[adapterPosition].sTARTDATE?.toDatetime())
            itemView.description_txt.text = items[adapterPosition].cONTENTDETAIL
        }

    }

}