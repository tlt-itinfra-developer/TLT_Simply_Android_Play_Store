package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.center



import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.graphics.*
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper
import android.util.Log
import android.view.*
import kotlinx.android.synthetic.main.fragment_dialog_remove_file.view.*
import kotlinx.android.synthetic.main.fragment_online_car_loan.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseFragment
import tlt.th.co.toyotaleasing.common.dialog.MsgdescNormalDialog
import tlt.th.co.toyotaleasing.common.dialog.NormalDialogFragment
import tlt.th.co.toyotaleasing.common.listener.OnHambergerClickListener
import tlt.th.co.toyotaleasing.manager.ContextManager
import tlt.th.co.toyotaleasing.manager.db.MasterDataManager
import tlt.th.co.toyotaleasing.manager.db.UserManager
import tlt.th.co.toyotaleasing.modules.custloan.MenuStepController
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.basicdetail.BasicLivenessActivity
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.center.common.CarLoanSwipeAdapter
import tlt.th.co.toyotaleasing.modules.home.customer.MainCustomerActivity
import tlt.th.co.toyotaleasing.modules.home.noncustomer.MainNonCustomerActivity
import tlt.th.co.toyotaleasing.modules.web.WebActivity
import tlt.th.co.toyotaleasing.view.DividerItemDecoration

class CarLoanFragment  : BaseFragment() ,  CarLoanSwipeAdapter.Listener, MsgdescNormalDialog.Listener{


