package com.example.datepickerdemo

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.calendarview.DateEducaRecyclerViewAdapter
import com.example.calendarview.DaysOfMonthEducaGenerator
import com.example.calendarview.view.CollapsibleCalendarView
import java.text.SimpleDateFormat
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
        val dateFormat = SimpleDateFormat("MMMM/yyyy", getCurrentLocale(this))
        val calendar = Calendar.getInstance().apply { timeInMillis = date }
        dateFormat.timeZone = calendar.timeZone
        calendarView.setMonthYearData(
            dateFormat.format(calendar.time)
        )
        daysOfMonthGenerator?.bindDataGenDays()
    }

    override fun onGenerateDaysOfMonthCompleted(
        dateSelects: MutableList<com.example.calendarview.DateSelect>,
        calendarSelect: Calendar
    ) {
        calendarView.replaceDataList(dateSelects)
    }

    fun getCurrentLocale(context: Context): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales.get(0)
        } else {

            context.resources.configuration.locale
        }
    }
}