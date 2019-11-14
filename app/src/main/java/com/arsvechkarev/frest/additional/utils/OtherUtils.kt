package com.arsvechkarev.frest.additional.utils

import android.content.Context
import android.graphics.Typeface
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.arsvechkarev.frest.R
import com.arsvechkarev.frest.additional.myviews.MyLineChart
import com.arsvechkarev.frest.models.reports.LineChartPoint
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
  addTextChangedListener(object : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(s: Editable?) {
      afterTextChanged.invoke(s.toString())
    }
  })
}

fun MyLineChart.onValueSelected(onValueSelected: (LineChartPoint) -> Unit) {
  setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
    override fun onNothingSelected() {}
    override fun onValueSelected(e: Entry, h: Highlight) {
      onValueSelected.invoke(LineChartPoint(e.x, e.y))
    }
  })
}

fun showNormalToast(context: Context, message: String) {
  val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
  val toastLayout = toast.view as ViewGroup
  val toastTextView = toastLayout.getChildAt(0) as TextView
  toastTextView.typeface = Typeface.DEFAULT
  toastTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
    context.resources.getDimension(R.dimen.text_small))
  toast.show()
}