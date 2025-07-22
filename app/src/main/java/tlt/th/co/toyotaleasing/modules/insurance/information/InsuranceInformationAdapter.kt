package tlt.th.co.toyotaleasing.modules.insurance.information

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_insurance_history.view.*
import tlt.th.co.toyotaleasing.R

class InsuranceInformationAdapter(private val itemList: List<InsuranceHistoryData>) : RecyclerView.Adapter<InsuranceInformationAdapter.ViewHolder>() {

    var isExpand = false
    val VISIBLE_POSITION = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InsuranceInformationAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_insurance_history, parent, false)
        return InsuranceInformationAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (isExpand) VISIBLE_POSITION else itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    fun expandInsruanceHistory() {
        when {
            isExpand -> notifyItemRangeInserted(VISIBLE_POSITION, itemList.size)
            else -> notifyItemRangeRemoved(VISIBLE_POSITION, itemList.size)
        }
        isExpand = !isExpand
    }

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data: InsuranceHistoryData) {

            val request = RequestOptions()
                    .placeholder(R.drawable.bg_placeholder)
                    .error(R.drawable.bg_placeholder)

            Glide.with(view.context)
                    .load(data.insuranceImgUrl)
                    .apply(request)
                    .into(view.insurance_history_iv)

            view.item_insurance_no_field_tv.text = data.insuranceNumber
            view.item_insurance_company_field_tv.text = data.insuranceCompany
            view.item_insurance_protection_type_field_tv.text = data.insuranceType
            view.item_insurance_insurance_value_field_tv.text = data.insuranceValue
            view.item_insurance_condition_field_tv.text = data.insuranceCondition
            view.item_insurance_end_of_protection_field_tv.text = data.insuranceEndOfDate

        }
    }
}