package tlt.th.co.toyotaleasing.modules.selectphone

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_select_phone.view.*
import tlt.th.co.toyotaleasing.R

class SelectPhoneAdapter(private val itemList: ArrayList<PhoneNumber>,
                         private val listener: OnPhoneNumberClickListener) : RecyclerView.Adapter<SelectPhoneAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_select_phone, parent, false)
        return ViewHolder(view, listener)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    fun addItems(items: List<PhoneNumber>) {
        itemList.addAll(items)
        notifyDataSetChanged()
    }

    class ViewHolder(private val view: View,
                     private val listener: OnPhoneNumberClickListener) : RecyclerView.ViewHolder(view) {
        fun bind(item: PhoneNumber) {
            view.txt_phone_number.text = item.phoneNumber

            view.item_phonenumber.setOnClickListener {
                listener.onClick(item)
            }
        }
    }

    interface OnPhoneNumberClickListener {
        fun onClick(phoneNumber: PhoneNumber)
    }
}