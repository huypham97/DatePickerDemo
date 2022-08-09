package com.example.datepickerdemo

import android.graphics.Typeface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.datepickerdemo.chart.DayAxisValueFormatter
import com.example.datepickerdemo.chart.MyAxisValueFormatter
import com.example.datepickerdemo.chart.RoundedBarChart
import com.example.datepickerdemo.chart.XYMarkerView
import com.example.mpchart.components.Legend
import com.example.mpchart.components.XAxis
import com.example.mpchart.components.YAxis
import com.example.mpchart.data.BarData
import com.example.mpchart.data.BarDataSet
import com.example.mpchart.data.BarEntry
import com.example.mpchart.formatter.IAxisValueFormatter
import com.example.mpchart.interfaces.datasets.IBarDataSet

class ChartActivity : AppCompatActivity() {

    private lateinit var chart: RoundedBarChart
    private val tfLight = Typeface.DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)
        chart = findViewById(R.id.chart)

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
        leftAxis.setLabelCount(6, false)
        leftAxis.valueFormatter = custom
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        leftAxis.spaceTop = 15f
        leftAxis.axisMinimum = 0f
        leftAxis.axisMaximum = 10f// this replaces setStartAtZero(true)
        leftAxis.setDrawAxisLine(false)
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
        val values = listOf(
            BarEntry(1F, 6.5F),
            BarEntry(2F, null),
            BarEntry(3F, 10F),
            BarEntry(4F, 4.6F),
            BarEntry(5F, 2.76F),
            BarEntry(6F, 6.5F),
            BarEntry(7F, 8.2F),
            BarEntry(8F, 10F),
            BarEntry(9F, 4.6F),
            BarEntry(10F, 0F)
        )

        val set: BarDataSet
        if (chart.data != null &&
            chart.data.dataSetCount > 0
        ) {
            set = chart.data.getDataSetByIndex(0) as BarDataSet
            set.setValues(values)
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            set = BarDataSet(values, "The year 2017")
            set.setDrawIcons(false)
            set.color = ContextCompat.getColor(this, R.color.pink)
            set.highLightColor = ContextCompat.getColor(this, R.color.pink_highlight)
            set.valueTextColor = ContextCompat.getColor(this, R.color.orange)
            val dataSets: ArrayList<IBarDataSet> = ArrayList()
            dataSets.add(set)
            val data = BarData(dataSets)
            data.setValueTextSize(10f)
            data.setValueTypeface(tfLight)
            data.barWidth = 0.7f
            chart.data = data
            chart.setVisibleXRangeMaximum(5F)
            chart.setVisibleXRangeMinimum(5F)
        }
    }
}