package com.kroegerama.kaiteki.recyclerview.example.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper
import kotlin.math.abs


class PullDismissLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val dragHelper: ViewDragHelper
    private var verticalTouchSlop: Float = 0f

    var listener: Listener? = null
    var animateAlpha: Boolean = false
    var minFlingVelocity: Float = 0f
    var heightPercent: Float = 1f
    var removeView: Boolean = true
    var alphaPercent: Float = 1f

    init {
        if (!isInEditMode) {
            val vc = ViewConfiguration.get(context)
            minFlingVelocity = vc.scaledMinimumFlingVelocity.toFloat()
        }
        dragHelper = ViewDragHelper.create(this, ViewDragCallback(this))
    }

    override fun computeScroll() {
        super.computeScroll()
        if (dragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        var pullingDown = false

        when (action) {
            MotionEvent.ACTION_DOWN -> {
                verticalTouchSlop = event.y
                val dy = event.y - verticalTouchSlop
                if (dy > dragHelper.touchSlop) {
                    pullingDown = true
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val dy = event.y - verticalTouchSlop
                if (dy > dragHelper.touchSlop) {
                    pullingDown = true
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> verticalTouchSlop = 0.0f
        }

        if (!dragHelper.shouldInterceptTouchEvent(event) && pullingDown) {
            if (dragHelper.viewDragState == ViewDragHelper.STATE_IDLE && dragHelper.checkTouchSlop(ViewDragHelper.DIRECTION_VERTICAL)) {
                val child = getChildAt(0)
                if (child != null && listener?.onShouldInterceptTouchEvent() == false) {
                    dragHelper.captureChildView(child, event.getPointerId(0))
                    return dragHelper.viewDragState == ViewDragHelper.STATE_DRAGGING
                }
            }
        }
        return false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        dragHelper.processTouchEvent(event)
        return dragHelper.capturedView != null
    }

    private class ViewDragCallback(
        private val parent: PullDismissLayout
    ) : ViewDragHelper.Callback() {
        private var startTop: Int = 0
        private var dragPercent: Float = 0f
        private var capturedView: View? = null
        private var dismissed: Boolean = false

        override fun tryCaptureView(view: View, i: Int) = capturedView == null

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int) = maxOf(0, top)

        override fun onViewCaptured(view: View, activePointerId: Int) {
            capturedView = view
            startTop = view.top
            dragPercent = 0.0f
            dismissed = false
        }

        override fun onViewPositionChanged(view: View, left: Int, top: Int, dx: Int, dy: Int) {
            val range = parent.height * parent.heightPercent
            val moved = abs(top - startTop)
            if (range > 0) {
                dragPercent = moved.toFloat() / range
            }
            if (parent.animateAlpha) {
                view.alpha = 1.0f - dragPercent * parent.alphaPercent
                parent.invalidate()
            }
        }

        override fun onViewDragStateChanged(state: Int) {
            if (capturedView != null && dismissed && state == ViewDragHelper.STATE_IDLE) {
                if (parent.removeView) parent.removeView(capturedView)
                parent.listener?.onDismissed()
            }
        }

        override fun onViewReleased(view: View, xv: Float, yv: Float) {
            dismissed = dragPercent >= 0.50f || abs(yv) > parent.minFlingVelocity && dragPercent > 0.20f
            val finalTop = when {
                !dismissed -> startTop
                yv > parent.minFlingVelocity -> (parent.height * parent.heightPercent).toInt()
                else -> view.top
            }
            parent.dragHelper.settleCapturedViewAt(0, finalTop)
            parent.invalidate()
        }
    }

    interface Listener {
        /**
         * Layout is pulled down to dismiss
         * Good time to finish activity, remove fragment or any view
         */
        fun onDismissed()

        /**
         * Convenient method to avoid layout overriding event
         * If you have a RecyclerView or ScrollerView in our layout your can
         * avoid PullDismissLayout to handle event.
         *
         * @return true when ignore pull down event, f
         * false for allow PullDismissLayout handle event
         */
        fun onShouldInterceptTouchEvent(): Boolean
    }

    companion object {
        private const val TAG = "PullDismissLayout"
    }
}