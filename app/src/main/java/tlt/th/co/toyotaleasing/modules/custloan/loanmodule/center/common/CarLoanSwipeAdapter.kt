package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.center.common

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_loan_car_list.view.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.extension.loadImageByUrl
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.center.CarLoanViewModel
import java.util.ArrayList

class CarLoanSwipeAdapter ( private val items: ArrayList<CarLoanViewModel.CarsLoanItems>
                            , private val listener : Listener  ) : RecyclerView.Adapter<CarLoanSwipeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_loan_car_list, parent, false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            val item = items[adapterPosition]
            itemView.txt_unique_id.text = item.rEFID
            itemView.txt_cuurent_sts_detail.text = item.rESDES
            itemView.txt_expire_date.text = item.eXPIREDATE
            itemView.txt_stamp_date_des.text = item.sTAMPDATE
            itemView.title_car_model.text = item.cARMODEL
            itemView.txt_model.text = item.cARGRADE
            itemView.txt_price.text = item.cARPRICE
            itemView.img_car.loadImageByUrl(item.cARIMAGE)
            itemView.txt_cuurent_sts_detail.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
            if(item.rEFSTATUS == "APPROVE") {
                itemView.lay_status_car.background = ContextCompat.getDrawable(itemView.context, R.drawable.bg_carloan)
                itemView.txt_cuurent_sts_detail.setTextColor(ContextCompat.getColor(itemView.context, R.color.Green))
            }else if(item.rEFSTATUS == "REJECT"||  item.rEFSTATUS == "CANCEL"){
                itemView.lay_status_car.background = ContextCompat.getDrawable(itemView.context, R.color.bg_disable)
                itemView.txt_cuurent_sts_detail.setTextColor(ContextCompat.getColor(itemView.context, R.color.cherry_red))
            }else{
                itemView.lay_status_car.background = ContextCompat.getDrawable(itemView.context, R.drawable.bg_carloan)
                itemView.txt_cuurent_sts_detail.setTextColor(ContextCompat.getColor(itemView.context, R.color.mustard))
            }
        }

        private val onCarListClick = View.OnClickListener {
            val item = items[adapterPosition]
            listener.onCarListClick(adapterPosition, item)
        }

        private val onSelectClicked = View.OnClickListener {
            val item = items[adapterPosition]
            if(!(item.rEFSTATUS == "REJECT"||
                    item.rEFSTATUS == "CANCEL")) {
                listener.onSelectClicked(adapterPosition, item)
            }
        }

        private val onDeleteClick = View.OnClickListener {
            val item = items[adapterPosition]
            listener.onDeleteClick(adapterPosition, item)
        }

        init {
            itemView.img_car.setOnClickListener(onCarListClick)
            itemView.img_car.setOnClickListener(onSelectClicked)
            itemView.lay_delete.setOnClickListener(onDeleteClick)

        }
    }

    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, items.size)
    }

    fun restoreItem(model: CarLoanViewModel.CarsLoanItems, position: Int) {
        items.add(position, model)
        // notify item added by position
        notifyItemInserted(position)
    }


    interface Listener {
        fun onCarListClick(index: Int, item: CarLoanViewModel.CarsLoanItems)
        fun onSelectClicked(index: Int, item: CarLoanViewModel.CarsLoanItems)
        fun onDeleteClick(index: Int, item: CarLoanViewModel.CarsLoanItems)
    }
}