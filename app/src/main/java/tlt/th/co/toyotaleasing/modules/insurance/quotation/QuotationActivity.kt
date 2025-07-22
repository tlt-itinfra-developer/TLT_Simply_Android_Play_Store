package tlt.th.co.toyotaleasing.modules.insurance.quotation

import android.app.Activity
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.appcompat.widget.AppCompatEditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_quotation.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.dialog.NormalDialogFragment
import tlt.th.co.toyotaleasing.common.extension.*

class QuotationActivity : BaseActivity(), NormalDialogFragment.Listener {

    private lateinit var viewModel: QuotationViewModel
    private val data: ArrayList<QuotationModel> by lazy {
        arrayListOf<QuotationModel>()
    }
    private var liveData = MediatorLiveData<ArrayList<QuotationModel>>()
    private var radioSelect = 0
    private var dataUser = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quotation)

        initViewModel()
        viewModel.getData()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(QuotationViewModel::class.java)

        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!)
        })

        viewModel.whenDefaultDataLoaded.observe(this, Observer {
            it?.let {
                initInstances(it)
                setupDataIntoViews(it)
                it.isStaffApp.ifTrue { supportForStaff(it) }
            }
        })

        viewModel.whenDisplayByStatus.observe(this, Observer {
            when (it!!) {
                QuotationViewModel.Status.CUSTOMER -> customerMode()
                else -> nonCustomerMode()
            }
        })

        viewModel.whenSendQuotationFailed.observe(this, Observer {
            Toast.makeText(this@QuotationActivity, it!!, Toast.LENGTH_SHORT).show()
        })

        viewModel.whenSendQuotationSuccess.observe(this, Observer {

        })

        liveData.observe(this, Observer<ArrayList<QuotationModel>> { _ ->
            checkInput()
        })
    }

    private fun initInstances(it: QuotationViewModel.Model) {
        data.add(QuotationModel(0, 0, 0))
        liveData.value = data

        activity_quotation_date.text = it.date

        recycler_view.apply {
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(this@QuotationActivity, LinearLayoutManager.VERTICAL, false)
            adapter = QuotationAdapter(data, liveData, it.insuranceCompanyList, it.insurancePolicyList, it.paymentMethodList)
            (adapter as QuotationAdapter).registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {

                override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                    super.onItemRangeRemoved(positionStart, itemCount)
                    form_quotation_btn_add_insurance.visibility = View.VISIBLE
                }

                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    super.onItemRangeInserted(positionStart, itemCount)
                    if (positionStart >= 3) {
                        form_quotation_btn_add_insurance.visibility = View.GONE
                    }
                }
            })
        }

        form_quotation_btn_add_insurance.setOnClickListener {
            data.add(QuotationModel(0, 0, 0))
            liveData.value = data
            recycler_view.adapter!!.notifyItemInserted(data.size)
        }

        if (dataUser) {
            //ถ้ามีข้อมูล
            setEdittextWatcher(activity_quotation_phone_tv, activity_quotation_email_tv,
                    form_quotation_input_name, form_quotation_input_tel,
                    form_quotation_input_email, form_quotation_input_car_number,
                    form_quotation_car_model)
        } else {
            //ถ้าไม่มีข้อมูล
            setEdittextWatcher(activity_quotation_phone_tv, activity_quotation_email_tv,
                    form_quotation_input_name, form_quotation_input_tel,
                    form_quotation_input_email, form_quotation_input_car_number,
                    form_quotation_car_model)

            activity_quotation_car_city_edit_spinner.setItems(it.provinceList)
        }

        form_quotation_input_more_detail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                if (form_quotation_input_more_detail.layout.lineCount > 4)
                    form_quotation_input_more_detail.text!!.delete(form_quotation_input_more_detail.text!!.length - 1, form_quotation_input_more_detail.text!!.length)
            }
        })

        //ใส่ *
        activity_quotation_topic_1.setText(getString(R.string.activity_quotation_topic_1), "*", "", R.color.cherry_red)
        activity_quotation_topic_2.setText(getString(R.string.activity_quotation_topic_2), "*", "", R.color.cherry_red)
