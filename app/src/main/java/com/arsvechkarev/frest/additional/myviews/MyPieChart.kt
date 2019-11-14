package com.arsvechkarev.frest.additional.myviews

import android.content.Context
import android.util.AttributeSet
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import java.util.ArrayList

class MyPieChart(context: Context, attrs: AttributeSet) : PieChart(context, attrs) {
  
  private lateinit var pieEntries: ArrayList<PieEntry>
  private lateinit var colors: ArrayList<Int>
  
  init {
    setUsePercentValues(true)
    description.isEnabled = false
    legend.isEnabled = false
    setNoDataText("")
    isDrawHoleEnabled = false
    contentDescription = ""
    setEntryLabelTextSize(15f)
    transparentCircleRadius = 0f
  }
  
  fun setEntries(pieEntries: ArrayList<PieEntry>): MyPieChart {
    this.pieEntries = pieEntries
    return this
  }
  
  fun setColors(colors: ArrayList<Int>): MyPieChart {
    this.colors = colors
    return this
  }
  
  fun update() {
    animateY(700, Easing.EaseInOutCubic)
    val dataSet = PieDataSet(pieEntries, "")
    dataSet.colors = colors
    dataSet.sliceSpace = 1f
    val pieData = PieData(dataSet)
    pieData.setDrawValues(false)
    data = pieData
  }
}
