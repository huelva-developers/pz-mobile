package com.huelvadevelopers.proyectozero

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.huelvadevelopers.proyectozero.model.Transaction
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import com.huelvadevelopers.proyectozero.R.id.chart
import android.R.attr.x



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
            //yAxis.granularity = 2000f
            chart.axisRight.isEnabled=false

            var xAxis = chart.xAxis
            xAxis.granularity = 1f
            xAxis.setDrawGridLines(false)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setValueFormatter { value, axis ->
                if(value>=0 && value<xLabel.size)
                    xLabel[value.toInt()]
                else
                    ""
            }

            chart.setVisibleXRangeMaximum(5f)

            return chart
        }
        fun getPieInstance(context : Context, type : Int) : PieChart {
            var chart : PieChart = PieChart(context)
            var highlight = false
            var highlighted = ArrayList<Int>()

            var dataSet: PieDataSet = pieDataSet(context, type, null)

            var pieData = PieData(dataSet)
            chart.centerText = if (type == 0) "Earnings" else "Expenses"
            chart.setCenterTextSize(20f)
            chart.description.isEnabled = false
            chart.data = pieData
            chart.setUsePercentValues(true)
            chart.legend.isEnabled = false
            chart.isHighlightPerTapEnabled = true
            chart.setEntryLabelColor(Color.BLACK)
            chart.setEntryLabelTextSize(10f)
            chart.isRotationEnabled = false

            chart.setOnChartValueSelectedListener(object :OnChartValueSelectedListener {
                override fun onNothingSelected() {
                    if(highlight) {
                        highlight = false
                        highlighted.clear()
                        chart.data.removeDataSet(0)
                        chart.data = PieData(pieDataSet(context, type, null))
                        chart.highlightValues(arrayOf<Highlight>())
                    }
                }

                override fun onValueSelected(e: Entry?, h: Highlight?) {
                    val countBefore = chart.data.dataSet.entryCount
                    chart.data.removeDataSet(0)
                    chart.data = PieData(pieDataSet(context, type, (e as PieEntry).label))
                    val countAfter = chart.data.dataSet.entryCount
                    var index = -1f
                    for (i in 0..chart.data.dataSet.entryCount - 1){
                        if(chart.data.dataSet.getEntryForIndex(i).label.equals(e.label)){
                            index=i.toFloat()
                            break
                        }
                    }
                    if(!highlighted.contains(h!!.x.toInt()) && index!=-1f){
                        val arrayHighlight = ArrayList<Highlight>()
                        val highlightedCount = highlighted.size
                        highlighted.clear()
                        val times : Int
                        if(highlight){
                            times = countAfter-(countBefore-highlightedCount+1)+1
                        }
                        else{
                            times = countAfter-countBefore+1
                        }
                        repeat(times){
                            arrayHighlight.add(Highlight(index, 0f, 0))
                            highlighted.add(index.toInt())
                            index++
                        }
                        chart.highlightValues(arrayHighlight.toTypedArray())
                        highlight = true
                    }
                    else {
                        highlight = false
                        highlighted.clear()
                        chart.highlightValues(arrayOf<Highlight>())
                    }
                    Log.v("size posicion", h?.x.toString())
                    Log.v("size2", chart.data.dataSet.entryCount.toString())
                    /*val h = Highlight(0f , 0f, 0) // dataset index for piechart is always 0
                    val h2 = Highlight(1f , 0f, 0) // dataset index for piechart is always 0
                    chart.highlightValues(arrayOf<Highlight>(h,h2))*/
                   /* chart.notifyDataSetChanged()
                    chart.invalidate()
                    chart.highlightValue(0f,0)*/
                }
            })

            chart.invalidate() // refresh

            /*var yAxis = chart.axisLeft
            yAxis.granularity = 2000f
            chart.axisRight.isEnabled=false

            var xAxis = chart.xAxis
            xAxis.granularity = 1f
            xAxis.setDrawGridLines(false)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setValueFormatter { value, axis ->
                xLabel[value.toInt()]
            }

            chart.setVisibleXRangeMaximum(5f)*/

            return chart
        }

        private fun pieDataSet(context: Context, type: Int, parentName : String?): PieDataSet {
            var entries = ArrayList<PieEntry>()
            var xLabel = ArrayList<String>()

            var valuesArray = (context as MainActivity).databaseManager.getEarningsAndExpensesWithTag(parentName)
            for ((index, values) in valuesArray.withIndex()) {
                entries.add(PieEntry(values[type] as Float, values[2] as String))
                xLabel.add(values[2] as String)
            }

            var dataSet: PieDataSet
            if (type == 0)
                dataSet = PieDataSet(entries, "Earnings") // add entries to dataset
            else
                dataSet = PieDataSet(entries, "Expenses")
            //dataSetEarnings.color = Color.GREEN
            dataSet.valueTextSize = 0f
            val MY_EARNINGS_COLORS = intArrayOf(Color.rgb(146, 208, 80), Color.rgb(0, 176, 80))
            val MY_EXPENSES_COLORS = intArrayOf(Color.rgb(208, 146, 80), Color.rgb(176, 0, 80))
            val colors = ArrayList<Int>()

            if (type == 0)
                for (c in MY_EARNINGS_COLORS) colors.add(c)
            else
                for (c in MY_EXPENSES_COLORS) colors.add(c)

            dataSet.colors = colors
            /*dataSetEarnings.setValueFormatter { value, entry, dataSetIndex, viewPortHandler ->
                ""
            }*/
            //dataSetEarnings.valueTextColor = Color.GREEN

            dataSet.sliceSpace=5f
            return dataSet
        }
    }
}