    private lateinit var isAuthen: String
    private lateinit var uniqeID: String
    private var recyclerView: RecyclerView? = null
    private var carLoanArrayList: ArrayList<CarLoanViewModel.CarsLoanItems>? = null
    private var adapterCarLoan: CarLoanSwipeAdapter? = null
    private val urlWebOnline = MasterDataManager.getInstance().getWebCarLoan() //!!.get(0).cARLOANWEB.toString()
    private val p = Paint()

//    private val carloanList = arrayOf("CHR", "Camry", "Hino", "Civic", "Yaris")

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(CarLoanViewModel::class.java)
    }

    private val onHambergerClickListener by lazy {
        context as OnHambergerClickListener
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_online_car_loan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AnalyticsManager.trackScreen(AnalyticsScreenName.ONLINE_APP)
        initViewModel()
        initInstance()
        viewModel.getIdentity()

    }

    private fun initInstance() {

        uniqeID = activity?.intent
                ?.getBundleExtra(MainCustomerActivity.DATA_POSITION_EXTRA)
                ?.getString(UNI_ID)?: ""
        recyclerView = view!!.findViewById(R.id.car_recyclerView) as RecyclerView
        viewModel.getDataCarsLoan("")
        viewModel.ClearDataEntity()
        lay_no_data.visibility = View.VISIBLE

        toolbar.setOnHambergerMenuClickListener {
//            AnalyticsManager.settingMenuHome()
            onHambergerClickListener.onHambergerClick()
        }

        swipe_refresh.setOnRefreshListener {
            viewModel.getDataCarsLoan("")
        }

        btngocal.setOnClickListener {
            try{

            }catch (e : Exception) {
                e.printStackTrace()
            }
        }

        btn_rgs_car_loan.setOnClickListener {
            viewModel.getIdentityByRegister()
        }

    }

    private fun initViewModel() {

        viewModel.whenLoading.observe(this, Observer {
            if (it != null) {
                swipe_refresh.isRefreshing = it
            }
        })


        viewModel.whenDataLoaded.observe(this, Observer {
            it?.let {
//                MenuStepController.open(context!!,uniqeID, it)
                swipe_refresh.isRefreshing = false
                setupDataIntoViews(it)
                if(uniqeID != "") {
                    it.forEach {
                        if(it.rEFID == uniqeID ){
                            AnalyticsManager.onlineCarClick()
                            viewModel.CheckStepAPI(uniqeID)
                        }
                    }
                }
            }
        })

        viewModel.whenDataNotFound.observe(this, Observer {
            swipe_refresh.isRefreshing = false
        })

        viewModel.whenSyncSuccessData.observe(this, Observer {
            it?.let {
                MenuStepController.open(context!!, it.ref_id , it.step , it.ref_url)
            }
        })

        viewModel.whenDataLoadedMessage.observe(this, Observer {
            it?.let {
                MsgdescNormalDialog.show(
                        fragmentManager = fragmentManager!!,
                        description = it ,
                        confirmButtonMessage = getString(R.string.dialog_button_ok)
                )
            }
        })

        viewModel.whenCheckIdentity.observe(this, Observer {
            it?.let{
                if(it){
                    lay_non_access.visibility = View.GONE
                    lay_no_data.visibility = View.VISIBLE
                    viewModel.getDataCarsLoan("")
                }else{
                    lay_no_data.visibility = View.GONE
                    lay_non_access.visibility = View.VISIBLE
                }
            }
        })

        viewModel.whenCheckIdentityByRegis.observe(this, Observer {
            it?.let{
                if(it){
                    lay_non_access.visibility = View.GONE
                    lay_no_data.visibility = View.VISIBLE
                    viewModel.getDataCarsLoan("")
                }else{
                    lay_non_access.visibility = View.VISIBLE
                    lay_no_data.visibility = View.GONE
                    LoanAuthenActivity.open(context!!)
                }
            }
        })
    }

    private fun setupDataIntoViews(items: List<CarLoanViewModel.CarsLoanItems>) {
        try{
            if (items?.isEmpty() == true) {
                return
            }
            lay_no_data.visibility = View.GONE
            carLoanArrayList = populateList(items)

            adapterCarLoan = CarLoanSwipeAdapter(carLoanArrayList!! , this@CarLoanFragment)
            recyclerView?.apply {
                layoutManager = LinearLayoutManager(context)
//                addItemDecoration(DividerItemDecoration(ContextCompat.getDrawable(context, R.drawable.item_divider_recycler_view)))
                adapter = adapterCarLoan
            }

//            enableSwipe()

        }catch(e : Exception) {
            Log.e("setupDataIntoViews" ,e.message.toString() )
        }

    }

    override fun onCarListClick(index: Int, item: CarLoanViewModel.CarsLoanItems) {
        viewModel.SetCancelCar(item.rEFID)
    }

    override fun onSelectClicked(index: Int, item: CarLoanViewModel.CarsLoanItems) {

        uniqeID = item.rEFID
        viewModel.CheckStepAPI(item.rEFID )
    }

    override fun onDeleteClick(index: Int, item: CarLoanViewModel.CarsLoanItems) {
        val deletedModel = item.rEFID
        try{

                try {
                    val dialog = LayoutInflater.from(context!!).inflate(R.layout.fragment_dialog_remove_file, null)
                    val mBuilder = AlertDialog.Builder(context!!).setView(dialog)
                    mBuilder.create().window!!.requestFeature(Window.FEATURE_NO_TITLE)
                    mBuilder.create().window!!.setBackgroundDrawableResource(android.R.color.transparent)
                    mBuilder.create().window!!.setLayout(resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels)
                    mBuilder.create().window!!.setGravity(Gravity.CENTER);
                    val alertDialog = mBuilder.show()
                   dialog!!.title.setText(ContextManager.getInstance().getApplicationContext().resources.getString(R.string.dialog_warning_delete) )
                   dialog!!.btn_cancel.setText(R.string.btn_cancel)
                   dialog!!.btn_confirm.setText(R.string.btn_ok)
                   dialog!!.btn_cancel.setOnClickListener {
                        adapterCarLoan!!.notifyDataSetChanged()
                        alertDialog.dismiss()
                    }
                   dialog!!.btn_confirm.setOnClickListener {
                        viewModel.SetCancelCar(deletedModel)
                        AnalyticsManager.onlineCarDelete()
                        adapterCarLoan!!.notifyDataSetChanged()
                        alertDialog.dismiss()
                    }
                } catch (e: Exception) {
                    e.message
                }

        }catch(e : Exception  ){
            e.printStackTrace()
        }

    }

    override fun onDialogConfirmClick() {
    }

    override fun onDialogCancelClick() {
    }


