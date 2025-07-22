package tlt.th.co.toyotaleasing.modules.setting.changeprofilecar

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.recyclerview.widget.LinearSnapHelper
//
import kotlinx.android.synthetic.main.activity_change_car_profile.*
import kotlinx.android.synthetic.main.item_car_profile.view.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.dialog.NormalDialogFragment
import tlt.th.co.toyotaleasing.common.extension.ifTrue
import tlt.th.co.toyotaleasing.common.extension.visible
//import tlt.th.co.toyotaleasing.manager.ImageManager
import tlt.th.co.toyotaleasing.modules.filtercar.FilterCarActivity

class ChangeCarProfileActivity : BaseActivity(), NormalDialogFragment.Listener {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(ChangeCarProfileViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_car_profile)

        initViewModel()
        initInstances()

        viewModel.getData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

//        if (resultCode != RESULT_OK
//                || requestCode != Define.ALBUM_REQUEST_CODE) {
//            return
//        }

        //val imageUri = Uri
      //  updateCurrentCar(imageUri)
    }

    private fun initViewModel() {
        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!)
        })

        viewModel.whenDataLoaded.observe(this, Observer {
            setupDataIntoViews(it)
            it?.isStaffApp?.ifTrue { supportForStaff(it) }
        })

        viewModel.whenCarProfileUploadFailure.observe(this, Observer {

        })

        viewModel.whenCarProfileUploadSuccess.observe(this, Observer {

        })
    }

    private fun supportForStaff(it: ChangeCarProfileViewModel.Model) {

    }

    private fun initInstances() {
        layout_item_car_profile.btn_previous.visible()
        layout_item_car_profile.btn_next.visible()

        btn_choose_image.setOnClickListener { chooseImage() }
        btn_reset.setOnClickListener { showRevertImageDialog() }

        layout_item_car_profile.btn_previous.setOnClickListener { previousCar() }
        layout_item_car_profile.btn_next.setOnClickListener { nextCar() }

        toolbar.setOnRightMenuTitleClickListener {
            AnalyticsManager.carProfileSelectCar()
            FilterCarActivity.open(this)
        }
    }

    private fun setupDataIntoViews(it: ChangeCarProfileViewModel.Model?) {
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerview)
        recyclerview.adapter = ChangeCarProfileAdapter(
                itemList = it?.carList ?: mutableListOf()
        )
        //indicator.attachToRecyclerView(recyclerview)
    }

    private fun chooseImage() {
        AnalyticsManager.carProfileCamera()
        //ImageManager.open(this)
    }

    private fun previousCar() {
        val previousPosition = recyclerview.currentPosition - 1

        if (previousPosition == -1) {
            return
        }

        recyclerview.scrollToPosition(previousPosition)
    }

    private fun nextCar() {
        val nextPosition = recyclerview.currentPosition + 1

        if (nextPosition == viewModel.whenDataLoaded.value?.carList?.size) {
            return
        }

        recyclerview.scrollToPosition(nextPosition)
    }

    private fun showRevertImageDialog() {
        AnalyticsManager.carProfileDefaultPicture()
        NormalDialogFragment.show(
                fragmentManager = supportFragmentManager,
                description = getString(R.string.profile_car_revert_image_descrption),
                confirmButtonMessage = getString(R.string.profile_car_button),
                cancelButtonMessage = getString(R.string.callcenter_dialog_btn_cancel)
        )
    }

    private fun revertImage() {
        val currentPosition = recyclerview.currentPosition
        val item = viewModel.whenDataLoaded.value
                ?.carList
                ?.getOrNull(currentPosition)?.apply {
                    imageUri = Uri.EMPTY
                    imageBase64 = ""
                }

        val adapter = recyclerview.adapter as ChangeCarProfileAdapter
        adapter.update(currentPosition, item)

        viewModel.resetCarImage(item?.contractNumber ?: "")
    }

//    private fun updateCurrentCar(uri: Uri?) {
//        uri?.let {
//            val currentPosition = recyclerview.currentPosition
//            val item = viewModel.whenDataLoaded
//                    .value
//                    ?.carList
//                    ?.getOrNull(currentPosition)?.apply {
//                        imageUri = uri
//                    }
//
//            val adapter = recyclerview.adapter as ChangeCarProfileAdapter
//            adapter.update(currentPosition, item)
//
//            viewModel.updateCarImage(item?.contractNumber ?: "", uri)
//        }
//    }

    override fun onDialogConfirmClick() {
        AnalyticsManager.carProfileConfirm()
        revertImage()
    }

    override fun onDialogCancelClick() {
        AnalyticsManager.carProfileCancel()
    }

    companion object {
        fun start(context: Context?) {
            val intent = Intent(context, ChangeCarProfileActivity::class.java)
            context?.startActivity(intent)
        }
    }
}
