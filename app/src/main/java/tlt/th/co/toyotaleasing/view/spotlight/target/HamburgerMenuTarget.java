package tlt.th.co.toyotaleasing.view.spotlight.target;

import android.animation.TimeInterpolator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import tlt.th.co.toyotaleasing.R;
import tlt.th.co.toyotaleasing.view.spotlight.OnTargetStateChangedListener;
import tlt.th.co.toyotaleasing.view.spotlight.shape.Shape;

public class HamburgerMenuTarget extends Target {

    private HamburgerMenuTarget(Shape shape, PointF point, View overlay, long duration, TimeInterpolator animation, OnTargetStateChangedListener listener) {
        super(shape, point, overlay, duration, animation, listener);
    }

    public static class Builder extends AbstractTargetBuilder<Builder, HamburgerMenuTarget> {

        @Override
        protected Builder self() {
            return this;
        }

        private CharSequence title;
        private CharSequence description;

        public Builder(@NonNull Activity context) {
            super(context);
        }

        public Builder setTitle(CharSequence title) {
            this.title = title;
            return this;
        }

        public Builder setDescription(CharSequence description) {
            this.description = description;
            return this;
        }

        @Override
        public HamburgerMenuTarget build() {
            View overlay = getContext().getLayoutInflater().inflate(R.layout.item_tutorial_screen1, null);
            ((TextView) overlay.findViewById(R.id.title)).setText(title);
            ((TextView) overlay.findViewById(R.id.description)).setText(description);
            calculatePosition(point, shape, overlay);
            return new HamburgerMenuTarget(shape, point, overlay, duration, animation, listener);
        }

        private void calculatePosition(final PointF point, final Shape shape, View overlay) {
            Point screenSize = new Point();
            ((WindowManager) overlay.getContext()
                    .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(screenSize);

            final ConstraintLayout layout = overlay.findViewById(R.id.container);
            layout.setPadding((shape.getWidth() / 2) + 10, 0, 0, 0);
            layout.setY((int) (point.y - 10));
        }
    }
}