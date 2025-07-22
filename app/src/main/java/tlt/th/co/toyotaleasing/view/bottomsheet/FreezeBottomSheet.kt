package tlt.th.co.toyotaleasing.view.bottomsheet

import android.content.Context
import com.google.android.material.bottomsheet.BottomSheetBehavior
import androidx.coordinatorlayout.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class FreezeBottomSheet<V : View>(context: Context, attrs: AttributeSet) : com.google.android.material.bottomsheet.BottomSheetBehavior<V>(context, attrs) {

    override fun onInterceptTouchEvent(parent: CoordinatorLayout, child: V, event: MotionEvent): Boolean {
        return false
    }

    override fun onTouchEvent(parent: CoordinatorLayout, child: V, event: MotionEvent): Boolean {
        return false
    }
}