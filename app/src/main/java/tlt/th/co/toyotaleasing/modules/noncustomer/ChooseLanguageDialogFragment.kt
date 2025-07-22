package tlt.th.co.toyotaleasing.modules.noncustomer

import android.app.Dialog
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.appcompat.app.AlertDialog
import android.text.Html
import android.text.Spanned
import tlt.th.co.toyotaleasing.manager.LocalizeManager
import tlt.th.co.toyotaleasing.modules.main.MainActivity

class ChooseLanguageDialogFragment : DialogFragment() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(ChooseLanguageDialogViewModel::class.java)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        isCancelable = false
        return activity?.let {

            var THAI = ""
            var ENGLISH = ""

            if (LocalizeManager.isThai()) {
                THAI = "<font color='#307DFA'>ภาษาไทย</font>"
                ENGLISH = "English"
            } else {
                THAI = "ภาษาไทย"
                ENGLISH = "<font color='#307DFA'>ENGLISH</font>"
            }

            val builder = AlertDialog.Builder(it)
            builder.setTitle("กรุณาเลือกภาษา\nPlease select language")
                    .setItems(arrayOf<Spanned>(Html.fromHtml(ENGLISH), Html.fromHtml(THAI))
                    ) { _, which ->
                        if (which == 0) {
                            viewModel.updateLanguage(LocalizeManager.ENGLISH)
                            LocalizeManager.changeToEN(context!!)
                        } else {
                            viewModel.updateLanguage(LocalizeManager.THAI)
                            LocalizeManager.changeToTH(context!!)
                        }
                        LocalizeManager.setIsFirstTime(false)
                        MainActivity.openWithClearStack(context!!)
                    }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}
