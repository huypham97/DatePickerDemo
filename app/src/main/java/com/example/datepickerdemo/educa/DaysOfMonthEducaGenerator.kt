package com.example.datepickerdemo.educa

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.datepickerdemo.DateSelect
import com.example.datepickerdemo.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class DaysOfMonthEducaGenerator(context: Context) {

    private var onGenerateDaysOfMonthCompleteListener: OnGenerateDaysOfMonthCompleteListener? = null
    private var onGenDateSelectCompleteListener: OnGenDateSelectCompleteListener? = null
    private var calendarBefore = Calendar.getInstance()
    private var calendarAfter = Calendar.getInstance()
    private lateinit var calendarSelected: Calendar
    private var monthSelected = 0
    private var yearSelected = 0

    private val colorDayUnavailable = ContextCompat.getColor(context, R.color.colorDayUnavailable)
    private val colorDayAvailable = ContextCompat.getColor(context, R.color.colorDayEducaAvailable)

    companion object {
        private const val DAYS_COUNT = 35
    }

    fun setOnGenerateDaysOfMonthCompleteListener(onGenerateDaysOfMonthCompleteListener: OnGenerateDaysOfMonthCompleteListener) {
        this.onGenerateDaysOfMonthCompleteListener = onGenerateDaysOfMonthCompleteListener
    }

    fun setOnGenDateSelectCompleteListener(onGenDateSelectCompleteListener: OnGenDateSelectCompleteListener) {
        this.onGenDateSelectCompleteListener = onGenDateSelectCompleteListener
    }

    fun getCalendarSelected(): Calendar {
        return calendarSelected
    }

    fun genDateCurrent() {
        val calendar = Calendar.getInstance()
        this.monthSelected = calendar.get(Calendar.MONTH)
        this.yearSelected = calendar.get(Calendar.YEAR)
        this.onGenDateSelectCompleteListener?.onGenDateSelectCompleted(calendar.timeInMillis)
    }

    fun genDateNextMonth() {
        this.monthSelected += 1
        if (this.monthSelected > 11) {
            this.monthSelected = 0
            this.yearSelected += 1
        }
        val calendar = GregorianCalendar(yearSelected, monthSelected, 1)
        this.onGenDateSelectCompleteListener?.onGenDateSelectCompleted(calendar.timeInMillis)
    }

    fun genDatePrevMonth() {
        this.monthSelected -= 1
        if (this.monthSelected < 0) {
            this.monthSelected = 11
            this.yearSelected -= 1
        }
        val calendar = GregorianCalendar(yearSelected, monthSelected, 1)
        this.onGenDateSelectCompleteListener?.onGenDateSelectCompleted(calendar.timeInMillis)
    }

    fun bindDataGenDays() {
        var monthBefore = this.monthSelected - 1
        var monthAfter = this.monthSelected + 1
        var yearBefore = this.yearSelected
        var yearAfter = this.yearSelected
        if (this.monthSelected == 0) {
            yearBefore = this.yearSelected - 1
            monthBefore = 11
        }
        if (this.monthSelected == 11) {
            yearAfter += 1
            monthAfter = 0
        }
        calendarAfter.apply {
            set(Calendar.YEAR, yearAfter)
            set(Calendar.MONTH, monthAfter)
            set(Calendar.DAY_OF_MONTH, 1)
        }
        calendarBefore.apply {
            set(Calendar.YEAR, yearBefore)
            set(Calendar.MONTH, monthBefore)
            set(Calendar.DAY_OF_MONTH, 1)
        }

        GlobalScope.launch(Dispatchers.Default) {
            daysOfMonthGenTask()
        }
    }

    private suspend fun daysOfMonthGenTask() {
        val daysGen = mutableListOf<DateSelect>()
        calendarSelected = GregorianCalendar(yearSelected, monthSelected, 1)
        var daysCountMonthSelect = calendarSelected.getActualMaximum(Calendar.DAY_OF_MONTH)
        var dayOfWeekStatSelect = calendarSelected.get(Calendar.DAY_OF_WEEK)
        var daysInMonthBefore = calendarBefore.getActualMaximum(Calendar.DAY_OF_MONTH)
        var daysCountMonthBefore = when (dayOfWeekStatSelect) {
            in 2..7 -> dayOfWeekStatSelect - 1 - 1
            else -> Calendar.DAY_OF_WEEK - 1
        }
        var daysCountMonthAfter = DAYS_COUNT - daysCountMonthSelect - daysCountMonthBefore

        if (daysCountMonthBefore > 0) {
            for (i in 0 until daysCountMonthBefore) {
                val dateSelect = DateSelect()
                calendarBefore.set(Calendar.DAY_OF_MONTH, daysInMonthBefore)
                dateSelect.apply {
                    date = calendarBefore.timeInMillis
                    isDisplay = false
                    available = false
                    textColor = colorDayUnavailable
                }
                daysGen.add(dateSelect)
                daysInMonthBefore -= 1
            }
        }

        for (i in 0 until daysCountMonthSelect) {
            val dateSelect = DateSelect()
            calendarSelected.set(Calendar.DAY_OF_MONTH, i + 1)
            dateSelect.apply {
                isDisplay = true
                date = calendarSelected.timeInMillis
                available = true
                textColor = colorDayAvailable
            }
            daysGen.add(dateSelect)
        }

        if (daysCountMonthAfter > 0) {
            for (i in 0 until daysCountMonthAfter) {
                val dateSelect = DateSelect()
                calendarAfter.set(Calendar.DAY_OF_MONTH, i + 1)
                dateSelect.apply {
                    textColor = colorDayAvailable;
                    isDisplay = false;
                    date = calendarAfter.timeInMillis;
                    isFullyBooked = false;
                    available = false;
                }
                daysGen.add(dateSelect)
            }
        }

        withContext(Dispatchers.Main) {
            onGenerateDaysOfMonthCompleteListener?.onGenerateDaysOfMonthCompleted(
                daysGen,
                calendarSelected
            )
        }

    }

    interface OnGenerateDaysOfMonthCompleteListener {
        fun onGenerateDaysOfMonthCompleted(
            dateSelects: MutableList<DateSelect>,
            calendarSelect: Calendar
        )
    }

    interface OnGenDateSelectCompleteListener {
        fun onGenDateSelectCompleted(date: Long)
    }
}