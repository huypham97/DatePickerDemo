package com.example.datepickerdemo.common

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.ScrollView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.datepickerdemo.DateSelect
import com.example.datepickerdemo.R
import com.example.datepickerdemo.educa.DateEducaRecyclerViewAdapter
import com.example.datepickerdemo.educa.view.ExpandIconView

abstract class BaseCalendarView : ScrollView {

    private var onButtonPrevClickListener: (() -> Unit)? = null
    private var onButtonNextClickListener: (() -> Unit)? = null
    protected lateinit var mInflater: LayoutInflater
    protected lateinit var expandIconView: ExpandIconView
    protected var mExpandIconColor = Color.BLACK
    protected lateinit var rv: RecyclerView
    protected lateinit var mAdapter: DateEducaRecyclerViewAdapter
    private var listener: DateEducaRecyclerViewAdapter.OnDateSelectListener? = null
    private lateinit var btnMonthPrev: ImageButton
    private lateinit var btnMonthNext: ImageButton
    private lateinit var tvMonthYear: TextView
    protected lateinit var svContainer: ScrollView

    open var state = STATE_COLLAPSED

    companion object {
        // State
        const val STATE_EXPANDED = 0
        const val STATE_COLLAPSED = 1
        const val STATE_PROCESSING = 2
    }

    constructor(context: Context?) : super(context) {
        initViews()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initViews()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initViews()
    }

    protected open fun initViews() {
        mInflater = LayoutInflater.from(context)
        val rootView = mInflater.inflate(R.layout.calendar_view, this, true)
        expandIconView = rootView.findViewById(R.id.expandIcon)
        rv = rootView.findViewById(R.id.rv_calendar)
        btnMonthPrev = rootView.findViewById(R.id.ib_month_prev)
        btnMonthNext = rootView.findViewById(R.id.ib_month_next)
        tvMonthYear = rootView.findViewById(R.id.tv_month_year)
        svContainer = rootView.findViewById(R.id.sv_container)

        mAdapter = DateEducaRecyclerViewAdapter(context)
        listener?.let { mAdapter.setOnDateSelectListener(it) }
        rv?.apply {
            layoutManager = GridLayoutManager(context, 7)
            adapter = mAdapter
        }
        btnMonthPrev.setOnClickListener {
            onButtonPrevClickListener?.invoke()
        }
        btnMonthNext.setOnClickListener {
            onButtonNextClickListener?.invoke()
        }
    }

    fun setExpandIconColor(color: Int) {
        this.mExpandIconColor = color
        expandIconView.setColor(color)
    }

    fun setOnDateSelectListener(listener: DateEducaRecyclerViewAdapter.OnDateSelectListener) {
        this.listener = listener
        mAdapter.setOnDateSelectListener(listener)
    }

    fun replaceDataList(days: MutableList<DateSelect>) {
        mAdapter.replaceDataList(days)
    }

    fun setOnButtonPrevClickListener(onButtonPrevClickListener: (() -> Unit)) {
        this.onButtonPrevClickListener = onButtonPrevClickListener
    }

    fun setOnButtonNextClickListener(onButtonNextClickListener: (() -> Unit)) {
        this.onButtonNextClickListener = onButtonNextClickListener
    }

    fun setMonthYearData(data: String) {
        this.tvMonthYear.text = data
    }
}