package com.example.datepickerdemo.educa

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.datepickerdemo.DateSelect
import com.example.datepickerdemo.R
import com.example.datepickerdemo.educa.view.CollapsibleCalendarView
import java.util.*

class CalendarEducaActivity : AppCompatActivity(),
    DateEducaRecyclerViewAdapter.OnDateSelectListener,
    DaysOfMonthEducaGenerator.OnGenerateDaysOfMonthCompleteListener,
    DaysOfMonthEducaGenerator.OnGenDateSelectCompleteListener {

    private var daysOfMonthGenerator: DaysOfMonthEducaGenerator? = null
    private lateinit var calendarView: CollapsibleCalendarView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_educa)

        calendarView = findViewById(R.id.calendar_view)

        daysOfMonthGenerator = DaysOfMonthEducaGenerator(this)
        daysOfMonthGenerator!!.setOnGenerateDaysOfMonthCompleteListener(this)
        daysOfMonthGenerator!!.setOnGenDateSelectCompleteListener(this)
        calendarView.setOnDateSelectListener(this)
        calendarView.setOnButtonPrevClickListener {
            daysOfMonthGenerator?.genDatePrevMonth()
        }
        calendarView.setOnButtonNextClickListener {
            daysOfMonthGenerator?.genDateNextMonth()
        }
        daysOfMonthGenerator!!.genDateCurrent()
    }

    override fun onDateSelected(date: Long, isChoose: Boolean) {
        if (isChoose) {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = date
            val day = calendar[Calendar.DATE]
            val month = calendar[Calendar.MONTH]
            val year = calendar[Calendar.YEAR]
//            tvMonth?.text = "Tháng ${String.format("%02d", month + 1)}"
//            tvYear?.text = "$year"
        } else {
            val calendar = Calendar.getInstance()
            val month = calendar[Calendar.MONTH]
            val year = calendar[Calendar.YEAR]
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
//        btnMonthPrev?.apply {
////            isEnabled = allowPrev
////            isSelected = allowPrev
//        }
        calendarView.replaceDataList(dateSelects)
    }

    override fun onGenDateSelectCompleted(date: Long) {
        val calendarCurrent = Calendar.getInstance()
        calendarCurrent.timeInMillis = date
//        tvMonth?.text = "Tháng ${calendarCurrent.get(Calendar.MONTH) + 1}"
//        tvYear?.text = "${calendarCurrent.get(Calendar.YEAR)}"
        daysOfMonthGenerator?.bindDataGenDays()
    }

}