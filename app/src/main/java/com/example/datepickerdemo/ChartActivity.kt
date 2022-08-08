package com.example.datepickerdemo

import android.graphics.Typeface
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.datepickerdemo.chart.DayAxisValueFormatter
import com.example.datepickerdemo.chart.MyAxisValueFormatter
import com.example.datepickerdemo.chart.XYMarkerView
import com.example.mpchart.charts.BarChart
import com.example.mpchart.components.Legend
import com.example.mpchart.components.XAxis
import com.example.mpchart.components.YAxis
import com.example.mpchart.data.BarData
import com.example.mpchart.data.BarDataSet
import com.example.mpchart.data.BarEntry
import com.example.mpchart.formatter.IAxisValueFormatter
import com.example.mpchart.interfaces.datasets.IBarDataSet
import com.example.mpchart.utils.Fill

class ChartActivity : AppCompatActivity() {

    private lateinit var chart: BarChart
    private lateinit var button: Button
    private val tfLight = Typeface.DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)
        chart = findViewById(R.id.chart)
        button = findViewById(R.id.btn_icon)

        button.setOnClickListener {
            for (set in chart.data.dataSets)
                set.setDrawIcons(!set.isDrawIconsEnabled)
            chart.invalidate()
        }

        chart.setDrawBarShadow(false)
        chart.setDrawValueAboveBar(true)

        chart.description.isEnabled = false

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(60)

        // scaling can now only be done on x- and y-axis separately

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false)

        chart.setDrawGridBackground(false)
        // chart.setDrawYLabels(false);

        // chart.setDrawYLabels(false);
        val xAxisFormatter: IAxisValueFormatter = DayAxisValueFormatter(chart)

        val xAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.typeface = tfLight
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f // only intervals of 1 day

        xAxis.labelCount = 7
        xAxis.valueFormatter = xAxisFormatter

        val custom: IAxisValueFormatter = MyAxisValueFormatter()

        val leftAxis = chart.axisLeft
        leftAxis.typeface = tfLight
        leftAxis.setLabelCount(8, false)
        leftAxis.valueFormatter = custom
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        leftAxis.spaceTop = 15f
        leftAxis.axisMinimum = 0f
        leftAxis.axisMaximum = 10f// this replaces setStartAtZero(true)
        leftAxis.enableGridDashedLine(10f, 10f, 0f)


        chart.axisRight.isEnabled = false
        chart.isScaleYEnabled = false


        val l = chart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(false)
        l.form = Legend.LegendForm.SQUARE
        l.formSize = 9f
        l.textSize = 11f
        l.xEntrySpace = 4f

        val mv = XYMarkerView(this, xAxisFormatter)
        mv.chartView = chart // For bounds control

        chart.marker = mv // Set the marker to the chart
    }

    override fun onResume() {
        super.onResume()
        setData(5, 10F)
        chart.invalidate()

    }

    private fun setData(count: Int, range: Float) {
        val start = 1f
        val values: ArrayList<BarEntry> = ArrayList()
        var i = start.toInt()
        /*while (i < start + count) {
            val _val = (Math.random() * (range - 0) + 0).toFloat()
            if (Math.random() * 100 < 25) {
                values.add(BarEntry(i.toFloat(), _val, resources.getDrawable(R.drawable.star)))
            } else {
                values.add(BarEntry(i.toFloat(), _val))
            }
            i++
        }*/
        values.add(BarEntry(1F, 6.5F))
        values.add(BarEntry(2F, 8.2F))
        values.add(BarEntry(3F, 10F))
        values.add(BarEntry(4F, 4.6F))
        values.add(BarEntry(5F, 0F, ContextCompat.getDrawable(this, R.drawable.star)))
        values.add(BarEntry(6F, 6.5F))
        values.add(BarEntry(7F, 8.2F))
        values.add(BarEntry(8F, 10F))
        values.add(BarEntry(9F, 4.6F))
        values.add(BarEntry(10F, 0F, ContextCompat.getDrawable(this, R.drawable.star)))

        val set1: BarDataSet
        if (chart.data != null &&
            chart.data.dataSetCount > 0
        ) {
            set1 = chart.data.getDataSetByIndex(0) as BarDataSet
            set1.setValues(values)
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            set1 = BarDataSet(values, "The year 2017")
            set1.setDrawIcons(false)
            val startColor1 = ContextCompat.getColor(this, android.R.color.holo_orange_light)
            val startColor2 = ContextCompat.getColor(this, android.R.color.holo_blue_light)
            val startColor3 = ContextCompat.getColor(this, android.R.color.holo_orange_light)
            val startColor4 = ContextCompat.getColor(this, android.R.color.holo_green_light)
            val startColor5 = ContextCompat.getColor(this, android.R.color.holo_red_light)
            val endColor1 = ContextCompat.getColor(this, android.R.color.holo_blue_dark)
            val endColor2 = ContextCompat.getColor(this, android.R.color.holo_purple)
            val endColor3 = ContextCompat.getColor(this, android.R.color.holo_green_dark)
            val endColor4 = ContextCompat.getColor(this, android.R.color.holo_red_dark)
            val endColor5 = ContextCompat.getColor(this, android.R.color.holo_orange_dark)
            val gradientFills: MutableList<Fill> = ArrayList()
            gradientFills.add(Fill(startColor1, endColor1))
            gradientFills.add(Fill(startColor2, endColor2))
            gradientFills.add(Fill(startColor3, endColor3))
            gradientFills.add(Fill(startColor4, endColor4))
            gradientFills.add(Fill(startColor5, endColor5))
            set1.fills = arrayListOf(Fill(ContextCompat.getColor(this, R.color.pink)))
            set1.highLightColor = ContextCompat.getColor(this, R.color.pink_highlight)
            val dataSets: ArrayList<IBarDataSet> = ArrayList()
            dataSets.add(set1)
            val data = BarData(dataSets)
            data.setValueTextSize(10f)
            data.setValueTypeface(tfLight)
            data.setBarWidth(0.9f)
            chart.data = data

        }
    }
}