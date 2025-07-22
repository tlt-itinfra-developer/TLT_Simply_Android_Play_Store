package tlt.th.co.toyotaleasing.modules.setting.changelanguage

import tlt.th.co.toyotaleasing.common.base.BaseViewModel

import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager
import tlt.th.co.toyotaleasing.model.request.SettingLanguageRequest

class ChangeLanguageViewModel : BaseViewModel() {

    fun updateLanguage(lang: String = "TH") {
        whenLoading.postValue(true)
        val request = SettingLanguageRequest.build(lang)

        TLTApiManager.getInstance()
                .updateLanguage(request) { isError, result ->
                    whenLoading.postValue(false)

                    if (isError) {
                        return@updateLanguage
                    }
                }
    }
}