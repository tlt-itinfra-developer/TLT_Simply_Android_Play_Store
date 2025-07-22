package tlt.th.co.toyotaleasing.modules.setting.changeprofilecar

import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_car_profile.view.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.extension.loadImageByBase64
import tlt.th.co.toyotaleasing.common.extension.loadImageByUri
import tlt.th.co.toyotaleasing.common.extension.loadImageByUrl

class ChangeCarProfileAdapter(private val itemList: MutableList<ChangeCarProfileViewModel.Item>) : RecyclerView.Adapter<ChangeCarProfileAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_car_profile, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    fun update(index: Int, item: ChangeCarProfileViewModel.Item?) {
        item?.let {
            itemList[index] = it
            notifyItemChanged(index)
        }
    }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind() {
            val item = itemList[adapterPosition]

            when {
                item.imageUri != Uri.EMPTY -> view.img_car_profile.loadImageByUri(item.imageUri)
                item.imageBase64.isNotEmpty() -> view.img_car_profile.loadImageByBase64(item.imageBase64)
                else -> view.img_car_profile.loadImageByUrl(item.imageUrl)
            }

            val context = view.context

            view.txt_car_license.text = item.carLicense
//            view.txt_car_model.text = "${context.getString(R.string.car_contract_model)} : ${item.carModel}"
            view.txt_car_model.text = context.getString(R.string.profile_car_model_txt, item.carModel)
            view.txt_car_no.text = context.getString(R.string.my_car_txt_contract_number, item.contractNumber)
        }
    }
}