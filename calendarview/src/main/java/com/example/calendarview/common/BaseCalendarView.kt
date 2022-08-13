package com.example.calendarview.common

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calendarview.DateEducaRecyclerViewAdapter
import com.example.calendarview.DateSelect
import com.example.calendarview.R
import com.example.calendarview.view.ExpandIconView
import java.text.DateFormatSymbols
import java.util.*

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
    private lateinit var tableHead: TableLayout

    open var state = STATE_COLLAPSED

    companion object {
        // State
        const val STATE_EXPANDED = 0
        const val STATE_COLLAPSED = 1
        const val STATE_PROCESSING = 2
        const val FIRST_DAY_OF_WEEK = Calendar.SUNDAY
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
        tableHead = rootView.findViewById(R.id.tl_days_of_week)

        setDaysOfWeekLabel()

        mAdapter = DateEducaRecyclerViewAdapter(context)
        listener?.let { mAdapter.setOnDateSelectListener(it) }
        rv.apply {
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

    private fun setDaysOfWeekLabel() {
        val rowCurrent = TableRow(context)
        rowCurrent.layoutParams = TableLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        for (i in 0..6) {
            val view = mInflater.inflate(R.layout.layout_day_of_week, null)
            val txtDayOfWeek = view.findViewById<View>(R.id.txt_day_of_week) as TextView
            txtDayOfWeek.text = DateFormatSymbols().shortWeekdays[(i + FIRST_DAY_OF_WEEK) % 7 + 1]
            view.layoutParams = TableRow.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f
            )
            rowCurrent.addView(view)
        }
        tableHead.addView(rowCurrent)
    }
}