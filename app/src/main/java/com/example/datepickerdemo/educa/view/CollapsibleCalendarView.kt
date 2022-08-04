package com.example.datepickerdemo.educa.view

import android.content.Context
import android.util.AttributeSet
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.GridLayout
import com.example.datepickerdemo.DateSelect
import com.example.datepickerdemo.common.BaseCalendarView
import java.util.*

class CollapsibleCalendarView : BaseCalendarView {

    var expanded = false
    private var currentHeight = 0

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun initViews() {
        super.initViews()
        expandIconView.setState(ExpandIconView.LESS, true)
        expandIconView.setOnClickListener {
            if (expanded) {
                collapse(400)
            } else {
                expand(400)
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

    private fun collapse(duration: Int) {
        if (state == STATE_EXPANDED) {
            state = STATE_PROCESSING
            adapter.getItemHeight()
            val anim = object : Animation() {
                override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                    rv?.apply {
                        currentHeight = layoutParams.height
                        layoutParams.height = getChildAt(0).height
                        requestLayout()
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
            val anim = object : Animation() {
                override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                    rv?.apply {
                        layoutParams.height = GridLayout.LayoutParams.WRAP_CONTENT
                        requestLayout()
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