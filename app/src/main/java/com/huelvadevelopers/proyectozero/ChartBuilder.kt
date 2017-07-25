package com.huelvadevelopers.proyectozero

import android.content.Context
import android.graphics.Color
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.huelvadevelopers.proyectozero.model.Transaction
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by DrAP on 20/07/2017.
 */

class ChartBuilder {
    companion object {
        fun getInstance(context : Context) : LineChart {
            var chart : LineChart = LineChart(context)

            var entriesEarnings = ArrayList<Entry>()
            var entriesExpenses = ArrayList<Entry>()
            var xLabel = ArrayList<String>()

            var valuesArray = (context as MainActivity).databaseManager.getEarningsAndExpenses()
            for((index, values) in valuesArray.withIndex()){
                entriesEarnings.add(Entry(index.toFloat(), values[0] as Float))
                entriesExpenses.add(Entry(index.toFloat(), values[1] as Float))
                xLabel.add(values[2] as String)
            }

            var dataSetEarnings = LineDataSet(entriesEarnings, "Earnings") // add entries to dataset
            dataSetEarnings.color = Color.GREEN
            dataSetEarnings.valueTextSize= 20F
            dataSetEarnings.lineWidth = 3f
            dataSetEarnings.setValueFormatter { value, entry, dataSetIndex, viewPortHandler ->
                ""
            }
            dataSetEarnings.valueTextColor = Color.GREEN


            var dataSetExpenses = LineDataSet(entriesExpenses, "Expenses") // add entries to dataset
            dataSetExpenses.color = Color.RED
            dataSetExpenses.valueTextSize= 20F
            dataSetExpenses.lineWidth = 3f
            dataSetExpenses.setValueFormatter { value, entry, dataSetIndex, viewPortHandler ->
                ""
            }
            dataSetExpenses.valueTextColor = Color.RED

            var lineData = LineData(dataSetEarnings)
            lineData.addDataSet(dataSetExpenses)
            chart.description.text = "Earnings vs Expenses"
            chart.setScaleEnabled(true)
            chart.setPinchZoom(true)
            chart.data = lineData
            chart.invalidate() // refresh

            var yAxis = chart.axisLeft
            yAxis.granularity = 2000f
            chart.axisRight.isEnabled=false

            var xAxis = chart.xAxis
            xAxis.granularity = 1f
            xAxis.setDrawGridLines(false)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setValueFormatter { value, axis ->
                xLabel[value.toInt()]
            }

            chart.setVisibleXRangeMaximum(5f)

            return chart
        }
    }
}