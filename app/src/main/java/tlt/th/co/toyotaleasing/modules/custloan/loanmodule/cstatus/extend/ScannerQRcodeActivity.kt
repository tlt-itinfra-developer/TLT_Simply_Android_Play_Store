package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.cstatus.extend


import android.content.Context
import android.content.Intent
import android.view.View
import com.journeyapps.barcodescanner.CaptureActivity
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import kotlinx.android.synthetic.main.fragment_custom_scanner.*
import tlt.th.co.toyotaleasing.R

class ScannerQRcodeActivity  : CaptureActivity(){


     override fun initializeContent(): DecoratedBarcodeView {
         setContentView(R.layout.fragment_custom_scanner)

         btn_close.setOnClickListener {
             onBackPressed()

         }

         return findViewById<View>(R.id.zxing_barcode_scanner) as DecoratedBarcodeView
     }


    companion object {
        fun open( context: Context) {
            val intent = Intent(context, ScannerQRcodeActivity::class.java)
            context.startActivity(intent)
        }
    }

}