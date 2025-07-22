package tlt.th.co.toyotaleasing.modules.insurance.hotline.accident

import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.adapter_hotline_accident.view.*
import kotlinx.android.synthetic.main.adapter_hotline_header.view.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.common.extension.callPhone
import tlt.th.co.toyotaleasing.common.extension.loadImageByUrl

class AccidentAdapter(@Nullable private val listAccident: List<AccidentViewModel.Model.AccidentModel> = arrayListOf()) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            AccidentViewType.HEADER_VIEW.value -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_hotline_header, parent, false)
                return AccidentHeaderViewHolder(view)
            }
            AccidentViewType.ACCIDENT_VIEW.value -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_hotline_accident, parent, false)
                return AccidentViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_hotline_accident, parent, false)
                return AccidentViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return listAccident.size
    }

    override fun getItemViewType(position: Int): Int {
        return listAccident[position].type.value
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AccidentHeaderViewHolder) {
            holder.bind()
        } else if (holder is AccidentViewHolder) {
            holder.bind(listAccident[position])
            holder.addOnClickListener(listAccident[holder.adapterPosition].telephone)
        }
    }

    inner class AccidentHeaderViewHolder(var view: View) : RecyclerView.ViewHolder(view) {

        fun bind() {
            view.adapter_header_name_tv.text = view.context.getString(R.string.hotline_accident_header)
        }
    }

    inner class AccidentViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(accidentModel: AccidentViewModel.Model.AccidentModel) {
            with(accidentModel) {
                itemView.adapter_hotline_accident_name_tv.text = name
                itemView.adapter_hotline_accident_tel_tv.text = telephoneDisplay
                itemView.adapter_hotline_accident_iv.loadImageByUrl(imageUrl)
            }
        }

        fun addOnClickListener(phoneNumber: String) {
            itemView.setOnClickListener {
                val filter = phoneNumber.substringBefore("กด").trim()
                callPhone(filter)
            }
        }

        private fun callPhone(phoneNumber: String) {
            AnalyticsManager.insuranceContactClaimCall()
            itemView.context.callPhone(phoneNumber)
        }
    }
}