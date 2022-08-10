package com.example.datepickerdemo

import android.graphics.Typeface
import android.os.Bundle
import android.widget.Toast
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
import com.example.mpchart.data.Entry
import com.example.mpchart.formatter.IAxisValueFormatter
import com.example.mpchart.highlight.Highlight
import com.example.mpchart.interfaces.datasets.IBarDataSet
import com.example.mpchart.listener.OnChartValueSelectedListener

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

        chart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            typeface = tfLight
            setDrawGridLines(false)
            granularity = 1f // only intervals of 1 day
            labelCount = 7
            valueFormatter = xAxisFormatter
            setDrawAxisLine(false)
        }

        val custom: IAxisValueFormatter = MyAxisValueFormatter()

        chart.axisLeft.apply {
            typeface = tfLight
            setLabelCount(6, false)
            valueFormatter = custom
            setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
            spaceTop = 15f
            axisMinimum = 0f
            axisMaximum = 10f// this replaces setStartAtZero(true)
            setDrawAxisLine(false)
            gridColor = ContextCompat.getColor(this@ChartActivity, R.color.grey_color)
            gridLineWidth = 1f
            enableGridDashedLine(10f, 10f, 0f)
        }

        chart.axisRight.isEnabled = false
        chart.isScaleYEnabled = false

        chart.legend.isEnabled = false

        val mv = XYMarkerView(this, xAxisFormatter)
        mv.chartView = chart // For bounds control

        chart.animateY(500)
        chart.marker = mv // Set the marker to the chart
        chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                if (e?.y == -1f) {
                    Toast.makeText(this@ChartActivity, "Hello", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected() {
                //
            }

        })
    }

    override fun onResume() {
        super.onResume()
        setData(5, 10F)
        chart.invalidate()

    }

    private fun setData(count: Int, range: Float) {
        val values = listOf(
            BarEntry(1F, 6.5F),
            BarEntry(2F, -1F),
            BarEntry(3F, 10F),
            BarEntry(4F, 4.6F),
            BarEntry(5F, 1F),
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
            set.entries = values
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
            data.barWidth = 0.8f
            chart.data = data
            chart.setVisibleXRangeMaximum(5F)
            chart.setVisibleXRangeMinimum(5F)
        }
    }
}