//        activity_quotation_topic_3.setText(getString(R.string.activity_quotation_topic_3), "*", "", R.color.cherry_red)
        form_quotation_radio_call.setText(getString(R.string.activity_quotation_radio_1), "*", "", R.color.cherry_red)
        form_quotation_radio_email.setText(getString(R.string.activity_quotation_radio_2), "*", "", R.color.cherry_red)

        activity_quotation_radio.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.form_quotation_radio_call -> {
                    radioSelect = 0
                }
                R.id.form_quotation_radio_email -> {
                    radioSelect = 1
                }
            }
        }

        form_quotation_radio_call.performClick()

        //ดูข้อมูล ประเภทความคุ้มครอง
        form_quotation_txt_view_coverage_type.setOnClickListener {
            QuotationDialogFragment.show(supportFragmentManager)
        }

        //เมื่อกด ขอใบเสนอราคา
        form_quotation_btn_submit.setOnClickListener {
            val isCustomer = viewModel.isCustomer()
            val name = if (isCustomer) {
                viewModel.whenDefaultDataLoaded.value?.fullname
            } else {
                form_quotation_input_name.text.toString().trim()
            }

            val tel = if (isCustomer) {
//                viewModel.whenDefaultDataLoaded.value?.phoneNumber
                activity_quotation_phone_tv.text.toString().trim()
            } else {
                form_quotation_input_tel.text.toString().trim()
            }

            val email = if (isCustomer) {
//                viewModel.whenDefaultDataLoaded.value?.email
                activity_quotation_email_tv.text.toString().trim()
            } else {
                form_quotation_input_email.text.toString().trim()
            }

            val carLicense = if (isCustomer) {
                viewModel.whenDefaultDataLoaded.value?.carLicense
            } else {
                form_quotation_input_car_number.text.toString().trim()
            }

            val carModel = if (isCustomer) {
                viewModel.whenDefaultDataLoaded.value?.vehicalModel
            } else {
                form_quotation_car_model.text.toString().trim()
            }

            val isStaffContact = radioSelect == 0

            viewModel.requestQuotation(
                    name ?: "",
                    tel ?: "",
                    email ?: "",
                    carLicense ?: "",
                    activity_quotation_car_city_edit_spinner.getSelectedPositionWithoutHint(),
                    carModel ?: "",
                    data,
                    form_quotation_input_more_detail.text.toString().trim(),
                    isStaffContact
            )

            val messageDialog = if (isStaffContact) {
                getString(R.string.quotation_staff_contact_message_dialog)
            } else {
                getString(R.string.quotation_email_contact_message_dialog)
            }

            NormalDialogFragment.show(
                    fragmentManager = supportFragmentManager,
                    description = messageDialog,
                    confirmButtonMessage = getString(R.string.refinance_diaglog_btn_confirm)
            )
        }
    }

    private fun supportForStaff(it: QuotationViewModel.Model) {

    }

    private fun setEdittextWatcher(vararg view: AppCompatEditText) {
        view.forEach {
            it.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    checkInput()
                }

                override fun afterTextChanged(s: Editable) {}
            })
        }
    }

    private fun checkInput() {
        if (dataUser
                && activity_quotation_phone_tv.text!!.isNotBlank()
                && activity_quotation_email_tv.text!!.isNotBlank()) {
            liveData.value?.forEach {
                if (it.company == 0 || it.converageType == 0 || it.payment == 0) {
                    form_quotation_btn_submit.isEnabled = false
                    form_quotation_btn_submit.setTextColor(ContextCompat.getColor(this, R.color.text_normal_lighter))
                    return
                }
            }
            form_quotation_btn_submit.isEnabled = true
            form_quotation_btn_submit.setTextColor(ContextCompat.getColor(this, R.color.white))
            return
        }

        if (!dataUser
                && form_quotation_input_name.text!!.isNotBlank()
                && form_quotation_input_tel.text!!.isNotBlank()
                && form_quotation_input_email.text!!.isNotBlank()
                && form_quotation_input_car_number.text!!.isNotBlank()
                && !activity_quotation_car_city_edit_spinner.isSelectHint()
                && form_quotation_car_model.text!!.isNotBlank()) {
            liveData.value?.forEach {
                if (it.company == 0 || it.converageType == 0 || it.payment == 0) {
                    form_quotation_btn_submit.isEnabled = false
                    form_quotation_btn_submit.setTextColor(ContextCompat.getColor(this, R.color.text_normal_lighter))
                    return
                }
            }

            form_quotation_btn_submit.isEnabled = true
            form_quotation_btn_submit.setTextColor(ContextCompat.getColor(this, R.color.white))
            return
        }

        form_quotation_btn_submit.isEnabled = false
        form_quotation_btn_submit.setTextColor(ContextCompat.getColor(this, R.color.text_normal_lighter))
    }

    override fun onDialogConfirmClick() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onDialogCancelClick() {

    }

    //    setLayoutเมื่อไม่มีข้อมูล
    private fun nonCustomerMode() {
        dataUser = false
        activity_quotation_layout.gone()
        activity_quotation_layout_non.visible()
    }

    //    setLayoutเมื่อมีข้อมูล
    private fun customerMode() {
        dataUser = true
        activity_quotation_layout.visible()
        activity_quotation_layout_non.gone()
    }

    private fun setupDataIntoViews(it: QuotationViewModel.Model) {
        form_quotation_txt_name.text = getString(R.string.activity_quotation_name_tv, it.fullname)
        activity_quotation_car_number_city_tv.text = getString(R.string.activity_quotation_car_number_city_tv, it.carLicense)
        activity_quotation_car_number_contract_tv.text = getString(R.string.activity_quotation_car_number_contract_tv, it.contractNumber)
        activity_quotation_car_series_tv.text = getString(R.string.activity_quotation_car_series_tv, it.vehicalModel)
//        activity_quotation_phone_tv.setText(it.phoneNumber)
//        activity_quotation_email_tv.setText(it.email)
    }

    companion object {
        const val REQUEST_CODE = 301

        fun startWithResult(fragment: Fragment?) {
            val intent = Intent(fragment?.context, QuotationActivity::class.java)
            fragment?.startActivityForResult(intent, REQUEST_CODE)
        }

        fun startWithResult(activity: Activity?) {
            val intent = Intent(activity, QuotationActivity::class.java)
            activity?.startActivityForResult(intent, REQUEST_CODE)
        }
    }
}
