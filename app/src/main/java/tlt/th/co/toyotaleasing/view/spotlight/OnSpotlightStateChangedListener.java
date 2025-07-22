package tlt.th.co.toyotaleasing.view.spotlight;

public interface OnSpotlightStateChangedListener {

    /**
     * Called when Spotlight is started
     */
    void onStarted();

    /**
     * Called when Spotlight is ended
     */
    void onEnded();

    /**
     * Called when position changed
     */
    void onPositionChanged(int position);
}