package com.example.calendarview.common

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.calendarview.DateSelect
import java.util.*

class CalendarViewModel: ViewModel() {

    val currentDayIndex = MutableLiveData<Int>()

    fun getCurrentDayIndex(days: MutableList<DateSelect>) {
        for (i in 0 until days.size) {
            val calendarData = Calendar.getInstance().apply { timeInMillis = days[i].date }
            val calendarToday = Calendar.getInstance()
            if (calendarData.get(Calendar.DATE) == calendarToday.get(Calendar.DATE)
                && calendarData.get(Calendar.MONTH) == calendarToday.get(Calendar.MONTH)
                && calendarData.get(Calendar.YEAR) == calendarToday.get(Calendar.YEAR)
            ) {
                currentDayIndex.value = i
                return
            }
        }
        currentDayIndex.value = 0
    }

}