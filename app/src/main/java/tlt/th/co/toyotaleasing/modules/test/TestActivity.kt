package tlt.th.co.toyotaleasing.modules.test

import android.content.Intent
import android.os.Bundle
import com.linecorp.linesdk.LineApiResponseCode
import com.linecorp.linesdk.auth.LineLoginApi
import kotlinx.android.synthetic.main.activity_test.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.base.BaseLoadingScreenDialogFragment


class TestActivity : BaseActivity() {

    private var loadingScreenDialog = BaseLoadingScreenDialogFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

//        loadingScreenDialog.showDialogActivity(supportFragmentManager, "BaseFragmentDialogLoadingScreen")

        btn.setOnClickListener {
            loadingScreenDialog.dismissDialog()
//            val lineLoginIntent = LineLoginApi.getLoginIntent(this, BuildConfig.LINE_CHANNEL_ID)
//            startActivityForResult(lineLoginIntent, 1)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode != 1) {
            return
        }

        val result = LineLoginApi.getLoginResultFromIntent(data)

        when (result.responseCode) {
            LineApiResponseCode.SUCCESS -> {
                txt_description.text = result.lineCredential!!.accessToken.accessToken
            }
            LineApiResponseCode.CANCEL -> {
                txt_description.text = result.errorData.message
            }
            else -> {
                txt_description.text = result.errorData.message
            }
        }
    }

    companion object {
    }
}
