package tlt.th.co.toyotaleasing.view.spotlight;

import tlt.th.co.toyotaleasing.view.spotlight.target.Target;

public interface OnTargetStateChangedListener<T extends Target> {
    /**
     * Called when Target is started
     */
    void onStarted(T target);

    /**
     * Called when Target is started
     */
    void onEnded(T target);
}