//     private fun enableSwipe() {
//
//        val simpleItemTouchCallback =
//                object : ItemTouchHelper.SimpleCallback(0,  ItemTouchHelper.LEFT) {
//
//                    override fun onMove(
//                            recyclerView: RecyclerView,
//                            viewHolder: RecyclerView.ViewHolder,
//                            target: RecyclerView.ViewHolder
//                    ): Boolean {
//                        return false
//                    }
//
//                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                        val position = viewHolder.adapterPosition
//                        val deletedModel = carLoanArrayList!![position].rEFID
//                        try{
//                            AlertDialog.Builder(context!!)
//                                    .setCancelable(false)
//                                    .setMessage(ContextManager.getInstance().getApplicationContext().resources.getString(R.string.dialog_warning_delete))
//                                    .setPositiveButton(ContextManager.getInstance().getApplicationContext().resources.getString(R.string.dialog_button_yes)) { dialog, id ->
//                                        viewModel.SetCancelCar(deletedModel)
//                                        adapterCarLoan!!.notifyDataSetChanged()
//                                       dialog!!.dismiss()
//                                    }
//                                    .setNegativeButton(ContextManager.getInstance().getApplicationContext().resources.getString(R.string.dialog_button_no)) { dialog, id ->
//                                        adapterCarLoan!!.notifyDataSetChanged()
//                                       dialog!!.dismiss()
//                                    }
//                                    .show()
//
////                            adapterCarLoan!!.removeItem(position)
//                        }catch(e : Exception  ){
//                            e.printStackTrace()
//                        }
//                    }
//
//                    override fun onChildDraw(
//                            c: Canvas,
//                            recyclerView: RecyclerView,
//                            viewHolder: RecyclerView.ViewHolder,
//                            dX: Float,
//                            dY: Float,
//                            actionState: Int,
//                            isCurrentlyActive: Boolean
//                    ) {
//
//                        val icon: Bitmap
//                        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
//
//                            val itemView = viewHolder.itemView
//                            val height = itemView.bottom.toFloat() - itemView.top.toFloat()
//                            val width = height / 3
//
//                                p.color = Color.parseColor("#D32F2F")
//                                val background = RectF(
//                                        itemView.right.toFloat() + dX,
//                                        itemView.top.toFloat(),
//                                        itemView.right.toFloat(),
//                                        itemView.bottom.toFloat()
//                                )
//                                c.drawRect(background, p)
//                                icon = BitmapFactory.decodeResource(resources, R.drawable.delete)
//                                val icon_dest = RectF(
//                                        itemView.right.toFloat() - 2 * width,
//                                        itemView.top.toFloat() + width,
//                                        itemView.right.toFloat() - width,
//                                        itemView.bottom.toFloat() - width
//                                )
//                                c.drawBitmap(icon, null, icon_dest, p)
//                        }
//                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
//                    }
//                }
//        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
//        itemTouchHelper.attachToRecyclerView(recyclerView)
//    }

    private fun populateList(items : List<CarLoanViewModel.CarsLoanItems>): ArrayList<CarLoanViewModel.CarsLoanItems> {

        val list = ArrayList<CarLoanViewModel.CarsLoanItems>()

        items.forEach {
            val imageModel = CarLoanViewModel.CarsLoanItems(
                     cARIMAGE = it.cARIMAGE ,
                    cARMODEL = it.cARMODEL ,
                    cARGRADE = it.cARGRADE ,
                    cARPRICE = it.cARPRICE,
                     eXPIREDATE = it.eXPIREDATE ,
                     rEFID = it.rEFID,
                     rEFSTATUS = it.rEFSTATUS,
                     sTAMPDATE = it.sTAMPDATE,
                    rESDES = it.rESDES
            )
            list.add(imageModel)
        }

        return list
    }



    companion object {
        const val UNI_ID = "UNI_ID"
        const val IS_SHOW_DIALOG_FROM_DEEPLINK_CAR_LOAN = "IS_SHOW_DIALOG_FROM_DEEPLINK_CAR_LOAN"
        const val AUTHEN = "AUTHEN"

        fun newInstance() = CarLoanFragment()

        fun startByInsightDeeplink(context: Context?,
                            item: String) {
            if (UserManager.getInstance().isCustomer()) {
                MainCustomerActivity.openInsightWithDeep(
                        context = context,
                        position = MainCustomerActivity.CAR_LOAN,
                        data = Bundle().apply {
                            putString(UNI_ID, item)
                        }
                )
            } else {
                MainNonCustomerActivity.openInsightWithDeep(
                        context = context,
                        authen = "N" ,
                        position = MainNonCustomerActivity.CAR_LOAN,
                        data = Bundle().apply {
                            putString(UNI_ID, item)
                        }
                )
            }
        }

        fun startByInsight(context: Context? , authen : String) {
            if (UserManager.getInstance().isCustomer() ) {
                MainCustomerActivity.openInsightWithDeep(
                        context = context,
                        position = MainCustomerActivity.CAR_LOAN,
                        data = Bundle().apply {
                        }
                )
            } else {
                if(authen == "Y") {
                    MainNonCustomerActivity.openInsightWithDeep(
                            context = context,
                            authen = authen ,
                            position = MainNonCustomerActivity.CAR_LOAN,
                            data = Bundle().apply {
                            }
                    )
                }else {
                    MainNonCustomerActivity.openInsightWithDeep(
                            context = context,
                            authen = authen ,
                            position = MainNonCustomerActivity.CAR_LOAN,
                            data = Bundle().apply {
                                putString(AUTHEN, authen)
                            }
                    )
                }
            }
        }
    }
}
