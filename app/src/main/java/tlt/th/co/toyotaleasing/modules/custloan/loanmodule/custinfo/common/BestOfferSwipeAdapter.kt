package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.custinfo.common

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_best_offer.view.*
import kotlinx.android.synthetic.main.item_loan_car_list.view.layout
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.custinfo.CInfoCreditResultViewModel

class BestOfferSwipeAdapter(private val items: List<CInfoCreditResultViewModel.dOffer>,
                       private val listener: Listener) : RecyclerView.Adapter<BestOfferSwipeAdapter.BestOfferViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  BestOfferViewHolder  {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_best_offer, parent, false)
        return BestOfferViewHolder(view)
    }

    override fun onBindViewHolder(holder: BestOfferViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = items.size


    inner class BestOfferViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            val item = items[adapterPosition]
            //itemView.txt_title.text = item.offerId
            itemView.txt_detail.text = item.offerTextValue
            if(item.flagSelect )
                itemView.rad_best_offer.isChecked = true
            else{
                itemView.rad_best_offer.isChecked = false
            }
        }

        private val onListClick = View.OnClickListener {
            val item = items[adapterPosition]
            listener.onListClick(adapterPosition, item)
        }

        init {
            itemView.rad_best_offer.setOnClickListener(onListClick)
        }
    }

    interface Listener {
        fun onListClick(index: Int, item: CInfoCreditResultViewModel.dOffer)
    }


}
