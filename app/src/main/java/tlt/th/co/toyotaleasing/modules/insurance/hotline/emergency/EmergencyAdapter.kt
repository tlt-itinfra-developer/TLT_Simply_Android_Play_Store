package tlt.th.co.toyotaleasing.modules.insurance.hotline.emergency

import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.adapter_hotline_emergency.view.*
import kotlinx.android.synthetic.main.adapter_hotline_header.view.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.common.extension.callPhone

class EmergencyAdapter(@Nullable private val listEmergency: List<EmergencyViewModel.Model.EmergencyModel> = arrayListOf()) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            EmergencyViewType.HEADER_VIEW.value -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_hotline_header, parent, false)
                return EmergencyHeaderViewHolder(view)
            }
            EmergencyViewType.EMERGENCY_VIEW.value -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_hotline_emergency, parent, false)
                return EmergencyViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_hotline_emergency, parent, false)
                return EmergencyViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return listEmergency.size
    }

    override fun getItemViewType(position: Int): Int {
        return listEmergency[position].type.value
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is EmergencyHeaderViewHolder) {
            holder.bind()
        } else if (holder is EmergencyViewHolder) {
            holder.bind(listEmergency[position])
            holder.addOnClickListener(listEmergency[holder.adapterPosition].telephone)
        }
    }

    inner class EmergencyHeaderViewHolder(var view: View) : RecyclerView.ViewHolder(view) {

        fun bind() {
            view.adapter_header_name_tv.text = view.context.getString(R.string.hotline_emergency_header)
        }
    }

    inner class EmergencyViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        fun bind(emergencyModel: EmergencyViewModel.Model.EmergencyModel) {
            with(emergencyModel) {
                itemView.adapter_hotline_emergency_name_tv.text = name
                itemView.adapter_hotline_emergency_tel_tv.text = telephone
            }
        }

        fun addOnClickListener(phoneNumber: String) {
            itemView.setOnClickListener {
                val filter = phoneNumber.substringBefore("กด").trim()
                callPhone(filter)
            }
        }

        private fun callPhone(phoneNumber: String) {
            AnalyticsManager.insuranceContactEmerCallContactCall()
            itemView.context.callPhone(phoneNumber)
        }
    }
}