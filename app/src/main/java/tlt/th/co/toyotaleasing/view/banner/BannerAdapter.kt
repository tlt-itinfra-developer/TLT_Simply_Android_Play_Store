package tlt.th.co.toyotaleasing.view.banner

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_banner.view.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.extension.loadImageByUrl

class BannerAdapter(private val bannerList: ArrayList<Banner>,
                    private val listener: Listener) : RecyclerView.Adapter<BannerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_banner, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = bannerList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(bannerList[position])


    }

    fun updateItems(banners: List<Banner>) {
        bannerList.clear()
        bannerList.addAll(banners)
        notifyDataSetChanged()
    }

    fun addData(banners: ArrayList<Banner>) {
        bannerList.addAll(banners)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(banner: Banner) {
            itemView.img_banner.loadImageByUrl(banner.imageUrl)
        }

        private val onBannerClicked = View.OnClickListener {
            val item = bannerList[adapterPosition]
            listener.onBannerClicked(
                    bannerName = item.bannerName,
                    bannerDescription = item.bannerDes2
            )
        }


        init {
            itemView.img_banner.setOnClickListener(onBannerClicked)
        }
    }

    interface Listener {
        fun onBannerClicked(bannerName: String, bannerDescription: String)
    }
}