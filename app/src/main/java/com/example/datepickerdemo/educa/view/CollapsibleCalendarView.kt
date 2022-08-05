package com.example.datepickerdemo.educa.view

import android.content.Context
import android.util.AttributeSet
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.GridLayout
import com.example.datepickerdemo.common.BaseCalendarView

class CollapsibleCalendarView : BaseCalendarView {

    private var mInitHeight: Int = 0
    var expanded = false

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun initViews() {
        super.initViews()
//        expandIconView.setState(ExpandIconView.MORE, true)
//        collapse(400)
        expandIconView.setOnClickListener {
            if (expanded) {
                collapse(400)
            } else {
                expand(400)
            }
        }

        post { collapseTo(0) }
    }

    private fun collapseTo(index: Int) {
        if (state == STATE_COLLAPSED) {
            val targetHeight = rv.getChildAt(index).measuredHeight
            svContainer.apply {
                layoutParams.height = targetHeight
                requestLayout()
            }
        }
    }

    override var state: Int
        get() = super.state
        set(state) {
            super.state = state
            if (state == STATE_COLLAPSED) {
                expanded = false
            }
            if (state == STATE_EXPANDED) {
                expanded = true
            }
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (rv.measuredHeight > mInitHeight)
            mInitHeight = rv.measuredHeight
    }

    private fun collapse(duration: Int) {
        if (state == STATE_EXPANDED) {
            state = STATE_PROCESSING
            mAdapter.getItemHeight()
            val currentHeight = mInitHeight
            val anim = object : Animation() {
                override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                    svContainer.apply {
                        if (mAdapter.itemCount > 0) {
                            val targetHeight = rv.getChildAt(0).height
                            layoutParams.height =
                                if (interpolatedTime == 1F) targetHeight else currentHeight - ((currentHeight - targetHeight) * interpolatedTime).toInt()
                        }
                        requestLayout()
                        if (interpolatedTime == 1F)
                            state = STATE_COLLAPSED
                    }
                }
            }
            anim.duration = duration.toLong()
            startAnimation(anim)
        }
        expandIconView.setState(ExpandIconView.MORE, true)
    }

    private fun expand(duration: Int) {
        if (state == STATE_COLLAPSED) {
            state = STATE_PROCESSING

            val currentHeight = svContainer.measuredHeight
            val targetHeight = mInitHeight

            val anim = object : Animation() {
                override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                    svContainer.apply {
                        layoutParams.height =
                            if (interpolatedTime == 1F) GridLayout.LayoutParams.WRAP_CONTENT else currentHeight - ((currentHeight - targetHeight) * interpolatedTime).toInt()
                        requestLayout()
                        if (interpolatedTime == 1F)
                            state = STATE_EXPANDED
                    }
                }
            }
            anim.duration = duration.toLong()
            startAnimation(anim)
        }
        expandIconView.setState(ExpandIconView.LESS, true)
    }
}