package tlt.th.co.toyotaleasing.modules.editaddress.installment

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast

import kotlinx.android.synthetic.main.fragment_edit_installment_address.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseFragment
import tlt.th.co.toyotaleasing.common.extension.loadImageByUri

class EditInstallmentAddressFragment : BaseFragment(),
        EditInstallmentAddressSuccessDialogFragment.Listener {

    private var imageUri: Uri? = null
    private var setProvinceByDefault: Boolean = false
    private var setDistrictByDefault: Boolean = false

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(EditInstallmentAdressViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit_installment_address, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initInstances()

        viewModel.getDefaultData()
    }

    private fun initViewModel() {
        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!, this)
        })

        viewModel.whenProvinceLoaded.observe(this, Observer {
            spinner_province.setItems(it!!)
        })

        viewModel.whenDistrictReload.observe(this, Observer {
            spinner_district.setItems(it!!)
        })

        viewModel.whenPostcodeLoaded.observe(this, Observer {
            edittext_postcode.setText(it)
        })

        viewModel.whenDataLoaded.observe(this, Observer {
            setupDataIntoViews(it!!)
        })

        viewModel.whenSubmitFormFailure.observe(this, Observer {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })

        viewModel.whenSubmitFormSuccess.observe(this, Observer {
            EditInstallmentAddressSuccessDialogFragment.show(fragmentManager!!, this)
        })
    }

    private fun initInstances() {
        edittext_address_line_1.addTextChangedListener(onEdittextTextWatcher)
        edittext_address_line_2.addTextChangedListener(onEdittextTextWatcher)
        edittext_address_line_3.addTextChangedListener(onEdittextTextWatcher)
        edittext_address_line_4.addTextChangedListener(onEdittextTextWatcher)
        edittext_postcode.addTextChangedListener(onEdittextTextWatcher)



        spinner_province.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (!setProvinceByDefault) {
                    viewModel.provinceChange(if (position > 0) position - 1 else 0)
                    formValidate()
                }
                setProvinceByDefault = false
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        spinner_district.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (!setDistrictByDefault) {
                    viewModel.amphurChange(if (position > 0) position - 1 else 0)
                    formValidate()
                }
                setDistrictByDefault = false
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        checkbox_accept_use_address_every_contract.setOnClickListener {
            AnalyticsManager.addressUseAllAddressClicked()
            formValidate()
        }

        btn_upload.setOnClickListener {
        }

        btn_confirm.setOnClickListener {
            AnalyticsManager.addressConfirmClicked()
            viewModel.submitForm(
                    address_line_1 = edittext_address_line_1.text.toString(),
                    address_line_2 = edittext_address_line_2.text.toString(),
                    address_line_3 = edittext_address_line_3.text.toString(),
                    address_line_4 = edittext_address_line_4.text.toString(),
                    districtPosition = spinner_district.getSelectedPositionWithoutHint(),
                    provincePosition = spinner_province.getSelectedPositionWithoutHint(),
                    postcode = edittext_postcode.text.toString(),
                    isUseInEveryContract = checkbox_accept_use_address_every_contract.isChecked,
                    imageUri = imageUri!!)
        }
    }

    private fun setupDataIntoViews(it: EditInstallmentAdressViewModel.Model) {
        setProvinceByDefault = true
        setDistrictByDefault = true

        edittext_address_line_1.setText(it.address)
        edittext_address_line_2.setText(it.soi)
        edittext_address_line_3.setText(it.road)
        edittext_address_line_4.setText(it.subdistrict)
//        spinner_province.setSelection(it.provincePosition + 1)
//        spinner_district.setSelection(it.districtPosition + 1)
        spinner_province.setSelection(it.provincePosition)
        spinner_district.setSelection(it.districtPosition)
        edittext_postcode.setText(it.postcode)

    }

    private val onEdittextTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(message: CharSequence?, start: Int, before: Int, count: Int) {
            formValidate()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun formValidate() {
        if (edittext_address_line_1.text.toString().trim().isEmpty()
                || edittext_postcode.text.toString().trim().isEmpty()
                || imageUri == null
                || spinner_province.isSelectHint()
                || spinner_district.isSelectHint()) {
            btn_confirm.isEnabled = false
            return
        }

        btn_confirm.isEnabled = true
    }

    override fun onEditInstallmentAddressSuccessConfirmClick() {
        AnalyticsManager.trackScreenDialog(AnalyticsScreenName.INSTALLMENT_MY_CONTRACT_CHANGE_ADDRESS_CONFIRM)
        AnalyticsManager.addressConfirmEnterClicked()
        activity?.finish()
    }

    companion object {
        fun newInstance() = EditInstallmentAddressFragment()
    }
}
