package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.ndid.common


import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_payment_method.view.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.extension.loadImageByUrl
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.ndid.LoanEContractSelectBankNDIDViewModel

class IDPEContractMethodAdapter(private val idpList: MutableList<LoanEContractSelectBankNDIDViewModel.IDPListModel> = arrayListOf(),
                                private var onClick: (Int, LoanEContractSelectBankNDIDViewModel.IDPListModel) -> Unit) : RecyclerView.Adapter<IDPEContractMethodAdapter.IDPMethodViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IDPMethodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ndid_dp_mehode, parent, false)
        return IDPMethodViewHolder(view)
    }

    override fun getItemCount(): Int {
        return idpList.size
    }

    override fun onBindViewHolder(holder: IDPMethodViewHolder, position: Int) {
        holder.bind()
    }

    inner class IDPMethodViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind() {
            val dpMethodModel = idpList[adapterPosition]

            view.iv_payment_bank.loadImageByUrl(dpMethodModel.nodeImgUrl)
            view.txt_payment_bank.text = dpMethodModel.marketingName

            view.setOnClickListener {
               onClick.invoke(adapterPosition, dpMethodModel)
            }
        }
    }
}