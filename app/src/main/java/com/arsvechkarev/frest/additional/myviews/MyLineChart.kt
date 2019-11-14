package com.arsvechkarev.frest.additional.myviews

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import com.arsvechkarev.frest.additional.datetime.toChartDate
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlin.math.roundToInt

/**
 * Line chart for stats by project
 *
 * @author Arseniy Svechkarev
 */
class MyLineChart(context: Context, attrs: AttributeSet) : LineChart(context, attrs) {
  
  init {
    xAxis.apply {
      valueFormatter = DateAxisFormatter
      setDrawGridLines(false)
      granularity = 1f
      position = XAxisPosition.BOTTOM
    }
    axisRight.apply {
      setDrawGridLines(false)
      axisMinimum = 0f
      granularity = 1f
      valueFormatter = YAxisFormatter
    }
    axisLeft.apply {
      axisMinimum = 0f
      isEnabled = false
    }
    description.isEnabled = false
    legend.isEnabled = false
    isDoubleTapToZoomEnabled = false
    setScaleEnabled(false)
  }
  
  fun update(entries: List<Entry>, color: Int) {
    val lineDataSet = LineDataSet(entries, "")
    lineDataSet.apply {
      setDrawCircles(false)
      this.color = color
      setDrawFilled(true)
      val gradient = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
        intArrayOf(color, Color.TRANSPARENT))
      fillDrawable = gradient
      setDrawHorizontalHighlightIndicator(false)
      setGradientColor(color, Color.TRANSPARENT)
    }
    data = LineData(lineDataSet).apply {
      setDrawValues(false)
    }
    invalidate()
    animateY(1000)
  }
  
  object YAxisFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float) = value.roundToInt().toString()
  }
  
  object DateAxisFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
      return value.toChartDate()
    }
  }
}
