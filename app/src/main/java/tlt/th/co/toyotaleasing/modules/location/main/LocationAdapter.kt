package tlt.th.co.toyotaleasing.modules.location.main

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_office_location.view.*
import kotlinx.android.synthetic.main.item_showroom_location.view.*
import tlt.th.co.toyotaleasing.R

class LocationAdapter(private val items: ArrayList<LocationViewModel.Location>,
                      private val listener: Listener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_office_location -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_office_location, parent, false)
                OfficeViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_showroom_location, parent, false)
                ShowRoomViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_office_location -> {
                val viewHolder = holder as OfficeViewHolder
                viewHolder.bind()
            }
            else -> {
                val viewHolder = holder as ShowRoomViewHolder
                viewHolder.bind()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position].type) {
            LocationViewModel.Type.OFFICE -> R.layout.item_office_location
            else -> R.layout.item_showroom_location
        }
    }

    override fun getItemCount(): Int = items.size

    fun update(items: List<LocationViewModel.Location>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    inner class OfficeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind() {
            val item = items[adapterPosition]

            itemView.txt_office_location_name.text = item.title
            itemView.txt_office_location_distance.text = itemView.context.getString(R.string.location_distance, item.distance)
            itemView.txt_office_location_address.text = item.address
            itemView.txt_office_location_tel.text = item.tel
            itemView.txt_office_responsibility.text = item.responsibility
        }

        private val onDirectionClick = View.OnClickListener {
            val item = items[adapterPosition]
            listener.onDirectionClick(adapterPosition, item)
        }

        init {
            itemView.office_location_direction.setOnClickListener(onDirectionClick)
        }
    }

    inner class ShowRoomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind() {
            val item = items[adapterPosition]

            itemView.txt_showroom_location_name.text = item.title
            itemView.txt_showroom_location_address.text = item.address
            itemView.txt_showroom_location_distance.text = itemView.context.getString(R.string.location_distance, item.distance)
        }

        private val onDetailClick = View.OnClickListener {
            val item = items[adapterPosition]
            listener.onDetailClick(adapterPosition, item)
        }

        private val onTelClick = View.OnClickListener {
            val item = items[adapterPosition]
            listener.onTelClick(adapterPosition, item)
        }

        private val onDirectionClick = View.OnClickListener {
            val item = items[adapterPosition]
            listener.onDirectionClick(adapterPosition, item)
        }

        init {
            itemView.showroom_location_detail.setOnClickListener(onDetailClick)
            itemView.showroom_location_tel.setOnClickListener(onTelClick)
            itemView.showroom_location_direction.setOnClickListener(onDirectionClick)
        }
    }

    interface Listener {
        fun onDetailClick(index: Int, item: LocationViewModel.Location)
        fun onTelClick(index: Int, item: LocationViewModel.Location)
        fun onDirectionClick(index: Int, item: LocationViewModel.Location)
    }
}