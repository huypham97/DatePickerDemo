package com.example.datepickerdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.calendarview.DateEducaRecyclerViewAdapter
import com.example.calendarview.DaysOfMonthEducaGenerator
import com.example.calendarview.view.CollapsibleCalendarView
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
        //
    }

    override fun onGenDateSelectCompleted(date: Long) {
        val calendarCurrent = Calendar.getInstance()
        calendarCurrent.timeInMillis = date
        calendarView.setMonthYearData(
            "Tháng ${calendarCurrent.get(Calendar.MONTH) + 1}/${
                calendarCurrent.get(
                    Calendar.YEAR
                )
            }"
        )
        daysOfMonthGenerator?.bindDataGenDays()
    }

    override fun onGenerateDaysOfMonthCompleted(
        dateSelects: MutableList<com.example.calendarview.DateSelect>,
        calendarSelect: Calendar
    ) {
        calendarView.replaceDataList(dateSelects)
    }

}