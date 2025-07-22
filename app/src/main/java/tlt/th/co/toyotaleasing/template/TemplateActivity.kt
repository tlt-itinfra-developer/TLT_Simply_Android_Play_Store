package tlt.th.co.toyotaleasing.template

import android.content.Context
import android.content.Intent
import android.os.Bundle
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseActivity

class TemplateActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_template)
    }

    companion object {
        fun start(context: Context?) {
            val intent = Intent(context, TemplateActivity::class.java)
            context?.startActivity(intent)
        }
    }
}
