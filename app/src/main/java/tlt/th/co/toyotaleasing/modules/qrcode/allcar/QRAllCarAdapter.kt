package tlt.th.co.toyotaleasing.modules.qrcode.allcar

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_qrcode_all_car.view.*
import tlt.th.co.toyotaleasing.R

class QRAllCarAdapter(private val items: MutableList<QRAllCarViewModel.Car> = mutableListOf(),
                      private val listener: Listener) : RecyclerView.Adapter<QRAllCarAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_qrcode_all_car, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    fun updateItems(items: MutableList<QRAllCarViewModel.Car> = mutableListOf()) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    inner class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        fun bind() {
            val item = items[adapterPosition]

            view.txt_license.text = item.carLicense

            view.btn_installment.isEnabled = item.isInstallmentEnable
            view.btn_tax.isEnabled = item.isTaxEnable
            view.btn_insurance.isEnabled = item.isInsuranceEnable
            view.btn_others.isEnabled = item.isOtherEnable

            view.btn_installment.setOnClickListener { listener.onInstallmentClick(adapterPosition, item) }
            view.btn_tax.setOnClickListener { listener.onTaxClick(adapterPosition, item) }
            view.btn_insurance.setOnClickListener { listener.onInsuranceClick(adapterPosition, item) }
            view.btn_others.setOnClickListener { listener.onOthersClick(adapterPosition, item) }
        }
    }

    interface Listener {
        fun onInstallmentClick(position: Int, item: QRAllCarViewModel.Car)
        fun onTaxClick(position: Int, item: QRAllCarViewModel.Car)
        fun onInsuranceClick(position: Int, item: QRAllCarViewModel.Car)
        fun onOthersClick(position: Int, item: QRAllCarViewModel.Car)
    }
}