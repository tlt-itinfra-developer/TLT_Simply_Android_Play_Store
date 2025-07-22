package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.geo.location

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_loan_showroom_location.view.*
import tlt.th.co.toyotaleasing.R

class LoanLocationAdapter(private val items: ArrayList<LoanLocationViewModel.Location>,
                          private val listener: Listener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_loan_showroom_location, parent, false)
        return  ShowRoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

         val viewHolder = holder as ShowRoomViewHolder
         viewHolder.bind()

    }

    override fun getItemViewType(position: Int): Int {
         return  R.layout.item_showroom_location
    }

    override fun getItemCount(): Int = items.size

    fun update(items: List<LoanLocationViewModel.Location>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    inner class ShowRoomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind() {
            val item = items[adapterPosition]

            itemView.txt_showroom_location_name.text = item.title
            itemView.txt_showroom_location_address.text = item.address
            itemView.txt_showroom_location_distance.text = itemView.context.getString(R.string.location_distance, item.distance)
            itemView.txt_showroom_office_hour.text = item.openTime

        }

//        private val onDetailClick = View.OnClickListener {
//            val item = items[adapterPosition]
//            listener.onDetailClick(adapterPosition, item)
//        }
//
//        private val onTelClick = View.OnClickListener {
//            val item = items[adapterPosition]
//            listener.onTelClick(adapterPosition, item)
//        }

        private val onDirectionClick = View.OnClickListener {
            val item = items[adapterPosition]
            listener.onDirectionClick(adapterPosition, item)
        }

        private val onSelectClick = View.OnClickListener {
            val item = items[adapterPosition]
            listener.onSelectClick(adapterPosition, item)
        }

        init {
//            itemView.showroom_location_detail.setOnClickListener(onDetailClick)
//            itemView.showroom_location_tel.setOnClickListener(onTelClick)
            itemView.showroom_location_direction.setOnClickListener(onDirectionClick)
            itemView.showroom_location_select.setOnClickListener(onSelectClick)
        }
    }

    interface Listener {
//        fun onDetailClick(index: Int, item: LoanLocationViewModel.Location)
//        fun onTelClick(index: Int, item: LoanLocationViewModel.Location)
        fun onDirectionClick(index: Int, item: LoanLocationViewModel.Location)
        fun onSelectClick(index: Int, item: LoanLocationViewModel.Location)
    }
}