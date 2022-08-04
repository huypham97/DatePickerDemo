package com.example.datepickerdemo.common

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.datepickerdemo.DateSelect
import com.example.datepickerdemo.R
import java.util.*

class DateRecyclerViewAdapter(context: Context) :
    RecyclerView.Adapter<DateRecyclerViewAdapter.DayViewHolder>() {

    private var days: MutableList<DateSelect> = mutableListOf()
    private var layoutInflater: LayoutInflater
    private var colorDaySelected = 0
    private var colorToday = 0
    private var colorDayUnavailable = 0
    private var calendarToday: Calendar
    private var calendar: Calendar
    private var calendarSelected: Long = 0L
    private var onDateSelectListener: OnDateSelectListener? = null

    init {
        layoutInflater = LayoutInflater.from(context)
        colorDayUnavailable = ContextCompat.getColor(context, R.color.colorDayUnavailable)
        colorDaySelected = ContextCompat.getColor(context, R.color.colorDaySelect)
        colorToday = ContextCompat.getColor(context, R.color.colorDayCurrent)
        calendarToday = Calendar.getInstance()
        calendar = Calendar.getInstance()
        calendarSelected = 0L
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        return DayViewHolder(
            layoutInflater.inflate(
                R.layout.row_recycler_view_day_select,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        holder.bindData(days[position])
    }

    override fun getItemCount(): Int = days.size

    fun setToday(today: Long) {
        calendarToday.timeInMillis = today
    }

    fun setOnDateSelectListener(onDateSelectListener: OnDateSelectListener) {
        this.onDateSelectListener = onDateSelectListener
    }

    fun replaceDataList(days: MutableList<DateSelect>) {
        this.days.clear()
        this.days.addAll(days)
        notifyDataSetChanged()
    }

    inner class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var tvDay: TextView = itemView.findViewById(R.id.row_recycler_view_day_select_tvDay)
        private var vMain: View = itemView

        fun bindData(dateSelect: DateSelect) {
            calendar.timeInMillis = dateSelect.date
            val days = calendar.get(Calendar.DATE)
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)
            var colorTextDisplay = dateSelect.textColor
            if (days < calendarToday.get(Calendar.DATE)
                && month == calendarToday.get(Calendar.MONTH)
                && year == calendarToday.get(Calendar.YEAR)
            ) {
                dateSelect.available = false
                colorTextDisplay = colorDayUnavailable
                tvDay.isSelected = false
                tvDay.background = null
            } else {
                var isDaySelected = false
                if (calendarSelected != null) {
                    isDaySelected = calendarSelected == dateSelect.date
                }
                if (isDaySelected) {
                    colorTextDisplay = colorDaySelected
                } else {
                    if (days == calendarToday.get(Calendar.DATE)
                        && month == calendarToday.get(Calendar.MONTH)
                        && year == calendarToday.get(Calendar.YEAR)
                    ) {
                        if (!isDaySelected) {
                            colorTextDisplay = colorToday
                        }
                    }
                }
                tvDay.setBackgroundResource(R.drawable.bg_day_select)
                tvDay.isSelected = isDaySelected
            }

            tvDay.apply {
                setTextColor(colorTextDisplay)
                text = days.toString()
                visibility = if (dateSelect.isDisplay) View.VISIBLE else View.INVISIBLE
                isEnabled = dateSelect.available
            }
            vMain.apply {
                isEnabled = dateSelect.available
                setOnClickListener {
                    var isChoose = false
                    if (dateSelect.date == calendarSelected) {
                        calendarSelected = 0L
                        isChoose = false
                    } else {
                        calendarSelected = dateSelect.date
                        isChoose = true
                    }
                    notifyDataSetChanged()
                    onDateSelectListener?.onDateSelected(dateSelect.date, isChoose)
                }
            }
        }

    }

    interface OnDateSelectListener {
        fun onDateSelected(date: Long, isChoose: Boolean)
    }
}