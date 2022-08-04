package com.example.datepickerdemo.common

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.datepickerdemo.DateSelect
import com.example.datepickerdemo.R
import com.example.datepickerdemo.educa.DateEducaRecyclerViewAdapter
import java.util.*

class CalendarActivity : AppCompatActivity(), DateEducaRecyclerViewAdapter.OnDateSelectListener,
    DaysOfMonthGenerator.OnGenerateDaysOfMonthCompleteListener,
    DaysOfMonthGenerator.OnGenDateSelectCompleteListener {

    private lateinit var adapter: DateEducaRecyclerViewAdapter
    private var daysOfMonthGenerator: DaysOfMonthGenerator? = null

    private var tvDate: TextView? = null
    private var btnPrev: ImageButton? = null
    private var btnNext: ImageButton? = null
    private var rv: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        tvDate = findViewById(R.id.tv_date)
        btnPrev = findViewById(R.id.ib_prev)
        btnNext = findViewById(R.id.ib_next)
        rv = findViewById(R.id.rv)

        btnPrev!!.setOnClickListener {
            daysOfMonthGenerator?.genDatePrevMonth()
        }
        btnNext!!.setOnClickListener {
            daysOfMonthGenerator?.genDateNextMonth()
        }

        adapter = DateEducaRecyclerViewAdapter(this)
        daysOfMonthGenerator = DaysOfMonthGenerator(this)
        daysOfMonthGenerator!!.setOnGenerateDaysOfMonthCompleteListener(this)
        daysOfMonthGenerator!!.setOnGenDateSelectCompleteListener(this)
        adapter.setOnDateSelectListener(this)
        daysOfMonthGenerator!!.genDateCurrent()

        rv?.apply {
            layoutManager = GridLayoutManager(this@CalendarActivity, 7)
            adapter = this@CalendarActivity.adapter
        }
    }

    override fun onDateSelected(date: Long, isChoose: Boolean) {
        if (isChoose) {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = date
            val day = calendar[Calendar.DATE]
            val month = calendar[Calendar.MONTH]
            val year = calendar[Calendar.YEAR]
            tvDate?.text =
                day.toString() + " " + getString(R.string.month_text) + " " + String.format(
                    "%02d",
                    month + 1
                ) + ", " + year
        } else {
            val calendar = Calendar.getInstance()
            val month = calendar[Calendar.MONTH]
            val year = calendar[Calendar.YEAR]
            tvDate!!.text =
                getString(R.string.month_text) + " " + String.format(
                    "%02d",
                    month + 1
                ) + ", " + year
        }
    }

    override fun onGenerateDaysOfMonthCompleted(
        dateSelects: MutableList<DateSelect>,
        calendarSelect: Calendar
    ) {
        val calendarCurrent = Calendar.getInstance()
        val monthCurrent = calendarCurrent[Calendar.MONTH]
        val yearCurrent = calendarCurrent[Calendar.YEAR]
        val allowPrev = calendarSelect.get(Calendar.YEAR) >= yearCurrent && calendarSelect.get(
            Calendar.MONTH
        ) > monthCurrent
        btnPrev?.apply {
            isEnabled = allowPrev
            isSelected = allowPrev
        }
        adapter.replaceDataList(dateSelects)
    }

    override fun onGenDateSelectCompleted(date: Long) {
        daysOfMonthGenerator?.bindDataGenDays()
    }
}