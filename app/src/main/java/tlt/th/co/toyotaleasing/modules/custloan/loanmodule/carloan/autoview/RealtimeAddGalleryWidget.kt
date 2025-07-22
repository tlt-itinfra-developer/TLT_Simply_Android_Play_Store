package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.carloan.autoview

import android.annotation.TargetApi
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.item_gallery_view.view.*
import kotlinx.android.synthetic.main.view_recyclerview.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.extension.loadImageByBase64
import tlt.th.co.toyotaleasing.util.ImageUtils

class RealtimeAddGalleryWidget : FrameLayout {

    var maximumImage = 5

    val imageUriList: ArrayList<Uri> = arrayListOf()
    lateinit var imageBase64UriList: MutableList<String>

    private lateinit var onImageAddedListener: ((Int) -> Unit)
    private lateinit var onImageChangedListener: (base64List: List<String>) -> Unit
    private lateinit var onImageRemoveListener: ((Int) -> Unit)
    private lateinit var onImageSyncListener: ((SyncImageModel) -> Unit)
    private lateinit var onImageLimitListener: ((Boolean) -> Unit)


    val galleryAdapter by lazy {
        GalleryAdapter()
    }

    @JvmOverloads
    constructor(
            context: Context,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = 0)
            : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
            context: Context,
            attrs: AttributeSet?,
            defStyleAttr: Int,
            defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        inflate(context, R.layout.view_recyclerview, this)
        val a = context.obtainStyledAttributes(attrs,
                R.styleable.AddGalleryWidget)

        maximumImage = a.getInt(
                R.styleable.AddGalleryWidget_maximumImage,
                0)
        imageBase64UriList = MutableList(maximumImage) { "" }
        initRecyclerView()

        a.recycle()
    }


    fun setOnAddImageListener(onAdd: (Int) -> Unit) {
        onImageAddedListener = onAdd
    }

    fun getImageList(): List<String> {
        return imageBase64UriList.filter { it != "" }
    }


    fun onImageChanged(listener: (base64List: List<String>) -> Unit) {
        onImageChangedListener = listener
    }

    fun triggerImageChanged() {
        if (!this::onImageChangedListener.isInitialized) {
            return
        }
        onImageChangedListener.invoke(getImageList())
    }


    fun triggerSyncImageChanged(onSync: (SyncImageModel) -> Unit) {
        onImageSyncListener = onSync
    }

    fun triggerRemoveImageChanged(onDelete: (Int) -> Unit) {
        onImageRemoveListener  = onDelete
    }

    fun triggerOverLimitImageAdd(onStatus : (Boolean) -> Unit) {
        onImageLimitListener  = onStatus
    }

    private fun initRecyclerView() {
        custom_view_rv.layoutManager = GridLayoutManager(this.context, 3)
        custom_view_rv.adapter = galleryAdapter
    }

    inner class GalleryAdapter : RecyclerView.Adapter<GalleryViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_gallery_view, parent, false)
            return GalleryViewHolder(view)
        }

        override fun getItemCount(): Int {
            return maximumImage
        }

        override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
            if (imageBase64UriList[position] != "") {
                holder.loadImage(imageBase64UriList[position])
                holder.itemView.gallery_close_iv.visibility = View.VISIBLE
            } else {
                holder.loadDefaultImage()
                holder.itemView.gallery_close_iv.visibility = View.GONE
            }

            holder.itemView.setOnClickListener {
                if (!this@RealtimeAddGalleryWidget::onImageAddedListener.isInitialized) {
                    return@setOnClickListener
                }

                onImageAddedListener.invoke(holder.adapterPosition)
            }

            holder.itemView.gallery_close_iv.setOnClickListener {
                removeImageGallery(holder.adapterPosition)
            }
        }

        fun addImageGallery(imgUriList: ArrayList<Uri> = arrayListOf()) {
            GlobalScope.launch(Dispatchers.Main) {
                //clearGallery()
               if(CheckFreeImage()) {
                   imgUriList.forEach {
                       CheckSizeImage()
                       val base64 = ImageUtils.encodeToBase64(it)
                       addImageGallery(base64)
//                    imageUriList.add(it)
                   }
               }else{
                   onImageLimitListener.invoke(true)
               }
            }
        }

        fun addImageGallery(base64Image: String) {
            val position = imageBase64UriList.indexOfFirst { it == "" }
            imageBase64UriList[position] = base64Image
            notifyItemChanged(position)
            triggerImageChanged()

            onImageSyncListener.invoke(SyncImageModel( position =position , base64Image = base64Image ))
        }

        private fun clearGallery() {
//            imageUriList.clear()

            for (position in 0 until maximumImage) {
                imageBase64UriList[position] = ""
            }
        }

        private fun removeImageGallery(position: Int) {
            imageBase64UriList[position] = ""

//            imageUriList.getOrNull(position)?.let {
//                imageUriList.removeAt(position)
//            }
            notifyItemChanged(position)
            triggerImageChanged()
            onImageRemoveListener.invoke(position)
        }

        private fun CheckFreeImage() : Boolean{
            var emptyImage = 0
            for (position in 0 until imageBase64UriList.size) {
                if( imageBase64UriList.get(position) == "" ||  imageBase64UriList.get(position) == null ){
                    emptyImage +=1
                }
            }
            if( emptyImage > 0 )
                return true
            else
                return false
        }


       private fun CheckSizeImage(){
//            if (file.length() > MAX_IMAGE_SIZE) {
//                var streamLength = MAX_IMAGE_SIZE
//                var compressQuality = 105
//                val bmpStream = ByteArrayOutputStream()
//                while (streamLength >= MAX_IMAGE_SIZE && compressQuality > 5) {
//                    bmpStream.use {
//                        it.flush()
//                        it.reset()
//                    }
//
//                    compressQuality -= 5
//                    val bitmap = BitmapFactory.decodeFile(file.absolutePath, BitmapFactory.Options())
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
//                    val bmpPicByteArray = bmpStream.toByteArray()
//                    streamLength = bmpPicByteArray.size
//                    if (BuildConfig.DEBUG) {
//                        Log.d("test upload", "Quality: $compressQuality")
//                        Log.d("test upload", "Size: $streamLength")
//                    }
//                }
//
//                FileOutputStream(file).use {
//                    it.write(bmpStream.toByteArray())
//                }
//            }
        }

    }

    data class SyncImageModel(
           var position: Int ,
           var base64Image: String
    )



    inner class GalleryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun loadImage(base64Image: String) {
            itemView.gallery_iv.loadImageByBase64(base64Image)
        }

        fun loadDefaultImage() {
            itemView.gallery_iv.setImageResource(R.drawable.upload)
        }
    }
}