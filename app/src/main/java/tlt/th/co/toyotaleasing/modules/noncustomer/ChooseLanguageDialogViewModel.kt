package tlt.th.co.toyotaleasing.modules.noncustomer

import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager
import tlt.th.co.toyotaleasing.model.request.SettingLanguageRequest

class ChooseLanguageDialogViewModel : BaseViewModel() {

    fun updateLanguage(lang : String = "TH") {
        val request = SettingLanguageRequest.build(lang)

        TLTApiManager.getInstance()
                .updateLanguage(request) { isError, result -> }
    }

}