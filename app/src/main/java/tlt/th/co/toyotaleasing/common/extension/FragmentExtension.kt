package tlt.th.co.toyotaleasing.common.extension

import androidx.fragment.app.Fragment
import android.widget.Toast

fun Fragment.showToast(message: String? = "") {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}