package tlt.th.co.toyotaleasing.manager

import org.greenrobot.eventbus.EventBus

object RouterManager {
    fun register(objects: Any) {
        if (EventBus.getDefault().isRegistered(objects)) {
            return
        }

        EventBus.getDefault().register(objects)
    }

    fun unregister(objects: Any) {
        if (!EventBus.getDefault().isRegistered(objects)) {
            return
        }

        EventBus.getDefault().unregister(objects)
    }

    fun sendSignal(objects: Any) {
        EventBus.getDefault().post(objects)
    }
}