package com.wasiur.customviews;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SlidingDrawer;

@SuppressWarnings("deprecation")
public class ExtendedSlidingDrawer extends SlidingDrawer {

    private boolean mVertical;
    private int mTopOffset;

	public ExtendedSlidingDrawer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        int orientation = attrs
                .getAttributeIntValue("android", "orientation", ORIENTATION_VERTICAL);
        mTopOffset = attrs.getAttributeIntValue("android", "topOffset", 0);
        mVertical = (orientation == SlidingDrawer.ORIENTATION_VERTICAL);
    }

	public ExtendedSlidingDrawer(Context context, AttributeSet attrs) {
        super(context, attrs);

        int orientation = attrs
                .getAttributeIntValue("android", "orientation", ORIENTATION_VERTICAL);
        mTopOffset = attrs.getAttributeIntValue("android", "topOffset", 0);
        mVertical = (orientation == SlidingDrawer.ORIENTATION_VERTICAL);
    }

	@Override
    protected void dispatchDraw(Canvas canvas) {
        final long drawingTime = getDrawingTime();
		final View handle = getHandle();
        final boolean isVertical = mVertical;

        drawChild(canvas, handle, drawingTime);

        //if (mTracking || mAnimating) {
		final Bitmap cache = getContent().getDrawingCache();
        if (cache != null) {
            if (isVertical) {
                canvas.drawBitmap(cache, 0, handle.getBottom(), null);
            } else {
                canvas.drawBitmap(cache, handle.getRight(), 0, null);
            }
        } else {
            canvas.save();
            canvas.translate(isVertical ? 0 : handle.getLeft() - mTopOffset,
                        isVertical ? handle.getTop() - mTopOffset : 0);
            drawChild(canvas, getContent(), drawingTime);
            canvas.restore();
        }
        //} else if (mExpanded) {
        //drawChild(canvas, mContent, drawingTime);
        //}
    }
}