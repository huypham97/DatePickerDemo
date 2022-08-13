package com.example.calendarview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class DateEducaRecyclerViewAdapter(context: Context) :
    RecyclerView.Adapter<DateEducaRecyclerViewAdapter.DayViewHolder>() {

    private var days: MutableList<DateSelect> = mutableListOf()
    private var layoutInflater: LayoutInflater
    private var colorDaySelected = 0
    private var colorToday = 0
    private var colorDayUnavailable = 0
    private var calendarToday: Calendar
    private var calendar: Calendar
    private var calendarSelected: Long = 0L
    private var onDateSelectListener: OnDateSelectListener? = null
    private var itemHeight = 0

    init {
        layoutInflater = LayoutInflater.from(context)
        colorDayUnavailable = ContextCompat.getColor(context, R.color.colorDayUnavailable)
        colorDaySelected = ContextCompat.getColor(context, R.color.colorDayEducaSelect)
        colorToday = ContextCompat.getColor(context, R.color.colorDayCurrent)
        calendarToday = Calendar.getInstance()
        calendar = Calendar.getInstance()
        calendarSelected = 0L
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = layoutInflater.inflate(
            R.layout.row_recycler_view_day_select,
            parent,
            false
        )
        return DayViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        itemHeight = holder.itemView.height
        holder.bindData(days[position])
    }

    override fun getItemCount(): Int = days.size

    fun setToday(today: Long) {
        calendarToday.timeInMillis = today
    }

    fun setOnDateSelectListener(onDateSelectListener: OnDateSelectListener) {
        this.onDateSelectListener = onDateSelectListener
    }

    fun getItem(position: Int): DateSelect = days[position]

    fun replaceDataList(days: MutableList<DateSelect>) {
        this.days.clear()
        this.days.addAll(days)
        notifyDataSetChanged()
    }

    fun getItemHeight() = itemHeight

    inner class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var tvDay: TextView = itemView.findViewById(R.id.row_recycler_view_day_select_tvDay)
        private var vNotify: View =
            itemView.findViewById(R.id.v_row_recycler_view_day_select_notify)
        private var vMain: View = itemView

        fun bindData(dateSelect: DateSelect) {
            calendar.timeInMillis = dateSelect.date
            val days = calendar.get(Calendar.DATE)
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)
            var colorTextDisplay = dateSelect.textColor
            val calendarSelect = Calendar.getInstance()
            calendarSelect.timeInMillis = dateSelect.date
            if ((days < calendarToday.get(Calendar.DATE)
                        && month == calendarToday.get(Calendar.MONTH)
                        && year == calendarToday.get(Calendar.YEAR))
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
                tvDay.setBackgroundResource(R.drawable.bg_day_select_educa)
                tvDay.isSelected = isDaySelected
            }

            tvDay.apply {
                setTextColor(colorTextDisplay)
                text = days.toString()
                visibility = if (dateSelect.isDisplay) View.VISIBLE else View.INVISIBLE
                isEnabled = dateSelect.available
            }

            dateSelect.isNotify = true
            vNotify.visibility = if (dateSelect.isNotify) View.VISIBLE else View.GONE

            vMain.apply {
                isEnabled = dateSelect.available
                setOnClickListener {
                    val isChoose: Boolean
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