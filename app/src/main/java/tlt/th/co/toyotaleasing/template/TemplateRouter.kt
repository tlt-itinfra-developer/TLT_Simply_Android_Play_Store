package tlt.th.co.toyotaleasing.template

import android.content.Context
import android.content.Intent
import android.os.Bundle
import tlt.th.co.toyotaleasing.common.base.BaseActivity

class TemplateRouter : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFlow()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun initFlow() {

    }

    companion object {
        fun open(context: Context) {
            val intent = Intent(context, TemplateRouter::class.java)
            context.startActivity(intent)
        }
    }
}
